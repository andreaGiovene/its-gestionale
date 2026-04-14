import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly authService = inject(AuthService);
  protected readonly title = signal('gestionale-frontend');

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      return;
    }

    this.authService.me().subscribe({
      next: (user) => this.authService.setCurrentUser(user),
      error: () => this.authService.logout(),
    });
  }
}
