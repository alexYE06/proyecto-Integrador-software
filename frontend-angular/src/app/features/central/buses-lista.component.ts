import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { Bus, AsignacionTurno } from '../../core/models/sistema.models';
import { RegistrarBusModalComponent } from './components/registrar-bus-modal.component';

interface BusConductorInfo extends Bus {
  conductorAsignado?: string;
  dniConductor?: string;
}

@Component({
  selector: 'app-buses-lista',
  standalone: true,
  imports: [CommonModule, RegistrarBusModalComponent],
  template: `
    <div class="contenido">
      <header class="barra-superior">
        <div>
          <h2>Padrón de Flota de Buses</h2>
          <p>Unidades de transporte registradas y su asignación operativa de chofer</p>
        </div>
        <div class="acciones-header">
          <button class="btn-nuevo-bus" (click)="mostrarModal = true">
            + Registrar Bus
          </button>
          <button class="btn-refrescar" (click)="cargarDatos()">
            🔄 Actualizar
          </button>
        </div>
      </header>

      <!-- TARJETAS KPIS -->
      <section class="grid-resumen">
        <div class="card-kpi">
          <span class="kpi-label">TOTAL BUSES EN FLOTA</span>
          <div class="kpi-numero azul">{{ buses.length }}</div>
          <span class="kpi-detalle">Vehículos registrados</span>
        </div>
        <div class="card-kpi">
          <span class="kpi-label">UNIDADES EN RUTA</span>
          <div class="kpi-numero verde">{{ contarActivos() }}</div>
          <span class="kpi-detalle">Operando en servicio</span>
        </div>
        <div class="card-kpi">
          <span class="kpi-label">CON CHOFER DESIGNADO</span>
          <div class="kpi-numero amarillo">{{ contarAsignados() }}</div>
          <span class="kpi-detalle">Turno de ruta vigente</span>
        </div>
      </section>

      <!-- TABLA DE BUSES -->
      <section class="card-tabla">
        <div class="tabla-scroll">
          <table class="tabla-datos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Código Unidad</th>
                <th>Número de Placa</th>
                <th>Modelo / Carrocería</th>
                <th>Capacidad</th>
                <th>Conductor Asignado</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let b of buses">
                <td><span class="id-tag">#{{ b.idBus || '-' }}</span></td>
                <td>
                  <span class="badge-unidad">🚌 {{ b.numeroUnidad }}</span>
                </td>
                <td>
                  <span class="badge-placa">{{ b.placa }}</span>
                </td>
                <td>{{ b.modelo || 'Mercedes-Benz O500' }}</td>
                <td><b>{{ b.capacidad || 40 }}</b> pasajeros</td>
                <td>
                  <div *ngIf="b.conductorAsignado" class="info-chofer-asignado">
                    <span class="ico-chofer">👨‍✈️</span>
                    <div>
                      <b>{{ b.conductorAsignado }}</b>
                      <small *ngIf="b.dniConductor"> (DNI: {{ b.dniConductor }})</small>
                    </div>
                  </div>
                  <span *ngIf="!b.conductorAsignado" class="sin-asignar">
                    ⚠️ Sin turno asignado
                  </span>
                </td>
                <td>
                  <span class="badge-estado" [ngClass]="b.estado === 'Activo' ? 'activo' : (b.estado === 'En Alerta' ? 'alerta' : 'mantenimiento')">
                    {{ b.estado || 'Activo' }}
                  </span>
                </td>
              </tr>
              <tr *ngIf="cargando && buses.length === 0">
                <td colspan="7" class="sin-datos">⏳ Conectando con la Central y cargando flota vehicular...</td>
              </tr>
              <tr *ngIf="!cargando && buses.length === 0">
                <td colspan="7" class="sin-datos">No hay buses registrados. Use el botón "+ Registrar Bus".</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- MODAL PARA REGISTRAR NUEVO BUS -->
      <app-registrar-bus-modal
        *ngIf="mostrarModal"
        (busRegistrado)="onBusRegistrado()"
        (cerrar)="mostrarModal = false"
        (onCerrar)="mostrarModal = false"
      ></app-registrar-bus-modal>
    </div>
  `,
  styles: [`
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

    .btn-nuevo-bus {
      padding: 9px 16px;
      background: #f59e0b;
      border: none;
      color: #ffffff;
      font-weight: 700;
      border-radius: 8px;
      font-size: 13px;
      cursor: pointer;
      box-shadow: 0 0 15px rgba(245, 158, 11, 0.3);
      transition: all 0.2s;
    }

    .btn-nuevo-bus:hover { background: #d97706; transform: translateY(-1px); }

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

    /* KPIS */
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
    .kpi-numero.amarillo { color: #fbbf24; }
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

    .badge-unidad {
      background: rgba(2, 132, 199, 0.15);
      border: 1px solid rgba(2, 132, 199, 0.3);
      color: #38bdf8;
      padding: 4px 8px;
      border-radius: 6px;
      font-size: 12px;
      font-weight: 700;
    }

    .badge-placa {
      background: #1e293b;
      border: 1px solid #475569;
      color: #ffffff;
      padding: 3px 8px;
      border-radius: 4px;
      font-family: monospace;
      font-weight: 700;
    }

    .info-chofer-asignado {
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .info-chofer-asignado small {
      color: #94a3b8;
    }

    .sin-asignar {
      color: #94a3b8;
      font-size: 12px;
      font-style: italic;
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

    .badge-estado.alerta {
      background: rgba(239, 68, 68, 0.15);
      color: #f87171;
    }

    .badge-estado.mantenimiento {
      background: rgba(245, 158, 11, 0.15);
      color: #fbbf24;
    }

    .sin-datos {
      text-align: center;
      padding: 30px;
      color: #64748b;
    }
  `]
})
export class BusesListaComponent implements OnInit {
  buses: BusConductorInfo[] = [];
  mostrarModal = false;
  cargando = true;

  private apiService = inject(ApiService);

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.apiService.getBuses().subscribe({
      next: (busesData) => {
        // Cargar también turnos para vincular conductor asignado a cada bus
        this.apiService.getTurnos().subscribe({
          next: (turnosData) => {
            this.vincularBusesConTurnos(busesData || [], turnosData || []);
            this.cargando = false;
          },
          error: () => {
            this.vincularBusesConTurnos(busesData || [], []);
            this.cargando = false;
          }
        });
      },
      error: (e) => {
        console.error('Error al cargar buses:', e);
        this.cargando = false;
      }
    });
  }

  private vincularBusesConTurnos(buses: Bus[], turnos: AsignacionTurno[]): void {
    this.buses = buses.map(bus => {
      const busInfo: BusConductorInfo = { ...bus };
      // Buscar turno asignado a este bus
      const turnoActivo = turnos.find(t => t.idBus === bus.idBus);
      if (turnoActivo) {
        busInfo.conductorAsignado = turnoActivo.nombreConductor;
        busInfo.dniConductor = turnoActivo.dniConductor;
      }
      return busInfo;
    });
  }

  contarActivos(): number {
    return this.buses.filter(b => b.estado === 'Activo' || !b.estado).length;
  }

  contarAsignados(): number {
    return this.buses.filter(b => !!b.conductorAsignado).length;
  }

  onBusRegistrado(): void {
    this.mostrarModal = false;
    this.cargarDatos();
  }
}
