import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, NavigationEnd, Router, RouterLink } from '@angular/router';
import { filter, Subscription } from 'rxjs';

interface BreadcrumbItem {
  label: string;
  url: string;
}

/**
 * Genera dinamicamente il breadcrumb della route attiva.
 * Legge i metadata `breadcrumb` dai child route e costruisce i link progressivi.
 */
@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './breadcrumb.html',
  styleUrl: './breadcrumb.scss',
})
export class BreadcrumbComponent implements OnInit, OnDestroy {
  private readonly router = inject(Router);
  private readonly activatedRoute = inject(ActivatedRoute);
  private routeSub?: Subscription;

  /** Elenco breadcrumb calcolato a partire dalla route corrente. */
  breadcrumbs: BreadcrumbItem[] = [];

  /** Ricalcola i breadcrumb all'avvio e a ogni navigazione completata. */
  ngOnInit(): void {
    this.updateBreadcrumbs();

    this.routeSub = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => this.updateBreadcrumbs());
  }

  /** Interrompe la subscription al router per evitare leak di memoria. */
  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  /** Aggiorna il modello locale leggendo lo snapshot della route attiva. */
  private updateBreadcrumbs(): void {
    this.breadcrumbs = this.collectBreadcrumbs(this.activatedRoute.snapshot.root);
  }

  /**
   * Scorre ricorsivamente la gerarchia delle route per costruire il percorso completo.
   * Gestisce anche segmenti dinamici come `:id` sostituendoli con i parametri reali.
   */
  private collectBreadcrumbs(route: ActivatedRouteSnapshot, parentUrl = ''): BreadcrumbItem[] {
    const breadcrumbs: BreadcrumbItem[] = [];

    for (const child of route.children) {
      const routeConfig = child.routeConfig;
      if (!routeConfig) {
        breadcrumbs.push(...this.collectBreadcrumbs(child, parentUrl));
        continue;
      }

      const path = routeConfig.path ?? '';
      if (!path || path === '**') {
        breadcrumbs.push(...this.collectBreadcrumbs(child, parentUrl));
        continue;
      }

      const segment = path
        .split('/')
        .map((chunk) => (chunk.startsWith(':') ? String(child.params[chunk.substring(1)] ?? chunk) : chunk))
        .join('/');

      const nextUrl = `${parentUrl}/${segment}`.replace(/\/+/g, '/');
      const labelValue = child.data['breadcrumb'];

      if (typeof labelValue === 'string' && labelValue.trim().length > 0) {
        breadcrumbs.push({
          label: labelValue,
          url: nextUrl,
        });
      }

      breadcrumbs.push(...this.collectBreadcrumbs(child, nextUrl));
    }

    return breadcrumbs;
  }
}
