import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AllievoService } from '@core/services/allievo.service';
import { Allievo } from '@shared/models';

/**
 * Elenco degli allievi con caricamento iniziale, apertura dettaglio e cancellazione.
 * Mantiene uno stato locale minimo per gestire UX, errori e refresh dopo le operazioni.
 */
@Component({
  selector: 'app-allievi-list',
  imports: [CommonModule, MatButtonModule, MatIconModule],
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
  /** Direzione ordinamento per cognome. */
  cognomeSortDirection: 'asc' | 'desc' = 'asc';
  /** Direzione ordinamento per corso. */
  corsoSortDirection: 'asc' | 'desc' = 'asc';

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
        this.allievi = this.sortAllievi(data);
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

  /** Alterna l'ordinamento per cognome. */
  toggleCognomeSort(): void {
    this.cognomeSortDirection = this.cognomeSortDirection === 'asc' ? 'desc' : 'asc';
    this.allievi = this.sortAllievi(this.allievi);
  }

  /** Alterna l'ordinamento per corso. */
  toggleCorsoSort(): void {
    this.corsoSortDirection = this.corsoSortDirection === 'asc' ? 'desc' : 'asc';
    this.allievi = this.sortAllievi(this.allievi);
  }

  /** Ordina gli allievi per cognome e, a parità, per corso. */
  private sortAllievi(allievi: Allievo[]): Allievo[] {
    const cognomeDirectionMultiplier = this.cognomeSortDirection === 'asc' ? 1 : -1;
    const corsoDirectionMultiplier = this.corsoSortDirection === 'asc' ? 1 : -1;

    return [...allievi].sort((first, second) => {
      const cognomeComparison = (first.cognome || '').localeCompare(second.cognome || '', 'it', {
        sensitivity: 'base',
      });

      if (cognomeComparison !== 0) {
        return cognomeComparison * cognomeDirectionMultiplier;
      }

      const firstCorsoLabel = this.getCorsoLabel(first);
      const secondCorsoLabel = this.getCorsoLabel(second);

      const corsoComparison = firstCorsoLabel.localeCompare(secondCorsoLabel, 'it', {
        sensitivity: 'base',
      });

      if (corsoComparison !== 0) {
        return corsoComparison * corsoDirectionMultiplier;
      }

      return first.id - second.id;
    });
  }

  /** Combinazione leggibile di nome corso e annualità per confronto e visualizzazione. */
  getCorsoLabel(allievo: Allievo): string {
    const corsoNome = allievo.corsoNome || '';
    const corsoAnnoAccademico = allievo.corsoAnnoAccademico || '';

    if (!corsoNome && !corsoAnnoAccademico) {
      return '';
    }

    if (!corsoAnnoAccademico) {
      return corsoNome;
    }

    if (!corsoNome) {
      return corsoAnnoAccademico;
    }

    return `${corsoNome} - ${corsoAnnoAccademico}`;
  }
}