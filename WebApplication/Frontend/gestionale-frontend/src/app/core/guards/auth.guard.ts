import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

/**
 * Guard di routing per protezione delle rotte autenticate.
 *
 * Logica di protezione (in ordine):
 * 1. Verifica autenticazione: se no token → redirect /login
 * 2. Se route non richiede ruoli specifici (data.roles undefined) → accesso consentito
 * 3. Se route richiede ruoli: verifica se utente ne ha almeno uno (some operator)
 * 4. Se ruolo non ammesso → redirect /dashboard
 * 5. Altrimenti accesso consentito
 *
 * Utilizzo nei routes:
 * {
 *   path: 'aziende',
 *   component: AziendeLis,
 *   canActivate: [authGuard],
 *   data: { roles: ['AMMINISTRATORE', 'JOB_PLACEMENT', 'DIDATTICA'] }
 * }
 *
 * @see AuthService.isAuthenticated
 * @see AuthService.hasRole
 * @see App (bootstrap currentUser)
 */
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
