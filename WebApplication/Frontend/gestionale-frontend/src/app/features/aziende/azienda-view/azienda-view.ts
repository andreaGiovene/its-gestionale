import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AziendaService } from '@core/services/azienda.service';
import { Azienda, Contatto } from '@shared/models';
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
  imports: [CommonModule],
  templateUrl: './azienda-view.html',
  styleUrl: './azienda-view.scss',
})
export class AziendaView implements OnInit, OnDestroy {
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

  /** Naviga verso la lista aziende. */
  back(): void {
    this.router.navigate(['/aziende']);
  }

  /** Restituisce il badge per il tipo di azienda. */
  getTipoBadgeClass(): string {
    if (!this.azienda) return '';
    // Assumiamo che il backend restituisca sempre il campo tipo
    // Per ora usiamo una logica di default
    return 'badge-info';
  }
}
