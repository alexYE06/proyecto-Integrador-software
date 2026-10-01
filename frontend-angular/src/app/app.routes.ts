import { Routes } from '@angular/router';
import { SelectorEntornoComponent } from './features/selector/selector-entorno.component';
import { LoginCentralComponent } from './features/auth/login-central.component';
import { LoginChoferComponent } from './features/auth/login-chofer.component';
import { CentralLayoutComponent } from './features/central/layout/central-layout.component';
import { MonitoreoFlotaComponent } from './features/central/monitoreo-flota.component';
import { IncidentesComponent } from './features/central/incidentes.component';
import { ConductoresListaComponent } from './features/central/conductores-lista.component';
import { BusesListaComponent } from './features/central/buses-lista.component';
import { ComisariasListaComponent } from './features/central/comisarias-lista.component';
import { TurnosListaComponent } from './features/central/turnos-lista.component';
import { ChoferDashboardComponent } from './features/chofer/chofer-dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { choferGuard } from './core/guards/chofer.guard';

export const routes: Routes = [
  { path: '', component: SelectorEntornoComponent },
  { path: 'login-central', component: LoginCentralComponent },
  { path: 'login-chofer', component: LoginChoferComponent },

  // Shell / Layout Maestro de la Central de Operaciones
  {
    path: 'central',
    component: CentralLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'monitoreo', pathMatch: 'full' },
      { path: 'monitoreo', component: MonitoreoFlotaComponent },
      { path: 'buses', component: BusesListaComponent },
      { path: 'incidentes', component: IncidentesComponent },
      { path: 'conductores', component: ConductoresListaComponent },
      { path: 'comisarias', component: ComisariasListaComponent },
      { path: 'turnos', component: TurnosListaComponent },
    ]
  },

  // Redirecciones retrocompatibles para URLs directas
  { path: 'monitoreo', redirectTo: 'central/monitoreo', pathMatch: 'full' },
  { path: 'buses', redirectTo: 'central/buses', pathMatch: 'full' },
  { path: 'incidentes', redirectTo: 'central/incidentes', pathMatch: 'full' },
  { path: 'conductores', redirectTo: 'central/conductores', pathMatch: 'full' },
  { path: 'comisarias', redirectTo: 'central/comisarias', pathMatch: 'full' },
  { path: 'turnos', redirectTo: 'central/turnos', pathMatch: 'full' },

  // Panel móvil del chofer en ruta (Protegido por DNI)
  { path: 'chofer', component: ChoferDashboardComponent, canActivate: [choferGuard] },

  { path: '**', redirectTo: '' }
];


