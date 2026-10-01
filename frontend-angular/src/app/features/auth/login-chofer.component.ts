import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { ChoferAuthService } from '../../core/services/chofer-auth.service';
import { Bus } from '../../core/models/sistema.models';

@Component({
  selector: 'app-login-chofer',
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <div class="contenedor-login">
      <div class="dispositivo-marco">
        <div class="caja-login">
          <button class="btn-volver" (click)="volver()">← Volver al selector</button>

          <div class="cabecera">
            <div class="icono-chofer">🚍</div>
            <h2>Acceso a Cabina en Ruta</h2>
            <p>Identifícate con tu DNI y selecciona la unidad asignada para iniciar el turno de monitoreo y botón de pánico.</p>
          </div>

          <form (ngSubmit)="iniciarTurno()">
            <!-- DNI CONDUCTOR -->
            <div class="campo">
              <label>DNI del Conductor (8 dígitos)</label>
              <div class="input-con-icono">
                <span class="icono-input">🪪</span>
                <input 
                  type="text" 
                  [(ngModel)]="dni" 
                  name="dni" 
                  maxlength="8"
                  placeholder="Ej. 71234567"
                  (input)="filtrarSoloNumeros()"
                  required 
                />
              </div>
            </div>

            <!-- PIN O CONTRASEÑA DE SEGURIDAD -->
            <div class="campo">
              <label>PIN o Contraseña de Conductor</label>
              <div class="input-con-icono">
                <span class="icono-input">🔒</span>
                <input 
                  type="password" 
                  [(ngModel)]="contrasena" 
                  name="contrasena" 
                  maxlength="20"
                  placeholder="PIN institucional (Ej. 1234)"
                  required 
                />
              </div>
            </div>

            <!-- UNIDAD ASIGNADA -->
            <div class="campo">
              <label>Unidad de Bus Asignada</label>
              <div class="input-con-icono">
                <span class="icono-input">🚌</span>
                <select [(ngModel)]="idBusSeleccionado" name="idBus">
                  <option [ngValue]="null">-- Asignación automática por defecto --</option>
                  <option *ngFor="let bus of buses" [ngValue]="bus.idBus">
                    {{ bus.numeroUnidad }} • {{ bus.placa }} ({{ bus.modelo }})
                  </option>
                </select>
              </div>
            </div>

            <!-- CHIPS DEMO RÁPIDOS -->
            <div class="seccion-chips">
              <span class="texto-chips">Conductores de prueba (PIN: 1234):</span>
              <div class="chips-grid">
                <button type="button" class="chip" (click)="seleccionarDemo('71234567')">
                  Carlos Mendoza (71234567)
                </button>
                <button type="button" class="chip" (click)="seleccionarDemo('73456789')">
                  Elena Castillo (73456789)
                </button>
              </div>
            </div>

            <!-- ERROR Y CARGANDO -->
            <div *ngIf="mensajeError" class="alerta-error">
              ⚠️ {{ mensajeError }}
            </div>

            <div *ngIf="cargando" class="cargando">
              <span class="spinner"></span> Validando credenciales con la Central...
            </div>

            <!-- BOTÓN ENTRAR -->
            <button type="submit" class="btn-iniciar" [disabled]="cargando || dni.length !== 8 || !contrasena">
              {{ cargando ? 'Verificando con Central...' : 'Iniciar Turno en Ruta' }}
            </button>
          </form>

          <div class="pie-info">
            <small>🔒 Solo conductores registrados en el padrón de Carmen de la Punta S.A. pueden operar la cabina de telemetría.</small>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .contenedor-login {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: radial-gradient(circle at top, #0e1e38 0%, #090d16 100%);
      font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      padding: 20px;
      box-sizing: border-box;
      color: #f8fafc;
    }

    .dispositivo-marco {
      max-width: 440px;
      width: 100%;
      background: #0f172a;
      border: 1px solid rgba(56, 189, 248, 0.2);
      border-radius: 28px;
      box-shadow: 0 25px 60px -15px rgba(0, 0, 0, 0.8), 0 0 40px rgba(14, 165, 233, 0.08);
      overflow: hidden;
    }

    .caja-login {
      padding: 36px 28px;
    }

    .btn-volver {
      background: none;
      border: none;
      color: #38bdf8;
      font-size: 13px;
      font-weight: 600;
      cursor: pointer;
      padding: 0 0 16px 0;
      display: inline-flex;
      align-items: center;
      transition: color 0.2s;
    }

    .btn-volver:hover {
      color: #7dd3fc;
    }

    .cabecera {
      text-align: center;
      margin-bottom: 26px;
    }

    .icono-chofer {
      font-size: 44px;
      display: inline-block;
      margin-bottom: 8px;
      background: rgba(14, 165, 233, 0.15);
      width: 72px;
      height: 72px;
      line-height: 72px;
      border-radius: 50%;
      border: 1px solid rgba(56, 189, 248, 0.3);
    }

    .cabecera h2 {
      font-size: 22px;
      font-weight: 800;
      margin: 0 0 8px 0;
      color: #ffffff;
      letter-spacing: -0.3px;
    }

    .cabecera p {
      font-size: 13px;
      line-height: 1.5;
      color: #94a3b8;
      margin: 0;
    }

    .campo {
      margin-bottom: 18px;
    }

    label {
      display: block;
      font-size: 12px;
      font-weight: 600;
      color: #cbd5e1;
      margin-bottom: 6px;
    }

    .input-con-icono {
      position: relative;
      display: flex;
      align-items: center;
    }

    .icono-input {
      position: absolute;
      left: 14px;
      font-size: 16px;
      pointer-events: none;
    }

    input, select {
      width: 100%;
      padding: 12px 14px 12px 42px;
      background: #1e293b;
      border: 1px solid #334155;
      border-radius: 12px;
      color: #ffffff;
      font-size: 14px;
      box-sizing: border-box;
      transition: all 0.2s;
    }

    input:focus, select:focus {
      outline: none;
      border-color: #0284c7;
      box-shadow: 0 0 0 3px rgba(2, 132, 199, 0.25);
    }

    select option {
      background: #0f172a;
      color: #ffffff;
    }

    .seccion-chips {
      margin-bottom: 18px;
      padding: 10px 12px;
      background: rgba(30, 41, 59, 0.5);
      border-radius: 12px;
      border: 1px solid rgba(255, 255, 255, 0.05);
    }

    .texto-chips {
      display: block;
      font-size: 11px;
      font-weight: 600;
      color: #94a3b8;
      margin-bottom: 8px;
    }

    .chips-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
    }

    .chip {
      background: rgba(14, 165, 233, 0.12);
      border: 1px solid rgba(56, 189, 248, 0.25);
      color: #38bdf8;
      padding: 5px 10px;
      border-radius: 20px;
      font-size: 11px;
      cursor: pointer;
      transition: all 0.2s;
    }

    .chip:hover {
      background: rgba(14, 165, 233, 0.25);
      border-color: #38bdf8;
      color: #ffffff;
    }

    .alerta-error {
      background: rgba(225, 29, 72, 0.15);
      border: 1px solid rgba(244, 63, 94, 0.35);
      color: #fda4af;
      padding: 12px;
      border-radius: 10px;
      font-size: 13px;
      margin-bottom: 16px;
      line-height: 1.4;
    }

    .cargando {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 12px;
      color: #38bdf8;
      margin-bottom: 14px;
      justify-content: center;
    }

    .spinner {
      width: 14px;
      height: 14px;
      border: 2px solid rgba(56, 189, 248, 0.3);
      border-top-color: #38bdf8;
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    .btn-iniciar {
      width: 100%;
      padding: 14px;
      background: linear-gradient(135deg, #0284c7 0%, #0891b2 100%);
      color: white;
      border: none;
      border-radius: 12px;
      font-size: 15px;
      font-weight: 700;
      cursor: pointer;
      box-shadow: 0 4px 15px rgba(2, 132, 199, 0.4);
      transition: all 0.2s;
    }

    .btn-iniciar:hover:not([disabled]) {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(2, 132, 199, 0.6);
      background: linear-gradient(135deg, #0369a1 0%, #06b6d4 100%);
    }

    .btn-iniciar[disabled] {
      opacity: 0.55;
      cursor: not-allowed;
      transform: none;
    }

    .pie-info {
      text-align: center;
      margin-top: 20px;
      padding-top: 14px;
      border-top: 1px solid rgba(255, 255, 255, 0.05);
    }

    .pie-info small {
      font-size: 11px;
      color: #64748b;
      line-height: 1.4;
      display: block;
    }
  `]
})
export class LoginChoferComponent implements OnInit {
  dni = '';
  contrasena = '';
  idBusSeleccionado: number | null = null;
  buses: Bus[] = [];
  cargando = false;
  mensajeError = '';

  private apiService = inject(ApiService);
  private choferAuthService = inject(ChoferAuthService);
  private router = inject(Router);

  ngOnInit(): void {
    // Si ya está autenticado, redirigir directo al dashboard
    if (this.choferAuthService.estaAutenticado()) {
      this.router.navigate(['/chofer']);
      return;
    }

    this.cargarFlotaBuses();
  }

  cargarFlotaBuses(): void {
    this.apiService.getBuses().subscribe({
      next: (data) => {
        this.buses = data || [];
        if (this.buses.length > 0 && !this.idBusSeleccionado) {
          this.idBusSeleccionado = this.buses[0].idBus;
        }
      },
      error: (err) => console.warn('No se pudo cargar la lista de buses en el selector:', err)
    });
  }

  filtrarSoloNumeros(): void {
    this.dni = this.dni.replace(/\D/g, '');
  }

  seleccionarDemo(dniDemo: string): void {
    this.dni = dniDemo;
    this.contrasena = '1234';
    this.mensajeError = '';
  }

  iniciarTurno(): void {
    if (!this.dni || this.dni.length !== 8) {
      this.mensajeError = 'Debe ingresar un DNI válido de 8 dígitos numéricos.';
      return;
    }

    if (!this.contrasena || !this.contrasena.trim()) {
      this.mensajeError = 'Debe ingresar el PIN o contraseña de seguridad.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    const busElegido = this.buses.find(b => b.idBus === this.idBusSeleccionado);
    const codigoUnidad = busElegido ? busElegido.numeroUnidad : undefined;

    this.apiService.conductorLogin(this.dni, this.contrasena, this.idBusSeleccionado || undefined, codigoUnidad).subscribe({
      next: (res) => {
        this.cargando = false;
        this.choferAuthService.guardarSesion(res);
        this.router.navigate(['/chofer']);
      },
      error: (err) => {
        this.cargando = false;
        if (err.error && err.error.mensaje) {
          this.mensajeError = err.error.mensaje;
        } else if (err.status === 401) {
          this.mensajeError = 'Credenciales no autorizadas o PIN de seguridad incorrecto.';
        } else {
          this.mensajeError = 'No se pudo conectar con el servidor de la central. Verifique que Spring Boot esté activo.';
        }
      }
    });
  }

  volver(): void {
    this.router.navigate(['/']);
  }
}
