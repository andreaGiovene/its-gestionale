import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AziendaService } from '@core/services/azienda.service';
import { Azienda } from '@shared/models';

@Component({
  selector: 'app-aziende-list',
  imports: [CommonModule],
  templateUrl: './aziende-list.html',
  styleUrl: './aziende-list.scss',
})
export class AziendeList implements OnInit {
  private readonly aziendaService = inject(AziendaService);
  private readonly router = inject(Router);

  aziende: Azienda[] = [];
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.loadAziende();
  }

  private loadAziende(): void {
    this.isLoading = true;
    this.error = null;

    this.aziendaService.findAll().subscribe({
      next: (data) => {
        this.aziende = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento aziende:', err);
        this.error = 'Errore nel caricamento delle aziende. Riprovare più tardi.';
        this.isLoading = false;
      },
    });
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
          this.loadAziende();
        },
        error: (err) => {
          console.error('Errore nell\'eliminazione:', err);
          alert('Errore: impossibile eliminare l\'azienda');
        },
      });
    }
  }
}
