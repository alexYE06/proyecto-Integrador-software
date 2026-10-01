import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { Conductor } from '../../core/models/sistema.models';
import { RegistrarConductorModalComponent } from './components/registrar-conductor-modal.component';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-conductores-lista',
  standalone: true,
  imports: [CommonModule, RegistrarConductorModalComponent],
  template: `
    <div class="contenido">
      <header class="barra-superior">
          <div>
            <h2>Padrón de Conductores y Personal en Ruta</h2>
            <p>Listado oficial de choferes asignables a unidades de transporte</p>
          </div>
          <div class="acciones-header">
            <button class="btn-nuevo-chofer" (click)="mostrarModal = true">
              + Registrar Chofer
            </button>
            <button class="btn-refrescar" (click)="cargarConductores()">
              🔄 Actualizar
            </button>
          </div>
        </header>

        <!-- TARJETAS RESUMEN -->
        <section class="grid-resumen">
          <div class="card-kpi">
            <span class="kpi-label">TOTAL CONDUCTORES</span>
            <div class="kpi-numero">{{ conductores.length }}</div>
            <span class="kpi-detalle">Registrados en base de datos</span>
          </div>
          <div class="card-kpi">
            <span class="kpi-label">ACTIVOS EN TURNO</span>
            <div class="kpi-numero verde">{{ contarActivos() }}</div>
            <span class="kpi-detalle">Aptos para conducción</span>
          </div>
          <div class="card-kpi">
            <span class="kpi-label">VERIFICACIÓN BREVETE</span>
            <div class="kpi-numero azul">100%</div>
            <span class="kpi-detalle">MTC Vigente</span>
          </div>
        </section>

        <!-- TABLA DE CONDUCTORES -->
        <section class="card-tabla">
          <div class="tabla-scroll">
            <table class="tabla-datos">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Conductor</th>
                  <th>DNI</th>
                  <th>Teléfono Móvil</th>
                  <th>Brevete / Licencia</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let c of conductores">
                  <td><span class="id-tag">#{{ c.idConductor || '-' }}</span></td>
                  <td>
                    <div class="nombre-conductor">
                      <b>{{ c.nombres }} {{ c.apellidos }}</b>
                    </div>
                  </td>
                  <td><code>{{ c.dni }}</code></td>
                  <td>{{ c.telefono }}</td>
                  <td><span class="badge-brevete">{{ c.licencia }}</span></td>
                  <td>
                    <span class="badge-estado" [ngClass]="c.estado === 'Activo' ? 'activo' : 'inactivo'">
                      {{ c.estado || 'Activo' }}
                    </span>
                  </td>
                </tr>
                <tr *ngIf="cargando && conductores.length === 0">
                  <td colspan="6" class="sin-datos">⏳ Conectando con la Central y cargando padrón de conductores...</td>
                </tr>
                <tr *ngIf="!cargando && conductores.length === 0">
                  <td colspan="6" class="sin-datos">No hay conductores registrados en el sistema. Utilice el botón "+ Registrar Chofer".</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <!-- MODAL PARA REGISTRAR NUEVO CONDUCTOR -->
        <app-registrar-conductor-modal 
          *ngIf="mostrarModal" 
          (conductorRegistrado)="cerrarModalYRecargar()"
          (onCerrar)="cerrarModalYRecargar()"
          (cerrar)="mostrarModal = false">
        </app-registrar-conductor-modal>
      </div>
  `,
  styles: [`
    /* VISTA CONDUCTORES */




    .contenido {
      flex-grow: 1;
      padding: 25px 30px;
      overflow-y: auto;
      box-sizing: border-box;
    }

    .barra-superior {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;
    }

    .barra-superior h2 { margin: 0 0 4px 0; font-size: 22px; color: #ffffff; }
    .barra-superior p { margin: 0; color: #64748b; font-size: 13px; }

    .acciones-header { display: flex; gap: 10px; }

    .btn-nuevo-chofer {
      padding: 9px 16px;
      background: #10b981;
      border: none;
      color: #ffffff;
      font-weight: 700;
      border-radius: 8px;
      font-size: 13px;
      cursor: pointer;
      box-shadow: 0 0 15px rgba(16, 185, 129, 0.3);
      transition: all 0.2s;
    }

    .btn-nuevo-chofer:hover { background: #059669; transform: translateY(-1px); }

    .btn-refrescar {
      padding: 9px 14px;
      background: #1e293b;
      border: 1px solid #334155;
      color: #cbd5e1;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 600;
      cursor: pointer;
    }

    .btn-refrescar:hover { background: #334155; }

    /* RESUMEN KPIS */
    .grid-resumen {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 16px;
      margin-bottom: 24px;
    }

    .card-kpi {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 18px 22px;
      box-shadow: 0 4px 6px -1px rgba(0,0,0,0.4);
    }

    .kpi-label { font-size: 10px; font-weight: 700; color: #64748b; letter-spacing: 0.5px; }
    .kpi-numero { font-size: 28px; font-weight: 800; margin: 4px 0; color: #ffffff; }
    .kpi-numero.verde { color: #34d399; }
    .kpi-numero.azul { color: #38bdf8; }
    .kpi-detalle { font-size: 11px; color: #64748b; }

    /* TABLA */
    .card-tabla {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 14px;
      padding: 20px;
      box-shadow: 0 4px 6px -1px rgba(0,0,0,0.5);
    }

    .tabla-scroll {
      max-height: 480px;
      overflow-y: auto;
    }

    .tabla-datos {
      width: 100%;
      border-collapse: collapse;
      font-size: 13px;
    }

    .tabla-datos th {
      text-align: left;
      padding: 12px 10px;
      border-bottom: 1px solid #1e293b;
      color: #64748b;
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .tabla-datos td {
      padding: 14px 10px;
      border-bottom: 1px solid #1e293b;
    }

    .id-tag {
      font-size: 11px;
      color: #64748b;
      font-family: monospace;
    }

    code {
      background: #1e293b;
      padding: 3px 6px;
      border-radius: 4px;
      font-size: 12px;
      color: #38bdf8;
    }

    .badge-brevete {
      background: rgba(16, 185, 129, 0.1);
      border: 1px solid rgba(16, 185, 129, 0.3);
      color: #34d399;
      padding: 3px 8px;
      border-radius: 6px;
      font-size: 11px;
      font-weight: 700;
    }

    .badge-estado {
      padding: 3px 8px;
      border-radius: 999px;
      font-size: 10px;
      font-weight: 700;
    }

    .badge-estado.activo {
      background: rgba(34, 197, 94, 0.15);
      color: #4ade80;
    }

    .badge-estado.inactivo {
      background: rgba(239, 68, 68, 0.15);
      color: #f87171;
    }

    .sin-datos {
      text-align: center;
      padding: 30px;
      color: #64748b;
    }
  `]
})
export class ConductoresListaComponent implements OnInit {
  conductores: Conductor[] = [];
  mostrarModal = false;
  cargando = true;

  constructor(
    private apiService: ApiService,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarConductores();
  }

  cargarConductores(): void {
    this.cargando = true;
    this.apiService.getConductores().subscribe({
      next: (data) => {
        this.conductores = data || [];
        this.cargando = false;
      },
      error: (e) => {
        console.error('Error al cargar conductores:', e);
        this.cargando = false;
      }
    });
  }

  contarActivos(): number {
    return this.conductores.filter(c => c.estado === 'Activo' || !c.estado).length;
  }

  cerrarModalYRecargar(): void {
    this.mostrarModal = false;
    this.cargarConductores();
  }

  irAMonitoreo(): void { this.router.navigate(['/monitoreo']); }
  irAIncidentes(): void { this.router.navigate(['/incidentes']); }
  salir(): void { this.authService.cerrarSesion(); }
}
