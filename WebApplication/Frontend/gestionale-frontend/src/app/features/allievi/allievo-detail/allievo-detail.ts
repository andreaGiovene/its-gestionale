import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, forkJoin, of } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AllievoService } from '@core/services/allievo.service';
import { CorsoService } from '@core/services/corso.service';
import { Allievo, Corso, UpdateAllievoRequest } from '@shared/models';

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

  form!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  error: string | null = null;
  isNew = false;
  allievo: Allievo | null = null;
  corsi: Corso[] = [];

  ngOnInit(): void {
    this.initForm();

    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params['id'];
      this.isNew = id === 'new';
      this.loadInitialData(this.isNew ? null : parseInt(id, 10));
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initForm(): void {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      cognome: ['', [Validators.required, Validators.maxLength(100)]],
      codiceFiscale: ['', [Validators.maxLength(32)]],
      dataDiNascita: [''],
      corsoId: [''],
      note: ['', [Validators.maxLength(2000)]],
    });
  }

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
              dataDiNascita: allievo.dataDiNascita,
              corsoId: allievo.corsoId ?? '',
              note: allievo.note,
            });
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

  save(): void {
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

  cancel(): void {
    this.router.navigate(['/allievi']);
  }
}