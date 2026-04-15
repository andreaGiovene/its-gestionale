import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Router } from '@angular/router';
import { vi } from 'vitest';

import { AuthService } from './auth.service';

/**
 * Verifica il comportamento del servizio di autenticazione.
 * In questo file controlliamo soprattutto la gestione del logout e il redirect finale.
 */
describe('AuthService', () => {
  let service: AuthService;
  let router: Router;

  /** Prepara il TestBed con HttpClient e router fittizio prima di ogni test. */
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideRouter([])],
    });

    service = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  /** Il logout deve rimuovere il token locale e riportare l'utente alla login. */
  it('should clear the token and redirect on logout', () => {
    // Spy sul router per verificare che il redirect venga richiesto con i parametri corretti.
    const navigateSpy = vi.spyOn(router, 'navigateByUrl').mockResolvedValue(true);
    localStorage.setItem('auth_token', 'token-test');

    service.logout();

    expect(localStorage.getItem('auth_token')).toBeNull();
    expect(navigateSpy).toHaveBeenCalledWith('/login', { replaceUrl: true });
  });
});