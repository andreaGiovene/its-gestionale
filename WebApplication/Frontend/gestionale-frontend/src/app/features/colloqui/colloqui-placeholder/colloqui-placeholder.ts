import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

interface ColloquioDraft {
  data: string;
  ora: string;
  allievo: string;
  azienda: string;
  esito?: string;
}

/**
 * Placeholder operativo per il flusso colloqui.
 * Permette di simulare inserimenti locali finché non è disponibile l'integrazione API.
 */
@Component({
  selector: 'app-colloqui-placeholder',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './colloqui-placeholder.html',
  styleUrl: './colloqui-placeholder.scss',
})
export class ColloquiPlaceholder {
  private readonly fb = inject(FormBuilder);

  /** Form locale per raccogliere i dati minimi di un colloquio. */
  readonly form = this.fb.group({
    data: ['', Validators.required],
    ora: ['', Validators.required],
    allievo: ['', [Validators.required, Validators.maxLength(100)]],
    azienda: ['', [Validators.required, Validators.maxLength(100)]],
    esito: ['', Validators.maxLength(300)],
  });

  /** Elenco delle bozze inserite nella sessione corrente. */
  bozze: ColloquioDraft[] = [];

  /** Valida la form e aggiunge una nuova bozza alla lista locale. */
  addBozza(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.bozze = [
      {
        data: value.data ?? '',
        ora: value.ora ?? '',
        allievo: value.allievo ?? '',
        azienda: value.azienda ?? '',
        esito: value.esito ?? '',
      },
      ...this.bozze,
    ];

    this.form.reset();
  }
}