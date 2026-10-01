import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { AsignacionTurno } from '../../core/models/sistema.models';
import { RegistrarTurnoModalComponent } from './components/registrar-turno-modal.component';

@Component({
  selector: 'app-turnos-lista',
  standalone: true,
  imports: [CommonModule, RegistrarTurnoModalComponent],
  template: `
    <div class="contenido">
      <header class="barra-superior">
        <div>
          <h2>Programación y Asignación de Turnos</h2>
          <p>Control operativo de conductores asignados a unidades de transporte en ruta</p>
        </div>
        <div class="acciones-header">
          <button class="btn-nuevo-turno" (click)="mostrarModal = true">
            + Asignar Turno
          </button>
          <button class="btn-refrescar" (click)="cargarTurnos()">
            🔄 Actualizar
          </button>
        </div>
      </header>

      <!-- TARJETAS RESUMEN / KPIS -->
      <section class="grid-resumen">
        <div class="card-kpi">
          <span class="kpi-label">TOTAL TURNOS</span>
          <div class="kpi-numero azul">{{ turnos.length }}</div>
          <span class="kpi-detalle">Programados en sistema</span>
        </div>
        <div class="card-kpi">
          <span class="kpi-label">TURNOS ACTIVOS</span>
          <div class="kpi-numero verde">{{ contarActivos() }}</div>
          <span class="kpi-detalle">En servicio en este momento</span>
        </div>
        <div class="card-kpi">
          <span class="kpi-label">CUMPLIMIENTO FLOTA</span>
          <div class="kpi-numero amarillo">100%</div>
          <span class="kpi-detalle">Unidades cubiertas</span>
        </div>
      </section>

      <!-- TABLA DE TURNOS -->
      <section class="card-tabla">
        <div class="tabla-scroll">
          <table class="tabla-datos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Fecha</th>
                <th>Horario de Ruta</th>
                <th>Conductor Designado</th>
                <th>Unidad Móvil</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let t of turnos">
                <td><span class="id-tag">#{{ t.idAsignacion || '-' }}</span></td>
                <td><b>{{ t.fecha }}</b></td>
                <td>
                  <span class="badge-horario">⏰ {{ t.horaInicio }} - {{ t.horaFin || 'En ruta' }}</span>
                </td>
                <td>
                  <div class="nombre-conductor">
                    <span>👨‍✈️</span>
                    <div>
                      <b>{{ t.nombreConductor || ('Conductor #' + t.idConductor) }}</b>
                      <small *ngIf="t.dniConductor"> (DNI: {{ t.dniConductor }})</small>
                    </div>
                  </div>
                </td>
                <td>
                  <span class="badge-bus">
                    🚌 {{ t.codigoUnidad || ('BUS-0' + t.idBus) }} • {{ t.placaBus || 'P-71A' }}
                  </span>
                </td>
                <td>
                  <span class="badge-estado" [ngClass]="t.estado === 'Activo' ? 'activo' : 'finalizado'">
                    {{ t.estado || 'Activo' }}
                  </span>
                </td>
              </tr>
              <tr *ngIf="cargando && turnos.length === 0">
                <td colspan="6" class="sin-datos">⏳ Conectando con la Central y cargando programación de turnos...</td>
              </tr>
              <tr *ngIf="!cargando && turnos.length === 0">
                <td colspan="6" class="sin-datos">No hay turnos programados. Use el botón "+ Asignar Turno".</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- MODAL PARA REGISTRAR NUEVO TURNO -->
      <app-registrar-turno-modal
        *ngIf="mostrarModal"
        (turnoRegistrado)="onTurnoRegistrado()"
        (cerrar)="mostrarModal = false"
        (onCerrar)="mostrarModal = false"
      ></app-registrar-turno-modal>
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

    .btn-nuevo-turno {
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

    .btn-nuevo-turno:hover { background: #059669; transform: translateY(-1px); }

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
    .kpi-numero.amarillo { color: #f59e0b; }
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

    .badge-horario {
      background: rgba(2, 132, 199, 0.15);
      border: 1px solid rgba(2, 132, 199, 0.3);
      color: #38bdf8;
      padding: 4px 8px;
      border-radius: 6px;
      font-size: 11px;
      font-weight: 600;
    }

    .nombre-conductor {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .nombre-conductor small {
      color: #94a3b8;
    }

    .badge-bus {
      background: rgba(245, 158, 11, 0.12);
      border: 1px solid rgba(245, 158, 11, 0.25);
      color: #fbbf24;
      padding: 4px 8px;
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

    .badge-estado.finalizado {
      background: rgba(148, 163, 184, 0.15);
      color: #94a3b8;
    }

    .sin-datos {
      text-align: center;
      padding: 30px;
      color: #64748b;
    }
  `]
})
export class TurnosListaComponent implements OnInit {
  turnos: AsignacionTurno[] = [];
  mostrarModal = false;
  cargando = true;

  private apiService = inject(ApiService);

  ngOnInit(): void {
    this.cargarTurnos();
  }

  cargarTurnos(): void {
    this.cargando = true;
    this.apiService.getTurnos().subscribe({
      next: (list) => {
        this.turnos = list || [];
        this.cargando = false;
      },
      error: (e) => {
        console.error('Error al cargar turnos:', e);
        this.cargando = false;
      }
    });
  }

  contarActivos(): number {
    return this.turnos.filter(t => t.estado === 'Activo' || !t.estado).length;
  }

  onTurnoRegistrado(): void {
    this.mostrarModal = false;
    this.cargarTurnos();
  }
}
