import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { SseService } from '../../../core/services/sse.service';
import { NotificacionService } from '../../../core/services/notificacion.service';
import { ApiService } from '../../../core/services/api.service';
import { RegistrarConductorModalComponent } from '../components/registrar-conductor-modal.component';
import { RegistrarBusModalComponent } from '../components/registrar-bus-modal.component';
import { RegistrarComisariaModalComponent } from '../components/registrar-comisaria-modal.component';
import { RegistrarTurnoModalComponent } from '../components/registrar-turno-modal.component';
import { ToastContainerComponent } from '../../../shared/components/toast-container.component';

@Component({
  selector: 'app-central-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    RegistrarConductorModalComponent,
    RegistrarBusModalComponent,
    RegistrarComisariaModalComponent,
    RegistrarTurnoModalComponent,
    ToastContainerComponent
  ],
  template: `
    <div class="pantalla-central">
      <!-- SIDEBAR MAESTRO REUTILIZABLE -->
      <aside class="sidebar">
        <div class="logo">
          <span class="icono-escudo">🛡️</span>
          <div>
            <div class="titulo-sat">SAT-CARMEN</div>
            <div class="sub-sat">ALERTA EN TIEMPO REAL</div>
          </div>
        </div>

        <div class="seccion-label">CONSOLA OPERACIÓN</div>
        <nav class="menu">
          <a class="item-menu" routerLink="/central/monitoreo" routerLinkActive="activo">
            <span class="ico">🗺️</span> Mapa en Vivo
          </a>
          <a class="item-menu" routerLink="/central/buses" routerLinkActive="activo">
            <span class="ico">🚍</span> Flota de Buses
          </a>
          <a class="item-menu incidente" routerLink="/central/incidentes" routerLinkActive="activo">
            <span class="ico">⚠️</span> Incidentes Activos
            <span class="badge-alerta" *ngIf="cantidadAlertas > 0">{{ cantidadAlertas }}</span>
          </a>
          <a class="item-menu" routerLink="/central/conductores" routerLinkActive="activo">
            <span class="ico">👥</span> Padrón Choferes
          </a>
          <a class="item-menu" routerLink="/central/comisarias" routerLinkActive="activo">
            <span class="ico">🚓</span> Bases Policiales
          </a>
          <a class="item-menu" routerLink="/central/turnos" routerLinkActive="activo">
            <span class="ico">📋</span> Asignación Turnos
          </a>
        </nav>

        <div class="seccion-label">FORMULARIOS DE REGISTRO</div>
        <div class="acciones-rapidas">
          <button class="btn-sidebar-accion btn-chofer" (click)="mostrarModalConductor = true">
            + Chofer
          </button>
          <button class="btn-sidebar-accion btn-bus" (click)="mostrarModalBus = true">
            + Bus
          </button>
          <button class="btn-sidebar-accion btn-pnp" (click)="mostrarModalComisaria = true">
            + Comisaría
          </button>
          <button class="btn-sidebar-accion btn-turno" (click)="mostrarModalTurno = true">
            + Turno
          </button>
        </div>

        <div class="pie-sidebar">
          <div class="usuario-info">
            <div class="avatar">{{ inicialesUsuario }}</div>
            <div>
              <div class="usuario-nom">{{ authService.currentUser()?.nombreUsuario || 'Operador Central' }}</div>
              <div class="usuario-rol">{{ authService.currentUser()?.rol || 'Administrador' }}</div>
            </div>
          </div>
          <button class="btn-salir" (click)="salir()">Cerrar sesión</button>
        </div>
      </aside>

      <!-- VISTA HIJA INYECTADA DINÁMICAMENTE -->
      <main class="contenedor-vistas">
        <router-outlet></router-outlet>
      </main>

      <!-- MODALES DE LAS 4 TABLAS DEL SISTEMA -->
      <app-registrar-conductor-modal
        *ngIf="mostrarModalConductor"
        (conductorRegistrado)="onConductorRegistrado()"
        (cerrar)="mostrarModalConductor = false"
        (onCerrar)="mostrarModalConductor = false"
      ></app-registrar-conductor-modal>

      <app-registrar-bus-modal
        *ngIf="mostrarModalBus"
        (busRegistrado)="onBusRegistrado()"
        (cerrar)="mostrarModalBus = false"
        (onCerrar)="mostrarModalBus = false"
      ></app-registrar-bus-modal>

      <app-registrar-comisaria-modal
        *ngIf="mostrarModalComisaria"
        (comisariaRegistrada)="onComisariaRegistrada()"
        (cerrar)="mostrarModalComisaria = false"
        (onCerrar)="mostrarModalComisaria = false"
      ></app-registrar-comisaria-modal>

      <app-registrar-turno-modal
        *ngIf="mostrarModalTurno"
        (turnoRegistrado)="onTurnoRegistrado()"
        (cerrar)="mostrarModalTurno = false"
        (onCerrar)="mostrarModalTurno = false"
      ></app-registrar-turno-modal>

      <!-- CONTENEDOR FLOTANTE DE NOTIFICACIONES -->
      <app-toast-container></app-toast-container>
    </div>
  `,
  styles: [`
    .pantalla-central {
      display: flex;
      min-height: 100vh;
      background: #090d16;
      font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      color: #f8fafc;
    }

    .sidebar {
      width: 250px;
      min-width: 250px;
      background: #0f172a;
      border-right: 1px solid #1e293b;
      display: flex;
      flex-direction: column;
      padding: 20px;
      box-sizing: border-box;
      height: 100vh;
      position: sticky;
      top: 0;
    }

    .logo {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 25px;
    }

    .icono-escudo {
      font-size: 28px;
    }

    .titulo-sat {
      font-weight: 800;
      font-size: 14px;
      color: #ffffff;
      letter-spacing: 0.5px;
    }

    .sub-sat {
      font-size: 9px;
      font-weight: 700;
      color: #64748b;
    }

    .seccion-label {
      font-size: 10px;
      font-weight: 700;
      color: #64748b;
      margin-bottom: 10px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .menu {
      display: flex;
      flex-direction: column;
      gap: 6px;
      margin-bottom: 25px;
    }

    .item-menu {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px 14px;
      border-radius: 8px;
      font-size: 13px;
      font-weight: 600;
      color: #94a3b8;
      cursor: pointer;
      text-decoration: none;
      transition: all 0.2s;
    }

    .item-menu:hover {
      background: #1e293b;
      color: #ffffff;
    }

    .item-menu.activo {
      background: #1e293b;
      color: #ffffff;
      border-left: 3px solid #3b82f6;
    }

    .item-menu.incidente.activo {
      border-left-color: #f43f5e;
    }

    .item-menu.incidente {
      color: #fb7185;
    }

    .badge-alerta {
      margin-left: auto;
      background: #e11d48;
      color: white;
      font-size: 11px;
      font-weight: 700;
      padding: 2px 7px;
      border-radius: 999px;
      animation: pulso 2s infinite;
    }

    @keyframes pulso {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.6; }
    }

    .acciones-rapidas {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 6px;
      margin-bottom: auto;
    }

    .btn-sidebar-accion {
      background: #1e293b;
      padding: 8px 6px;
      border-radius: 8px;
      font-size: 11px;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.2s;
      border: 1px solid transparent;
      text-align: center;
    }

    .btn-sidebar-accion.btn-chofer {
      color: #38bdf8;
      border-color: rgba(56, 189, 248, 0.4);
    }
    .btn-sidebar-accion.btn-chofer:hover {
      background: rgba(56, 189, 248, 0.15);
    }

    .btn-sidebar-accion.btn-bus {
      color: #f59e0b;
      border-color: rgba(245, 158, 11, 0.4);
    }
    .btn-sidebar-accion.btn-bus:hover {
      background: rgba(245, 158, 11, 0.15);
    }

    .btn-sidebar-accion.btn-pnp {
      color: #ef4444;
      border-color: rgba(239, 68, 68, 0.4);
    }
    .btn-sidebar-accion.btn-pnp:hover {
      background: rgba(239, 68, 68, 0.15);
    }

    .btn-sidebar-accion.btn-turno {
      color: #10b981;
      border-color: rgba(16, 185, 129, 0.4);
    }
    .btn-sidebar-accion.btn-turno:hover {
      background: rgba(16, 185, 129, 0.15);
    }

    .pie-sidebar {
      border-top: 1px solid #1e293b;
      padding-top: 16px;
      margin-top: auto;
    }

    .usuario-info {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 12px;
    }

    .avatar {
      width: 34px;
      height: 34px;
      border-radius: 50%;
      background: #3b82f6;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      font-weight: 700;
    }

    .usuario-nom {
      font-size: 12px;
      font-weight: 700;
      color: #ffffff;
    }

    .usuario-rol {
      font-size: 10px;
      color: #64748b;
    }

    .btn-salir {
      width: 100%;
      padding: 8px;
      background: #1e293b;
      color: #f87171;
      border: 1px solid #334155;
      border-radius: 6px;
      font-size: 11px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-salir:hover {
      background: #ef4444;
      color: #ffffff;
      border-color: #ef4444;
    }

    .contenedor-vistas {
      flex: 1;
      overflow-y: auto;
      height: 100vh;
    }
  `]
})
export class CentralLayoutComponent implements OnInit {
  cantidadAlertas = 1;
  mostrarModalConductor = false;
  mostrarModalBus = false;
  mostrarModalComisaria = false;
  mostrarModalTurno = false;

  constructor(
    public authService: AuthService,
    private sseService: SseService,
    private notificacionService: NotificacionService,
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarConteoAlertas();
    this.escucharAlertasTiempoReal();
  }

  get inicialesUsuario(): string {
    const nom = this.authService.currentUser()?.nombreUsuario || 'OP';
    return nom.substring(0, 2).toUpperCase();
  }

  cargarConteoAlertas(): void {
    this.apiService.getAlertas().subscribe({
      next: (alertas) => {
        this.cantidadAlertas = alertas.filter(a => a.estado !== 'Atendida' && a.estado !== 'Falsa Alarma').length;
      },
      error: () => {}
    });
  }

  escucharAlertasTiempoReal(): void {
    this.sseService.alertaRecibida$.subscribe({
      next: (alerta) => {
        this.cantidadAlertas++;
        this.notificacionService.alertaCritica(
          '🚨 ¡ALERTA DE PÁNICO EN RUTA!',
          `Unidad BUS-0${alerta.idBus} emitió señal de extorsión/auxilio en tiempo real.`
        );
      }
    });

    this.sseService.cambioEstado$.subscribe({
      next: (evento) => {
        this.notificacionService.info(
          'Estado Actualizado',
          `Incidente #${evento.idAlerta} ahora está en: "${evento.nuevoEstado}".`
        );
      }
    });
  }

  onConductorRegistrado(): void {
    this.mostrarModalConductor = false;
    this.notificacionService.exito(
      'Chofer Registrado',
      'El nuevo conductor fue agregado exitosamente al padrón oficial.'
    );
  }

  onBusRegistrado(): void {
    this.mostrarModalBus = false;
    this.notificacionService.exito(
      'Bus Registrado',
      'La nueva unidad de transporte fue incorporada a la flota activa.'
    );
  }

  onComisariaRegistrada(): void {
    this.mostrarModalComisaria = false;
    this.notificacionService.exito(
      'Comisaría Registrada',
      'La dependencia policial fue agregada al directorio de auxilio rápido.'
    );
  }

  onTurnoRegistrado(): void {
    this.mostrarModalTurno = false;
    this.notificacionService.exito(
      'Turno Programado',
      'El conductor fue asignado a la unidad de transporte exitosamente.'
    );
  }

  salir(): void {
    this.authService.cerrarSesion();
  }
}
