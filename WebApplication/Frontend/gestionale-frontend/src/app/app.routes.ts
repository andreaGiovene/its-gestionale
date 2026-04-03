import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/auth/login/login';
import { CorsiList } from './features/corsi/corsi-list/corsi-list';
import { CorsoDetail } from './features/corsi/corso-detail/corso-detail';
import { AziendeList } from './features/aziende/aziende-list/aziende-list';
import { AziendaDetail } from './features/aziende/azienda-detail/azienda-detail';
import { AllieviList } from './features/allievi/allievi-list/allievi-list';
import { AllievoDetail } from './features/allievi/allievo-detail/allievo-detail';
import { ColloquiPlaceholder } from './features/colloqui/colloqui-placeholder/colloqui-placeholder';
import { TirociniPlaceholder } from './features/tirocini/tirocini-placeholder/tirocini-placeholder';
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

      // Allievi routes
      { path: 'allievi', component: AllieviList },
      { path: 'allievi/:id', component: AllievoDetail },

      // Placeholder routes
      { path: 'colloqui', component: ColloquiPlaceholder },
      { path: 'tirocini', component: TirociniPlaceholder },
    ]
  },
  { path: '**', redirectTo: '' }
];