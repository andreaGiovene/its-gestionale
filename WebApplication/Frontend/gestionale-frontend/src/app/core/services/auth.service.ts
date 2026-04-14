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
