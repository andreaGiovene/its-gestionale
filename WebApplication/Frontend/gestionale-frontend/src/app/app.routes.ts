import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout';
import { Dashboard } from './features/dashboard/dashboard';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      // Redirect dalla root alla dashboard
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
    ]
  }
];