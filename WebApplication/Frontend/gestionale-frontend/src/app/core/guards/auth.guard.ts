import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Accesso consentito solo se esiste token in storage.
  // In caso contrario redirect deterministico alla login.
  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  // Nessuna restrizione di ruolo: basta essere autenticati.
  const allowedRoles = route.data['roles'] as string[] | undefined;

  if (!allowedRoles || allowedRoles.length === 0) {
    return true;
  }

  // Se almeno un ruolo ammesso coincide con quello dell'utente, l'accesso e consentito.
  const hasAccess = allowedRoles.some((role) => authService.hasRole(role));

  if (!hasAccess) {
    return router.createUrlTree(['/dashboard']);
  }

  return true;
};
