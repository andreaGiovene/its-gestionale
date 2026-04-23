import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AziendaService } from '@core/services/azienda.service';
import { ColloquioService } from '@core/services/colloquio.service';
import { Azienda, Colloquio, StatoEsitoColloquio, TipoEventoColloquio } from '@shared/models';

@Component({
  selector: 'app-colloqui-per-azienda',
  imports: [CommonModule],
  templateUrl: './colloqui-per-azienda.html',
  styleUrl: './colloqui-per-azienda.scss',
})
export class ColloquiPerAzienda implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly aziendaService = inject(AziendaService);
  private readonly colloquioService = inject(ColloquioService);
  private readonly destroy$ = new Subject<void>();

  azienda: Azienda | null = null;
  colloqui: Colloquio[] = [];
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((paramMap) => {
      const id = Number(paramMap.get('id'));
      if (Number.isNaN(id)) {
        this.error = 'Identificativo azienda non valido.';
        this.isLoading = false;
        return;
      }

      this.loadData(id);
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get sortedColloqui(): Colloquio[] {
    return [...this.colloqui].sort((left, right) => new Date(right.dataColloquio).getTime() - new Date(left.dataColloquio).getTime());
  }

  getEsitoLabel(esito: StatoEsitoColloquio): string {
    const mapping: Record<StatoEsitoColloquio, string> = {
      IN_ATTESA: 'In attesa',
      POSITIVO: 'Positivo',
      NEGATIVO: 'Negativo',
      RITIRATO: 'Ritirato',
      NON_PRESENTATO: 'Non presentato',
    };

    return mapping[esito] ?? esito;
  }

  getTipoEventoLabel(tipoEvento?: TipoEventoColloquio): string {
    if (!tipoEvento) {
      return '-';
    }

    return tipoEvento === 'MATCHING_DAY' ? 'Matching day' : 'Fuori matching day';
  }

  goBack(): void {
    this.router.navigate(['/aziende']);
  }

  private loadData(aziendaId: number): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      azienda: this.aziendaService.findById(aziendaId),
      colloqui: this.colloquioService.findAll({ aziendaId }),
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({ azienda, colloqui }) => {
          this.azienda = azienda;
          this.colloqui = colloqui;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore caricamento colloqui azienda:', err);
          this.error = 'Impossibile caricare i colloqui dell\'azienda.';
          this.isLoading = false;
        },
      });
  }

  trackByColloquioId(_: number, colloquio: Colloquio): number {
    return colloquio.id;
  }
}
