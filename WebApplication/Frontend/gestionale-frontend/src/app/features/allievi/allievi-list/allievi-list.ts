import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AllievoService } from '@core/services/allievo.service';
import { Allievo } from '@shared/models';

@Component({
  selector: 'app-allievi-list',
  imports: [CommonModule],
  templateUrl: './allievi-list.html',
  styleUrl: './allievi-list.scss',
})
export class AllieviList implements OnInit {
  private readonly allievoService = inject(AllievoService);
  private readonly router = inject(Router);

  allievi: Allievo[] = [];
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.loadAllievi();
  }

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

  viewDetail(id: number): void {
    this.router.navigate(['/allievi', id]);
  }

  createNew(): void {
    this.router.navigate(['/allievi', 'new']);
  }

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