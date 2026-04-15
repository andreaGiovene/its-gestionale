import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

/**
 * Rappresenta una singola voce del menu laterale.
 * Ogni item espone etichetta, icona Material e route di destinazione.
 */
export interface SidebarItem {
  label: string;
  icon: string;
  route: string;
}

/**
 * Menu laterale riutilizzabile del layout applicativo.
 * Riceve l'elenco delle voci in input e notifica il click di navigazione al parent.
 */
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatListModule, MatIconModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class SidebarComponent {
  @Input({ required: true }) items: SidebarItem[] = [];
  @Output() navigate = new EventEmitter<void>();

  onNavigate(): void {
    this.navigate.emit();
  }
}
