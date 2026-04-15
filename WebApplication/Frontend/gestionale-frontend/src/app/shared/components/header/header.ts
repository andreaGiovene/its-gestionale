import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';

/**
 * Barra superiore dell'applicazione.
 * Espone il titolo corrente, il toggle del menu su mobile e l'azione di logout.
 */
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatToolbarModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class HeaderComponent {
  /** Titolo mostrato nell'area centrale della toolbar. */
  @Input() title = 'ITS - Sistema Gestione Tirocini';
  /** Mostra il pulsante hamburger solo quando il layout è in modalità mobile. */
  @Input() showMenuButton = false;
  /** Evento emesso quando il parent deve aprire/chiudere il menu laterale. */
  @Output() menuToggle = new EventEmitter<void>();
  /** Evento emesso quando l'utente richiede il logout. */
  @Output() logout = new EventEmitter<void>();

  /** Notifica al layout che il menu mobile deve essere alternato. */
  onMenuToggle(): void {
    this.menuToggle.emit();
  }

  /** Notifica al parent che l'utente vuole uscire dall'applicazione. */
  onLogout(): void {
    this.logout.emit();
  }
}
