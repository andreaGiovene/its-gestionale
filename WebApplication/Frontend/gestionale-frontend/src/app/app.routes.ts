import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/auth/login/login';
import { CorsiList } from './features/corsi/corsi-list/corsi-list';
import { CorsoDetail } from './features/corsi/corso-detail/corso-detail';
import { AziendeList } from './features/aziende/aziende-list/aziende-list';
import { AziendaDetail } from './features/aziende/azienda-detail/azienda-detail';
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
      
      // Corsi routes
      { path: 'corsi', component: CorsiList },
      { path: 'corsi/:id', component: CorsoDetail },
      
      // Aziende routes
      { path: 'aziende', component: AziendeList },
      { path: 'aziende/:id', component: AziendaDetail },
    ]
  },
  { path: '**', redirectTo: '' }
];