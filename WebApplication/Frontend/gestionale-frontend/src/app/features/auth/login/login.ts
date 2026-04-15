import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { catchError, of } from 'rxjs';

import { AuthService } from '../../../core/services/auth.service';

/**
 * Schermata di autenticazione dell'applicazione.
 * Gestisce validazione, stato di caricamento e redirect alla dashboard dopo il login.
 */
@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  /** Indica che la richiesta di login è in corso. */
  loading = false;
  /** Messaggio di errore mostrato all'utente in caso di credenziali errate. */
  errorMessage = '';

  /** Form reattiva con email e password, entrambe obbligatorie. */
  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  /**
   * Esegue il login, recupera il profilo utente e porta alla dashboard.
   * Se la form non è valida, marca i campi come toccati per mostrare gli errori.
   */
  onSubmit(): void {
    if (this.loginForm.invalid || this.loading) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.authService
          .me()
          .pipe(catchError(() => of(null)))
          .subscribe((user) => {
            if (user) {
              this.authService.setCurrentUser(user);
            }

            this.loading = false;
            void this.router.navigateByUrl('/dashboard');
          });
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err?.status === 401 || err?.status === 403
            ? 'Credenziali non valide'
            : 'Errore durante l\'autenticazione';
      },
    });
  }
}
