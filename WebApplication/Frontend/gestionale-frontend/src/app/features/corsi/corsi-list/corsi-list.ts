import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CorsoService } from '@core/services/corso.service';
import { Corso } from '@shared/models';

@Component({
  selector: 'app-corsi-list',
  imports: [CommonModule],
  templateUrl: './corsi-list.html',
  styleUrl: './corsi-list.scss',
})
export class CorsiList implements OnInit {
  private readonly corsoService = inject(CorsoService);
  private readonly router = inject(Router);

  allCorsi: Corso[] = [];
  corsi: Corso[] = [];
  searchTerm = '';
  selectedStato = '';
  selectedAnnoAccademico = '';
  isLoading = true;
  error: string | null = null;

  get statiDisponibili(): string[] {
    return [...new Set(this.allCorsi.map((c) => c.stato).filter(Boolean))].sort((a, b) =>
      a.localeCompare(b),
    );
  }

  get anniDisponibili(): string[] {
    return [...new Set(this.allCorsi.map((c) => c.annoAccademico).filter(Boolean))].sort((a, b) =>
      b.localeCompare(a),
    );
  }

  ngOnInit(): void {
    this.loadCorsi();
  }

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

  applyFilters(): void {
    const search = this.searchTerm.trim().toLowerCase();

    this.corsi = this.allCorsi.filter((corso) => {
      const matchNome = search.length === 0 || corso.nome.toLowerCase().includes(search);
      const matchStato = this.selectedStato.length === 0 || corso.stato === this.selectedStato;
      const matchAnno =
        this.selectedAnnoAccademico.length === 0 ||
        corso.annoAccademico === this.selectedAnnoAccademico;

      return matchNome && matchStato && matchAnno;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedStato = '';
    this.selectedAnnoAccademico = '';
    this.applyFilters();
  }

  viewDetail(id: number): void {
    this.router.navigate(['/corsi', id]);
  }

  createNew(): void {
    this.router.navigate(['/corsi', 'new']);
  }

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
