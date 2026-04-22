import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/auth/login/login';
import { CorsiList } from './features/corsi/corsi-list/corsi-list';
import { CorsoDetail } from './features/corsi/corso-detail/corso-detail';
import { AziendeList } from './features/aziende/aziende-list/aziende-list';
import { AziendaDetail } from './features/aziende/azienda-detail/azienda-detail';
import { AziendaView } from './features/aziende/azienda-view/azienda-view';
import { AllieviList } from './features/allievi/allievi-list/allievi-list';
import { AllievoDetail } from './features/allievi/allievo-detail/allievo-detail';
import { ColloquiPlaceholder } from './features/colloqui/colloqui-placeholder/colloqui-placeholder';
import { TirociniPlaceholder } from './features/tirocini/tirocini-placeholder/tirocini-placeholder';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login, data: { breadcrumb: 'Login' } },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      // Redirect dalla root alla dashboard
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard, data: { breadcrumb: 'Dashboard' } },
      
      // Corsi routes
      { path: 'corsi', component: CorsiList, data: { breadcrumb: 'Corsi' } },
      { path: 'corsi/:id', component: CorsoDetail, data: { breadcrumb: 'Dettaglio Corso' } },
      
      // Aziende routes
      { path: 'aziende', component: AziendeList, data: { breadcrumb: 'Aziende' } },
      { path: 'aziende/new', component: AziendaDetail, data: { breadcrumb: 'Nuova Azienda' } },
      { path: 'aziende/:id/edit', component: AziendaDetail, data: { breadcrumb: 'Modifica Azienda' } },
      { path: 'aziende/:id', component: AziendaView, data: { breadcrumb: 'Dettaglio Azienda' } },

      // Allievi routes
      { path: 'allievi', component: AllieviList, data: { breadcrumb: 'Allievi' } },
      { path: 'allievi/new', component: AllievoDetail, data: { breadcrumb: 'Nuovo Allievo' } },
      { path: 'allievi/:id/edit', component: AllievoDetail, data: { breadcrumb: 'Modifica Allievo' } },
      { path: 'allievi/:id', component: AllievoDetail, data: { breadcrumb: 'Dettaglio Allievo' } },

      // Placeholder routes
      { path: 'colloqui', component: ColloquiPlaceholder, data: { breadcrumb: 'Colloqui' } },
      { path: 'tirocini', component: TirociniPlaceholder, data: { breadcrumb: 'Tirocini' } },
    ]
  },
  { path: '**', redirectTo: '' }
];