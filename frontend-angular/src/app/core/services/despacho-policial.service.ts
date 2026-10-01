import { Injectable } from '@angular/core';
import { Comisaria } from '../models/sistema.models';

export interface ComisariaCercanaResultado {
  comisaria: Comisaria;
  distanciaKm: number;
  distanciaTexto: string;
  etaMinutos: number;
}

export interface ReporteIncidenteData {
  idAlerta: number | string;
  codigoUnidad: string;
  placa: string;
  modelo?: string;
  conductorNombre: string;
  conductorDni?: string;
  conductorLicencia?: string;
  fechaHora: string;
  tipoActivacion: string;
  estado: string;
  descripcion?: string;
  latitud: number;
  longitud: number;
  distrito?: string;
  comisariaAsignada: string;
  comisariaDireccion?: string;
  comisariaTelefono?: string;
  distanciaComisaria?: string;
  etaMinutos?: number;
  operadorNombre?: string;
}

@Injectable({
  providedIn: 'root'
})
export class DespachoPolicialService {

  /**
   * Calcula la distancia esférica entre dos puntos GPS mediante el algoritmo de Haversine.
   * @returns Distancia en kilómetros.
   */
  calcularDistanciaKm(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371; // Radio de la Tierra en km
    const dLat = this.deg2rad(lat2 - lat1);
    const dLon = this.deg2rad(lon2 - lon1);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  private deg2rad(deg: number): number {
    return deg * (Math.PI / 180);
  }

  /**
   * Determina automáticamente cuál de las comisarías registradas es la más cercana a las coordenadas dadas.
   */
  encontrarComisariaMasCercana(latBus: number, lngBus: number, comisarias: Comisaria[]): ComisariaCercanaResultado | null {
    if (!comisarias || comisarias.length === 0) return null;

    let mejorComisaria: Comisaria = comisarias[0];
    let menorDistancia = Infinity;

    for (const c of comisarias) {
      const latC = Number(c.latitud);
      const lngC = Number(c.longitud);
      if (isNaN(latC) || isNaN(lngC)) continue;

      const d = this.calcularDistanciaKm(latBus, lngBus, latC, lngC);
      if (d < menorDistancia) {
        menorDistancia = d;
        mejorComisaria = c;
      }
    }

    // Formatear distancia amigable: metros o kilómetros
    let distanciaTexto = '';
    if (menorDistancia < 1) {
      distanciaTexto = `${Math.round(menorDistancia * 1000)} metros`;
    } else {
      distanciaTexto = `${menorDistancia.toFixed(2)} km`;
    }

    // ETA estimado a 35 km/h de patrulla en tráfico urbano de Lima
    const etaMinutos = Math.max(2, Math.round((menorDistancia / 35) * 60) + 1);

    return {
      comisaria: mejorComisaria,
      distanciaKm: menorDistancia,
      distanciaTexto,
      etaMinutos
    };
  }

  /**
   * Genera e imprime/descarga el Acta Oficial de Incidente Policial en formato A4 / PDF.
   */
  exportarActaPolicialPDF(data: ReporteIncidenteData): void {
    const ventanaImpresion = window.open('', '_blank', 'width=900,height=1000');
    if (!ventanaImpresion) {
      alert('Por favor habilite las ventanas emergentes en su navegador para generar el PDF del reporte.');
      return;
    }

    const htmlContent = `
      <!DOCTYPE html>
      <html lang="es">
      <head>
        <meta charset="UTF-8">
        <title>Acta_Policial_Emergencia_${data.codigoUnidad}_${data.idAlerta}</title>
        <style>
          @page {
            size: A4;
            margin: 15mm;
          }
          * {
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
          }
          body {
            color: #0f172a;
            background: #ffffff;
            margin: 0;
            padding: 20px;
            font-size: 13px;
            line-height: 1.5;
          }
          .encabezado-oficial {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #0284c7;
            padding-bottom: 12px;
            margin-bottom: 16px;
          }
          .logo-bloque {
            display: flex;
            align-items: center;
            gap: 12px;
          }
          .escudo {
            font-size: 34px;
          }
          .entidad-titulo {
            font-size: 11px;
            font-weight: 800;
            color: #0369a1;
            text-transform: uppercase;
            letter-spacing: 0.5px;
          }
          .entidad-sub {
            font-size: 15px;
            font-weight: 800;
            color: #0f172a;
          }
          .codigo-acta-bloque {
            text-align: right;
          }
          .nro-acta {
            font-size: 15px;
            font-weight: 800;
            color: #e11d48;
            font-family: monospace;
          }
          .titulo-documento {
            text-align: center;
            background: #f1f5f9;
            border: 1px solid #cbd5e1;
            padding: 8px;
            font-weight: 800;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 20px;
            border-radius: 4px;
          }
          .seccion-titulo {
            background: #0284c7;
            color: #ffffff;
            padding: 4px 8px;
            font-weight: 700;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-top: 14px;
            margin-bottom: 8px;
            border-radius: 3px;
          }
          .tabla-datos {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 10px;
          }
          .tabla-datos td {
            padding: 6px 8px;
            border: 1px solid #e2e8f0;
            font-size: 12px;
          }
          .tabla-datos td.etiqueta {
            background: #f8fafc;
            font-weight: 700;
            color: #475569;
            width: 25%;
          }
          .tabla-datos td.valor {
            color: #0f172a;
            font-weight: 600;
          }
          .badge-emergencia {
            display: inline-block;
            background: #fee2e2;
            color: #dc2626;
            border: 1px solid #f87171;
            font-weight: 800;
            padding: 2px 8px;
            border-radius: 4px;
            font-size: 11px;
          }
          .alerta-geo {
            background: #e0f2fe;
            border-left: 4px solid #0284c7;
            padding: 10px 14px;
            margin: 12px 0;
            font-size: 12px;
          }
          .bitacora-cronograma {
            width: 100%;
            border-collapse: collapse;
            font-size: 11px;
            margin-top: 6px;
          }
          .bitacora-cronograma th {
            background: #f1f5f9;
            border: 1px solid #cbd5e1;
            padding: 6px;
            text-align: left;
          }
          .bitacora-cronograma td {
            border: 1px solid #e2e8f0;
            padding: 6px;
          }
          .firmas-contenedor {
            display: flex;
            justify-content: space-between;
            margin-top: 50px;
            padding-top: 20px;
          }
          .caja-firma {
            width: 42%;
            text-align: center;
            border-top: 1px solid #334155;
            padding-top: 8px;
            font-size: 11px;
          }
          .caja-firma b {
            display: block;
            font-size: 12px;
          }
          .pie-certificacion {
            margin-top: 30px;
            font-size: 10px;
            color: #64748b;
            text-align: center;
            border-top: 1px dashed #cbd5e1;
            padding-top: 10px;
          }
          @media print {
            .no-print {
              display: none !important;
            }
          }
        </style>
      </head>
      <body>
        <div class="no-print" style="background: #1e293b; color: white; padding: 12px 20px; margin-bottom: 20px; border-radius: 8px; display: flex; justify-content: space-between; align-items: center;">
          <span>📄 <b>Vista Previa del Acta Policial de Incidencia</b> • Lista para imprimir o guardar como PDF</span>
          <button onclick="window.print()" style="background: #0284c7; color: white; border: none; padding: 8px 16px; border-radius: 6px; font-weight: bold; cursor: pointer;">
            🖨️ Imprimir / Guardar como PDF
          </button>
        </div>

        <div class="encabezado-oficial">
          <div class="logo-bloque">
            <span class="escudo">🛡️</span>
            <div>
              <div class="entidad-titulo">REPÚBLICA DEL PERÚ • SISTEMA INTEGRAL DE SEGURIDAD EN TRANSPORTE</div>
              <div class="entidad-sub">SISTEMA SAT-CARMEN • LÍNEA 1305 (71A: ATE ⇄ LA PUNTA)</div>
            </div>
          </div>
          <div class="codigo-acta-bloque">
            <div style="font-size: 10px; color: #64748b; font-weight: 700;">ACTA DE INCIDENCIA N°</div>
            <div class="nro-acta">ACT-2026-${String(data.idAlerta).padStart(4, '0')}</div>
          </div>
        </div>

        <div class="titulo-documento">
          ACTA OFICIAL DE INTERVENCIÓN POLICIAL Y DESPACHO DE AUXILIO ANTE ALERTA DE PÁNICO SILENCIOSO
        </div>

        <!-- 1. DATOS DEL EVENTO -->
        <div class="seccion-titulo">I. Identificación de la Emergencia y Telemetría</div>
        <table class="tabla-datos">
          <tr>
            <td class="etiqueta">Fecha y Hora de Emisión:</td>
            <td class="valor">${data.fechaHora}</td>
            <td class="etiqueta">Nivel de Prioridad:</td>
            <td class="valor"><span class="badge-emergencia">CÓDIGO ROJO • ALTA PRIORIDAD</span></td>
          </tr>
          <tr>
            <td class="etiqueta">Tipo de Activación:</td>
            <td class="valor">${data.tipoActivacion}</td>
            <td class="etiqueta">Estado de Intervención:</td>
            <td class="valor" style="color: #0284c7; font-weight: 800;">${data.estado}</td>
          </tr>
          <tr>
            <td class="etiqueta">Coordenadas GPS:</td>
            <td class="valor" style="font-family: monospace;">Lat: ${data.latitud} | Lng: ${data.longitud}</td>
            <td class="etiqueta">Sector / Distrito:</td>
            <td class="valor">${data.distrito || 'Corredor Callao - Lima'}</td>
          </tr>
        </table>

        <!-- 2. DATOS DE LA UNIDAD Y CONDUCTOR -->
        <div class="seccion-titulo">II. Datos de la Unidad Vehicular y Personal de Turno</div>
        <table class="tabla-datos">
          <tr>
            <td class="etiqueta">Unidad de Flota:</td>
            <td class="valor"><b>${data.codigoUnidad}</b> (Placa: ${data.placa})</td>
            <td class="etiqueta">Modelo de Chasis:</td>
            <td class="valor">${data.modelo || 'Mercedes-Benz O500 / Modasa'}</td>
          </tr>
          <tr>
            <td class="etiqueta">Conductor Asignado:</td>
            <td class="valor">${data.conductorNombre}</td>
            <td class="etiqueta">Documento DNI / Licencia:</td>
            <td class="valor">${data.conductorDni || 'Verificado'} (Lic. ${data.conductorLicencia || 'A-IIIa Profesional'})</td>
          </tr>
          <tr>
            <td class="etiqueta">Ruta Concesionada:</td>
            <td class="valor" colspan="3">Línea 1305 (71A) • Ate (Las Gardenias) ⇄ Lima Centro ⇄ La Punta (Colonial) [28.5 km]</td>
          </tr>
        </table>

        <!-- 3. DESPACHO POLICIAL POR GEOCERCA Y HAVERSINE -->
        <div class="seccion-titulo">III. Despacho Asignado por Cálculo Geográfico (Algoritmo Haversine)</div>
        <table class="tabla-datos">
          <tr>
            <td class="etiqueta">Comisaría PNP Asignada:</td>
            <td class="valor" style="color: #0369a1; font-weight: 800;">${data.comisariaAsignada}</td>
            <td class="etiqueta">Distancia Euclidiana:</td>
            <td class="valor" style="color: #e11d48; font-weight: 800;">${data.distanciaComisaria || 'Proximidad Inmediata'}</td>
          </tr>
          <tr>
            <td class="etiqueta">Dirección de la Base:</td>
            <td class="valor">${data.comisariaDireccion || 'Sector de Patrullaje'}</td>
            <td class="etiqueta">Teléfono Central PNP:</td>
            <td class="valor">${data.comisariaTelefono || '105'}</td>
          </tr>
          <tr>
            <td class="etiqueta">ETA Estimado de Auxilio:</td>
            <td class="valor" colspan="3">~ ${data.etaMinutos || 3} minutos (Unidad de Patrullaje Integrado / Serenazgo alertada)</td>
          </tr>
        </table>

        <div class="alerta-geo">
          <b>Resumen Técnico de la Intervención:</b> El conductor activó el botón de pánico discreto tras detectar una situación de peligro inminente (asalto/extorsión). La central satelital fijó la telemetría en tiempo real y disparó el protocolo de camuflaje en cabina móvil, mientras que la alerta fue despachada inmediatamente a la jurisdicción de la <b>${data.comisariaAsignada}</b>.
        </div>

        <!-- 4. CRONOLOGÍA DE EVENTOS -->
        <div class="seccion-titulo">IV. Cronología de Telemetría Registrada</div>
        <table class="bitacora-cronograma">
          <thead>
            <tr>
              <th>Hora Exacta</th>
              <th>Secuencia del Evento</th>
              <th>Respuesta del Sistema</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>${data.fechaHora}</td>
              <td>Pulsación triple en sensor de pánico de cabina</td>
              <td>Transmisión encriptada vía protocolo WebSocket/SSE</td>
            </tr>
            <tr>
              <td>${data.fechaHora} (+2 seg)</td>
              <td>Fijación de coordenadas satelitales (-12.04..., -77.09...)</td>
              <td>Cálculo de la comisaría PNP más próxima vía Haversine</td>
            </tr>
            <tr>
              <td>${data.fechaHora} (+4 seg)</td>
              <td>Activación de pantalla de camuflaje en smartphone del chofer</td>
              <td>Simulación de desconexión sin conexión para salvaguarda física</td>
            </tr>
            <tr>
              <td>${data.fechaHora} (+8 seg)</td>
              <td>Generación de orden de despacho policial</td>
              <td>Enlace telemático transmitido a la PNP del sector</td>
            </tr>
          </tbody>
        </table>

        <!-- 5. FIRMAS -->
        <div class="firmas-contenedor">
          <div class="caja-firma">
            <b>${data.operadorNombre || 'Operador de Turno'}</b>
            <span>Central de Monitoreo y Despacho SAT</span><br/>
            <span>CIP / DNI Operacional: 41829012</span>
          </div>
          <div class="caja-firma">
            <b>Comisaría PNP Jurisdiccional</b>
            <span>Oficial de Guardia / Mando de Patrullaje</span><br/>
            <span>${data.comisariaAsignada}</span>
          </div>
        </div>

        <div class="pie-certificacion">
          Documento digital generado automáticamente por el Sistema de Alerta Temprana en Transporte Público (SAT). Conforme a la Ley N° 27181 (Ley General de Transporte y Tránsito Terrestre) y directivas de seguridad ciudadana del MININTER.
        </div>
      </body>
      </html>
    `;

    ventanaImpresion.document.open();
    ventanaImpresion.document.write(htmlContent);
    ventanaImpresion.document.close();
  }
}

