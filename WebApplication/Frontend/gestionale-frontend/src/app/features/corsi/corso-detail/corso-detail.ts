import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CorsoService } from '@core/services/corso.service';
import { Corso, UpdateCorsoRequest } from '@shared/models';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-corso-detail',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './corso-detail.html',
  styleUrl: './corso-detail.scss',
})
/**
 * Gestisce creazione e modifica di un corso tramite form reattiva.
 */
export class CorsoDetail implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly corsoService = inject(CorsoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  /** Subject usato per chiudere le subscription al destroy del componente. */
  private readonly destroy$ = new Subject<void>();

  /** Form principale del dettaglio corso. */
  form!: FormGroup;
  /** Flag caricamento iniziale dati. */
  isLoading = true;
  /** Flag invio/salvataggio in corso. */
  isSubmitting = false;
  /** Messaggio errore da mostrare in UI. */
  error: string | null = null;
  /** Indica se la pagina è in modalità creazione (`/corsi/new`). */
  isNew = false;
  /** Corso corrente in modalità modifica, nullo in creazione. */
  corso: Corso | null = null;

  /**
   * Inizializza form e determina modalità nuova/modifica in base alla route.
   */
  ngOnInit(): void {
    this.initForm();
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params['id'];
      if (id === 'new') {
        this.isNew = true;
        this.isLoading = false;
      } else {
        this.loadCorso(parseInt(id, 10));
      }
    });
  }

  /**
   * Rilascia le subscription attive legate al ciclo vita del componente.
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Crea la struttura e le validazioni della form corso.
   */
  private initForm(): void {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(255)]],
      annoAccademico: ['', [Validators.maxLength(32)]],
      stato: ['', [Validators.required, Validators.maxLength(32)]],
    });
  }

  /**
   * Carica un corso esistente e popola la form in modalità modifica.
   * @param id identificativo del corso
   */
  private loadCorso(id: number): void {
    this.isLoading = true;
    this.error = null;

    this.corsoService
      .findById(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.corso = data;
          this.form.patchValue({
            nome: data.nome,
            annoAccademico: data.annoAccademico,
            stato: data.stato,
          });
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento corso:', err);
          this.error = 'Errore nel caricamento del corso.';
          this.isLoading = false;
        },
      });
  }

  /**
   * Salva il corso: create in modalità new, update in modalità edit.
   */
  save(): void {
    if (this.form.invalid) {
      alert('Form non valido compila tutti i campi richiesti');
      return;
    }

    this.isSubmitting = true;
    this.error = null;

    const payload: UpdateCorsoRequest = this.form.value;

    const operation$ = this.isNew
      ? this.corsoService.create(payload)
      : this.corsoService.update(this.corso!.id, payload);

    operation$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (saved) => {
        this.isSubmitting = false;
        this.router.navigate(['/corsi']);
      },
      error: (err) => {
        console.error('Errore nel salvataggio:', err);
        this.error = 'Errore nel salvataggio del corso.';
        this.isSubmitting = false;
      },
    });
  }

  /**
   * Torna alla lista corsi senza salvare modifiche.
   */
  cancel(): void {
    this.router.navigate(['/corsi']);
  }
}
