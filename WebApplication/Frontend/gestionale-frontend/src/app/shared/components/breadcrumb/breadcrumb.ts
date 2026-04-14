import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, NavigationEnd, Router, RouterLink } from '@angular/router';
import { filter, Subscription } from 'rxjs';

interface BreadcrumbItem {
  label: string;
  url: string;
}

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

  breadcrumbs: BreadcrumbItem[] = [];

  ngOnInit(): void {
    this.updateBreadcrumbs();

    this.routeSub = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => this.updateBreadcrumbs());
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  private updateBreadcrumbs(): void {
    this.breadcrumbs = this.collectBreadcrumbs(this.activatedRoute.snapshot.root);
  }

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
