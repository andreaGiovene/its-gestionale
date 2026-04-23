import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AllievoService } from '@core/services/allievo.service';
import { ColloquioService } from '@core/services/colloquio.service';
import { Allievo, Colloquio, StatoEsitoColloquio, TipoEventoColloquio } from '@shared/models';

@Component({
  selector: 'app-colloqui-storico-allievo',
  imports: [CommonModule],
  templateUrl: './colloqui-storico-allievo.html',
  styleUrl: './colloqui-storico-allievo.scss',
})
export class ColloquiStoricoAllievo implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly allievoService = inject(AllievoService);
  private readonly colloquioService = inject(ColloquioService);
  private readonly destroy$ = new Subject<void>();

  allievo: Allievo | null = null;
  colloqui: Colloquio[] = [];
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((paramMap) => {
      const id = Number(paramMap.get('id'));
      if (Number.isNaN(id)) {
        this.error = 'Identificativo allievo non valido.';
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
    this.router.navigate(['/allievi']);
  }

  private loadData(allievoId: number): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      allievo: this.allievoService.findById(allievoId),
      colloqui: this.colloquioService.findAll({ allievoId }),
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({ allievo, colloqui }) => {
          this.allievo = allievo;
          this.colloqui = colloqui;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore caricamento storico colloqui allievo:', err);
          this.error = 'Impossibile caricare lo storico colloqui dell\'allievo.';
          this.isLoading = false;
        },
      });
  }

  trackByColloquioId(_: number, colloquio: Colloquio): number {
    return colloquio.id;
  }
}
