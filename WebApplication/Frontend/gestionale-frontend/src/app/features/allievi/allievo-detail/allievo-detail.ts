import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, combineLatest, forkJoin, of } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AllievoService } from '@core/services/allievo.service';
import { CorsoService } from '@core/services/corso.service';
import { Allievo, Corso, UpdateAllievoRequest } from '@shared/models';

/**
 * Form di creazione/modifica dell'allievo.
 * Carica in parallelo l'elenco corsi e il dettaglio record, poi salva il payload normalizzato.
 */
@Component({
  selector: 'app-allievo-detail',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './allievo-detail.html',
  styleUrl: './allievo-detail.scss',
})
export class AllievoDetail implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly allievoService = inject(AllievoService);
  private readonly corsoService = inject(CorsoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  /** Form principale del dettaglio allievo. */
  form!: FormGroup;
  /** Stato di caricamento dei dati iniziali. */
  isLoading = true;
  /** Stato di invio del salvataggio. */
  isSubmitting = false;
  /** Messaggio di errore mostrato nella UI. */
  error: string | null = null;
  /** Indica se il componente sta creando un nuovo allievo. */
  isNew = false;
  /** Indica se il componente è in modalità modifica (create/edit). */
  isEditMode = false;
  /** Entità caricata in modalità modifica. */
  allievo: Allievo | null = null;
  /** Elenco corsi da usare nella select di associazione. */
  corsi: Corso[] = [];

  /** Indica se la vista corrente è in sola lettura. */
  get isReadOnly(): boolean {
    return !this.isNew && !this.isEditMode;
  }

  /** Restituisce il corso selezionato nel form, se presente. */
  get selectedCorso(): Corso | undefined {
    const corsoId = Number(this.form?.get('corsoId')?.value);
    if (!corsoId) {
      return undefined;
    }

    return this.corsi.find((corso) => corso.id === corsoId);
  }

  /** Ritorna un'etichetta leggibile per il corso, includendo l'annualità. */
  getCorsoLabel(corso: Corso): string {
    return corso.annoAccademico ? `${corso.nome} - ${corso.annoAccademico}` : corso.nome;
  }

  /** Inizializza il form e reagisce ai cambi di parametro della route. */
  ngOnInit(): void {
    this.initForm();

    combineLatest([this.route.url, this.route.paramMap])
      .pipe(takeUntil(this.destroy$))
      .subscribe(([segments, paramMap]) => {
        const routePath = this.route.snapshot.routeConfig?.path ?? '';
        const idParam = paramMap.get('id');

        this.isNew = routePath === 'allievi/new' || idParam === 'new';
        this.isEditMode = this.isNew || routePath === 'allievi/:id/edit' || segments.some((segment) => segment.path === 'edit');

        if (this.isNew) {
          this.loadInitialData(null);
          return;
        }

        const allievoId = idParam ? Number(idParam) : NaN;
        if (Number.isNaN(allievoId)) {
          this.error = 'Identificativo allievo non valido.';
          this.isLoading = false;
          return;
        }

        this.loadInitialData(allievoId);
      });
  }

  /** Chiude tutte le subscription legate al ciclo vita del componente. */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /** Definisce campi e validazioni del form allievo. */
  private initForm(): void {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      cognome: ['', [Validators.required, Validators.maxLength(100)]],
      codiceFiscale: ['', [Validators.maxLength(32)]],
      telefono: ['', [Validators.maxLength(20)]],
      dataDiNascita: [''],
      corsoId: [''],
      note: ['', [Validators.maxLength(2000)]],
    });
  }

  /**
   * Carica corsi e, se presente, allievo esistente; in modifica precompila la form.
   * @param allievoId identificativo dell'allievo oppure null in creazione
   */
  private loadInitialData(allievoId: number | null): void {
    this.isLoading = true;
    this.error = null;

    const allievoRequest = allievoId === null ? of(null) : this.allievoService.findById(allievoId);

    forkJoin({
      corsi: this.corsoService.findAll(),
      allievo: allievoRequest,
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({ corsi, allievo }) => {
          this.corsi = corsi;
          this.allievo = allievo;

          if (allievo) {
            this.form.patchValue({
              nome: allievo.nome,
              cognome: allievo.cognome,
              codiceFiscale: allievo.codiceFiscale,
              telefono: allievo.telefono,
              dataDiNascita: allievo.dataDiNascita,
              corsoId: allievo.corsoId ?? '',
              note: allievo.note,
            });
          }

          if (this.isReadOnly) {
            this.form.disable({ emitEvent: false });
          } else {
            this.form.enable({ emitEvent: false });
          }

          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento dettaglio allievo:', err);
          this.error = 'Errore nel caricamento dei dati dell\'allievo.';
          this.isLoading = false;
        },
      });
  }

  /** Valida, normalizza e invia i dati al servizio di persistenza. */
  save(): void {
    if (this.isReadOnly) {
      return;
    }

    if (this.form.invalid) {
      alert('Form non valido: compila tutti i campi richiesti');
      return;
    }

    this.isSubmitting = true;
    this.error = null;

    const raw = this.form.value;
    const payload: UpdateAllievoRequest = {
      nome: raw.nome,
      cognome: raw.cognome,
      codiceFiscale: raw.codiceFiscale || undefined,
      telefono: raw.telefono || undefined,
      dataDiNascita: raw.dataDiNascita || undefined,
      note: raw.note || undefined,
      corsoId: raw.corsoId ? Number(raw.corsoId) : null,
    };

    const operation$ = this.isNew
      ? this.allievoService.create(payload)
      : this.allievoService.update(this.allievo!.id, payload);

    operation$.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/allievi']);
      },
      error: (err) => {
        console.error('Errore nel salvataggio allievo:', err);
        this.error = 'Errore nel salvataggio dell\'allievo.';
        this.isSubmitting = false;
      },
    });
  }

  /** Torna alla lista senza mantenere eventuali modifiche non salvate. */
  cancel(): void {
    this.router.navigate(['/allievi']);
  }

  /** Passa dalla vista dettaglio alla modalità modifica. */
  goToEdit(): void {
    if (!this.allievo?.id) {
      return;
    }

    this.router.navigate(['/allievi', this.allievo.id, 'edit']);
  }

  goToColloquiStorico(): void {
    if (!this.allievo?.id) {
      return;
    }

    this.router.navigate(['/colloqui/allievo', this.allievo.id]);
  }
}