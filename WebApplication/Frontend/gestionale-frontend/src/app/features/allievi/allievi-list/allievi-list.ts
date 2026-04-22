import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AllievoService } from '@core/services/allievo.service';
import { CorsoService } from '@core/services/corso.service';
import { Allievo, Corso } from '@shared/models';
import {
  catchError,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  forkJoin,
  map,
  of,
  startWith,
  Subject,
  switchMap,
  takeUntil,
  tap,
} from 'rxjs';

/**
 * Vista di ricerca trasversale degli allievi.
 * La pagina interroga il backend solo quando l'utente digita un testo valido,
 * evitando il caricamento massivo iniziale e mantenendo attivi gli stati UX.
 */
@Component({
  selector: 'app-allievi-list',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './allievi-list.html',
  styleUrl: './allievi-list.scss',
})
export class AllieviList implements OnInit, OnDestroy {
  private readonly allievoService = inject(AllievoService);
  private readonly corsoService = inject(CorsoService);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  /** Campo di ricerca reattivo per nome, cognome o codice fiscale. */
  readonly searchControl = new FormControl('', { nonNullable: true });
  /** Filtro per annualita accademica. */
  readonly annoAccademicoControl = new FormControl('', { nonNullable: true });
  /** Filtro per corso specifico. */
  readonly corsoIdControl = new FormControl<number | null>(null);

  /** Dati caricati dal backend e mostrati in tabella. */
  allievi: Allievo[] = [];
  /** Corsi disponibili per i filtri. */
  corsi: Corso[] = [];
  /** Flag di caricamento filtri corso/annualita. */
  isFiltersLoading = true;
  /** Stato di caricamento della ricerca corrente. */
  isLoading = true;
  /** Messaggio di errore visibile all'utente in caso di fallimento della richiesta. */
  error: string | null = null;
  /** Stato iniziale prima che l'utente inizi a cercare. */
  isInitialState = true;
  /** Colonna attualmente usata per l'ordinamento locale. */
  sortColumn: SortableColumn = 'cognome';
  /** Direzione corrente dell'ordinamento locale. */
  sortDirection: SortDirection = 'asc';

  /** Avvia il listener reattivo della ricerca all'apertura della pagina. */
  ngOnInit(): void {
    this.loadCorsi();
    this.setupAnnoFilterSync();
    this.setupSearchStream();
    this.setInitialState();
  }

  /** Pulisce le subscription della pagina. */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /** Prepara lo stato iniziale, senza risultati e senza chiamate al backend. */
  private setInitialState(): void {
    this.allievi = [];
    this.error = null;
    this.isLoading = false;
    this.isInitialState = true;
  }

  /** Carica i corsi per popolare i filtri di annualita e corso. */
  private loadCorsi(): void {
    this.isFiltersLoading = true;

    this.corsoService.findAll()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.corsi = [...data].sort((first, second) => {
            const annoComparison = (first.annoAccademico || '').localeCompare(second.annoAccademico || '', 'it', {
              sensitivity: 'base',
            });

            if (annoComparison !== 0) {
              return annoComparison;
            }

            return (first.nome || '').localeCompare(second.nome || '', 'it', {
              sensitivity: 'base',
            });
          });
          this.isFiltersLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento corsi per i filtri:', err);
          this.error = 'Errore nel caricamento dei filtri corso. Riprovare più tardi.';
          this.isFiltersLoading = false;
        },
      });
  }

  /** Mantiene coerente il filtro corso quando cambia l'annualita selezionata. */
  private setupAnnoFilterSync(): void {
    this.annoAccademicoControl.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe((annoAccademico) => {
        const selectedCorsoId = this.corsoIdControl.value;
        if (!selectedCorsoId || !annoAccademico) {
          return;
        }

        const isCorsoCompatible = this.filteredCorsi.some((corso) => corso.id === selectedCorsoId);
        if (!isCorsoCompatible) {
          this.corsoIdControl.setValue(null);
        }
      });
  }

  /** Ascolta il campo di ricerca e lancia le query in modo reattivo con debounce. */
  private setupSearchStream(): void {
    const search$ = this.searchControl.valueChanges.pipe(
      startWith(this.searchControl.value),
      map((value) => value.trim()),
      debounceTime(300),
      distinctUntilChanged(),
    );

    const annoAccademico$ = this.annoAccademicoControl.valueChanges.pipe(
      startWith(this.annoAccademicoControl.value),
      distinctUntilChanged(),
    );

    const corsoId$ = this.corsoIdControl.valueChanges.pipe(
      startWith(this.corsoIdControl.value),
      distinctUntilChanged(),
    );

    combineLatest([search$, annoAccademico$, corsoId$])
      .pipe(
        tap(([searchTerm, annoAccademico, corsoId]) => {
          this.error = null;

          const hasAnyFilter = Boolean(searchTerm) || Boolean(annoAccademico) || Boolean(corsoId);

          if (!hasAnyFilter) {
            this.setInitialState();
            return;
          }

          this.isInitialState = false;
          this.isLoading = true;
          this.allievi = [];
        }),
        switchMap(([searchTerm, annoAccademico, corsoId]) => {
          const hasAnyFilter = Boolean(searchTerm) || Boolean(annoAccademico) || Boolean(corsoId);

          if (!hasAnyFilter) {
            return of([] as Allievo[]);
          }

          if (corsoId && !searchTerm) {
            return this.allievoService.findByCorso(corsoId).pipe(
              map((allievi) => this.applyClientFilters(allievi, annoAccademico, corsoId)),
              catchError((err) => {
                console.error('Errore nella ricerca per corso:', err);
                this.error = 'Errore nella ricerca degli allievi. Riprovare più tardi.';
                return of([] as Allievo[]);
              }),
            );
          }

          if (!searchTerm && annoAccademico) {
            return this.searchByAnnoAccademico(annoAccademico).pipe(
              catchError((err) => {
                console.error('Errore nella ricerca per annualita:', err);
                this.error = 'Errore nella ricerca degli allievi. Riprovare più tardi.';
                return of([] as Allievo[]);
              }),
            );
          }

          return this.allievoService.search(searchTerm).pipe(
            map((allievi) => this.applyClientFilters(allievi, annoAccademico, corsoId)),
            catchError((err) => {
              console.error('Errore nella ricerca allievi:', err);
              this.error = 'Errore nella ricerca degli allievi. Riprovare più tardi.';
              return of([] as Allievo[]);
            }),
          );
        }),
        takeUntil(this.destroy$),
      )
      .subscribe((data) => {
        if (this.isInitialState) {
          this.allievi = [];
          return;
        }

        this.allievi = this.sortAllievi(data);
        this.isLoading = false;
      });
  }

  /** Applica i filtri client-side su annualita e corso ai risultati ricevuti dal backend. */
  private applyClientFilters(
    allievi: Allievo[],
    annoAccademico: string,
    corsoId: number | null,
  ): Allievo[] {
    return allievi.filter((allievo) => {
      const matchesAnno = !annoAccademico || allievo.corsoAnnoAccademico === annoAccademico;
      const matchesCorso = !corsoId || allievo.corsoId === corsoId;
      return matchesAnno && matchesCorso;
    });
  }

  /** Ricerca allievi per sola annualita interrogando i corsi dell'anno selezionato. */
  private searchByAnnoAccademico(annoAccademico: string) {
    const courseIds = this.corsi
      .filter((corso) => corso.annoAccademico === annoAccademico)
      .map((corso) => corso.id);

    if (courseIds.length === 0) {
      return of([] as Allievo[]);
    }

    return forkJoin(courseIds.map((id) => this.allievoService.findByCorso(id))).pipe(
      map((responses) => {
        const uniqueById = new Map<number, Allievo>();

        responses.flat().forEach((allievo) => {
          uniqueById.set(allievo.id, allievo);
        });

        return Array.from(uniqueById.values());
      }),
      map((allievi) => this.applyClientFilters(allievi, annoAccademico, null)),
    );
  }

  /** Apre la pagina di dettaglio dell'allievo selezionato. */
  viewDetail(id: number): void {
    this.router.navigate(['/allievi', id]);
  }

  /** Apre il dettaglio del corso associato all'allievo selezionato. */
  openCorso(corsoId: number | undefined, event: Event): void {
    event.stopPropagation();

    if (!corsoId) {
      return;
    }

    this.router.navigate(['/corsi', corsoId]);
  }

  /** Svuota il campo di ricerca e ripristina lo stato iniziale. */
  clearSearch(event: Event): void {
    event.stopPropagation();
    this.searchControl.setValue('');
  }

  /** Svuota tutti i filtri e riporta la vista allo stato iniziale. */
  clearAllFilters(event: Event): void {
    event.stopPropagation();
    this.searchControl.setValue('');
    this.annoAccademicoControl.setValue('');
    this.corsoIdControl.setValue(null);
  }

  /** Restituisce true quando e' presente almeno un filtro valorizzato. */
  get hasActiveFilters(): boolean {
    return Boolean(this.searchControl.value.trim())
      || Boolean(this.annoAccademicoControl.value)
      || Boolean(this.corsoIdControl.value);
  }

  /** Elenco annualita disponibili, ordinate in modo decrescente. */
  get annualitaDisponibili(): string[] {
    return [...new Set(this.corsi.map((corso) => corso.annoAccademico).filter(Boolean))]
      .sort((first, second) => second.localeCompare(first, 'it', { sensitivity: 'base' }));
  }

  /** Corsi disponibili in base all'annualita selezionata. */
  get filteredCorsi(): Corso[] {
    const annoAccademico = this.annoAccademicoControl.value;

    if (!annoAccademico) {
      return this.corsi;
    }

    return this.corsi.filter((corso) => corso.annoAccademico === annoAccademico);
  }

  /** Etichetta leggibile per il filtro corso. */
  getCorsoFilterLabel(corso: Corso): string {
    return `${corso.nome} - ${corso.annoAccademico}`;
  }

  /** Alterna l'ordinamento locale sulla colonna richiesta. */
  toggleSort(column: SortableColumn): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    this.allievi = this.sortAllievi(this.allievi);
  }

  /** Ordina gli allievi in memoria sulla colonna selezionata. */
  private sortAllievi(allievi: Allievo[]): Allievo[] {
    const directionMultiplier = this.sortDirection === 'asc' ? 1 : -1;

    return [...allievi].sort((first, second) => {
      const firstValue = this.getSortableValue(first, this.sortColumn);
      const secondValue = this.getSortableValue(second, this.sortColumn);

      const valueComparison = firstValue.localeCompare(secondValue, 'it', {
        sensitivity: 'base',
      });

      if (valueComparison !== 0) {
        return valueComparison * directionMultiplier;
      }

      return first.id - second.id;
    });
  }

  /** Restituisce il valore usato dall'ordinamento in base alla colonna scelta. */
  private getSortableValue(allievo: Allievo, column: SortableColumn): string {
    switch (column) {
      case 'nome':
        return allievo.nome || '';
      case 'cognome':
        return allievo.cognome || '';
      case 'codiceFiscale':
        return allievo.codiceFiscale || '';
      case 'corso':
        return this.getCorsoLabel(allievo);
      default:
        return '';
    }
  }

  /** Combinazione leggibile di nome corso e annualità per confronto e visualizzazione. */
  getCorsoLabel(allievo: Allievo): string {
    const corsoNome = allievo.corsoNome || '';
    const corsoAnnoAccademico = allievo.corsoAnnoAccademico || '';

    if (!corsoNome && !corsoAnnoAccademico) {
      return '';
    }

    if (!corsoAnnoAccademico) {
      return corsoNome;
    }

    if (!corsoNome) {
      return corsoAnnoAccademico;
    }

    return `${corsoNome} - ${corsoAnnoAccademico}`;
  }

  /** Restituisce l'icona della colonna attiva o l'indicatore di ordinamento neutro. */
  getSortIcon(column: SortableColumn): string {
    if (this.sortColumn !== column) {
      return 'swap_vert';
    }

    return this.sortDirection === 'asc' ? 'north' : 'south';
  }

  /** Identifica la direzione corrente della colonna per l'attributo aria-label. */
  getSortAriaLabel(column: SortableColumn): string {
    if (this.sortColumn !== column) {
      return `Ordina per ${column}`;
    }

    return this.sortDirection === 'asc'
      ? `Ordina per ${column} in ordine decrescente`
      : `Ordina per ${column} in ordine crescente`;
  }
}

type SortDirection = 'asc' | 'desc';
type SortableColumn = 'nome' | 'cognome' | 'codiceFiscale' | 'corso';