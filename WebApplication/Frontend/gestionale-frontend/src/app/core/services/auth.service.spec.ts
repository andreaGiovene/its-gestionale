import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Router } from '@angular/router';

import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideRouter([])],
    });

    service = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should clear the token and redirect on logout', () => {
    const navigateSpy = spyOn(router, 'navigateByUrl').and.resolveTo(true);
    localStorage.setItem('auth_token', 'token-test');

    service.logout();

    expect(localStorage.getItem('auth_token')).toBeNull();
    expect(navigateSpy).toHaveBeenCalledWith('/login', { replaceUrl: true });
  });
});