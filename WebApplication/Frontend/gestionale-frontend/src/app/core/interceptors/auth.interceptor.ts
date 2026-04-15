import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';

/**
 * Interceptor HTTP che aggiunge automaticamente il token JWT alle richieste protette.
 * Se il token non è disponibile, lascia passare la richiesta senza modificarla.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  /** Recupera il token salvato lato client tramite il servizio di autenticazione. */
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Se non c'è token, la richiesta prosegue invariata.
  if (!token) {
    return next(req);
  }

  // Clona la richiesta originale aggiungendo l'header Authorization in formato Bearer.
  // In questo modo i servizi API non devono occuparsi della propagazione del token.
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq);
};
