import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { forkJoin, Subject, distinctUntilChanged, startWith, takeUntil } from 'rxjs';

import { AllievoService } from '@core/services/allievo.service';
import { AziendaService } from '@core/services/azienda.service';
import { TirocinioService } from '@core/services/tirocinio.service';
import { Allievo, Azienda, CreateTirocinioRequest, StatoTirocinio, Tirocinio } from '@shared/models';

interface StatoOption {
  value: StatoTirocinio;
  label: string;
}

/**
 * Pagina di gestione dei tirocini collegata al backend.
 * Permette di registrare, aggiornare e filtrare i tirocini tramite l'API /api/tirocini.
 */
@Component({
  selector: 'app-tirocini',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './tirocini.html',
  styleUrl: './tirocini.scss',
})
export class Tirocini implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly allievoService = inject(AllievoService);
  private readonly aziendaService = inject(AziendaService);
  private readonly tirocinioService = inject(TirocinioService);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  readonly statoOptions: StatoOption[] = [
    { value: 'IN_CORSO', label: 'In corso' },
    { value: 'CONCLUSO', label: 'Concluso' },
    { value: 'INTERROTTO', label: 'Interrotto' },
  ];

  allievi: Allievo[] = [];
  aziende: Azienda[] = [];
  tirocini: Tirocinio[] = [];
  filteredAllievi: Allievo[] = [];
  filteredAziende: Azienda[] = [];
  filteredFilterAllievi: Allievo[] = [];
  filteredFilterAziende: Azienda[] = [];

  isLoading = true;
  isLoadingList = false;
  isSubmitting = false;
  isEditing = false;
  error: string | null = null;
  listError: string | null = null;
  successMessage: string | null = null;
  editingId: number | null = null;

  readonly allievoSearchControl = this.fb.nonNullable.control('');
  readonly aziendaSearchControl = this.fb.nonNullable.control('');
  readonly filterAllievoSearchControl = this.fb.nonNullable.control('');
  readonly filterAziendaSearchControl = this.fb.nonNullable.control('');

  readonly form = this.fb.group({
    allievoId: [null as number | null, Validators.required],
    aziendaId: [null as number | null, Validators.required],
    dataInizio: [this.todayIsoDate(), Validators.required],
    dataFine: [''],
    tipo: ['', [Validators.maxLength(50)]],
    frequenza: ['', [Validators.maxLength(50)]],
    esito: ['IN_CORSO' as StatoTirocinio, Validators.required],
  });

  readonly filterForm = this.fb.group({
    allievoId: [null as number | null],
    aziendaId: [null as number | null],
    start: [''],
    end: [''],
  });

  ngOnInit(): void {
    this.bindSearchControls();
    this.loadLookupData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  startEdit(tirocinio: Tirocinio): void {
    this.isEditing = true;
    this.editingId = tirocinio.id;
    this.successMessage = null;
    this.error = null;

    this.form.reset({
      allievoId: tirocinio.allievoId ?? null,
      aziendaId: tirocinio.aziendaId ?? null,
      dataInizio: tirocinio.dataInizio,
      dataFine: tirocinio.dataFine ?? '',
      tipo: tirocinio.tipo ?? '',
      frequenza: tirocinio.frequenza ?? '',
      esito: tirocinio.esito,
    });
    this.form.controls.allievoId.disable();
    this.form.controls.aziendaId.disable();
    this.allievoSearchControl.setValue(this.getAllievoLabelById(tirocinio.allievoId), { emitEvent: false });
    this.aziendaSearchControl.setValue(this.getAziendaLabelById(tirocinio.aziendaId), { emitEvent: false });
    this.filteredAllievi = [];
    this.filteredAziende = [];
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.editingId = null;
    this.form.controls.allievoId.enable();
    this.form.controls.aziendaId.enable();
    this.form.reset({
      dataInizio: this.todayIsoDate(),
      esito: 'IN_CORSO',
      dataFine: '',
      tipo: '',
      frequenza: '',
      allievoId: null,
      aziendaId: null,
    });
    this.allievoSearchControl.setValue('', { emitEvent: false });
    this.aziendaSearchControl.setValue('', { emitEvent: false });
    this.filteredAllievi = [];
    this.filteredAziende = [];
  }

  saveTirocinio(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const allievoId = Number(value.allievoId);
    const aziendaId = Number(value.aziendaId);
    const payload: CreateTirocinioRequest = {
      dataInizio: value.dataInizio ?? this.todayIsoDate(),
      dataFine: value.dataFine?.trim() ? value.dataFine : undefined,
      tipo: value.tipo?.trim() ? value.tipo.trim() : undefined,
      frequenza: value.frequenza?.trim() ? value.frequenza.trim() : undefined,
      esito: value.esito ?? 'IN_CORSO',
    };

    this.isSubmitting = true;
    this.error = null;
    this.successMessage = null;

    const request$ = this.isEditing && this.editingId !== null
      ? this.tirocinioService.update(this.editingId, payload)
      : this.tirocinioService.create(allievoId, aziendaId, payload);

    request$
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.successMessage = this.isEditing ? 'Tirocinio aggiornato con successo.' : 'Tirocinio registrato con successo.';
          this.isSubmitting = false;
          this.form.controls.allievoId.enable();
          this.form.controls.aziendaId.enable();
          this.isEditing = false;
          this.editingId = null;
          this.form.reset({
            dataInizio: this.todayIsoDate(),
            esito: 'IN_CORSO',
            dataFine: '',
            tipo: '',
            frequenza: '',
            allievoId: null,
            aziendaId: null,
          });
          this.allievoSearchControl.setValue('', { emitEvent: false });
          this.aziendaSearchControl.setValue('', { emitEvent: false });
          this.loadTirocini();
        },
        error: (err) => {
          console.error('Errore salvataggio tirocinio:', err);
          this.error = err?.error?.message ?? 'Errore durante il salvataggio del tirocinio.';
          this.isSubmitting = false;
        },
      });
  }

  deleteTirocinio(tirocinio: Tirocinio): void {
    if (!confirm('Eliminare questo tirocinio?')) {
      return;
    }

    this.tirocinioService
      .delete(tirocinio.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.successMessage = 'Tirocinio eliminato con successo.';
          if (this.editingId === tirocinio.id) {
            this.cancelEdit();
          }
          this.loadTirocini();
        },
        error: (err) => {
          console.error('Errore eliminazione tirocinio:', err);
          this.error = 'Errore durante l\'eliminazione del tirocinio.';
        },
      });
  }

  applyFilters(): void {
    this.loadTirocini();
  }

  resetFilters(): void {
    this.filterForm.reset({
      allievoId: null,
      aziendaId: null,
      start: '',
      end: '',
    });
    this.filterAllievoSearchControl.setValue('', { emitEvent: false });
    this.filterAziendaSearchControl.setValue('', { emitEvent: false });
    this.filteredFilterAllievi = [];
    this.filteredFilterAziende = [];
    this.loadTirocini();
  }

  getAllievoLabel(allievo: Allievo): string {
    return `${allievo.nome} ${allievo.cognome}`.trim();
  }

  getAllievoLabelById(allievoId: number | null | undefined): string {
    if (!allievoId) {
      return '';
    }

    return this.getAllievoLabel(this.allievi.find((allievo) => allievo.id === allievoId) ?? { id: allievoId, nome: '', cognome: '' });
  }

  getAziendaLabelById(aziendaId: number | null | undefined): string {
    if (!aziendaId) {
      return '';
    }

    return this.aziende.find((azienda) => azienda.id === aziendaId)?.ragioneSociale ?? '';
  }

  getTirocinioAllievo(tirocinio: Tirocinio): string {
    if (tirocinio.allievoId) {
      const match = this.allievi.find((allievo) => allievo.id === tirocinio.allievoId);
      if (match) {
        return this.getAllievoLabel(match);
      }
    }

    return '-';
  }

  getTirocinioAzienda(tirocinio: Tirocinio): string {
    if (tirocinio.aziendaId) {
      const match = this.aziende.find((azienda) => azienda.id === tirocinio.aziendaId);
      if (match) {
        return match.ragioneSociale;
      }
    }

    return '-';
  }

  getStatoLabel(esito: StatoTirocinio): string {
    return this.statoOptions.find((option) => option.value === esito)?.label ?? esito;
  }

  trackByTirocinioId(_: number, tirocinio: Tirocinio): number {
    return tirocinio.id;
  }

  goToAllievo(allievoId?: number): void {
    if (!allievoId) {
      return;
    }

    this.router.navigate(['/allievi', allievoId]);
  }

  goToAzienda(aziendaId?: number): void {
    if (!aziendaId) {
      return;
    }

    this.router.navigate(['/aziende', aziendaId]);
  }

  selectAllievo(allievo: Allievo): void {
    this.form.controls.allievoId.setValue(allievo.id);
    this.allievoSearchControl.setValue(this.getAllievoLabel(allievo), { emitEvent: false });
    this.filteredAllievi = [];
  }

  selectAzienda(azienda: Azienda): void {
    this.form.controls.aziendaId.setValue(azienda.id);
    this.aziendaSearchControl.setValue(azienda.ragioneSociale, { emitEvent: false });
    this.filteredAziende = [];
  }

  selectFilterAllievo(allievo: Allievo): void {
    this.filterForm.controls.allievoId.setValue(allievo.id);
    this.filterAllievoSearchControl.setValue(this.getAllievoLabel(allievo), { emitEvent: false });
    this.filteredFilterAllievi = [];
  }

  selectFilterAzienda(azienda: Azienda): void {
    this.filterForm.controls.aziendaId.setValue(azienda.id);
    this.filterAziendaSearchControl.setValue(azienda.ragioneSociale, { emitEvent: false });
    this.filteredFilterAziende = [];
  }

  private loadLookupData(): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      allievi: this.allievoService.findAll(),
      aziende: this.aziendaService.findAll(),
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({ allievi, aziende }) => {
          this.allievi = allievi;
          this.aziende = aziende;
          this.refreshSearchResults();
          this.loadTirocini();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore caricamento lookup tirocini:', err);
          this.error = 'Impossibile caricare allievi e aziende.';
          this.isLoading = false;
        },
      });
  }

  private loadTirocini(): void {
    const value = this.filterForm.getRawValue();

    this.isLoadingList = true;
    this.listError = null;

    this.tirocinioService
      .findAll({
        allievoId: value.allievoId ?? undefined,
        aziendaId: value.aziendaId ?? undefined,
        start: value.start?.trim() ? value.start : undefined,
        end: value.end?.trim() ? value.end : undefined,
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (items) => {
          this.tirocini = items;
          this.isLoadingList = false;
        },
        error: (err) => {
          console.error('Errore caricamento lista tirocini:', err);
          this.listError = 'Errore durante il caricamento dei tirocini.';
          this.isLoadingList = false;
        },
      });
  }

  private bindSearchControls(): void {
    this.allievoSearchControl.valueChanges
      .pipe(startWith(this.allievoSearchControl.value), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((term) => {
        if (!this.isEditing) {
          this.form.controls.allievoId.setValue(null);
        }

        this.filteredAllievi = this.filterAllievi(term, this.allievi);
      });

    this.aziendaSearchControl.valueChanges
      .pipe(startWith(this.aziendaSearchControl.value), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((term) => {
        if (!this.isEditing) {
          this.form.controls.aziendaId.setValue(null);
        }

        this.filteredAziende = this.filterAziende(term, this.aziende);
      });

    this.filterAllievoSearchControl.valueChanges
      .pipe(startWith(this.filterAllievoSearchControl.value), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((term) => {
        this.filteredFilterAllievi = this.filterAllievi(term, this.allievi);
      });

    this.filterAziendaSearchControl.valueChanges
      .pipe(startWith(this.filterAziendaSearchControl.value), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((term) => {
        this.filteredFilterAziende = this.filterAziende(term, this.aziende);
      });
  }

  private refreshSearchResults(): void {
    this.filteredAllievi = this.filterAllievi(this.allievoSearchControl.value, this.allievi);
    this.filteredAziende = this.filterAziende(this.aziendaSearchControl.value, this.aziende);
    this.filteredFilterAllievi = this.filterAllievi(this.filterAllievoSearchControl.value, this.allievi);
    this.filteredFilterAziende = this.filterAziende(this.filterAziendaSearchControl.value, this.aziende);
  }

  private filterAllievi(term: string, source: Allievo[]): Allievo[] {
    const normalizedTerm = term.trim().toLowerCase();

    if (!normalizedTerm) {
      return [];
    }

    return source
      .filter((allievo) => {
        const haystack = [
          allievo.nome,
          allievo.cognome,
          allievo.email,
          allievo.codiceFiscale,
          allievo.telefono,
        ]
          .filter(Boolean)
          .join(' ')
          .toLowerCase();

        return haystack.includes(normalizedTerm);
      })
      .slice(0, 8);
  }

  private filterAziende(term: string, source: Azienda[]): Azienda[] {
    const normalizedTerm = term.trim().toLowerCase();

    if (!normalizedTerm) {
      return [];
    }

    return source
      .filter((azienda) => {
        const haystack = [
          azienda.ragioneSociale,
          azienda.partitaIva,
          azienda.email,
          azienda.citta,
          azienda.indirizzo,
        ]
          .filter(Boolean)
          .join(' ')
          .toLowerCase();

        return haystack.includes(normalizedTerm);
      })
      .slice(0, 8);
  }

  private todayIsoDate(): string {
    return new Date().toISOString().split('T')[0];
  }
}
