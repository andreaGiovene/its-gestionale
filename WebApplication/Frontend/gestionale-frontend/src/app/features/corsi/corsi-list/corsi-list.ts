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

  corsi: Corso[] = [];
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.loadCorsi();
  }

  private loadCorsi(): void {
    this.isLoading = true;
    this.error = null;
    
    this.corsoService.findAll().subscribe({
      next: (data) => {
        this.corsi = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento corsi:', err);
        this.error = 'Errore nel caricamento dei corsi. Riprovare più tardi.';
        this.isLoading = false;
      },
    });
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
