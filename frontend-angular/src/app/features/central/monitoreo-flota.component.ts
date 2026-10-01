import { Component, OnInit, OnDestroy, AfterViewInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { Bus, Alerta, Comisaria, AsignacionTurno } from '../../core/models/sistema.models';
import { RegistrarConductorModalComponent } from './components/registrar-conductor-modal.component';
import { AuthService } from '../../core/services/auth.service';
import { SseService } from '../../core/services/sse.service';
import { DespachoPolicialService, ComisariaCercanaResultado, ReporteIncidenteData } from '../../core/services/despacho-policial.service';
import * as L from 'leaflet';

interface BusUbicacion {
  idBus: number;
  codigoUnidad: string;
  placa: string;
  modelo: string;
  conductor: string;
  estado: string; // 'Activo', 'En Alerta', 'Mantenimiento'
  lat: number;
  lng: number;
  velocidad: number;
  rumbo: string;
  distritoActual: string;
  waypointIndex: number;
  direccionRuta: number; // 1 hacia Ate, -1 hacia Callao
  comisariaCercana?: ComisariaCercanaResultado | null;
}

interface ParadaEmblematica {
  nombre: string;
  distrito: string;
  lat: number;
  lng: number;
}

@Component({
  selector: 'app-monitoreo-flota',
  standalone: true,
  imports: [CommonModule, FormsModule, RegistrarConductorModalComponent],
  template: `
    <div class="contenido">
      <!-- HEADER CONSOLA -->
      <header class="barra-superior">
        <div>
          <h2>Centro de Control GPS • Línea 1305 (71A)</h2>
          <p>Trazado oficial Moovit: Ate (Las Gardenias) ⇄ Lima ⇄ Callao (La Punta / Colonial)</p>
        </div>
        <div class="botones-header">
          <!-- SELECTOR DE VISTA DEL MAPA -->
          <button class="btn-accion-header" (click)="toggleModoMapa()">
            {{ mapaModoOscuro ? '☀️ Modo Calles' : '🌙 Modo Táctico Oscuro' }}
          </button>
          
          <!-- BOTONES DE ENFOQUE POR SECTOR DE LA RUTA -->
          <button class="btn-accion-header" (click)="centrarEnCarmenDeLaLegua()">
            🎯 Carmen de la Legua / Callao
          </button>
          <button class="btn-accion-header" (click)="centrarEnLimaCentro()">
            🏛️ Lima Centro (Bolognesi/Grau)
          </button>
          <button class="btn-accion-header" (click)="centrarEnAteSantaAnita()">
            🏙️ Santa Anita / Ate
          </button>
          <button class="btn-accion-header" (click)="verTodaLaRuta()">
            🗺️ Ver Toda la Ruta 71A
          </button>

          <!-- ENFOQUE DE ALERTA -->
          <button *ngIf="alertasActivas.length > 0" class="btn-accion-header btn-rojo" (click)="centrarEnAlerta()">
            🚨 Enfocar Emergencia ({{ alertasActivas.length }})
          </button>
          
          <button class="btn-accion-header btn-verde" (click)="mostrarModalConductor = true">
            + Registrar Chofer
          </button>
          <button class="btn-accion-header" (click)="cargarDatos()">
            🔄 Recargar
          </button>
        </div>
      </header>

      <!-- TARJETAS DE KPIS SUPERIORES -->
      <section class="grid-kpis">
        <div class="kpi-card verde">
          <div class="kpi-titulo">UNIDADES EN RUTA 71A</div>
          <div class="kpi-valor">{{ busesEnMapa.length }}</div>
          <div class="kpi-sub">recorriendo 83 paradas oficiales</div>
        </div>
        <div class="kpi-card rojo" [class.parpadeo]="alertasActivas.length > 0">
          <div class="kpi-titulo">ALERTAS ACTIVAS</div>
          <div class="kpi-valor">{{ alertasActivas.length }}</div>
          <div class="kpi-sub">{{ alertasActivas.length > 0 ? '¡Atención inmediata en cabina!' : 'Sin incidentes de asalto en curso' }}</div>
        </div>
        <div class="kpi-card azul">
          <div class="kpi-titulo">BASES POLICIALES EN RUTA</div>
          <div class="kpi-valor">{{ comisarias.length }}</div>
          <div class="kpi-sub">despacho rápido por geolocalización</div>
        </div>
        <div class="kpi-card naranja">
          <div class="kpi-titulo">LONGITUD DEL CORREDOR</div>
          <div class="kpi-valor">28.5 km</div>
          <div class="kpi-sub">La Punta ⇄ Santa Anita ⇄ Ate Vitarte</div>
        </div>
      </section>

      <!-- SECCIÓN PRINCIPAL: MAPA INTERACTIVO + PANEL LATERAL -->
      <section class="grid-dashboard">
        <!-- CONTENEDOR DEL MAPA LEAFLET -->
        <div class="card-mapa">
          <div class="mapa-header">
            <div class="mapa-titulo">
              <span class="icono-radar">📡</span>
              <span>Monitoreo Satelital en Tiempo Real • Recorrido Línea 1305 (71A)</span>
            </div>
            
            <div class="mapa-controles">
              <label class="control-check">
                <input type="checkbox" [(ngModel)]="verBuses" (change)="toggleCapaBuses()" />
                <span>Buses ({{ busesEnMapa.length }})</span>
              </label>
              <label class="control-check">
                <input type="checkbox" [(ngModel)]="verComisarias" (change)="toggleCapaComisarias()" />
                <span>Comisarías ({{ comisarias.length }})</span>
              </label>
              <label class="control-check">
                <input type="checkbox" [(ngModel)]="verParadas" (change)="toggleCapaParadas()" />
                <span>Paradas Clave</span>
              </label>
              <label class="control-check">
                <input type="checkbox" [(ngModel)]="verRuta" (change)="toggleCapaRuta()" />
                <span>Ruta 71A</span>
              </label>
              <span class="tag-live">● TELEMETRÍA EN VIVO</span>
            </div>
          </div>

          <!-- LIENZO DE LEAFLET -->
          <div class="mapa-canvas-wrapper">
            <div id="mapa-leaflet" class="mapa-leaflet" [class.mapa-oscuro-tiles]="mapaModoOscuro"></div>

            <!-- Leyenda flotante elegante sobre el mapa -->
            <div class="mapa-leyenda">
              <div class="item-leyenda"><span class="punto-verde"></span> Bus en servicio regular</div>
              <div class="item-leyenda"><span class="punto-rojo"></span> ¡Alerta de pánico silenciosa!</div>
              <div class="item-leyenda"><span class="punto-azul"></span> Comisaría PNP de sector</div>
              <div class="item-leyenda"><span class="punto-amarillo"></span> Parada emblemática Línea 1305</div>
              <div class="item-leyenda"><span class="linea-celeste"></span> Corredor Ate ⇄ La Punta</div>
            </div>
          </div>
        </div>

        <!-- PANEL LATERAL DE TELEMETRÍA Y CONTROL DE UNIDADES -->
        <div class="card-lista">
          <div class="cabecera-lista">
            <h3>Flota en Servicio (Línea 71A)</h3>
            <span class="tag-conteo">{{ busesEnMapa.length }} unidades</span>
          </div>
          <p class="subtitulo-lista">Haz clic sobre un bus para seguirlo por GPS en el mapa</p>

          <div class="lista-unidades-scroll">
            <div 
              *ngFor="let bus of busesEnMapa" 
              class="tarjeta-unidad-item"
              [class.en-peligro]="bus.estado === 'En Alerta'"
              (click)="enfocarBus(bus)">
              
              <div class="unidad-header-item">
                <span class="tag-unidad-codigo">🚌 {{ bus.codigoUnidad }}</span>
                <span class="tag-unidad-placa">{{ bus.placa }}</span>
                <span class="badge-estado" [ngClass]="bus.estado === 'En Alerta' ? 'badge-peligro' : 'badge-activo'">
                  {{ bus.estado === 'En Alerta' ? '🚨 PÁNICO ACTIVO' : '● EN MARCHA' }}
                </span>
              </div>

              <div class="unidad-cuerpo-item">
                <div class="item-conductor">
                  <span>👨‍✈️</span> <b>{{ bus.conductor }}</b>
                </div>
                <div class="item-distrito">
                  <span>📍 Sector:</span> <b class="texto-distrito">{{ bus.distritoActual }}</b>
                </div>
                <div class="item-telemetria">
                  <span>⚡ {{ bus.velocidad }} km/h</span>
                  <span>🧭 {{ bus.rumbo }}</span>
                </div>

                <!-- DESPACHO POLICIAL ASISTIDO Y EXPORTACIÓN PDF -->
                <div class="fila-accion-bus">
                  <span class="pnp-proxima" *ngIf="bus.comisariaCercana">
                    🚓 {{ bus.comisariaCercana.comisaria.nombre }} ({{ bus.comisariaCercana.distanciaTexto }})
                  </span>
                  <button 
                    class="btn-acta-rapida" 
                    title="Generar Acta Oficial de Incidente Policial en PDF"
                    (click)="$event.stopPropagation(); exportarActaBus(bus)">
                    📄 Acta PDF
                  </button>
                </div>
              </div>
            </div>

            <div *ngIf="busesEnMapa.length === 0" class="sin-unidades">
              Cargando flota y posicionamiento satelital...
            </div>
          </div>

          <!-- RESUMEN DE DEPENDENCIAS POLICIALES CERCANAS -->
          <div class="seccion-comisarias-resumen">
            <div class="comisarias-titulo">
              <span>🚓 Puntos de Auxilio Policial PNP</span>
              <small>{{ comisarias.length }} bases</small>
            </div>
            <div class="comisarias-chips">
              <button 
                *ngFor="let c of comisarias" 
                class="chip-comisaria"
                (click)="enfocarComisaria(c)">
                🚓 {{ c.nombre }}
              </button>
            </div>
          </div>
        </div>
      </section>

      <!-- MODAL PARA REGISTRAR CONDUCTOR -->
      <app-registrar-conductor-modal 
        *ngIf="mostrarModalConductor" 
        (onCerrar)="mostrarModalConductor = false">
      </app-registrar-conductor-modal>
    </div>
  `,
  styles: [`
    .contenido {
      flex-grow: 1;
      padding: 24px 28px;
      overflow-y: auto;
      box-sizing: border-box;
      background: #090d16;
      color: #f8fafc;
      min-height: 100vh;
    }

    .barra-superior {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      flex-wrap: wrap;
      gap: 12px;
    }

    .barra-superior h2 {
      margin: 0 0 4px 0;
      font-size: 22px;
      color: #ffffff;
      font-weight: 800;
    }

    .barra-superior p {
      margin: 0;
      color: #94a3b8;
      font-size: 13px;
    }

    .botones-header {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }

    .btn-accion-header {
      padding: 7px 12px;
      background: #1e293b;
      border: 1px solid #334155;
      color: #f8fafc;
      border-radius: 8px;
      font-size: 11px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-accion-header:hover {
      background: #334155;
      border-color: #475569;
    }

    .btn-accion-header.btn-verde {
      background: #059669;
      border-color: #10b981;
      color: #ffffff;
    }

    .btn-accion-header.btn-verde:hover {
      background: #10b981;
    }

    .btn-accion-header.btn-rojo {
      background: #e11d48;
      border-color: #f43f5e;
      color: #ffffff;
      animation: pulso-emergencia 1.5s infinite;
    }

    @keyframes pulso-emergencia {
      0% { box-shadow: 0 0 0 0 rgba(244, 63, 94, 0.7); }
      70% { box-shadow: 0 0 0 10px rgba(244, 63, 94, 0); }
      100% { box-shadow: 0 0 0 0 rgba(244, 63, 94, 0); }
    }

    /* KPIS */
    .grid-kpis {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      margin-bottom: 20px;
    }

    .kpi-card {
      background: #0f172a;
      border-radius: 12px;
      padding: 16px 20px;
      border: 1px solid #1e293b;
      box-shadow: 0 4px 10px rgba(0,0,0,0.4);
    }

    .kpi-titulo {
      font-size: 10px;
      font-weight: 700;
      color: #64748b;
      margin-bottom: 4px;
      letter-spacing: 0.5px;
    }

    .kpi-valor {
      font-size: 26px;
      font-weight: 800;
      margin-bottom: 4px;
    }

    .kpi-card.verde .kpi-valor { color: #34d399; }
    .kpi-card.rojo .kpi-valor { color: #fb7185; }
    .kpi-card.azul .kpi-valor { color: #38bdf8; }
    .kpi-card.naranja .kpi-valor { color: #fbbf24; }

    .kpi-sub {
      font-size: 11px;
      color: #64748b;
    }

    /* GRID DASHBOARD */
    .grid-dashboard {
      display: grid;
      grid-template-columns: 2.2fr 1fr;
      gap: 20px;
    }

    .card-mapa, .card-lista {
      background: #0f172a;
      border-radius: 16px;
      border: 1px solid #1e293b;
      padding: 18px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.5);
    }

    .mapa-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 14px;
      flex-wrap: wrap;
      gap: 10px;
    }

    .mapa-titulo {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 13px;
      font-weight: 700;
      color: #ffffff;
    }

    .icono-radar {
      font-size: 16px;
    }

    .mapa-controles {
      display: flex;
      align-items: center;
      gap: 12px;
      flex-wrap: wrap;
    }

    .control-check {
      font-size: 11px;
      color: #cbd5e1;
      display: flex;
      align-items: center;
      gap: 5px;
      cursor: pointer;
    }

    .control-check input {
      accent-color: #0284c7;
      cursor: pointer;
    }

    .tag-live {
      background: rgba(239, 68, 68, 0.15);
      color: #f43f5e;
      border: 1px solid rgba(239, 68, 68, 0.3);
      padding: 2px 8px;
      border-radius: 6px;
      font-size: 10px;
      font-weight: 800;
      letter-spacing: 0.5px;
      animation: pulso-texto 2s infinite;
    }

    @keyframes pulso-texto {
      0% { opacity: 1; }
      50% { opacity: 0.5; }
      100% { opacity: 1; }
    }

    /* MAPA CANVAS WRAPPER */
    .mapa-canvas-wrapper {
      position: relative;
      width: 100%;
      height: 560px;
      border-radius: 12px;
      overflow: hidden;
      border: 1px solid #334155;
    }

    .mapa-leaflet {
      width: 100%;
      height: 100%;
      background: #0b1120;
    }

    /* FILTRO MODO OSCURO PARA OPENSTREETMAP LIBRE DE API KEY */
    :host ::ng-deep .mapa-oscuro-tiles .leaflet-tile {
      filter: brightness(0.65) invert(1) contrast(3) hue-rotate(200deg) saturate(0.3) !important;
    }

    .mapa-leyenda {
      position: absolute;
      bottom: 16px;
      left: 16px;
      z-index: 1000;
      background: rgba(15, 23, 42, 0.90);
      backdrop-filter: blur(8px);
      border: 1px solid rgba(255, 255, 255, 0.1);
      padding: 10px 14px;
      border-radius: 10px;
      display: flex;
      flex-direction: column;
      gap: 5px;
      font-size: 11px;
      color: #cbd5e1;
      box-shadow: 0 4px 12px rgba(0,0,0,0.5);
    }

    .item-leyenda {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .punto-verde {
      width: 10px;
      height: 10px;
      border-radius: 50%;
      background: #10b981;
      box-shadow: 0 0 6px #10b981;
    }

    .punto-rojo {
      width: 10px;
      height: 10px;
      border-radius: 50%;
      background: #ef4444;
      box-shadow: 0 0 8px #ef4444;
    }

    .punto-azul {
      width: 10px;
      height: 10px;
      border-radius: 50%;
      background: #0284c7;
      box-shadow: 0 0 6px #0284c7;
    }

    .punto-amarillo {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #f59e0b;
      box-shadow: 0 0 6px #f59e0b;
    }

    .linea-celeste {
      width: 16px;
      height: 3px;
      background: #38bdf8;
      border-radius: 2px;
    }

    /* PANEL LATERAL */
    .cabecera-lista {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 2px;
    }

    .cabecera-lista h3 {
      margin: 0;
      font-size: 14px;
      font-weight: 700;
      color: #ffffff;
    }

    .subtitulo-lista {
      font-size: 11px;
      color: #64748b;
      margin: 0 0 14px 0;
    }

    .tag-conteo {
      font-size: 11px;
      color: #38bdf8;
      background: rgba(56, 189, 248, 0.12);
      padding: 2px 8px;
      border-radius: 6px;
      font-weight: 700;
    }

    .lista-unidades-scroll {
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-height: 380px;
      overflow-y: auto;
      padding-right: 4px;
      margin-bottom: 16px;
    }

    .tarjeta-unidad-item {
      background: #1e293b;
      border: 1px solid #334155;
      border-radius: 10px;
      padding: 10px 12px;
      cursor: pointer;
      transition: all 0.2s;
    }

    .tarjeta-unidad-item:hover {
      background: #25334a;
      border-color: #0284c7;
      transform: translateY(-1px);
    }

    .tarjeta-unidad-item.en-peligro {
      background: rgba(225, 29, 72, 0.12);
      border-color: rgba(244, 63, 94, 0.45);
    }

    .unidad-header-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 6px;
    }

    .tag-unidad-codigo {
      font-size: 12px;
      font-weight: 800;
      color: #f8fafc;
    }

    .tag-unidad-placa {
      font-size: 11px;
      color: #94a3b8;
      background: rgba(0,0,0,0.3);
      padding: 1px 6px;
      border-radius: 4px;
      font-family: monospace;
    }

    .badge-estado {
      font-size: 9px;
      font-weight: 800;
      padding: 2px 6px;
      border-radius: 4px;
    }

    .badge-activo {
      background: rgba(34, 197, 94, 0.15);
      color: #4ade80;
    }

    .badge-peligro {
      background: rgba(239, 68, 68, 0.25);
      color: #fca5a5;
    }

    .unidad-cuerpo-item {
      font-size: 11px;
    }

    .item-conductor {
      color: #cbd5e1;
      margin-bottom: 3px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .item-distrito {
      font-size: 10px;
      color: #94a3b8;
      margin-bottom: 4px;
    }

    .texto-distrito {
      color: #38bdf8;
    }

    .item-telemetria {
      display: flex;
      justify-content: space-between;
      color: #64748b;
      font-size: 10px;
      padding-top: 4px;
      border-top: 1px solid rgba(255, 255, 255, 0.05);
    }

    .fila-accion-bus {
      margin-top: 6px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 6px;
    }

    .pnp-proxima {
      font-size: 9px;
      color: #38bdf8;
      background: rgba(2, 132, 199, 0.15);
      padding: 2px 6px;
      border-radius: 4px;
      border: 1px solid rgba(2, 132, 199, 0.25);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      max-width: 170px;
    }

    .btn-acta-rapida {
      background: #0284c7;
      color: #ffffff;
      border: none;
      padding: 3px 8px;
      border-radius: 4px;
      font-size: 10px;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.2s;
      white-space: nowrap;
    }

    .btn-acta-rapida:hover {
      background: #0369a1;
      transform: scale(1.05);
    }

    .sin-unidades {
      text-align: center;
      padding: 20px;
      color: #64748b;
      font-size: 11px;
    }

    /* COMISARÍAS ENLACE */
    .seccion-comisarias-resumen {
      border-top: 1px solid #1e293b;
      padding-top: 12px;
    }

    .comisarias-titulo {
      display: flex;
      justify-content: space-between;
      font-size: 11px;
      font-weight: 700;
      color: #94a3b8;
      margin-bottom: 8px;
    }

    .comisarias-chips {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .chip-comisaria {
      background: rgba(2, 132, 199, 0.12);
      border: 1px solid rgba(2, 132, 199, 0.25);
      color: #38bdf8;
      padding: 6px 10px;
      border-radius: 6px;
      font-size: 11px;
      text-align: left;
      cursor: pointer;
      transition: all 0.2s;
    }

    .chip-comisaria:hover {
      background: rgba(2, 132, 199, 0.25);
      border-color: #38bdf8;
      color: #ffffff;
    }
  `]
})
export class MonitoreoFlotaComponent implements OnInit, AfterViewInit, OnDestroy {
  busesEnMapa: BusUbicacion[] = [];
  alertasActivas: Alerta[] = [];
  comisarias: Comisaria[] = [];
  mostrarModalConductor = false;

  mapaModoOscuro = true;
  verBuses = true;
  verComisarias = true;
  verParadas = true;
  verRuta = true;

  private map: L.Map | null = null;
  private marcadoresBuses: Map<number, L.Marker> = new Map();
  private marcadoresComisarias: L.Marker[] = [];
  private marcadoresParadas: L.Marker[] = [];
  private lineaRuta: L.Polyline | null = null;
  private timerSimulacionGps: any;
  private lineaDespachoPnp: L.Polyline | null = null;

  private apiService = inject(ApiService);
  private router = inject(Router);
  public authService = inject(AuthService);
  private sseService = inject(SseService);
  private despachoService = inject(DespachoPolicialService);

  // Paradas emblemáticas de la Línea 1305 (71A) según Moovit
  readonly paradasEmblematicas: ParadaEmblematica[] = [
    { nombre: 'Terminal La Punta / Puerto', distrito: 'Callao', lat: -12.0725, lng: -77.1610 },
    { nombre: 'Av. Sáenz Peña / Garibaldi', distrito: 'Callao', lat: -12.0590, lng: -77.1320 },
    { nombre: 'Cruce Faucett / Colonial', distrito: 'Carmen de la Legua', lat: -12.0535, lng: -77.0980 },
    { nombre: 'UNMSM San Marcos (A. García)', distrito: 'Lima Cercado', lat: -12.0575, lng: -77.0720 },
    { nombre: 'Av. Venezuela / Tingo María', distrito: 'Breña', lat: -12.0605, lng: -77.0520 },
    { nombre: 'Plaza Bolognesi', distrito: 'Breña / Lima', lat: -12.0600, lng: -77.0390 },
    { nombre: 'Estación Grau (Metro L1)', distrito: 'Lima Centro', lat: -12.0575, lng: -77.0145 },
    { nombre: 'Hosp. Hipólito Unanue (Bravo Chico)', distrito: 'El Agustino', lat: -12.0465, lng: -76.9910 },
    { nombre: 'Municipalidad de Santa Anita', distrito: 'Santa Anita', lat: -12.0485, lng: -76.9715 },
    { nombre: 'Terminal Las Gardenias', distrito: 'Ate Vitarte', lat: -12.0340, lng: -76.9270 }
  ];

  // 24 waypoints secuenciales que trazan fielmente la Línea 1305 de Moovit de extremo a extremo
  private readonly waypointsRuta: [number, number][] = [
    [-12.0725, -77.1610], // 0. La Punta / Puerto del Callao
    [-12.0640, -77.1430], // 1. Chucuito / Sáenz Peña
    [-12.0590, -77.1320], // 2. Plaza Garibaldi (Callao)
    [-12.0560, -77.1150], // 3. Av. Colonial / Santa Rosa (Bellavista)
    [-12.0535, -77.0980], // 4. Av. Colonial con Faucett (Carmen de la Legua)
    [-12.0550, -77.0860], // 5. Av. Colonial / Universitaria (Unidad Vecinal 3)
    [-12.0575, -77.0720], // 6. Av. Venezuela / San Marcos (A. García y García)
    [-12.0590, -77.0620], // 7. Av. Venezuela / Canziani / Elio
    [-12.0605, -77.0520], // 8. Av. Venezuela cruce Tingo María
    [-12.0595, -77.0440], // 9. Av. Arica / Pilcomayo / Napo (Breña)
    [-12.0600, -77.0390], // 10. Plaza Bolognesi
    [-12.0580, -77.0360], // 11. Av. Garcilaso de la Vega (Wilson)
    [-12.0565, -77.0270], // 12. Paseo Colón / Av. Grau con Abancay
    [-12.0575, -77.0145], // 13. Av. Miguel Grau / Estación Grau (Metro L1)
    [-12.0555, -77.0060], // 14. Av. Grau / Jr. Chimbote (El Agustino)
    [-12.0520, -76.9990], // 15. Av. Riva Agüero / Mun. El Agustino
    [-12.0465, -76.9910], // 16. Óvalo La Paz / Hosp. Hipólito Unanue
    [-12.0435, -76.9805], // 17. César Vallejo / La Atarjea / Chepén
    [-12.0485, -76.9715], // 18. Av. Los Ruiseñores / Mun. Santa Anita
    [-12.0450, -76.9620], // 19. Av. Los Eucaliptos / Los Chancas
    [-12.0425, -76.9500], // 20. Av. Encalada / Av. Ferrocarril
    [-12.0390, -76.9390], // 21. Ferrocarril & Huarochirí / Lúcumos
    [-12.0365, -76.9320], // 22. Ferrocarril / La Cultura / 1 de Mayo
    [-12.0340, -76.9270]  // 23. Terminal Las Gardenias (Ate Vitarte)
  ];

  ngOnInit(): void {
    this.cargarDatos();
    this.escucharEventosTiempoReal();
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.inicializarMapa();
    }, 200);
  }

  ngOnDestroy(): void {
    if (this.timerSimulacionGps) {
      clearInterval(this.timerSimulacionGps);
    }
    if (this.map) {
      this.map.remove();
    }
  }

  private inicializarMapa(): void {
    const contenedor = document.getElementById('mapa-leaflet');
    if (!contenedor || this.map) return;

    // Centro inicial: Carmen de la Legua Reynoso / Callao
    this.map = L.map('mapa-leaflet', {
      center: [-12.0535, -77.0980],
      zoom: 13,
      zoomControl: true
    });

    // OpenStreetMap standard 100% libre sin API key
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19
    }).addTo(this.map);

    // Dibujar el trazado oficial de la Línea 1305 (71A)
    this.dibujarRuta();

    // Renderizar paradas clave, comisarías y buses
    this.renderizarParadasEnMapa();
    this.renderizarComisariasEnMapa();
    this.renderizarBusesEnMapa();

    // Iniciar movimiento continuo y fluido de telemetría a lo largo de la ruta de Moovit
    this.iniciarSimulacionTelemetria();
  }

  cargarDatos(): void {
    // 1. Cargar comisarías desde MySQL
    this.apiService.getComisarias().subscribe({
      next: (data) => {
        this.comisarias = data || [];
        this.renderizarComisariasEnMapa();
      },
      error: (e) => console.warn('Error al cargar comisarías:', e)
    });

    // 2. Cargar alertas activas
    this.apiService.getAlertas().subscribe({
      next: (alertas) => {
        this.alertasActivas = (alertas || []).filter(a => a.estado !== 'Atendida' && a.estado !== 'Falsa Alarma');
      },
      error: (e) => console.warn('Error al cargar alertas:', e)
    });

    // 3. Cargar flota de buses y turnos cruzados
    this.apiService.getBuses().subscribe({
      next: (buses) => {
        this.apiService.getTurnos().subscribe({
          next: (turnos) => {
            this.generarFlotaUbicaciones(buses || [], turnos || []);
          },
          error: () => this.generarFlotaUbicaciones(buses || [], [])
        });
      },
      error: (e) => console.warn('Error al cargar buses:', e)
    });
  }

  private generarFlotaUbicaciones(buses: Bus[], turnos: AsignacionTurno[]): void {
    // Distribuir estratégicamente los 12+ buses a lo largo de los 24 waypoints de la ruta oficial 1305
    const indicesIniciales = [0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22];

    this.busesEnMapa = buses.map((b, idx) => {
      const turno = turnos.find(t => t.idBus === b.idBus);
      const waypointIdx = indicesIniciales[idx % indicesIniciales.length];
      const coord = this.waypointsRuta[waypointIdx];
      const tieneAlerta = this.alertasActivas.some(a => a.idBus === b.idBus);
      const vaHaciaAte = idx % 2 === 0;

      return {
        idBus: b.idBus,
        codigoUnidad: b.numeroUnidad,
        placa: b.placa,
        modelo: b.modelo || 'Mercedes-Benz O500',
        conductor: turno ? turno.nombreConductor || 'Conductor en ruta' : 'Sin turno asignado',
        estado: tieneAlerta ? 'En Alerta' : (b.estado || 'Activo'),
        lat: coord[0] + (Math.random() * 0.0008 - 0.0004),
        lng: coord[1] + (Math.random() * 0.0008 - 0.0004),
        velocidad: tieneAlerta ? 0.0 : Math.round(35 + Math.random() * 8),
        rumbo: vaHaciaAte ? 'Hacia Gardenias (Ate)' : 'Hacia La Punta (Callao)',
        distritoActual: this.obtenerDistritoPorCoords(coord[0], coord[1]),
        waypointIndex: waypointIdx,
        direccionRuta: vaHaciaAte ? 1 : -1,
        comisariaCercana: this.despachoService.encontrarComisariaMasCercana(coord[0], coord[1], this.comisarias)
      };
    });

    this.renderizarBusesEnMapa();
  }

  private dibujarRuta(): void {
    if (!this.map) return;
    this.lineaRuta = L.polyline(this.waypointsRuta, {
      color: '#38bdf8',
      weight: 5,
      opacity: 0.85,
      dashArray: '8, 8'
    }).addTo(this.map);
  }

  private renderizarParadasEnMapa(): void {
    if (!this.map) return;
    this.marcadoresParadas.forEach(p => p.remove());
    this.marcadoresParadas = [];

    if (!this.verParadas) return;

    this.paradasEmblematicas.forEach(p => {
      const iconoParada = L.divIcon({
        className: 'parada-custom-marker',
        html: `
          <div style="
            background: rgba(245, 158, 11, 0.95);
            border: 2px solid #ffffff;
            color: #ffffff;
            width: 22px;
            height: 22px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 11px;
            box-shadow: 0 0 8px rgba(245, 158, 11, 0.8);
          ">🚏</div>
        `,
        iconSize: [22, 22],
        iconAnchor: [11, 11]
      });

      const marker = L.marker([p.lat, p.lng], { icon: iconoParada }).addTo(this.map!);
      marker.bindPopup(`
        <div style="font-family: system-ui; font-size: 12px; color: #0f172a; min-width: 170px;">
          <strong style="color: #d97706; font-size: 13px;">🚏 ${p.nombre}</strong><br/>
          <span>Distrito: <b>${p.distrito}</b></span><br/>
          <small style="color: #64748b;">Parada oficial de la Línea 1305 (71A)</small>
        </div>
      `);
      this.marcadoresParadas.push(marker);
    });
  }

  private renderizarComisariasEnMapa(): void {
    if (!this.map) return;
    this.marcadoresComisarias.forEach(m => m.remove());
    this.marcadoresComisarias = [];

    if (!this.verComisarias) return;

    this.comisarias.forEach(c => {
      const lat = Number(c.latitud) || -12.042;
      const lng = Number(c.longitud) || -77.090;

      const iconoPnp = L.divIcon({
        className: 'marcador-pnp-wrapper',
        html: `
          <div style="
            background: #0284c7;
            border: 2px solid #ffffff;
            color: #ffffff;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 0 10px rgba(2, 132, 199, 0.9);
            font-size: 15px;
          ">🚓</div>
        `,
        iconSize: [30, 30],
        iconAnchor: [15, 15]
      });

      const marcador = L.marker([lat, lng], { icon: iconoPnp }).addTo(this.map!);
      marcador.bindPopup(`
        <div style="font-family: system-ui; font-size: 12px; color: #0f172a; min-width: 190px;">
          <strong style="color: #0284c7; font-size: 13px;">🚓 ${c.nombre}</strong><br/>
          <span style="color: #475569;">📍 ${c.direccion}</span><br/>
          <span style="color: #0f172a; font-weight: 600;">📞 Central: ${c.telefono}</span><br/>
          <div style="margin-top: 6px; padding: 4px 6px; background: #e0f2fe; border-radius: 4px; font-size: 11px; color: #0369a1;">
            ✓ Base policial de enlace para auxilio en ruta
          </div>
        </div>
      `);

      this.marcadoresComisarias.push(marcador);
    });
  }

  private renderizarBusesEnMapa(): void {
    if (!this.map) return;

    if (!this.verBuses) {
      this.marcadoresBuses.forEach(m => m.remove());
      this.marcadoresBuses.clear();
      return;
    }

    this.busesEnMapa.forEach(b => {
      const enAlerta = b.estado === 'En Alerta';
      const colorFondo = enAlerta ? '#e11d48' : '#10b981';
      const sombraColor = enAlerta ? 'rgba(244, 63, 94, 1)' : 'rgba(16, 185, 129, 0.8)';
      const iconoTexto = enAlerta ? '🚨' : '🚌';
      const animacionPulso = enAlerta ? 'animation: pulso-emergencia 1s infinite;' : '';

      const iconoBus = L.divIcon({
        className: 'marcador-bus-custom',
        html: `
          <div style="
            background: ${colorFondo};
            border: 2px solid #ffffff;
            color: #ffffff;
            padding: 2px 7px;
            border-radius: 12px;
            font-size: 10px;
            font-weight: 800;
            display: flex;
            align-items: center;
            gap: 3px;
            white-space: nowrap;
            box-shadow: 0 0 14px ${sombraColor};
            cursor: pointer;
            ${animacionPulso}
          ">
            <span>${iconoTexto}</span>
            <span>${b.codigoUnidad}</span>
          </div>
        `,
        iconSize: [60, 24],
        iconAnchor: [30, 12]
      });

      if (this.marcadoresBuses.has(b.idBus)) {
        const marker = this.marcadoresBuses.get(b.idBus)!;
        marker.setLatLng([b.lat, b.lng]);
        marker.setIcon(iconoBus);
      } else {
        const marker = L.marker([b.lat, b.lng], { icon: iconoBus }).addTo(this.map!);
        marker.bindPopup(`
          <div style="font-family: system-ui; font-size: 12px; color: #0f172a; min-width: 220px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
              <strong style="color: ${colorFondo}; font-size: 14px;">${iconoTexto} ${b.codigoUnidad}</strong>
              <span style="background: #e2e8f0; padding: 2px 5px; border-radius: 4px; font-size: 10px; font-weight: bold;">${b.placa}</span>
            </div>
            <div><b>Conductor:</b> ${b.conductor}</div>
            <div><b>Sector:</b> ${b.distritoActual}</div>
            <div><b>Rumbo:</b> ${b.rumbo}</div>
            <div><b>Velocidad:</b> ${b.velocidad} km/h</div>
            <div><b>Línea:</b> 1305 (71A)</div>
            <div style="margin-top: 6px; padding: 6px 8px; background: #e0f2fe; border-radius: 6px; font-size: 11px;">
              <strong style="color: #0369a1;">🚓 Base PNP más cercana (Haversine):</strong><br/>
              <span>${b.comisariaCercana ? b.comisariaCercana.comisaria.nombre : 'Comisaría PNP'} (a <b>${b.comisariaCercana ? b.comisariaCercana.distanciaTexto : 'Proximidad'}</b>)</span><br/>
              <span style="color: #475569;">📞 Central: ${b.comisariaCercana ? b.comisariaCercana.comisaria.telefono : '105'} · ETA ~${b.comisariaCercana ? b.comisariaCercana.etaMinutos : 3} min</span>
            </div>
            <div style="margin-top: 6px; padding: 4px 6px; border-radius: 4px; font-weight: 700; font-size: 11px; text-align: center; color: white; background: ${colorFondo};">
              ${enAlerta ? '⚠️ ¡BOTÓN DE PÁNICO ACTIVADO!' : '● EN RUTA OPERATIVA NORMAL'}
            </div>
          </div>
        `);
        this.marcadoresBuses.set(b.idBus, marker);
      }
    });
  }

  private iniciarSimulacionTelemetria(): void {
    // Simula avance real por las avenidas de la Línea 1305 cada 3.2 segundos
    this.timerSimulacionGps = setInterval(() => {
      this.busesEnMapa.forEach(b => {
        if (b.estado !== 'En Alerta') {
          // Desplazarse al siguiente punto de la ruta
          b.waypointIndex += b.direccionRuta;

          // Rebotar en los extremos (Ate ⇄ Callao)
          if (b.waypointIndex >= this.waypointsRuta.length - 1) {
            b.waypointIndex = this.waypointsRuta.length - 1;
            b.direccionRuta = -1;
            b.rumbo = 'Hacia La Punta (Callao)';
          } else if (b.waypointIndex <= 0) {
            b.waypointIndex = 0;
            b.direccionRuta = 1;
            b.rumbo = 'Hacia Gardenias (Ate)';
          }

          const target = this.waypointsRuta[b.waypointIndex];
          b.lat = target[0] + (Math.random() * 0.0006 - 0.0003);
          b.lng = target[1] + (Math.random() * 0.0006 - 0.0003);
          b.velocidad = Math.round(30 + Math.random() * 12);
          b.distritoActual = this.obtenerDistritoPorCoords(b.lat, b.lng);
          b.comisariaCercana = this.despachoService.encontrarComisariaMasCercana(b.lat, b.lng, this.comisarias);

          const m = this.marcadoresBuses.get(b.idBus);
          if (m) {
            m.setLatLng([b.lat, b.lng]);
          }
        }
      });
    }, 3200);
  }

  private obtenerDistritoPorCoords(lat: number, lng: number): string {
    if (lng <= -77.120) return 'Callao Centro / Puerto';
    if (lng <= -77.085) return 'Carmen de la Legua / Bellavista';
    if (lng <= -77.050) return 'Lima Cercado / Breña (Venezuela)';
    if (lng <= -77.010) return 'Centro de Lima / Paseo Colón / Grau';
    if (lng <= -76.985) return 'El Agustino (Riva Agüero)';
    if (lng <= -76.955) return 'Santa Anita (Ruiseñores)';
    return 'Ate Vitarte (Gardenias)';
  }

  escucharEventosTiempoReal(): void {
    this.sseService.alertaRecibida$.subscribe({
      next: (nuevaAlerta) => {
        this.alertasActivas = [nuevaAlerta, ...this.alertasActivas];

        const bus = this.busesEnMapa.find(b => b.idBus === nuevaAlerta.idBus);
        if (bus) {
          bus.estado = 'En Alerta';
          bus.velocidad = 0;
          this.renderizarBusesEnMapa();
          this.centrarEnAlerta();
        }
      }
    });

    this.sseService.cambioEstado$.subscribe({
      next: (evento) => {
        const alerta = this.alertasActivas.find(a => a.idAlerta === evento.idAlerta);
        if (alerta) {
          alerta.estado = evento.nuevoEstado;
          if (evento.nuevoEstado === 'Atendida' || evento.nuevoEstado === 'Falsa Alarma') {
            this.alertasActivas = this.alertasActivas.filter(a => a.idAlerta !== evento.idAlerta);
          }
        }
      }
    });
  }

  enfocarBus(bus: BusUbicacion): void {
    if (!this.map) return;
    this.map.flyTo([bus.lat, bus.lng], 16, { duration: 1.2 });
    const marcador = this.marcadoresBuses.get(bus.idBus);
    if (marcador) {
      marcador.openPopup();
    }

    // Trazar enlace táctico con la comisaría más cercana si el bus está en alerta o al seleccionarlo
    if (bus.comisariaCercana && this.map) {
      const latC = Number(bus.comisariaCercana.comisaria.latitud);
      const lngC = Number(bus.comisariaCercana.comisaria.longitud);
      if (!isNaN(latC) && !isNaN(lngC)) {
        if (this.lineaDespachoPnp) {
          this.lineaDespachoPnp.remove();
        }
        this.lineaDespachoPnp = L.polyline([
          [bus.lat, bus.lng],
          [latC, lngC]
        ], {
          color: bus.estado === 'En Alerta' ? '#f43f5e' : '#0284c7',
          weight: 4,
          opacity: 0.9,
          dashArray: '6, 6'
        }).addTo(this.map);
      }
    }
  }

  exportarActaBus(bus: BusUbicacion): void {
    if (!bus.comisariaCercana) {
      bus.comisariaCercana = this.despachoService.encontrarComisariaMasCercana(bus.lat, bus.lng, this.comisarias);
    }
    const dataReporte: ReporteIncidenteData = {
      idAlerta: 100 + bus.idBus,
      codigoUnidad: bus.codigoUnidad,
      placa: bus.placa,
      modelo: bus.modelo,
      conductorNombre: bus.conductor,
      fechaHora: new Date().toLocaleString('es-PE'),
      tipoActivacion: bus.estado === 'En Alerta' ? 'BOTON_PANICO_3_PULSOS' : 'TELEMETRIA_RUTINARIA_GPS',
      estado: bus.estado === 'En Alerta' ? 'Auxilio Despachado' : 'En Marcha Operativa',
      latitud: bus.lat,
      longitud: bus.lng,
      distrito: bus.distritoActual,
      comisariaAsignada: bus.comisariaCercana?.comisaria.nombre || 'Comisaría PNP Bellavista',
      comisariaDireccion: bus.comisariaCercana?.comisaria.direccion || 'Jurisdicción del Corredor 1305',
      comisariaTelefono: bus.comisariaCercana?.comisaria.telefono || '105',
      distanciaComisaria: bus.comisariaCercana?.distanciaTexto || '350 metros',
      etaMinutos: bus.comisariaCercana?.etaMinutos || 3,
      operadorNombre: this.authService.currentUser()?.nombreUsuario || 'Operador Central SAT'
    };

    this.despachoService.exportarActaPolicialPDF(dataReporte);
  }

  enfocarComisaria(c: Comisaria): void {
    if (!this.map) return;
    const lat = Number(c.latitud) || -12.042;
    const lng = Number(c.longitud) || -77.090;
    this.map.flyTo([lat, lng], 16, { duration: 1.2 });
  }

  centrarEnCarmenDeLaLegua(): void {
    if (!this.map) return;
    this.map.flyTo([-12.0535, -77.0980], 14, { duration: 1.0 });
  }

  centrarEnLimaCentro(): void {
    if (!this.map) return;
    this.map.flyTo([-12.0580, -77.0300], 14, { duration: 1.0 });
  }

  centrarEnAteSantaAnita(): void {
    if (!this.map) return;
    this.map.flyTo([-12.0440, -76.9550], 14, { duration: 1.0 });
  }

  verTodaLaRuta(): void {
    if (!this.map || !this.lineaRuta) return;
    this.map.fitBounds(this.lineaRuta.getBounds(), { padding: [30, 30] });
  }

  centrarEnAlerta(): void {
    if (!this.map || this.alertasActivas.length === 0) return;
    const busEnAlerta = this.busesEnMapa.find(b => b.estado === 'En Alerta');
    if (busEnAlerta) {
      this.enfocarBus(busEnAlerta);
    } else {
      this.router.navigate(['/central/incidentes']);
    }
  }

  toggleModoMapa(): void {
    this.mapaModoOscuro = !this.mapaModoOscuro;
  }

  toggleCapaBuses(): void {
    this.renderizarBusesEnMapa();
  }

  toggleCapaComisarias(): void {
    this.renderizarComisariasEnMapa();
  }

  toggleCapaParadas(): void {
    this.renderizarParadasEnMapa();
  }

  toggleCapaRuta(): void {
    if (!this.map || !this.lineaRuta) return;
    if (this.verRuta) {
      this.lineaRuta.addTo(this.map);
    } else {
      this.lineaRuta.remove();
    }
  }

  irAIncidentes(): void {
    this.router.navigate(['/central/incidentes']);
  }
}
