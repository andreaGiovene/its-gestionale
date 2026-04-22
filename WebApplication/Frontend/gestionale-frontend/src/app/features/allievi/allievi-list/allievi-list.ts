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
import { Allievo } from '@shared/models';
import { catchError, debounceTime, distinctUntilChanged, map, of, Subject, switchMap, takeUntil, tap } from 'rxjs';

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
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  /** Campo di ricerca reattivo per nome, cognome o codice fiscale. */
  readonly searchControl = new FormControl('', { nonNullable: true });

  /** Dati caricati dal backend e mostrati in tabella. */
  allievi: Allievo[] = [];
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

  /** Ascolta il campo di ricerca e lancia le query in modo reattivo con debounce. */
  private setupSearchStream(): void {
    this.searchControl.valueChanges
      .pipe(
        map((value) => value.trim()),
        debounceTime(300),
        distinctUntilChanged(),
        tap((searchTerm) => {
          this.error = null;

          if (!searchTerm) {
            this.setInitialState();
            return;
          }

          this.isInitialState = false;
          this.isLoading = true;
          this.allievi = [];
        }),
        switchMap((searchTerm) => {
          if (!searchTerm) {
            return of([] as Allievo[]);
          }

          return this.allievoService.search(searchTerm).pipe(
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