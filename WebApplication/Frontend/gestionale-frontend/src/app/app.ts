import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';

/**
 * Componente root dell'applicazione Angular.
 *
 * Responsabilità:
 * - Outlet di routing (RouterOutlet per visualizzare componenti attivi)
 * - Bootstrap dell'autenticazione: al caricamento dell'app,
 *   se esiste un token valido, carica il profilo utente (me()) per popolare currentUser
 * - Fallback di logout se il profilo non è raggiungibile (token scaduto/invalido)
 *
 * Lifecycle:
 * - ngOnInit: viene eseguito dopo il mount del componente
 * - Se isAuthenticated() → chiama me() per caricare MeResponse
 * - Salva MeResponse in AuthService.currentUser per uso nel guard e altrove
 * - Se errore nel me() → logout automatico (token invalido)
 *
 * Template (app.html):
 * - <router-outlet></router-outlet> per routing view
 *
 * Nota: questo è il primo punto di accesso per restore dello stato utente
 * dopo refresh (F5) della pagina; il token persiste in localStorage.
 *
 * @see AuthService
 * @see authGuard
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly authService = inject(AuthService);
  protected readonly title = signal('gestionale-frontend');

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      return;
    }

    this.authService.me().subscribe({
      next: (user) => this.authService.setCurrentUser(user),
      error: () => this.authService.logout(),
    });
  }
}
