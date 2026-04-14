import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
}

export interface MeResponse {
  idUtente: number;
  email: string;
  ruolo: string;
  attivo: boolean;
}

/**
 * Servizio centralizzato di autenticazione e autorizzazione.
 *
 * Responsabilità:
 * - Persistenza del token JWT in localStorage
 * - Gestione dello stato utente corrente (currentUser)
 * - Verifica dell'autenticazione (isAuthenticated)
 * - Controllo dei permessi per ruolo (hasRole)
 * - Logout e pulizia dello stato
 *
 * Pattern:
 * - Token salvato in localStorage con chiave 'auth_token'
 * - Aggiunto automaticamente alle richieste HTTP via interceptor
 * - MeResponse (profilo utente) caricato al bootstrap dell'app (App.ngOnInit)
 * - Supporta logout locale (stateless, no server invalidation)
 *
 * Flusso login:
 * 1. Credenziali inviate a /auth/login
 * 2. Token ricevuto e salvato in localStorage via tap(setToken)
 * 3. App chiama me() per popolare currentUser
 * 4. Guard di routing valida isAuthenticated() e hasRole()
 *
 * @see AuthGuard
 * @see App
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly apiBase = 'http://localhost:8080';
  private readonly tokenKey = 'auth_token';
  private currentUser: MeResponse | null = null;

  // Login applicativo:
  // - invia credenziali al backend
  // - persiste il token ricevuto nel localStorage
  // Nota: il token e semplificato in fase di sviluppo ma il contratto
  // resta invariato (campo token, tokenType, expiresInSeconds).
  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiBase}/auth/login`, payload)
      .pipe(tap((res) => this.setToken(res.token)));
  }

  // Recupera il profilo dell'utente corrente.
  // L'header Authorization viene aggiunto automaticamente dall'interceptor.
  me(): Observable<MeResponse> {
    return this.http.get<MeResponse>(`${this.apiBase}/auth/me`);
  }

  setCurrentUser(user: MeResponse | null): void {
    this.currentUser = user;
  }

  getCurrentUser(): MeResponse | null {
    return this.currentUser;
  }

  hasRole(role: string): boolean {
    return this.currentUser?.ruolo === role;
  }

  // Logout locale: elimina solo lo stato client-side.
  // In modalita stateless non e richiesto endpoint server di invalidazione.
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.currentUser = null;
    void this.router.navigateByUrl('/login', { replaceUrl: true });
  }

  // Espone il token grezzo per interceptor/guard.
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Check veloce usato dal guard di routing.
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // Scrittura centralizzata storage auth.
  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }
}
