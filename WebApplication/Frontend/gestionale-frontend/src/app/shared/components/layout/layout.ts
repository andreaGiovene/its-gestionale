import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';

import { AuthService } from '../../../core/services/auth.service';
import { BreadcrumbComponent } from '../breadcrumb/breadcrumb';
import { HeaderComponent } from '../header/header';
import { SidebarComponent, SidebarItem } from '../sidebar/sidebar';

/**
 * Layout autenticato dell'applicazione.
 * Coordina sidebar, header, breadcrumb e contenuto delle route figlie.
 */
@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    MatSidenavModule,
    MatIconModule,
    SidebarComponent,
    HeaderComponent,
    BreadcrumbComponent,
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class LayoutComponent implements OnInit, OnDestroy {
  private readonly authService = inject(AuthService);
  private readonly breakpointObserver = inject(BreakpointObserver);
  private breakpointSub?: Subscription;

  /** Riferimento al sidenav Material per aprire/chiudere il menu da codice. */
  @ViewChild(MatSidenav) sidenav?: MatSidenav;
  /** Indica se il layout è in modalità mobile. */
  isMobile = false;

  /** Voci della navigazione laterale disponibili nell'area autenticata. */
  menuItems: SidebarItem[] = [
    { label: 'Dashboard',  icon: 'dashboard',   route: '/dashboard' },
    { label: 'Corsi',      icon: 'school',       route: '/corsi' },
    { label: 'Allievi',    icon: 'people',       route: '/allievi' },
    { label: 'Aziende',    icon: 'business',     route: '/aziende' },
    { label: 'Colloqui',   icon: 'handshake',    route: '/colloqui' },
    { label: 'Tirocini',   icon: 'work',         route: '/tirocini' },
  ];

  /** Allinea l'apertura del sidenav alla breakpoint strategy del layout. */
  ngOnInit(): void {
    this.breakpointSub = this.breakpointObserver
      .observe('(max-width: 959px)')
      .subscribe((state) => {
        this.isMobile = state.matches;

        if (this.sidenav) {
          if (this.isMobile) {
            this.sidenav.close();
          } else {
            this.sidenav.open();
          }
        }
      });
  }

  /** Pulisce la subscription del breakpoint observer. */
  ngOnDestroy(): void {
    this.breakpointSub?.unsubscribe();
  }

  /** Alterna il menu laterale solo quando il layout è in modalità mobile. */
  toggleSidenav(): void {
    if (this.isMobile) {
      this.sidenav?.toggle();
    }
  }

  /** Chiude il menu mobile dopo la navigazione verso una voce laterale. */
  onMenuNavigate(): void {
    if (this.isMobile) {
      this.sidenav?.close();
    }
  }

  /** Delega all'AuthService la procedura di logout. */
  onLogout(): void {
    this.authService.logout();
  }
}