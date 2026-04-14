import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatButtonModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class LayoutComponent {
  private readonly authService = inject(AuthService);

  // Voci del menu laterale
  // In futuro aggiungeremo qui la logica per nascondere voci
  // in base al ruolo dell'utente (RBAC)
  menuItems = [
    { label: 'Dashboard',  icon: 'dashboard',   route: '/dashboard' },
    { label: 'Corsi',      icon: 'school',       route: '/corsi' },
    { label: 'Allievi',    icon: 'people',       route: '/allievi' },
    { label: 'Aziende',    icon: 'business',     route: '/aziende' },
    { label: 'Colloqui',   icon: 'handshake',    route: '/colloqui' },
    { label: 'Tirocini',   icon: 'work',         route: '/tirocini' },
  ];

  onLogout(): void {
    this.authService.logout();
  }
}