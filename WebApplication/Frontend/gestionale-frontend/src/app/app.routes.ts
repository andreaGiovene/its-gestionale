import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/auth/login/login';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      // Redirect dalla root alla dashboard
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
    ]
  },
  { path: '**', redirectTo: '' }
];