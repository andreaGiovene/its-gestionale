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
export class CorsoDetail implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly corsoService = inject(CorsoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  form!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  error: string | null = null;
  isNew = false;
  corso: Corso | null = null;

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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initForm(): void {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(255)]],
      annoAccademico: ['', [Validators.maxLength(32)]],
      stato: ['', [Validators.required, Validators.maxLength(32)]],
    });
  }

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

  cancel(): void {
    this.router.navigate(['/corsi']);
  }
}
