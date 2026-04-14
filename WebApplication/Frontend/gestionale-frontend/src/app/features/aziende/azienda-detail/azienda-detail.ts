import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AziendaService } from '@core/services/azienda.service';
import { Azienda, UpdateAziendaRequest } from '@shared/models';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-azienda-detail',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './azienda-detail.html',
  styleUrl: './azienda-detail.scss',
})
export class AziendaDetail implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly aziendaService = inject(AziendaService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  form!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  error: string | null = null;
  isNew = false;
  azienda: Azienda | null = null;

  ngOnInit(): void {
    this.initForm();
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params['id'];
      if (id === 'new') {
        this.isNew = true;
        this.isLoading = false;
      } else {
        this.loadAzienda(parseInt(id, 10));
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initForm(): void {
    this.form = this.fb.group({
      ragioneSociale: ['', [Validators.required, Validators.maxLength(100)]],
      partitaIva: ['', [Validators.required, Validators.maxLength(20)]],
      telefono: ['', [Validators.maxLength(20)]],
      email: ['', [Validators.maxLength(100)]],
      indirizzo: ['', [Validators.maxLength(150)]],
      cap: ['', [Validators.maxLength(10)]],
      citta: ['', [Validators.maxLength(100)]],
    });
  }

  private loadAzienda(id: number): void {
    this.isLoading = true;
    this.error = null;

    this.aziendaService
      .findById(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.azienda = data;
          this.form.patchValue({
            ragioneSociale: data.ragioneSociale,
            partitaIva: data.partitaIva,
            telefono: data.telefono,
            email: data.email,
            indirizzo: data.indirizzo,
            cap: data.cap,
            citta: data.citta,
          });
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento azienda:', err);
          this.error = 'Errore nel caricamento dell\'azienda.';
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

    const payload: UpdateAziendaRequest = this.form.value;

    const operation$ = this.isNew
      ? this.aziendaService.create(payload)
      : this.aziendaService.update(this.azienda!.id, payload);

    operation$.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/aziende']);
      },
      error: (err) => {
        console.error('Errore nel salvataggio:', err);
        this.error = 'Errore nel salvataggio dell\'azienda.';
        this.isSubmitting = false;
      },
    });
  }

  cancel(): void {
    this.router.navigate(['/aziende']);
  }
}
