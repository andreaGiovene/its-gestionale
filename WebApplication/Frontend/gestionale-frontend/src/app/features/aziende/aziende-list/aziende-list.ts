import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AziendaService } from '@core/services/azienda.service';
import { CorsoService } from '@core/services/corso.service';
import { Azienda, AziendaSearchTipo, Corso } from '@shared/models';

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

  aziende: Azienda[] = [];
  corsi: Corso[] = [];
  isLoading = true;
  isSearching = false;
  error: string | null = null;
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;

  readonly searchForm = this.fb.group({
    tipo: ['' as AziendaSearchTipo | ''],
    ragioneSociale: [''],
    corsoId: [''],
  });

  ngOnInit(): void {
    this.loadCorsi();
    this.search();
  }

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

  resetFilters(): void {
    this.searchForm.reset({
      tipo: '',
      ragioneSociale: '',
      corsoId: '',
    });
    this.search(0);
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.search(this.currentPage - 1);
    }
  }

  nextPage(): void {
    if (this.currentPage + 1 < this.totalPages) {
      this.search(this.currentPage + 1);
    }
  }

  viewDetail(id: number): void {
    this.router.navigate(['/aziende', id]);
  }

  createNew(): void {
    this.router.navigate(['/aziende', 'new']);
  }

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
