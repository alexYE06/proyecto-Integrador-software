import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { Conductor } from '../../../core/models/sistema.models';

@Component({
  selector: 'app-registrar-conductor-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="overlay-modal" (click)="cerrarModal()">
      <div class="ventana-modal" (click)="$event.stopPropagation()">
        <header class="header-modal">
          <div class="titulo-grupo">
            <span class="icono">👤</span>
            <div>
              <h3>Registrar Nuevo Conductor</h3>
              <p>Dar de alta a personal de conducción en flota Carmen de la Punta</p>
            </div>
          </div>
          <button class="btn-cerrar" (click)="cerrarModal()">✕</button>
        </header>

        <form (ngSubmit)="guardarConductor()" class="formulario-modal">
          <div class="fila-doble">
            <div class="campo">
              <label>Nombres</label>
              <input type="text" [(ngModel)]="nuevoConductor.nombres" name="nombres" placeholder="Ej. Carlos Alberto" required />
            </div>
            <div class="campo">
              <label>Apellidos</label>
              <input type="text" [(ngModel)]="nuevoConductor.apellidos" name="apellidos" placeholder="Ej. Mendoza Ruiz" required />
            </div>
          </div>

          <div class="fila-doble">
            <div class="campo">
              <label>DNI (8 dígitos)</label>
              <input type="text" maxlength="8" [(ngModel)]="nuevoConductor.dni" name="dni" placeholder="71234567" required />
            </div>
            <div class="campo">
              <label>Teléfono móvil</label>
              <input type="text" maxlength="9" [(ngModel)]="nuevoConductor.telefono" name="telefono" placeholder="987654321" required />
            </div>
          </div>

          <div class="campo">
            <label>Nro. Licencia de Conducir (Brevete)</label>
            <input type="text" [(ngModel)]="nuevoConductor.licencia" name="licencia" placeholder="Q71234567" required />
          </div>

          <div *ngIf="mensaje" class="mensaje-exito">
            ✓ {{ mensaje }}
          </div>

          <div *ngIf="error" class="mensaje-error">
            ⚠️ {{ error }}
          </div>

          <footer class="footer-modal">
            <button type="button" class="btn-cancelar" (click)="cerrarModal()">Cancelar</button>
            <button type="submit" class="btn-guardar" [disabled]="guardando">
              {{ guardando ? 'Guardando en MySQL...' : 'Guardar Conductor' }}
            </button>
          </footer>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .overlay-modal {
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.75);
      backdrop-filter: blur(4px);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 1000;
      padding: 15px;
    }

    .ventana-modal {
      width: 100%;
      max-width: 480px;
      background: #0f172a;
      border: 1px solid #334155;
      border-radius: 16px;
      box-shadow: 0 25px 50px -12px rgba(0,0,0,0.8);
      overflow: hidden;
      color: #f8fafc;
    }

    .header-modal {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 18px 22px;
      background: #1e293b;
      border-bottom: 1px solid #334155;
    }

    .titulo-grupo {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .icono {
      font-size: 24px;
      background: #334155;
      padding: 6px;
      border-radius: 8px;
    }

    h3 {
      margin: 0;
      font-size: 15px;
      color: #ffffff;
    }

    p {
      margin: 2px 0 0 0;
      font-size: 11px;
      color: #94a3b8;
    }

    .btn-cerrar {
      background: transparent;
      border: none;
      color: #94a3b8;
      font-size: 18px;
      cursor: pointer;
    }

    .btn-cerrar:hover {
      color: #ffffff;
    }

    .formulario-modal {
      padding: 22px;
    }

    .fila-doble {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 14px;
    }

    .campo {
      margin-bottom: 16px;
    }

    label {
      display: block;
      font-size: 11px;
      font-weight: 700;
      color: #94a3b8;
      margin-bottom: 6px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    input {
      width: 100%;
      padding: 10px 12px;
      background: #1e293b;
      border: 1px solid #334155;
      border-radius: 8px;
      color: #ffffff;
      font-size: 13px;
      box-sizing: border-box;
      outline: none;
      transition: border-color 0.2s;
    }

    input:focus {
      border-color: #10b981;
    }

    .mensaje-exito {
      background: rgba(16, 185, 129, 0.15);
      border: 1px solid rgba(16, 185, 129, 0.3);
      color: #34d399;
      padding: 8px 12px;
      border-radius: 6px;
      font-size: 12px;
      margin-bottom: 15px;
    }

    .mensaje-error {
      background: rgba(239, 68, 68, 0.15);
      border: 1px solid rgba(239, 68, 68, 0.3);
      color: #f87171;
      padding: 8px 12px;
      border-radius: 6px;
      font-size: 12px;
      margin-bottom: 15px;
    }

    .footer-modal {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 10px;
    }

    .btn-cancelar {
      padding: 9px 16px;
      background: transparent;
      border: 1px solid #475569;
      color: #cbd5e1;
      border-radius: 8px;
      font-size: 13px;
      cursor: pointer;
    }

    .btn-guardar {
      padding: 9px 18px;
      background: #10b981;
      border: none;
      color: #ffffff;
      font-weight: 600;
      border-radius: 8px;
      font-size: 13px;
      cursor: pointer;
    }

    .btn-guardar:hover:not(:disabled) {
      background: #059669;
    }

    .btn-guardar:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  `]
})
export class RegistrarConductorModalComponent {
  @Output() cerrar = new EventEmitter<void>();
  @Output() onCerrar = new EventEmitter<void>();
  @Output() conductorRegistrado = new EventEmitter<any>();

  nuevoConductor: Conductor = {
    nombres: '',
    apellidos: '',
    dni: '',
    telefono: '',
    licencia: '',
    estado: 'Activo'
  };

  guardando = false;
  mensaje = '';
  error = '';

  constructor(private apiService: ApiService) {}

  guardarConductor() {
    this.mensaje = '';
    this.error = '';
    this.guardando = true;

    this.apiService.registrarConductor(this.nuevoConductor).subscribe({
      next: (res: any) => {
        this.guardando = false;
        this.mensaje = res.mensaje || 'Conductor registrado con éxito.';
        this.conductorRegistrado.emit(res);
        setTimeout(() => this.cerrarModal(), 1200);
      },
      error: (e: any) => {
        this.guardando = false;
        this.error = e.error?.mensaje || e.error?.error || 'Error al guardar el conductor';
      }
    });
  }

  cerrarModal() {
    this.cerrar.emit();
    this.onCerrar.emit();
  }
}
