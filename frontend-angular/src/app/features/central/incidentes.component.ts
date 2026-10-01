import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { Alerta, Bus, Conductor, Comisaria } from '../../core/models/sistema.models';
import { RegistrarConductorModalComponent } from './components/registrar-conductor-modal.component';
import { AuthService } from '../../core/services/auth.service';
import { DespachoPolicialService, ComisariaCercanaResultado, ReporteIncidenteData } from '../../core/services/despacho-policial.service';

interface IncidenteEnriquecido {
  alerta: Alerta;
  bus?: Bus;
  conductor?: Conductor;
  comisariaCercana?: ComisariaCercanaResultado | null;
  latitud: number;
  longitud: number;
  distrito: string;
}

@Component({
  selector: 'app-incidentes',
  standalone: true,
  imports: [CommonModule, FormsModule, RegistrarConductorModalComponent],
  template: `
    <div class="contenido">
      <!-- HEADER SUPERIOR -->
      <header class="barra-superior">
        <div>
          <h2>Gestión de Incidentes Críticos • SAT Línea 1305 (71A)</h2>
          <p>Protocolo táctico de auxilio ante pánico silencioso, despacho policial y emisión de actas</p>
        </div>
        <div class="acciones-header">
          <button class="btn-secundario" (click)="cargarDatos()">
            🔄 Actualizar
          </button>
          <button class="btn-secundario" (click)="mostrarModalConductor = true">
            + Registrar Chofer
          </button>
          <button class="btn-secundario btn-volver-mapa" (click)="irAMonitoreo()">
            🗺️ Volver a Monitoreo
          </button>
        </div>
      </header>

      <!-- SECCIÓN: SELECTOR DE INCIDENTE SI HAY VARIOS EN LA BD -->
      <section class="barra-incidentes-tabs" *ngIf="incidentes.length > 0">
        <span class="label-tabs">Incidentes en Base de Datos:</span>
        <div class="lista-tabs">
          <button 
            *ngFor="let inc of incidentes" 
            class="tab-incidente" 
            [class.activo]="inc.alerta.idAlerta === incidenteActivo?.alerta?.idAlerta"
            [class.critico]="inc.alerta.estado === 'Recibida' || inc.alerta.estado === 'En Evaluación'"
            (click)="seleccionarIncidente(inc)">
            <span class="ico-tab">🚨</span>
            <b>#{{ inc.alerta.idAlerta }}</b>
            <span>{{ inc.bus?.numeroUnidad || 'BUS-001' }}</span>
            <span class="mini-badge-estado" [ngClass]="obtenerClaseEstado(inc.alerta.estado)">{{ inc.alerta.estado }}</span>
          </button>
        </div>
      </section>

      <!-- SI NO HAY INCIDENTES -->
      <div *ngIf="incidentes.length === 0 && !cargando" class="caja-vacia">
        <span>🛡️</span>
        <h3>No hay incidentes de emergencia registrados</h3>
        <p>Todas las unidades circulan con normalidad a lo largo del corredor de la Línea 1305.</p>
      </div>

      <!-- BANNER DE ALERTA SILENCIOSA DETALLADO -->
      <section class="banner-alerta" *ngIf="incidenteActivo">
        <div class="banner-izq">
          <div class="banner-linea-1">
            <span class="unidad-alerta">{{ incidenteActivo.bus?.numeroUnidad || 'BUS-001' }}</span>
            <span class="placa-unidad">{{ incidenteActivo.bus?.placa || 'ABC-123' }}</span>
            <span class="titulo-alerta">Alerta Silenciosa Detectada</span>
            <span class="badge-urgencia">ALTA PRIORIDAD</span>
            <span class="badge-pulsos">{{ incidenteActivo.alerta.tipoActivacion || '3 PULSOS EN BOTÓN' }}</span>
          </div>
          <div class="banner-linea-2">
            INCIDENTE #{{ incidenteActivo.alerta.idAlerta }} · Conductor: <b>{{ incidenteActivo.conductor ? (incidenteActivo.conductor.nombres + ' ' + incidenteActivo.conductor.apellidos) : 'Luis Quispe Torres' }}</b> · 
            DNI: <b>{{ incidenteActivo.conductor?.dni || '71234567' }}</b> · Ruta: <b>Línea 1305 (71A: Ate ⇄ La Punta)</b> · 
            Hora: <b>{{ formatearFecha(incidenteActivo.alerta.fechaHora) }}</b>
          </div>
        </div>

        <div class="banner-der">
          <div class="selector-estado">
            <label>Estado de Intervención:</label>
            <select [(ngModel)]="incidenteActivo.alerta.estado" (change)="cambiarEstadoAlerta(incidenteActivo)">
              <option value="Recibida">🔴 Recibida</option>
              <option value="En Evaluación">🟡 En Evaluación</option>
              <option value="Auxilio Despachado">🔵 Auxilio Despachado</option>
              <option value="Atendida">🟢 Atendida (Cerrada)</option>
            </select>
          </div>
        </div>
      </section>

      <!-- GRID 2 COLUMNAS (MAPA + DESPACHO POLICIAL) -->
      <section class="grid-cuerpo" *ngIf="incidenteActivo">
        <!-- COLUMNA IZQUIERDA: TELEMETRÍA Y POSICIONAMIENTO -->
        <div class="col-izq">
          <div class="tarjeta-mapa">
            <div class="cabecera-tarjeta">
              <span>📍 Posicionamiento Táctico Satelital</span>
              <span class="coordenadas">
                Lat: {{ incidenteActivo.latitud | number:'1.4-4' }} | Lng: {{ incidenteActivo.longitud | number:'1.4-4' }} ({{ incidenteActivo.distrito }})
              </span>
            </div>
            
            <div class="mapa-tactico">
              <div class="radar-scan"></div>
              
              <!-- Punto Bus en Peligro -->
              <div class="punto-bus-alerta">
                <span class="icono-bus">🚌</span>
                <div class="callout">
                  <b>{{ incidenteActivo.bus?.numeroUnidad || 'BUS-001' }}</b><br/>
                  {{ incidenteActivo.distrito }}<br/>
                  <small style="color: #f87171;">¡Sensor Silencioso Activo!</small>
                </div>
              </div>

              <!-- Comisaría más cercana calculada -->
              <div class="punto-comisaria" *ngIf="incidenteActivo.comisariaCercana">
                <div class="pin-pnp">🚓</div>
                <div class="callout-pnp">
                  <b>{{ incidenteActivo.comisariaCercana.comisaria.nombre }}</b><br/>
                  Distancia: <b style="color: #38bdf8;">{{ incidenteActivo.comisariaCercana.distanciaTexto }}</b><br/>
                  ETA patrulla: <b>~ {{ incidenteActivo.comisariaCercana.etaMinutos }} min</b>
                </div>
              </div>
            </div>

            <div class="pie-mapa-tactico">
              <span>🎯 Cálculo Haversine en tiempo real: <b>{{ incidenteActivo.comisariaCercana?.comisaria?.nombre || 'Calculando...' }}</b></span>
              <span class="tag-distancia">{{ incidenteActivo.comisariaCercana?.distanciaTexto }}</span>
            </div>
          </div>

          <!-- EVIDENCIA DE AUDIO Y PROTOCOLO -->
          <div class="tarjeta-evidencia">
            <div class="evidencia-header">
              <h4>🎙️ Audio de Cabina Encriptado (Captura Automática de Seguridad)</h4>
              <span class="tag-audio-seguro">SILENCIOSO • STREAMING EN VIVO</span>
            </div>
            <p class="desc-audio">Grabación de micrófono perimetral enviada automáticamente al saltar el botón de pánico:</p>
            <div class="reproductor-audio">
              <button class="btn-play" (click)="toggleAudio()">
                {{ reproduciendoAudio ? '⏸ Pausar Audio' : '▶ Reproducir Grabación (15s)' }}
              </button>
              <div class="barra-audio">
                <div class="progreso-audio" [style.width]="reproduciendoAudio ? '80%' : '45%'"></div>
              </div>
              <span class="tiempo-audio">{{ reproduciendoAudio ? '00:12 / 00:15' : '00:15' }}</span>
            </div>
          </div>
        </div>

        <!-- COLUMNA DERECHA: DESPACHO Y POLICÍA -->
        <div class="col-der">
          <!-- Tarjeta Despacho PNP -->
          <div class="tarjeta-despacho">
            <div class="cabecera-despacho">
              <h3>🚨 Despacho Inmediato PNP / Serenazgo</h3>
              <span class="badge-haversine">CÁLCULO AUTOMÁTICO HAVERSINE</span>
            </div>
            
            <p class="sub-despacho">
              Comisaría fijada por radio euclidiano más próximo: 
              <b style="color: #38bdf8;">{{ incidenteActivo.comisariaCercana?.comisaria?.nombre || 'Comisaría PNP El Agustino' }}</b>
            </p>
            
            <div class="info-despacho">
              <div class="item-info">
                <span>Dirección de la Base:</span>
                <b>{{ incidenteActivo.comisariaCercana?.comisaria?.direccion || 'Sector de patrullaje' }}</b>
              </div>
              <div class="item-info">
                <span>Teléfono Directo de Base:</span>
                <b style="color: #4ade80;">{{ incidenteActivo.comisariaCercana?.comisaria?.telefono || '(01) 456-7890' }} / 105</b>
              </div>
              <div class="item-info">
                <span>Distancia Euclidiana Calculada:</span>
                <b style="color: #f59e0b;">{{ incidenteActivo.comisariaCercana?.distanciaTexto || '450 metros' }}</b>
              </div>
              <div class="item-info">
                <span>ETA Estimado de Intervención:</span>
                <b style="color: #38bdf8;">~ {{ incidenteActivo.comisariaCercana?.etaMinutos || 3 }} minutos (Unidad Rápida)</b>
              </div>
            </div>

            <div *ngIf="incidenteActivo.alerta.estado === 'Auxilio Despachado'" class="alerta-exito-despacho">
              ✓ Auxilio policial transmitido exitosamente a la guardia de la comisaría y cuadrante móvil.
            </div>

            <!-- BOTONES DE ACCIÓN: DESPACHAR Y EXPORTAR PDF -->
            <div class="acciones-despacho-grid">
              <button 
                class="btn-despachar-pnp" 
                (click)="despacharAuxilio(incidenteActivo)" 
                [disabled]="incidenteActivo.alerta.estado === 'Auxilio Despachado'">
                🚨 {{ incidenteActivo.alerta.estado === 'Auxilio Despachado' ? 'AUXILIO YA DESPACHADO' : 'DESPACHAR AUXILIO POLICIAL AHORA' }}
              </button>

              <button class="btn-pdf-acta" (click)="exportarReportePDF(incidenteActivo)">
                📄 Exportar Acta Policial (PDF)
              </button>
            </div>
          </div>

          <!-- Bitácora de Acciones -->
          <div class="tarjeta-bitacora">
            <h4>📋 Cronología de Eventos y Auditoría del Incidente</h4>
            <ul class="lista-bitacora">
              <li>
                <span class="hora">{{ formatearHora(incidenteActivo.alerta.fechaHora) }}</span>
                <span>Alerta silenciosa disparada por chofer en cabina</span>
              </li>
              <li>
                <span class="hora">{{ formatearHora(incidenteActivo.alerta.fechaHora, 2) }}</span>
                <span>Telemetría fijada: Lat {{ incidenteActivo.latitud | number:'1.3-3' }}, Lng {{ incidenteActivo.longitud | number:'1.3-3' }}</span>
              </li>
              <li>
                <span class="hora">{{ formatearHora(incidenteActivo.alerta.fechaHora, 5) }}</span>
                <span>Algoritmo asignó: <b>{{ incidenteActivo.comisariaCercana?.comisaria?.nombre || 'Comisaría PNP' }}</b></span>
              </li>
              <li *ngIf="incidenteActivo.alerta.estado === 'Auxilio Despachado' || incidenteActivo.alerta.estado === 'Atendida'">
                <span class="hora">{{ formatearHora(incidenteActivo.alerta.fechaHora, 12) }}</span>
                <span style="color: #38bdf8;">Despacho de auxilio transmitido al oficial de guardia</span>
              </li>
              <li *ngIf="incidenteActivo.alerta.estado === 'Atendida'">
                <span class="hora">{{ formatearHora(incidenteActivo.alerta.fechaHora, 30) }}</span>
                <span style="color: #4ade80;">Intervención completada con éxito. Incidente cerrado en MySQL.</span>
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- MODAL MODULAR PARA REGISTRAR CONDUCTOR -->
      <app-registrar-conductor-modal 
        *ngIf="mostrarModalConductor" 
        (cerrar)="mostrarModalConductor = false">
      </app-registrar-conductor-modal>
    </div>
  `,
  styles: [`
    .contenido {
      flex-grow: 1;
      padding: 24px 30px;
      color: #ffffff;
      background-color: #0b1120;
      min-height: 100vh;
      box-sizing: border-box;
      font-family: system-ui, -apple-system, sans-serif;
    }

    .barra-superior {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      border-bottom: 1px solid #1e293b;
      padding-bottom: 16px;
    }

    .barra-superior h2 {
      margin: 0 0 4px 0;
      font-size: 22px;
      color: #ffffff;
    }

    .barra-superior p {
      margin: 0;
      font-size: 13px;
      color: #94a3b8;
    }

    .acciones-header {
      display: flex;
      gap: 10px;
    }

    .btn-secundario {
      background: #1e293b;
      color: #cbd5e1;
      border: 1px solid #334155;
      padding: 8px 14px;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-secundario:hover {
      background: #334155;
      color: #ffffff;
    }

    .btn-volver-mapa {
      background: rgba(2, 132, 199, 0.2);
      border-color: #0284c7;
      color: #38bdf8;
    }

    /* BARRA DE TABS DE INCIDENTES */
    .barra-incidentes-tabs {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 18px;
      background: #0f172a;
      border: 1px solid #1e293b;
      padding: 10px 16px;
      border-radius: 10px;
      overflow-x: auto;
    }

    .label-tabs {
      font-size: 11px;
      font-weight: 700;
      color: #64748b;
      text-transform: uppercase;
      white-space: nowrap;
    }

    .lista-tabs {
      display: flex;
      gap: 8px;
    }

    .tab-incidente {
      background: #1e293b;
      border: 1px solid #334155;
      color: #cbd5e1;
      padding: 6px 12px;
      border-radius: 6px;
      font-size: 12px;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      transition: all 0.2s;
      white-space: nowrap;
    }

    .tab-incidente:hover {
      background: #334155;
      color: #ffffff;
    }

    .tab-incidente.activo {
      background: rgba(225, 29, 72, 0.2);
      border-color: #f43f5e;
      color: #ffffff;
    }

    .tab-incidente.critico {
      animation: tab-pulso 2s infinite;
    }

    @keyframes tab-pulso {
      0% { border-color: rgba(244, 63, 94, 0.4); }
      50% { border-color: rgba(244, 63, 94, 1); }
      100% { border-color: rgba(244, 63, 94, 0.4); }
    }

    .mini-badge-estado {
      font-size: 10px;
      padding: 2px 6px;
      border-radius: 4px;
      font-weight: 700;
    }

    .estado-rojo { background: rgba(239, 68, 68, 0.2); color: #f87171; }
    .estado-amarillo { background: rgba(245, 158, 11, 0.2); color: #fbbf24; }
    .estado-azul { background: rgba(2, 132, 199, 0.2); color: #38bdf8; }
    .estado-verde { background: rgba(34, 197, 94, 0.2); color: #4ade80; }

    /* BANNER PRINCIPAL DE ALERTA */
    .banner-alerta {
      background: linear-gradient(135deg, rgba(225, 29, 72, 0.2) 0%, rgba(15, 23, 42, 0.95) 100%);
      border: 1px solid rgba(225, 29, 72, 0.5);
      border-left: 6px solid #e11d48;
      border-radius: 12px;
      padding: 18px 24px;
      margin-bottom: 24px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 16px;
      box-shadow: 0 4px 20px rgba(225, 29, 72, 0.15);
    }

    .banner-linea-1 {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 8px;
      flex-wrap: wrap;
    }

    .unidad-alerta {
      font-size: 18px;
      font-weight: 900;
      color: #ffffff;
      background: #e11d48;
      padding: 3px 10px;
      border-radius: 6px;
    }

    .placa-unidad {
      background: #1e293b;
      border: 1px solid #475569;
      color: #f1f5f9;
      font-size: 12px;
      font-weight: 800;
      padding: 3px 8px;
      border-radius: 4px;
      font-family: monospace;
    }

    .titulo-alerta {
      font-size: 16px;
      font-weight: 800;
      color: #ffffff;
    }

    .badge-urgencia {
      background: #f43f5e;
      color: white;
      font-size: 10px;
      font-weight: 800;
      padding: 3px 8px;
      border-radius: 4px;
      letter-spacing: 0.5px;
    }

    .badge-pulsos {
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.2);
      color: #cbd5e1;
      font-size: 11px;
      padding: 3px 8px;
      border-radius: 4px;
    }

    .banner-linea-2 {
      font-size: 13px;
      color: #cbd5e1;
    }

    .selector-estado label {
      display: block;
      font-size: 11px;
      color: #94a3b8;
      margin-bottom: 5px;
      font-weight: 600;
    }

    .selector-estado select {
      background: #1e293b;
      border: 1px solid #475569;
      color: #ffffff;
      padding: 8px 14px;
      border-radius: 8px;
      font-size: 13px;
      font-weight: 700;
      cursor: pointer;
    }

    /* GRID CUERPO */
    .grid-cuerpo {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 24px;
    }

    @media (max-width: 992px) {
      .grid-cuerpo {
        grid-template-columns: 1fr;
      }
    }

    /* TARJETA MAPA TÁCTICO */
    .tarjeta-mapa {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      overflow: hidden;
      margin-bottom: 20px;
    }

    .cabecera-tarjeta {
      padding: 14px 18px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #1e293b;
      font-size: 13px;
      font-weight: 700;
    }

    .coordenadas {
      font-family: monospace;
      font-size: 11px;
      color: #38bdf8;
    }

    .mapa-tactico {
      position: relative;
      height: 260px;
      background: radial-gradient(circle, #172554 10%, #0b1120 90%);
      display: flex;
      align-items: center;
      justify-content: space-around;
      overflow: hidden;
      border-top: 1px solid #1e293b;
      border-bottom: 1px solid #1e293b;
    }

    .radar-scan {
      position: absolute;
      width: 320px;
      height: 320px;
      border-radius: 50%;
      border: 1px dashed rgba(2, 132, 199, 0.3);
      animation: radar-giro 6s linear infinite;
    }

    @keyframes radar-giro {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    .punto-bus-alerta {
      position: relative;
      z-index: 10;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .icono-bus {
      font-size: 34px;
      filter: drop-shadow(0 0 12px #e11d48);
      animation: bus-rebote 1.5s infinite alternate;
    }

    @keyframes bus-rebote {
      from { transform: translateY(0); }
      to { transform: translateY(-6px); }
    }

    .callout {
      background: rgba(15, 23, 42, 0.95);
      border: 1px solid #e11d48;
      color: #ffffff;
      padding: 6px 10px;
      border-radius: 6px;
      font-size: 11px;
      text-align: center;
      margin-top: 6px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.5);
    }

    .punto-comisaria {
      position: relative;
      z-index: 10;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .pin-pnp {
      font-size: 32px;
      filter: drop-shadow(0 0 10px #0284c7);
    }

    .callout-pnp {
      background: rgba(15, 23, 42, 0.95);
      border: 1px solid #0284c7;
      color: #ffffff;
      padding: 6px 10px;
      border-radius: 6px;
      font-size: 11px;
      text-align: center;
      margin-top: 6px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.5);
    }

    .pie-mapa-tactico {
      padding: 10px 18px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 11px;
      color: #94a3b8;
    }

    .tag-distancia {
      background: rgba(2, 132, 199, 0.2);
      color: #38bdf8;
      border: 1px solid rgba(2, 132, 199, 0.4);
      padding: 2px 8px;
      border-radius: 4px;
      font-weight: 800;
    }

    /* EVIDENCIA AUDIO */
    .tarjeta-evidencia {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 18px;
    }

    .evidencia-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 6px;
    }

    .evidencia-header h4 {
      margin: 0;
      font-size: 13px;
      color: #ffffff;
    }

    .tag-audio-seguro {
      background: rgba(245, 158, 11, 0.15);
      color: #fbbf24;
      font-size: 9px;
      font-weight: 800;
      padding: 2px 6px;
      border-radius: 4px;
      border: 1px solid rgba(245, 158, 11, 0.3);
    }

    .desc-audio {
      font-size: 12px;
      color: #94a3b8;
      margin: 0 0 14px 0;
    }

    .reproductor-audio {
      display: flex;
      align-items: center;
      gap: 12px;
      background: #1e293b;
      padding: 10px 14px;
      border-radius: 8px;
    }

    .btn-play {
      background: #0284c7;
      color: white;
      border: none;
      padding: 8px 14px;
      border-radius: 6px;
      font-size: 11px;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-play:hover {
      background: #0369a1;
    }

    .barra-audio {
      flex-grow: 1;
      height: 6px;
      background: #334155;
      border-radius: 3px;
      overflow: hidden;
    }

    .progreso-audio {
      height: 100%;
      background: #38bdf8;
      transition: width 0.3s ease;
    }

    .tiempo-audio {
      font-size: 11px;
      color: #94a3b8;
      font-family: monospace;
    }

    /* TARJETA DESPACHO POLICIAL */
    .tarjeta-despacho {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .cabecera-despacho {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 6px;
    }

    .cabecera-despacho h3 {
      margin: 0;
      font-size: 16px;
      color: #ffffff;
    }

    .badge-haversine {
      background: rgba(34, 197, 94, 0.15);
      border: 1px solid rgba(34, 197, 94, 0.3);
      color: #4ade80;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 9px;
      font-weight: 800;
    }

    .sub-despacho {
      font-size: 12px;
      color: #94a3b8;
      margin: 0 0 16px 0;
    }

    .info-despacho {
      background: #1e293b;
      border-radius: 8px;
      padding: 14px;
      margin-bottom: 16px;
      display: flex;
      flex-direction: column;
      gap: 10px;
    }

    .item-info {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
    }

    .item-info span {
      color: #94a3b8;
    }

    .alerta-exito-despacho {
      background: rgba(34, 197, 94, 0.15);
      border: 1px solid rgba(34, 197, 94, 0.3);
      color: #4ade80;
      padding: 10px;
      border-radius: 8px;
      font-size: 12px;
      margin-bottom: 14px;
    }

    .acciones-despacho-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
    }

    .btn-despachar-pnp {
      padding: 12px;
      background: #e11d48;
      color: #ffffff;
      font-size: 12px;
      font-weight: 800;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      box-shadow: 0 0 15px rgba(225, 29, 72, 0.3);
      transition: all 0.2s;
    }

    .btn-despachar-pnp:hover:not(:disabled) {
      background: #f43f5e;
      transform: translateY(-2px);
    }

    .btn-despachar-pnp:disabled {
      background: #334155;
      color: #94a3b8;
      box-shadow: none;
      cursor: not-allowed;
    }

    .btn-pdf-acta {
      padding: 12px;
      background: #0284c7;
      color: #ffffff;
      font-size: 12px;
      font-weight: 800;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      box-shadow: 0 0 15px rgba(2, 132, 199, 0.3);
      transition: all 0.2s;
    }

    .btn-pdf-acta:hover {
      background: #0369a1;
      transform: translateY(-2px);
    }

    /* BITÁCORA */
    .tarjeta-bitacora {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 18px;
    }

    .tarjeta-bitacora h4 {
      margin: 0 0 12px 0;
      font-size: 12px;
      color: #94a3b8;
    }

    .lista-bitacora {
      list-style: none;
      padding: 0;
      margin: 0;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .lista-bitacora li {
      font-size: 11px;
      display: flex;
      gap: 12px;
      border-bottom: 1px solid rgba(255,255,255,0.04);
      padding-bottom: 6px;
    }

    .lista-bitacora .hora {
      color: #64748b;
      font-family: monospace;
      font-weight: 700;
    }

    .caja-vacia {
      text-align: center;
      padding: 60px 20px;
      background: #0f172a;
      border: 1px dashed #334155;
      border-radius: 12px;
    }

    .caja-vacia span {
      font-size: 40px;
      display: block;
      margin-bottom: 12px;
    }
  `]
})
export class IncidentesComponent implements OnInit {
  incidentes: IncidenteEnriquecido[] = [];
  incidenteActivo: IncidenteEnriquecido | null = null;
  cargando = true;
  mostrarModalConductor = false;
  reproduciendoAudio = false;

  private apiService = inject(ApiService);
  private router = inject(Router);
  public authService = inject(AuthService);
  private despachoService = inject(DespachoPolicialService);

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    
    // Cargar comisarías, buses, conductores y alertas concurrentemente
    this.apiService.getComisarias().subscribe({
      next: (comisarias) => {
        this.apiService.getBuses().subscribe({
          next: (buses) => {
            this.apiService.getConductores().subscribe({
              next: (conductores) => {
                this.apiService.getAlertas().subscribe({
                  next: (alertas) => {
                    this.procesarIncidentes(alertas || [], buses || [], conductores || [], comisarias || []);
                    this.cargando = false;
                  },
                  error: () => this.cargando = false
                });
              },
              error: () => this.cargando = false
            });
          },
          error: () => this.cargando = false
        });
      },
      error: () => this.cargando = false
    });
  }

  private procesarIncidentes(alertas: Alerta[], buses: Bus[], conductores: Conductor[], comisarias: Comisaria[]): void {
    // Coordenadas fijas por defecto a lo largo de la Línea 1305 para alertas registradas
    const coordenadasRuta: { [key: number]: { lat: number; lng: number; distrito: string } } = {
      1: { lat: -12.0450, lng: -77.0950, distrito: 'Carmen de la Legua' },
      2: { lat: -12.0575, lng: -77.0720, distrito: 'Lima Cercado (San Marcos)' },
      3: { lat: -12.0480, lng: -76.9950, distrito: 'El Agustino' },
      4: { lat: -12.0485, lng: -76.9715, distrito: 'Santa Anita' },
      5: { lat: -12.0340, lng: -76.9270, distrito: 'Ate Vitarte' }
    };

    this.incidentes = alertas.map((a, idx) => {
      const bus = buses.find(b => b.idBus === a.idBus);
      const conductor = conductores.find(c => c.idConductor === a.idConductor);
      
      const geo = coordenadasRuta[(a.idAlerta || 1) % 5 + 1] || { lat: -12.0480, lng: -76.9950, distrito: 'El Agustino' };
      
      // Cálculo automático con el algoritmo de Haversine hacia las 10 comisarías en MySQL
      const comisariaCercana = this.despachoService.encontrarComisariaMasCercana(geo.lat, geo.lng, comisarias);

      return {
        alerta: a,
        bus,
        conductor,
        comisariaCercana,
        latitud: geo.lat,
        longitud: geo.lng,
        distrito: geo.distrito
      };
    });

    if (this.incidentes.length > 0) {
      this.incidenteActivo = this.incidentes[0];
    }
  }

  seleccionarIncidente(inc: IncidenteEnriquecido): void {
    this.incidenteActivo = inc;
  }

  cambiarEstadoAlerta(inc: IncidenteEnriquecido): void {
    if (!inc.alerta.idAlerta) return;
    const opId = this.authService.currentUser()?.idUsuario || 1;

    this.apiService.actualizarEstadoAlerta(inc.alerta.idAlerta, inc.alerta.estado, opId).subscribe({
      next: () => {
        console.log(`Estado de alerta #${inc.alerta.idAlerta} actualizado a ${inc.alerta.estado} en MySQL`);
      },
      error: (e) => console.error('Error al actualizar estado:', e)
    });
  }

  despacharAuxilio(inc: IncidenteEnriquecido): void {
    inc.alerta.estado = 'Auxilio Despachado';
    this.cambiarEstadoAlerta(inc);
  }

  exportarReportePDF(inc: IncidenteEnriquecido): void {
    const dataReporte: ReporteIncidenteData = {
      idAlerta: inc.alerta.idAlerta || 1,
      codigoUnidad: inc.bus?.numeroUnidad || 'BUS-001',
      placa: inc.bus?.placa || 'ABC-123',
      modelo: inc.bus?.modelo || 'Mercedes-Benz O500',
      conductorNombre: inc.conductor ? `${inc.conductor.nombres} ${inc.conductor.apellidos}` : 'Luis Quispe Torres',
      conductorDni: inc.conductor?.dni || '71234567',
      conductorLicencia: inc.conductor?.licencia || 'A-IIIa Profesional',
      fechaHora: this.formatearFecha(inc.alerta.fechaHora),
      tipoActivacion: inc.alerta.tipoActivacion || 'BOTON_PANICO_3_PULSOS',
      estado: inc.alerta.estado || 'Recibida',
      latitud: inc.latitud,
      longitud: inc.longitud,
      distrito: inc.distrito,
      comisariaAsignada: inc.comisariaCercana?.comisaria.nombre || 'Comisaría PNP El Agustino',
      comisariaDireccion: inc.comisariaCercana?.comisaria.direccion || 'Av. Riva Agüero cdra 12',
      comisariaTelefono: inc.comisariaCercana?.comisaria.telefono || '(01) 327-0921',
      distanciaComisaria: inc.comisariaCercana?.distanciaTexto || '450 metros',
      etaMinutos: inc.comisariaCercana?.etaMinutos || 3,
      operadorNombre: this.authService.currentUser()?.nombreUsuario || 'Operador Central SAT'
    };

    this.despachoService.exportarActaPolicialPDF(dataReporte);
  }

  toggleAudio(): void {
    this.reproduciendoAudio = !this.reproduciendoAudio;
  }

  formatearFecha(fechaStr?: string): string {
    if (!fechaStr) return new Date().toLocaleString('es-PE');
    try {
      return new Date(fechaStr).toLocaleString('es-PE', {
        dateStyle: 'medium',
        timeStyle: 'medium'
      });
    } catch {
      return fechaStr;
    }
  }

  formatearHora(fechaStr?: string, agregarSegundos = 0): string {
    const base = fechaStr ? new Date(fechaStr) : new Date();
    if (agregarSegundos > 0) {
      base.setSeconds(base.getSeconds() + agregarSegundos);
    }
    return base.toLocaleTimeString('es-PE');
  }

  obtenerClaseEstado(estado: string): string {
    switch (estado) {
      case 'Recibida': return 'estado-rojo';
      case 'En Evaluación': return 'estado-amarillo';
      case 'Auxilio Despachado': return 'estado-azul';
      case 'Atendida': return 'estado-verde';
      default: return 'estado-azul';
    }
  }

  irAMonitoreo(): void {
    this.router.navigate(['/central/monitoreo']);
  }
}

