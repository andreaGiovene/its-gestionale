import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AziendaService } from '@core/services/azienda.service';
import { CorsoService } from '@core/services/corso.service';
import { Azienda, AziendaSearchTipo, Corso } from '@shared/models';

/**
 * Elenco aziende con filtri di ricerca, paginazione e azioni di navigazione CRUD.
 * La lista è supportata da un form reattivo per centralizzare i criteri di ricerca.
 */
@Component({
  selector: 'app-aziende-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './aziende-list.html',
  styleUrl: './aziende-list.scss',
})
export class AziendeList implements OnInit {
  private readonly aziendaService = inject(AziendaService);
  private readonly corsoService = inject(CorsoService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  /** Risultati correnti della ricerca. */
  aziende: Azienda[] = [];
  /** Elenco corsi usato per filtrare le aziende per associazione didattica. */
  corsi: Corso[] = [];
  /** Stato di caricamento della prima ricerca. */
  isLoading = true;
  /** Stato della chiamata di ricerca, usato per disabilitare il submit. */
  isSearching = false;
  /** Messaggio di errore visibile in pagina. */
  error: string | null = null;
  /** Numero totale di elementi trovati dal backend. */
  totalElements = 0;
  /** Numero totale di pagine disponibili. */
  totalPages = 0;
  /** Pagina corrente in base zero. */
  currentPage = 0;
  /** Dimensione pagina usata nelle richieste. */
  pageSize = 10;

  /** Form reattiva per i filtri di ricerca lato server. */
  readonly searchForm = this.fb.group({
    tipo: ['' as AziendaSearchTipo | ''],
    ragioneSociale: [''],
    corsoId: [''],
  });

  /** Carica i corsi di supporto e lancia la prima ricerca. */
  ngOnInit(): void {
    this.loadCorsi();
    this.search();
  }

  /** Recupera i corsi da usare nel filtro di relazione. */
  private loadCorsi(): void {
    this.corsoService.findAll().subscribe({
      next: (data) => {
        this.corsi = data;
      },
      error: (err) => {
        console.error('Errore nel caricamento corsi:', err);
      },
    });
  }

  /** Esegue la ricerca con i filtri correnti e aggiorna la paginazione. */
  search(page = 0): void {
    this.isLoading = true;
    this.isSearching = true;
    this.error = null;

    const formValue = this.searchForm.getRawValue();
    const corsoIdValue = formValue.corsoId;
    const corsoId = corsoIdValue === '' || corsoIdValue === null ? null : Number(corsoIdValue);

    this.aziendaService.getCerca({
      tipo: formValue.tipo ?? '',
      ragioneSociale: formValue.ragioneSociale ?? '',
      corsoId,
      page,
      size: this.pageSize,
    }).subscribe({
      next: (response) => {
        this.aziende = response.content;
        this.currentPage = response.number;
        this.pageSize = response.size;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.isLoading = false;
        this.isSearching = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento aziende:', err);
        this.error = 'Errore nel caricamento delle aziende. Riprovare più tardi.';
        this.isLoading = false;
        this.isSearching = false;
      },
    });
  }

  /** Ripristina tutti i filtri e torna alla prima pagina dei risultati. */
  resetFilters(): void {
    this.searchForm.reset({
      tipo: '',
      ragioneSociale: '',
      corsoId: '',
    });
    this.search(0);
  }

  /** Vai alla pagina precedente, se disponibile. */
  previousPage(): void {
    if (this.currentPage > 0) {
      this.search(this.currentPage - 1);
    }
  }

  /** Vai alla pagina successiva, se disponibile. */
  nextPage(): void {
    if (this.currentPage + 1 < this.totalPages) {
      this.search(this.currentPage + 1);
    }
  }

  /** Apre il dettaglio dell'azienda selezionata. */
  viewDetail(id: number): void {
    this.router.navigate(['/aziende', id]);
  }

  /** Porta alla schermata di creazione di una nuova azienda. */
  createNew(): void {
    this.router.navigate(['/aziende', 'new']);
  }

  /** Elimina un'azienda dopo conferma esplicita e ricarica la pagina corrente. */
  delete(id: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Sei sicuro di voler eliminare questa azienda?')) {
      this.aziendaService.delete(id).subscribe({
        next: () => {
          this.search(this.currentPage);
        },
        error: (err) => {
          console.error('Errore nell\'eliminazione:', err);
          alert('Errore: impossibile eliminare l\'azienda');
        },
      });
    }
  }
}
