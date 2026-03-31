import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Se non c'e token, la richiesta prosegue invariata.
  if (!token) {
    return next(req);
  }

  // Decorazione uniforme di tutte le richieste HTTP con header Bearer.
  // Questo evita duplicazione della logica auth nei singoli servizi API.
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq);
};
