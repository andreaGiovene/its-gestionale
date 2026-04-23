import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AziendaService } from '@core/services/azienda.service';
import { Azienda, Contatto, CreateContattoRequest } from '@shared/models';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { forkJoin } from 'rxjs';

/**
 * Pagina di dettaglio azienda con visualizzazione anagrafica e contatti.
 * Mostra i dati in sola lettura con pulsante "Modifica" e "Torna".
 */
@Component({
  selector: 'app-azienda-view',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './azienda-view.html',
  styleUrl: './azienda-view.scss',
})
export class AziendaView implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly aziendaService = inject(AziendaService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  /** Dati dell'azienda. */
  azienda: Azienda | null = null;
  /** Contatti associati all'azienda. */
  contatti: Contatto[] = [];
  /** Stato di caricamento. */
  isLoading = true;
  /** Messaggio di errore. */
  error: string | null = null;
  /** Stato invio form contatto. */
  isSubmittingContatto = false;
  /** Errore specifico inserimento contatto. */
  contattoError: string | null = null;

  readonly ruoliContatto: Contatto['ruolo'][] = [
    'TITOLARE',
    'DIRETTORE',
    'RESPONSABILE_HR',
    'RESPONSABILE_TIROCINI',
    'TUTOR_AZIENDALE',
  ];

  readonly contattoForm = this.fb.group({
    nome: ['', [Validators.required, Validators.maxLength(50)]],
    cognome: ['', [Validators.maxLength(50)]],
    ruolo: [null as Contatto['ruolo'] | null, [Validators.required]],
    telefono: ['', [Validators.maxLength(20)]],
    email: ['', [Validators.email, Validators.maxLength(100)]],
  });

  /** Carica i dati dell'azienda e dei contatti. */
  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = parseInt(params['id'], 10);
      if (!Number.isNaN(id)) {
        this.loadAziendaAndContatti(id);
      } else {
        this.error = 'ID azienda non valido.';
        this.isLoading = false;
      }
    });
  }

  /** Chiude le subscription attive quando il componente viene distrutto. */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /** Carica l'azienda e i relativi contatti in parallelo. */
  private loadAziendaAndContatti(id: number): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      azienda: this.aziendaService.findById(id),
      contatti: this.aziendaService.getContatti(id),
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (result) => {
          this.azienda = result.azienda;
          this.contatti = result.contatti;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento dati azienda:', err);
          // Distingui tra 404 e altri errori
          if (err.status === 404) {
            this.error = 'Azienda non trovata.';
          } else {
            this.error = 'Errore nel caricamento dei dati.';
          }
          this.isLoading = false;
        },
      });
  }

  /** Naviga verso il form di modifica. */
  edit(): void {
    this.router.navigate(['/aziende', this.azienda!.id, 'edit']);
  }

  goToColloquiAzienda(): void {
    if (!this.azienda?.id) {
      return;
    }

    this.router.navigate(['/colloqui/azienda', this.azienda.id]);
  }

  /** Naviga verso la lista aziende. */
  back(): void {
    this.router.navigate(['/aziende']);
  }

  submitContatto(): void {
    if (!this.azienda) {
      return;
    }

    if (this.contattoForm.invalid) {
      this.contattoForm.markAllAsTouched();
      return;
    }

    this.isSubmittingContatto = true;
    this.contattoError = null;

    const value = this.contattoForm.getRawValue();
    const payload: CreateContattoRequest = {
      nome: (value.nome ?? '').trim(),
      cognome: value.cognome?.trim() || undefined,
      ruolo: value.ruolo as Contatto['ruolo'],
      telefono: value.telefono?.trim() || undefined,
      email: value.email?.trim() || undefined,
    };

    this.aziendaService
      .createContatto(this.azienda.id, payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (created) => {
          this.contatti = [created, ...this.contatti];
          this.contattoForm.reset({
            nome: '',
            cognome: '',
            ruolo: null,
            telefono: '',
            email: '',
          });
          this.isSubmittingContatto = false;
        },
        error: (err) => {
          console.error('Errore nel salvataggio contatto:', err);
          this.contattoError = err?.error?.message || 'Errore nel salvataggio del contatto aziendale.';
          this.isSubmittingContatto = false;
        },
      });
  }

  getTipoBadgeClass(): string {
    return this.azienda?.tipoAzienda === 'MADRINA' ? 'bg-primary' : 'bg-secondary';
  }

  getTipoLabel(): string {
    return this.azienda?.tipoAzienda === 'MADRINA' ? 'Madrina' : 'Normale';
  }

  getRuoloLabel(ruolo: Contatto['ruolo']): string {
    return ruolo.replaceAll('_', ' ').toLowerCase().replace(/(^|\s)\S/g, (char) => char.toUpperCase());
  }
}
