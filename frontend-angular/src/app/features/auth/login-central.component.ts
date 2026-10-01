import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login-central',
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <div class="contenedor-login">
      <div class="caja-login">
        <button class="btn-volver" (click)="volver()">← Volver al selector</button>

        <div class="cabecera">
          <div class="icono-escudo">🛡️</div>
          <h2>Central de Operaciones</h2>
          <p>Autenticación de personal de monitoreo y despacho</p>
        </div>

        <form (ngSubmit)="iniciarSesion()">
          <div class="campo">
            <label>Usuario</label>
            <input 
              type="text" 
              [(ngModel)]="usuario" 
              name="usuario" 
              placeholder="Ej. admin.central u operador1"
              required 
            />
          </div>

          <div class="campo">
            <label>Contraseña</label>
            <input 
              type="password" 
              [(ngModel)]="contrasena" 
              name="contrasena" 
              placeholder="••••••••"
              required 
            />
          </div>

          <div *ngIf="mensajeError" class="alerta-error">
            ⚠️ {{ mensajeError }}
          </div>

          <div *ngIf="cargando" class="cargando">
            Verificando credenciales con MySQL...
          </div>

          <button type="submit" class="btn-ingresar" [disabled]="cargando">
            {{ cargando ? 'Iniciando sesión...' : 'Ingresar a la Central' }}
          </button>
        </form>

        <div class="ayuda-credenciales">
          <small>Credenciales por defecto: <b>admin.central</b> / <b>admin123</b></small>
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
      background: #0f172a;
      font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      padding: 20px;
      box-sizing: border-box;
    }

    .caja-login {
      max-width: 420px;
      width: 100%;
      background: #1e293b;
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 16px;
      padding: 35px;
      box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.4);
      color: #f8fafc;
    }

    .btn-volver {
      background: transparent;
      border: none;
      color: #94a3b8;
      font-size: 13px;
      cursor: pointer;
      margin-bottom: 20px;
      padding: 0;
      transition: color 0.2s;
    }

    .btn-volver:hover {
      color: #f8fafc;
    }

    .cabecera {
      text-align: center;
      margin-bottom: 30px;
    }

    .icono-escudo {
      font-size: 40px;
      margin-bottom: 10px;
    }

    h2 {
      font-size: 22px;
      margin: 0 0 6px 0;
      color: #ffffff;
    }

    p {
      font-size: 13px;
      color: #94a3b8;
      margin: 0;
    }

    .campo {
      margin-bottom: 20px;
    }

    label {
      display: block;
      font-size: 12px;
      font-weight: 600;
      color: #cbd5e1;
      margin-bottom: 6px;
    }

    input {
      width: 100%;
      padding: 12px 14px;
      background: #0f172a;
      border: 1px solid #334155;
      border-radius: 8px;
      color: #ffffff;
      font-size: 14px;
      box-sizing: border-box;
      outline: none;
      transition: border-color 0.2s;
    }

    input:focus {
      border-color: #e11d48;
    }

    .alerta-error {
      background: rgba(225, 29, 72, 0.15);
      border: 1px solid rgba(225, 29, 72, 0.3);
      color: #fb7185;
      padding: 10px 14px;
      border-radius: 8px;
      font-size: 13px;
      margin-bottom: 18px;
    }

    .cargando {
      font-size: 12px;
      color: #38bdf8;
      margin-bottom: 15px;
      text-align: center;
    }

    .btn-ingresar {
      width: 100%;
      padding: 12px;
      background: #e11d48;
      color: #ffffff;
      font-weight: 600;
      border: none;
      border-radius: 8px;
      font-size: 14px;
      cursor: pointer;
      transition: background 0.2s;
    }

    .btn-ingresar:hover:not(:disabled) {
      background: #f43f5e;
    }

    .btn-ingresar:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .ayuda-credenciales {
      margin-top: 25px;
      text-align: center;
      font-size: 11px;
      color: #64748b;
    }
  `]
})
export class LoginCentralComponent implements OnInit {
  usuario = 'admin.central';
  contrasena = 'admin123';
  mensajeError = '';
  cargando = false;
  returnUrl = '/central/monitoreo';

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Si ya tiene sesión activa, va directo al dashboard
    if (this.authService.estaAutenticado()) {
      this.router.navigate(['/central/monitoreo']);
    }
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/central/monitoreo';
  }

  iniciarSesion() {
    this.mensajeError = '';
    this.cargando = true;

    this.apiService.login(this.usuario, this.contrasena).subscribe({
      next: (res) => {
        this.cargando = false;
        // Guardar sesión en el servicio de autenticación
        this.authService.guardarSesion(res);
        // Redirigir a la ruta solicitada previamente o por defecto
        this.router.navigateByUrl(this.returnUrl);
      },
      error: (err) => {
        this.cargando = false;
        this.mensajeError = err.error?.error || 'Error al conectar con el servidor de autenticación';
      }
    });
  }

  volver() {
    this.router.navigate(['/']);
  }
}
