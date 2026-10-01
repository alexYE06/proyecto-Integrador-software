import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { ChoferAuthService } from '../../core/services/chofer-auth.service';
import { Alerta, ConductorLoginResponse } from '../../core/models/sistema.models';

@Component({
  selector: 'app-chofer-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="pantalla-movil-contenedor">
      
      <!-- ========================================== -->
      <!-- MODO CAMUFLAJE: PANTALLA FALSA DE ERROR 404 -->
      <!-- (Se activa automáticamente al pulsar pánico) -->
      <!-- ========================================== -->
      <div *ngIf="modoCamuflaje" class="dispositivo-movil camuflaje-screen">
        <!-- Status Bar del navegador del teléfono -->
        <div class="camuflaje-status-bar">
          <span>{{ horaActual }}</span>
          <span>📶 Sin señal • 82% 🔋</span>
        </div>

        <!-- Barra de dirección web simulada de navegador móvil -->
        <div class="camuflaje-browser-bar">
          <span class="browser-icon">⚠️</span>
          <span class="browser-url">carmendelapunta.pe/telemetria</span>
          <span class="browser-reload" (click)="simularReintento()">↻</span>
        </div>

        <!-- Contenido idéntico a pantalla de desconexión Chrome / Android -->
        <div class="camuflaje-body">
          <div class="dino-container" (click)="toqueSecretoCamuflaje()" title="Toque triple secreto para salir del camuflaje">
            <div class="dino-icono">🦖</div>
          </div>

          <h1 class="camuflaje-title">No hay conexión a Internet</h1>
          <p class="camuflaje-desc">
            No se pudo establecer comunicación con el host de la red móvil de transporte.
          </p>

          <div class="camuflaje-tips">
            <p>Prueba lo siguiente:</p>
            <ul>
              <li>Comprobar los cables de red, antena y cobertura 4G/LTE</li>
              <li>Reiniciar la antena GPS del vehículo</li>
              <li>Comprobar la configuración del proxy de red</li>
            </ul>
          </div>

          <div class="camuflaje-error-code">
            ERR_INTERNET_DISCONNECTED • HTTP 404 (Not Found)
          </div>

          <button class="btn-camuflaje-reintentar" (click)="simularReintento()">
            {{ reintentando ? 'Buscando red...' : 'Reintentar' }}
          </button>

          <!-- Salida secreta para demostración / prueba -->
          <div class="camuflaje-secreto-hint" (click)="salirCamuflajeDirecto()">
            <span>[🔒 Toque triple en dinosaurio o clic aquí para salir del camuflaje de prueba]</span>
          </div>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- PANTALLA NORMAL: CABINA DE TELEMETRÍA MÓVIL -->
      <!-- ========================================== -->
      <div *ngIf="!modoCamuflaje" class="dispositivo-movil">
        <!-- BARRA SUPERIOR MÓVIL -->
        <header class="header-movil">
          <div class="status-bar">
            <span>{{ horaActual }}</span>
            <span>📶 4G • 98% 🔋</span>
          </div>
          <div class="info-chofer">
            <button class="btn-cerrar-turno" (click)="cerrarTurno()">
              🚪 Cerrar Turno
            </button>
            <div class="unidad-badge">
              {{ chofer?.codigoUnidad || 'BUS-071' }} • {{ chofer?.placa || 'A1A-710' }}
            </div>
          </div>
        </header>

        <!-- CUERPO PRINCIPAL -->
        <main class="cuerpo-movil">
          <!-- TARJETA DEL CONDUCTOR EN TURNO -->
          <div class="tarjeta-conductor">
            <div class="icono-conductor">👨‍✈️</div>
            <div class="datos-conductor">
              <h3>{{ chofer?.nombres }} {{ chofer?.apellidos }}</h3>
              <p class="licencia-info">
                DNI: <b>{{ chofer?.dni }}</b> • Lic: <b>{{ chofer?.licencia }}</b>
              </p>
              <div class="estado-ruta">
                <span [class]="motorEncendido ? 'tag-en-ruta' : 'tag-detenido'">
                  {{ motorEncendido ? '● EN SERVICIO' : '⏸️ MOTOR EN STANDBY' }}
                </span>
                <span class="subtexto-ruta">Ruta 71A: Callao - Carmen de la Legua</span>
              </div>
            </div>
          </div>

          <!-- CONTROL DE ENCENDIDO / MOTOR DEL AUTOBÚS -->
          <div class="tarjeta-motor" [class.activo]="motorEncendido">
            <div class="motor-info">
              <span class="motor-icono">{{ motorEncendido ? '⚡' : '🔑' }}</span>
              <div class="motor-detalles">
                <div class="motor-titulo">{{ motorEncendido ? 'Motor en Marcha (Activo)' : 'Motor Apagado / En Cochera' }}</div>
                <div class="motor-sub">{{ motorEncendido ? 'Transmitiendo telemetría en tiempo real' : 'Presiona el botón para encender unidad y salir a ruta' }}</div>
              </div>
            </div>
            <button 
              type="button" 
              class="btn-motor" 
              [class.btn-apagar]="motorEncendido"
              (click)="toggleMotor()">
              {{ motorEncendido ? '🛑 Apagar Motor' : '⚡ Iniciar Marcha / Encender' }}
            </button>
          </div>

          <!-- BOTÓN DE PÁNICO SILENCIOSO -->
          <div class="seccion-panico">
            <div class="instruccion">
              <span class="titulo-alerta">⚠️ BOTÓN DE EMERGENCIA SILENCIOSA</span>
              <small>Activa el auxilio en Central y disfraza la pantalla en Modo 404</small>
            </div>

            <button 
              class="boton-panico-pulsador" 
              [class.pulsado]="alertaEnviada"
              [disabled]="enviandoAlerta"
              (click)="activarAlertaSilenciosa()">
              <span class="icono-panico">{{ alertaEnviada ? '🚨' : '🆘' }}</span>
              <span class="texto-panico">
                {{ enviandoAlerta ? 'TRANSMITIENDO...' : (alertaEnviada ? '¡ALERTA EMITIDA!' : 'ENVIAR ALERTA SILENCIOSA') }}
              </span>
              <span class="sub-panico">{{ alertaEnviada ? 'Central Notificada • Disfraz Activo' : 'Triple pulsación discreta' }}</span>
            </button>

            <!-- MENSAJE DE CONFIRMACIÓN SI VUELVE DEL CAMUFLAJE -->
            <div *ngIf="alertaEnviada" class="mensaje-exito">
              <div class="alerta-check">✓ Alerta despachada silenciosamente a la Central.</div>
              <small>Coordenadas GPS enviadas: -12.0562, -77.0841 • Bus {{ chofer?.codigoUnidad }}</small>
              <div class="botones-alerta-acciones">
                <button class="btn-volver-camuflaje" (click)="activarCamuflajeManualmente()">
                  🕶️ Ocultar pantalla (Volver a 404)
                </button>
                <button class="btn-rearmar" (click)="rearmarBoton()">↺ Rearmar pulsador</button>
              </div>
            </div>
          </div>

          <!-- TELEMETRÍA Y RUTA EN VIVO -->
          <div class="tarjeta-info">
            <div class="telemetria-cabecera">
              <h4>Telemetría en Vivo</h4>
              <span class="pulso-gps" [class.inactivo]="!motorEncendido"></span>
            </div>

            <div class="fila-info">
              <span>Unidad Móvil:</span>
              <b>{{ chofer?.codigoUnidad }} ({{ chofer?.placa }})</b>
            </div>
            <div class="fila-info">
              <span>Estado de Marcha:</span>
              <b [style.color]="motorEncendido ? '#34d399' : '#f59e0b'">
                {{ motorEncendido ? 'En movimiento (38.5 km/h)' : '0.0 km/h (Detenido)' }}
              </b>
            </div>
            <div class="fila-info">
              <span>Próximo paradero:</span>
              <b>{{ motorEncendido ? 'Av. Faucett cruce Colonial' : 'Terminal Callao (Espera)' }}</b>
            </div>
            <div class="fila-info">
              <span>Transmisión GPS:</span>
              <b [style.color]="motorEncendido ? '#34d399' : '#94a3b8'">
                {{ motorEncendido ? 'Activa (Frecuencia 1s)' : 'Standby / En Reposo' }}
              </b>
            </div>
          </div>
        </main>
      </div>
    </div>
  `,
  styles: [`
    .pantalla-movil-contenedor {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #090d16;
      font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      padding: 15px;
      box-sizing: border-box;
    }

    .dispositivo-movil {
      max-width: 390px;
      width: 100%;
      min-height: 720px;
      background: #0f172a;
      border: 8px solid #1e293b;
      border-radius: 40px;
      box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.85), 0 0 35px rgba(2, 132, 199, 0.1);
      display: flex;
      flex-direction: column;
      overflow: hidden;
      color: #f8fafc;
      position: relative;
    }

    /* ------------------------------------------------ */
    /* ESTILOS DE LA PANTALLA CAMUFLAJE (CHROME DARK 404) */
    /* ------------------------------------------------ */
    .camuflaje-screen {
      background: #202124 !important;
      color: #e8eaed !important;
      border-color: #3c4043 !important;
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
    }

    .camuflaje-status-bar {
      background: #292a2d;
      color: #9aa0a6;
      display: flex;
      justify-content: space-between;
      padding: 8px 16px;
      font-size: 11px;
      font-weight: 500;
      border-bottom: 1px solid #3c4043;
    }

    .camuflaje-browser-bar {
      background: #292a2d;
      border-bottom: 1px solid #3c4043;
      padding: 8px 14px;
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 12px;
      color: #9aa0a6;
    }

    .browser-url {
      flex: 1;
      background: #35363a;
      border: 1px solid #5f6368;
      border-radius: 16px;
      padding: 4px 12px;
      color: #e8eaed;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .browser-reload {
      font-size: 16px;
      font-weight: bold;
      color: #9aa0a6;
      cursor: pointer;
      user-select: none;
    }

    .camuflaje-body {
      padding: 30px 24px;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      flex: 1;
      background: #202124;
    }

    .dino-container {
      margin-bottom: 16px;
      cursor: pointer;
      user-select: none;
      padding: 4px;
      border-radius: 8px;
      transition: transform 0.15s;
    }

    .dino-container:active {
      transform: scale(1.1);
    }

    .dino-icono {
      font-size: 48px;
      filter: invert(85%) opacity(85%);
    }

    .camuflaje-title {
      font-size: 20px;
      font-weight: 600;
      color: #e8eaed;
      margin: 0 0 12px 0;
      line-height: 1.3;
    }

    .camuflaje-desc {
      font-size: 13px;
      color: #9aa0a6;
      margin: 0 0 16px 0;
      line-height: 1.5;
    }

    .camuflaje-tips {
      font-size: 12px;
      color: #9aa0a6;
      margin-bottom: 20px;
      padding-left: 0;
    }

    .camuflaje-tips p {
      margin: 0 0 6px 0;
      font-weight: 500;
      color: #e8eaed;
    }

    .camuflaje-tips ul {
      margin: 0;
      padding-left: 20px;
    }

    .camuflaje-tips li {
      margin-bottom: 6px;
      line-height: 1.4;
      color: #9aa0a6;
    }

    .camuflaje-error-code {
      font-size: 11px;
      color: #80868b;
      font-family: monospace;
      margin-bottom: 24px;
    }

    .btn-camuflaje-reintentar {
      background: #8ab4f8;
      color: #202124;
      border: none;
      padding: 9px 20px;
      border-radius: 4px;
      font-size: 13px;
      font-weight: 700;
      cursor: pointer;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
      transition: background 0.2s;
    }

    .btn-camuflaje-reintentar:hover {
      background: #aecbfa;
    }

    .camuflaje-secreto-hint {
      margin-top: auto;
      padding-top: 24px;
      font-size: 10px;
      color: #5f6368;
      cursor: pointer;
      text-align: center;
      width: 100%;
    }

    .camuflaje-secreto-hint:hover {
      color: #80868b;
    }

    /* ------------------------------------------------ */
    /* PANTALLA DASHBOARD ORIGINAL                      */
    /* ------------------------------------------------ */
    .header-movil {
      background: #1e293b;
      padding: 14px 18px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.08);
    }

    .status-bar {
      display: flex;
      justify-content: space-between;
      font-size: 11px;
      color: #94a3b8;
      margin-bottom: 10px;
      font-weight: 500;
    }

    .info-chofer {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .btn-cerrar-turno {
      background: rgba(239, 68, 68, 0.15);
      border: 1px solid rgba(239, 68, 68, 0.3);
      color: #f87171;
      font-size: 11px;
      font-weight: 700;
      padding: 4px 10px;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-cerrar-turno:hover {
      background: rgba(239, 68, 68, 0.3);
      color: #ffffff;
    }

    .unidad-badge {
      background: #0284c7;
      color: #ffffff;
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 11px;
      font-weight: 800;
      letter-spacing: 0.5px;
    }

    .cuerpo-movil {
      padding: 18px;
      flex-grow: 1;
      display: flex;
      flex-direction: column;
      gap: 14px;
    }

    .tarjeta-conductor {
      background: #1e293b;
      border: 1px solid rgba(255, 255, 255, 0.06);
      border-radius: 16px;
      padding: 14px;
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .icono-conductor {
      font-size: 32px;
      background: #334155;
      padding: 10px;
      border-radius: 50%;
      border: 1px solid rgba(255, 255, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .datos-conductor {
      flex: 1;
      min-width: 0;
    }

    .tarjeta-conductor h3 {
      font-size: 15px;
      font-weight: 700;
      margin: 0 0 2px 0;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .licencia-info {
      font-size: 11px;
      color: #94a3b8;
      margin: 0 0 6px 0;
    }

    .estado-ruta {
      display: flex;
      align-items: center;
      gap: 6px;
      flex-wrap: wrap;
    }

    .tag-en-ruta {
      font-size: 9px;
      font-weight: 800;
      color: #34d399;
      background: rgba(16, 185, 129, 0.15);
      padding: 2px 6px;
      border-radius: 4px;
    }

    .tag-detenido {
      font-size: 9px;
      font-weight: 800;
      color: #f59e0b;
      background: rgba(245, 158, 11, 0.15);
      padding: 2px 6px;
      border-radius: 4px;
    }

    .subtexto-ruta {
      font-size: 10px;
      color: #cbd5e1;
    }

    /* CONTROL DE MOTOR */
    .tarjeta-motor {
      background: rgba(30, 41, 59, 0.7);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 14px;
      padding: 12px 14px;
      display: flex;
      flex-direction: column;
      gap: 10px;
      transition: all 0.3s;
    }

    .tarjeta-motor.activo {
      border-color: rgba(52, 211, 153, 0.35);
      background: rgba(16, 185, 129, 0.06);
    }

    .motor-info {
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .motor-icono {
      font-size: 22px;
    }

    .motor-detalles {
      flex: 1;
    }

    .motor-titulo {
      font-size: 12px;
      font-weight: 700;
      color: #f8fafc;
    }

    .motor-sub {
      font-size: 10px;
      color: #94a3b8;
    }

    .btn-motor {
      width: 100%;
      background: #0284c7;
      color: #ffffff;
      border: none;
      padding: 8px 12px;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-motor:hover {
      background: #0369a1;
    }

    .btn-motor.btn-apagar {
      background: rgba(239, 68, 68, 0.2);
      border: 1px solid rgba(239, 68, 68, 0.4);
      color: #fca5a5;
    }

    .btn-motor.btn-apagar:hover {
      background: rgba(239, 68, 68, 0.35);
      color: #ffffff;
    }

    /* PÁNICO */
    .seccion-panico {
      background: rgba(225, 29, 72, 0.08);
      border: 1px dashed rgba(225, 29, 72, 0.45);
      border-radius: 20px;
      padding: 16px 14px;
      text-align: center;
    }

    .titulo-alerta {
      display: block;
      font-size: 12px;
      font-weight: 800;
      color: #fb7185;
      margin-bottom: 2px;
      letter-spacing: 0.3px;
    }

    .instruccion small {
      font-size: 10px;
      color: #94a3b8;
      display: block;
      margin-bottom: 12px;
    }

    .boton-panico-pulsador {
      width: 130px;
      height: 130px;
      border-radius: 50%;
      background: radial-gradient(circle, #f43f5e 0%, #be123c 100%);
      border: 5px solid #fda4af;
      color: white;
      box-shadow: 0 0 30px rgba(244, 63, 94, 0.6);
      cursor: pointer;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      transition: all 0.25s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }

    .boton-panico-pulsador:hover {
      box-shadow: 0 0 45px rgba(244, 63, 94, 0.85);
      transform: scale(1.03);
    }

    .boton-panico-pulsador:active, .boton-panico-pulsador.pulsado {
      transform: scale(0.95);
      background: #881337;
      border-color: #f43f5e;
      box-shadow: 0 0 50px rgba(244, 63, 94, 1);
    }

    .icono-panico {
      font-size: 30px;
      margin-bottom: 3px;
    }

    .texto-panico {
      font-size: 10px;
      font-weight: 800;
      line-height: 1.2;
      padding: 0 8px;
    }

    .sub-panico {
      font-size: 8px;
      opacity: 0.85;
      margin-top: 3px;
    }

    .mensaje-exito {
      margin-top: 12px;
      font-size: 11px;
      color: #4ade80;
      background: rgba(34, 197, 94, 0.12);
      border: 1px solid rgba(34, 197, 94, 0.25);
      padding: 10px;
      border-radius: 8px;
      text-align: center;
    }

    .alerta-check {
      font-weight: 700;
      margin-bottom: 3px;
    }

    .botones-alerta-acciones {
      display: flex;
      gap: 8px;
      justify-content: center;
      margin-top: 8px;
    }

    .btn-volver-camuflaje {
      background: #334155;
      border: 1px solid #475569;
      color: #e2e8f0;
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 10px;
      font-weight: 600;
      cursor: pointer;
    }

    .btn-rearmar {
      background: transparent;
      border: 1px solid rgba(74, 222, 128, 0.4);
      color: #4ade80;
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 10px;
      cursor: pointer;
    }

    .btn-rearmar:hover {
      background: rgba(74, 222, 128, 0.2);
    }

    /* TELEMETRÍA */
    .tarjeta-info {
      background: #1e293b;
      border-radius: 16px;
      padding: 12px 16px;
      border: 1px solid rgba(255, 255, 255, 0.05);
    }

    .telemetria-cabecera {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;
    }

    .tarjeta-info h4 {
      margin: 0;
      font-size: 12px;
      color: #94a3b8;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .pulso-gps {
      width: 8px;
      height: 8px;
      background: #34d399;
      border-radius: 50%;
      box-shadow: 0 0 8px #34d399;
      animation: pulso 1.5s infinite;
    }

    .pulso-gps.inactivo {
      background: #64748b;
      box-shadow: none;
      animation: none;
    }

    @keyframes pulso {
      0% { opacity: 1; transform: scale(1); }
      50% { opacity: 0.4; transform: scale(1.3); }
      100% { opacity: 1; transform: scale(1); }
    }

    .fila-info {
      display: flex;
      justify-content: space-between;
      font-size: 11px;
      padding: 5px 0;
      border-bottom: 1px solid rgba(255, 255, 255, 0.04);
    }

    .fila-info:last-child {
      border-bottom: none;
    }
  `]
})
export class ChoferDashboardComponent implements OnInit, OnDestroy {
  alertaEnviada = false;
  enviandoAlerta = false;
  motorEncendido = false; // El motor empieza apagado según requerimiento de seguridad
  modoCamuflaje = false;  // Pantalla 404 de camuflaje al activar pánico
  reintentando = false;

  chofer: ConductorLoginResponse | null = null;
  horaActual = '08:42';
  private timerReloj: any;

  // Contador de toques secretos para salir del camuflaje
  private toquesSecretos = 0;
  private ultimoToque = 0;

  private apiService = inject(ApiService);
  private choferAuthService = inject(ChoferAuthService);
  private router = inject(Router);

  ngOnInit(): void {
    this.chofer = this.choferAuthService.obtenerSesion();
    if (!this.chofer) {
      this.router.navigate(['/login-chofer']);
      return;
    }

    this.actualizarHora();
    this.timerReloj = setInterval(() => this.actualizarHora(), 10000);
  }

  ngOnDestroy(): void {
    if (this.timerReloj) {
      clearInterval(this.timerReloj);
    }
  }

  actualizarHora(): void {
    const ahora = new Date();
    this.horaActual = ahora.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  toggleMotor(): void {
    this.motorEncendido = !this.motorEncendido;
  }

  activarAlertaSilenciosa(): void {
    if (!this.chofer || this.enviandoAlerta) return;

    // Redirección inmediata y automática al camuflaje 404 para proteger la vida del chofer
    this.modoCamuflaje = true;
    this.enviandoAlerta = true;
    this.alertaEnviada = true;

    const nuevaAlerta: Alerta = {
      idConductor: this.chofer.idConductor,
      idBus: this.chofer.idBus,
      tipoActivacion: 'BOTON_PANICO_3_PULSOS',
      estado: 'Recibida',
      descripcion: `Alerta silenciosa generada desde smartphone del conductor ${this.chofer.nombres} ${this.chofer.apellidos} (Unidad ${this.chofer.codigoUnidad}, Placa ${this.chofer.placa})`
    };

    // Enviamos la alerta en segundo plano a la Central
    this.apiService.registrarAlerta(nuevaAlerta).subscribe({
      next: (res) => {
        this.enviandoAlerta = false;
        console.log('Alerta registrada en tiempo real:', res);
      },
      error: (e) => {
        this.enviandoAlerta = false;
        console.error('Error al registrar alerta en Spring Boot:', e);
      }
    });
  }

  // Toque secreto para salir del camuflaje (3 toques rápidos)
  toqueSecretoCamuflaje(): void {
    const ahora = Date.now();
    if (ahora - this.ultimoToque < 800) {
      this.toquesSecretos++;
    } else {
      this.toquesSecretos = 1;
    }
    this.ultimoToque = ahora;

    if (this.toquesSecretos >= 3) {
      this.salirCamuflajeDirecto();
    }
  }

  salirCamuflajeDirecto(): void {
    this.modoCamuflaje = false;
    this.toquesSecretos = 0;
  }

  activarCamuflajeManualmente(): void {
    this.modoCamuflaje = true;
  }

  simularReintento(): void {
    this.reintentando = true;
    setTimeout(() => {
      this.reintentando = false;
    }, 1500);
  }

  rearmarBoton(): void {
    this.alertaEnviada = false;
    this.enviandoAlerta = false;
    this.modoCamuflaje = false;
  }

  cerrarTurno(): void {
    this.choferAuthService.cerrarSesion();
  }
}
