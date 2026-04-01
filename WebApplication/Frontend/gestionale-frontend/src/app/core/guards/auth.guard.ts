import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Accesso consentito solo se esiste token in storage.
  // In caso contrario redirect deterministico alla login.
  if (authService.isAuthenticated()) {
    return true;
  }

  return router.createUrlTree(['/login']);
};
