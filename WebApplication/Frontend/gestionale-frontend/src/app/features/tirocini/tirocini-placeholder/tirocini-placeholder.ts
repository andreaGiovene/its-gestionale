import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

interface TirocinioDraft {
  allievo: string;
  azienda: string;
  tutorAziendale: string;
  dataInizio: string;
  dataFine: string;
  stato: string;
}

@Component({
  selector: 'app-tirocini-placeholder',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './tirocini-placeholder.html',
  styleUrl: './tirocini-placeholder.scss',
})
export class TirociniPlaceholder {
  private readonly fb = inject(FormBuilder);

  readonly form = this.fb.group({
    allievo: ['', [Validators.required, Validators.maxLength(100)]],
    azienda: ['', [Validators.required, Validators.maxLength(100)]],
    tutorAziendale: ['', [Validators.required, Validators.maxLength(100)]],
    dataInizio: ['', Validators.required],
    dataFine: ['', Validators.required],
    stato: ['PIANIFICATO', Validators.required],
  });

  bozze: TirocinioDraft[] = [];

  addBozza(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.bozze = [
      {
        allievo: value.allievo ?? '',
        azienda: value.azienda ?? '',
        tutorAziendale: value.tutorAziendale ?? '',
        dataInizio: value.dataInizio ?? '',
        dataFine: value.dataFine ?? '',
        stato: value.stato ?? 'PIANIFICATO',
      },
      ...this.bozze,
    ];

    this.form.reset({ stato: 'PIANIFICATO' });
    this.form.markAsPristine();
    this.form.markAsUntouched();
  }
}