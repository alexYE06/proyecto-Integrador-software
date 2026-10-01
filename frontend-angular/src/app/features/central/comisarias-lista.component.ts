import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { Comisaria } from '../../core/models/sistema.models';
import { RegistrarComisariaModalComponent } from './components/registrar-comisaria-modal.component';

@Component({
  selector: 'app-comisarias-lista',
  standalone: true,
  imports: [CommonModule, RegistrarComisariaModalComponent],
  template: `
    <div class="contenido">
      <header class="barra-superior">
        <div>
          <h2>Directorio de Comisarías y Bases Policiales</h2>
          <p>Dependencias PNP del Callao para auxilio rápido ante asaltos o extorsión</p>
        </div>
        <div class="acciones-header">
          <button class="btn-nueva-comisaria" (click)="mostrarModal = true">
            + Registrar Comisaría
          </button>
          <button class="btn-refrescar" (click)="cargarComisarias()">
            🔄 Actualizar
          </button>
        </div>
      </header>

      <!-- TARJETAS RESUMEN / KPIS -->
      <section class="grid-resumen">
        <div class="card-kpi">
          <span class="kpi-label">TOTAL DEPENDENCIAS</span>
          <div class="kpi-numero rojo">{{ comisarias.length }}</div>
          <span class="kpi-detalle">Registradas para despacho</span>
        </div>
        <div class="card-kpi">
          <span class="kpi-label">JURISDICCIÓN PRINCIPAL</span>
          <div class="kpi-numero azul">Callao</div>
          <span class="kpi-detalle">Carmen de la Legua y Bellavista</span>
        </div>
        <div class="card-kpi">
          <span class="kpi-label">ENLACE DE EMERGENCIA</span>
          <div class="kpi-numero verde">105 PNP</div>
          <span class="kpi-detalle">Canal directo activo</span>
        </div>
      </section>

      <!-- TABLA DE COMISARÍAS -->
      <section class="card-tabla">
        <div class="tabla-scroll">
          <table class="tabla-datos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Dependencia Policial</th>
                <th>Dirección / Ubicación</th>
                <th>Teléfono de Contacto</th>
                <th>Coordenadas GPS</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let c of comisarias">
                <td><span class="id-tag">#{{ c.idComisaria || '-' }}</span></td>
                <td>
                  <div class="nombre-comisaria">
                    <span class="icono-pnp">🚓</span>
                    <b>{{ c.nombre }}</b>
                  </div>
                </td>
                <td>{{ c.direccion }}</td>
                <td>
                  <span class="badge-tel">📞 {{ c.telefono }}</span>
                </td>
                <td>
                  <code>{{ c.latitud || '-12.042' }}, {{ c.longitud || '-77.090' }}</code>
                </td>
              </tr>
              <tr *ngIf="cargando && comisarias.length === 0">
                <td colspan="5" class="sin-datos">⏳ Conectando con la Central y cargando dependencias policiales...</td>
              </tr>
              <tr *ngIf="!cargando && comisarias.length === 0">
                <td colspan="5" class="sin-datos">No hay comisarías registradas. Use el botón "+ Registrar Comisaría".</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- MODAL PARA REGISTRAR NUEVA COMISARÍA -->
      <app-registrar-comisaria-modal
        *ngIf="mostrarModal"
        (comisariaRegistrada)="onComisariaRegistrada()"
        (cerrar)="mostrarModal = false"
        (onCerrar)="mostrarModal = false"
      ></app-registrar-comisaria-modal>
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

    .btn-nueva-comisaria {
      padding: 9px 16px;
      background: #ef4444;
      border: none;
      color: #ffffff;
      font-weight: 700;
      border-radius: 8px;
      font-size: 13px;
      cursor: pointer;
      box-shadow: 0 0 15px rgba(239, 68, 68, 0.3);
      transition: all 0.2s;
    }

    .btn-nueva-comisaria:hover { background: #dc2626; transform: translateY(-1px); }

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
    .kpi-numero.rojo { color: #f87171; }
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

    .nombre-comisaria {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .icono-pnp {
      font-size: 16px;
    }

    .badge-tel {
      background: rgba(14, 165, 233, 0.12);
      border: 1px solid rgba(56, 189, 248, 0.25);
      color: #38bdf8;
      padding: 4px 8px;
      border-radius: 6px;
      font-size: 12px;
      font-weight: 600;
    }

    code {
      background: #1e293b;
      padding: 3px 6px;
      border-radius: 4px;
      font-size: 11px;
      color: #94a3b8;
    }

    .sin-datos {
      text-align: center;
      padding: 30px;
      color: #64748b;
    }
  `]
})
export class ComisariasListaComponent implements OnInit {
  comisarias: Comisaria[] = [];
  mostrarModal = false;
  cargando = true;

  private apiService = inject(ApiService);

  ngOnInit(): void {
    this.cargarComisarias();
  }

  cargarComisarias(): void {
    this.cargando = true;
    this.apiService.getComisarias().subscribe({
      next: (list) => {
        this.comisarias = list || [];
        this.cargando = false;
      },
      error: (e) => {
        console.error('Error al cargar comisarías:', e);
        this.cargando = false;
      }
    });
  }

  onComisariaRegistrada(): void {
    this.mostrarModal = false;
    this.cargarComisarias();
  }
}
