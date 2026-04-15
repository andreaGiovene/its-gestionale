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

/**
 * Placeholder operativo per il flusso tirocini.
 * Modella una raccolta locale di bozze in attesa del backend definitivo.
 */
@Component({
  selector: 'app-tirocini-placeholder',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './tirocini-placeholder.html',
  styleUrl: './tirocini-placeholder.scss',
})
export class TirociniPlaceholder {
  private readonly fb = inject(FormBuilder);

  /** Form locale per l'inserimento di una bozza di tirocinio. */
  readonly form = this.fb.group({
    allievo: ['', [Validators.required, Validators.maxLength(100)]],
    azienda: ['', [Validators.required, Validators.maxLength(100)]],
    tutorAziendale: ['', [Validators.required, Validators.maxLength(100)]],
    dataInizio: ['', Validators.required],
    dataFine: ['', Validators.required],
    stato: ['PIANIFICATO', Validators.required],
  });

  /** Elenco delle bozze salvate solo lato client. */
  bozze: TirocinioDraft[] = [];

  /** Valida la form e aggiunge la bozza in testa alla lista locale. */
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