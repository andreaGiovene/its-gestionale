import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

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
}