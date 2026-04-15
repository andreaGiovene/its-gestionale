import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AllievoService } from '@core/services/allievo.service';
import { Allievo } from '@shared/models';

/**
 * Elenco degli allievi con caricamento iniziale, apertura dettaglio e cancellazione.
 * Mantiene uno stato locale minimo per gestire UX, errori e refresh dopo le operazioni.
 */
@Component({
  selector: 'app-allievi-list',
  imports: [CommonModule],
  templateUrl: './allievi-list.html',
  styleUrl: './allievi-list.scss',
})
export class AllieviList implements OnInit {
  private readonly allievoService = inject(AllievoService);
  private readonly router = inject(Router);

  /** Dati caricati dal backend e mostrati in tabella. */
  allievi: Allievo[] = [];
  /** Stato di caricamento per la prima fetch e i refresh successivi. */
  isLoading = true;
  /** Messaggio di errore visibile all'utente in caso di fallimento della richiesta. */
  error: string | null = null;

  /** Carica gli allievi appena il componente entra in vista. */
  ngOnInit(): void {
    this.loadAllievi();
  }

  /** Recupera la lista dal servizio e aggiorna gli stati della UI. */
  private loadAllievi(): void {
    this.isLoading = true;
    this.error = null;

    this.allievoService.findAll().subscribe({
      next: (data) => {
        this.allievi = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento allievi:', err);
        this.error = 'Errore nel caricamento degli allievi. Riprovare più tardi.';
        this.isLoading = false;
      },
    });
  }

  /** Apre la pagina di dettaglio dell'allievo selezionato. */
  viewDetail(id: number): void {
    this.router.navigate(['/allievi', id]);
  }

  /** Porta alla schermata di creazione di un nuovo allievo. */
  createNew(): void {
    this.router.navigate(['/allievi', 'new']);
  }

  /** Elimina un record dopo conferma esplicita dell'utente. */
  delete(id: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Sei sicuro di voler eliminare questo allievo?')) {
      this.allievoService.delete(id).subscribe({
        next: () => {
          this.loadAllievi();
        },
        error: (err) => {
          console.error('Errore nell\'eliminazione:', err);
          alert('Errore: impossibile eliminare l\'allievo');
        },
      });
    }
  }
}