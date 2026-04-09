import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CorsoService } from '@core/services/corso.service';
import { Corso } from '@shared/models';

@Component({
  selector: 'app-corsi-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './corsi-list.html',
  styleUrl: './corsi-list.scss',
})
/**
 * Gestisce la lista corsi con filtri client-side e azioni CRUD di navigazione/eliminazione.
 */
export class CorsiList implements OnInit {
  private readonly corsoService = inject(CorsoService);
  private readonly router = inject(Router);

  /** Dataset completo ricevuto da API, usato come source of truth per i filtri. */
  allCorsi: Corso[] = [];
  /** Dataset visualizzato in tabella dopo l'applicazione dei filtri correnti. */
  filteredCorsi: Corso[] = [];

  /** Filtro nome da dropdown (match esatto). */
  selectedNomeCorso = '';
  /** Filtro testuale libero sul nome (contains, case-insensitive). */
  searchTerm = '';
  /** Filtro stato (match esatto). */
  selectedStato = '';
  /** Filtro anno accademico (match esatto). */
  selectedAnnoAccademico = '';

  /** Stato UI durante caricamento dati. */
  isLoading = true;
  /** Messaggio errore da mostrare in pagina in caso di fetch fallita. */
  error: string | null = null;

  /**
   * Restituisce l'elenco univoco e ordinato dei nomi corso disponibili.
   */
  get nomiCorsiDisponibili(): string[] {
    return [
      ...new Set(this.allCorsi.map((c) => c.nome).filter((value): value is string => !!value)),
    ].sort((a, b) => a.localeCompare(b));
  }

  /**
   * Restituisce l'elenco univoco e ordinato degli stati disponibili.
   */
  get statiDisponibili(): string[] {
    return [
      ...new Set(this.allCorsi.map((c) => c.stato).filter((value): value is string => !!value)),
    ].sort((a, b) => a.localeCompare(b));
  }

  /**
   * Restituisce l'elenco univoco degli anni accademici in ordine decrescente.
   */
  get anniDisponibili(): string[] {
    return [
      ...new Set(
        this.allCorsi
          .map((c) => c.annoAccademico)
          .filter((value): value is string => !!value),
      ),
    ].sort((a, b) => b.localeCompare(a));
  }

  ngOnInit(): void {
    this.loadCorsi();
  }

  /**
   * Carica i corsi dal backend e inizializza la lista filtrata.
   */
  private loadCorsi(): void {
    this.isLoading = true;
    this.error = null;

    this.corsoService.findAll().subscribe({
      next: (data) => {
        this.allCorsi = data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento corsi:', err);
        this.error = 'Errore nel caricamento dei corsi. Riprovare più tardi.';
        this.isLoading = false;
      },
    });
  }

  /**
   * Applica in AND tutti i filtri attivi e aggiorna la lista visualizzata.
   */
  applyFilters(): void {
    const search = this.searchTerm.trim().toLowerCase();

    this.filteredCorsi = this.allCorsi.filter((corso) => {
      const matchNome =
        this.selectedNomeCorso.length === 0 || corso.nome === this.selectedNomeCorso;
      const matchNomeSearch = search.length === 0 || corso.nome.toLowerCase().includes(search);
      const matchStato = this.selectedStato.length === 0 || corso.stato === this.selectedStato;
      const matchAnno =
        this.selectedAnnoAccademico.length === 0 ||
        corso.annoAccademico === this.selectedAnnoAccademico;

      return matchNome && matchNomeSearch && matchStato && matchAnno;
    });
  }

  /**
   * Ripristina lo stato iniziale dei filtri e ricalcola la lista.
   */
  resetFilters(): void {
    this.selectedNomeCorso = '';
    this.searchTerm = '';
    this.selectedStato = '';
    this.selectedAnnoAccademico = '';
    this.applyFilters();
  }

  /**
   * Naviga alla pagina dettaglio del corso selezionato.
   * @param id identificativo del corso
   */
  viewDetail(id: number): void {
    this.router.navigate(['/corsi', id]);
  }

  /**
   * Naviga alla pagina di creazione nuovo corso.
   */
  createNew(): void {
    this.router.navigate(['/corsi', 'new']);
  }

  /**
   * Elimina un corso dopo conferma utente e ricarica i dati dalla API.
   * @param id identificativo del corso da eliminare
   * @param event evento del click, usato per evitare la propagazione alla riga tabella
   */
  delete(id: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Sei sicuro di voler eliminare questo corso?')) {
      this.corsoService.delete(id).subscribe({
        next: () => {
          this.loadCorsi();
        },
        error: (err) => {
          console.error('Errore nell\'eliminazione:', err);
          alert('Errore: impossibile eliminare il corso');
        },
      });
    }
  }
}
