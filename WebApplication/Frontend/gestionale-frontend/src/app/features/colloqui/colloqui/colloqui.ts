import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subject, forkJoin } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AllievoService } from '@core/services/allievo.service';
import { AziendaService } from '@core/services/azienda.service';
import { ColloquioService } from '@core/services/colloquio.service';
import { Allievo, Azienda, Colloquio, StatoEsitoColloquio } from '@shared/models';

interface EsitoOption {
  value: StatoEsitoColloquio;
  label: string;
}

/**
 * Pagina di registrazione colloqui collegata al backend.
 * Carica allievi/aziende per le select e invia il payload all'API /api/colloqui.
 */
@Component({
  selector: 'app-colloqui',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './colloqui.html',
  styleUrl: './colloqui.scss',
})
export class Colloqui implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly allievoService = inject(AllievoService);
  private readonly aziendaService = inject(AziendaService);
  private readonly colloquioService = inject(ColloquioService);
  private readonly destroy$ = new Subject<void>();

  readonly esitoOptions: EsitoOption[] = [
    { value: 'IN_ATTESA', label: 'In attesa' },
    { value: 'POSITIVO', label: 'Positivo' },
    { value: 'NEGATIVO', label: 'Negativo' },
    { value: 'RITIRATO', label: 'Ritirato' },
    { value: 'NON_PRESENTATO', label: 'Non presentato' },
  ];

  allievi: Allievo[] = [];
  aziende: Azienda[] = [];

  isLoading = true;
  isLoadingList = false;
  isSubmitting = false;
  error: string | null = null;
  listError: string | null = null;
  successMessage: string | null = null;
  ultimoCreato: Colloquio | null = null;
  colloqui: Colloquio[] = [];
  readonly pageSizeOptions = [5, 10, 20];
  pageSize = 5;
  currentPage = 1;

  /** Form di registrazione colloquio con payload allineato al backend. */
  readonly form = this.fb.group({
    dataColloquio: [this.todayIsoDate(), Validators.required],
    allievoId: [null as number | null, Validators.required],
    aziendaId: [null as number | null, Validators.required],
    esito: ['IN_ATTESA' as StatoEsitoColloquio, Validators.required],
    noteFeedback: ['', [Validators.maxLength(300)]],
  });

  /** Filtri lista colloqui (supporta filtro combinato allievo + azienda). */
  readonly filterForm = this.fb.group({
    allievoId: [null as number | null],
    aziendaId: [null as number | null],
  });

  ngOnInit(): void {
    this.loadLookupData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /** Invia la registrazione colloquio all'API backend. */
  registerColloquio(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.error = null;
    this.successMessage = null;

    const value = this.form.getRawValue();
    const allievoId = Number(value.allievoId);
    const aziendaId = Number(value.aziendaId);

    this.colloquioService
      .create(allievoId, aziendaId, {
        dataColloquio: value.dataColloquio ?? this.todayIsoDate(),
        esito: value.esito ?? 'IN_ATTESA',
        noteFeedback: value.noteFeedback?.trim() ? value.noteFeedback.trim() : undefined,
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (creato) => {
          this.ultimoCreato = creato;
          this.successMessage = 'Colloquio registrato con successo.';
          this.isSubmitting = false;
          this.resetFormAfterSubmit();
          this.loadColloqui();
        },
        error: (err) => {
          console.error('Errore registrazione colloquio:', err);
          this.error = 'Errore durante la registrazione del colloquio.';
          this.isSubmitting = false;
        },
      });
  }

  getAllievoLabel(allievo: Allievo): string {
    return `${allievo.nome} ${allievo.cognome}`.trim();
  }

  applyFilters(): void {
    this.currentPage = 1;
    this.loadColloqui();
  }

  resetFilters(): void {
    this.filterForm.reset({
      allievoId: null,
      aziendaId: null,
    });
    this.currentPage = 1;
    this.loadColloqui();
  }

  getColloquioAllievo(colloquio: Colloquio): string {
    if (colloquio.allievoNome || colloquio.allievano) {
      return `${colloquio.allievoNome ?? ''} ${colloquio.allievano ?? ''}`.trim();
    }

    if (colloquio.allievoId) {
      const match = this.allievi.find((allievo) => allievo.id === colloquio.allievoId);
      if (match) {
        return this.getAllievoLabel(match);
      }
    }

    return '-';
  }

  getEsitoLabel(esito: StatoEsitoColloquio): string {
    return this.esitoOptions.find((option) => option.value === esito)?.label ?? esito;
  }

  get sortedColloqui(): Colloquio[] {
    return [...this.colloqui].sort((left, right) => {
      const leftTime = new Date(left.dataColloquio).getTime();
      const rightTime = new Date(right.dataColloquio).getTime();

      if (leftTime !== rightTime) {
        return rightTime - leftTime;
      }

      return (right.id ?? 0) - (left.id ?? 0);
    });
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.sortedColloqui.length / this.pageSize));
  }

  get paginatedColloqui(): Colloquio[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.sortedColloqui.slice(startIndex, startIndex + this.pageSize);
  }

  get hasPreviousPage(): boolean {
    return this.currentPage > 1;
  }

  get hasNextPage(): boolean {
    return this.currentPage < this.totalPages;
  }

  onPageSizeChange(value: string | number): void {
    const nextPageSize = Number(value);
    if (!Number.isFinite(nextPageSize) || nextPageSize <= 0) {
      return;
    }

    this.pageSize = nextPageSize;
    this.currentPage = 1;
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) {
      return;
    }

    this.currentPage = page;
  }

  previousPage(): void {
    this.goToPage(this.currentPage - 1);
  }

  nextPage(): void {
    this.goToPage(this.currentPage + 1);
  }

  trackByColloquioId(_: number, colloquio: Colloquio): number {
    return colloquio.id;
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
          this.loadColloqui();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore caricamento lookup colloqui:', err);
          this.error = 'Impossibile caricare allievi e aziende.';
          this.isLoading = false;
        },
      });
  }

  private loadColloqui(): void {
    const value = this.filterForm.getRawValue();
    const allievoId = value.allievoId;
    const aziendaId = value.aziendaId;

    this.isLoadingList = true;
    this.listError = null;

    this.colloquioService
      .findAll({
        allievoId: allievoId ?? undefined,
        aziendaId: aziendaId ?? undefined,
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (items) => {
          this.colloqui = items;
          this.currentPage = 1;
          this.isLoadingList = false;
        },
        error: (err) => {
          console.error('Errore caricamento lista colloqui:', err);
          this.listError = 'Errore durante il caricamento della lista colloqui.';
          this.isLoadingList = false;
        },
      });
  }

  private resetFormAfterSubmit(): void {
    this.form.reset({
      dataColloquio: this.todayIsoDate(),
      allievoId: null,
      aziendaId: null,
      esito: 'IN_ATTESA',
      noteFeedback: '',
    });
    this.form.markAsPristine();
    this.form.markAsUntouched();
  }

  private todayIsoDate(): string {
    return new Date().toISOString().slice(0, 10);
  }
}
