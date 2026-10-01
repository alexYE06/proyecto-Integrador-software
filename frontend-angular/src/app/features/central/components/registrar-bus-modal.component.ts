import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { Bus } from '../../../core/models/sistema.models';

@Component({
  selector: 'app-registrar-bus-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="overlay-modal" (click)="cerrarModal()">
      <div class="ventana-modal" (click)="$event.stopPropagation()">
        <header class="header-modal">
          <div class="titulo-grupo">
            <span class="icono">🚌</span>
            <div>
              <h3>Registrar Nueva Unidad de Bus</h3>
              <p>Incorporar vehículo al padrón de flota de Carmen de la Punta S.A.</p>
            </div>
          </div>
          <button class="btn-cerrar" (click)="cerrarModal()">✕</button>
        </header>

        <form (ngSubmit)="guardarBus()" class="formulario-modal">
          <div class="fila-doble">
            <div class="campo">
              <label>Código de Unidad</label>
              <input 
                type="text" 
                [(ngModel)]="nuevoBus.numeroUnidad" 
                name="numeroUnidad" 
                placeholder="Ej. BUS-075" 
                required 
              />
            </div>
            <div class="campo">
              <label>Número de Placa</label>
              <input 
                type="text" 
                [(ngModel)]="nuevoBus.placa" 
                name="placa" 
                placeholder="Ej. A8B-715" 
                required 
              />
            </div>
          </div>

          <div class="fila-doble">
            <div class="campo">
              <label>Modelo / Fabricante</label>
              <input 
                type="text" 
                [(ngModel)]="nuevoBus.modelo" 
                name="modelo" 
                placeholder="Ej. Mercedes-Benz O500" 
                required 
              />
            </div>
            <div class="campo">
              <label>Capacidad Pasajeros</label>
              <input 
                type="number" 
                min="1" 
                max="100" 
                [(ngModel)]="nuevoBus.capacidad" 
                name="capacidad" 
                required 
              />
            </div>
          </div>

          <div class="campo">
            <label>Estado Operativo</label>
            <select [(ngModel)]="nuevoBus.estado" name="estado">
              <option value="Activo">Activo (En servicio)</option>
              <option value="En Mantenimiento">En Mantenimiento</option>
              <option value="Inactivo">Inactivo / Fuera de ruta</option>
            </select>
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
              {{ guardando ? 'Guardando en MySQL...' : 'Registrar Unidad' }}
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
      z-index: 9999;
      padding: 16px;
    }

    .ventana-modal {
      background: #0f172a;
      border: 1px solid #334155;
      border-radius: 16px;
      width: 100%;
      max-width: 480px;
      box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.7);
      overflow: hidden;
      animation: aparecer 0.2s ease-out;
      color: #f8fafc;
    }

    @keyframes aparecer {
      from { opacity: 0; transform: scale(0.96); }
      to { opacity: 1; transform: scale(1); }
    }

    .header-modal {
      padding: 18px 22px;
      border-bottom: 1px solid #1e293b;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .titulo-grupo {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .icono {
      font-size: 26px;
      background: rgba(14, 165, 233, 0.15);
      padding: 6px;
      border-radius: 8px;
    }

    .header-modal h3 {
      margin: 0 0 2px 0;
      font-size: 16px;
      color: #ffffff;
      font-weight: 700;
    }

    .header-modal p {
      margin: 0;
      font-size: 11px;
      color: #64748b;
    }

    .btn-cerrar {
      background: transparent;
      border: none;
      color: #94a3b8;
      font-size: 18px;
      cursor: pointer;
      padding: 4px;
      line-height: 1;
      border-radius: 4px;
    }

    .btn-cerrar:hover {
      color: #ffffff;
      background: rgba(255, 255, 255, 0.05);
    }

    .formulario-modal {
      padding: 22px;
      display: flex;
      flex-direction: column;
      gap: 14px;
    }

    .fila-doble {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
    }

    .campo {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .campo label {
      font-size: 11px;
      font-weight: 600;
      color: #94a3b8;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .campo input, .campo select {
      background: #1e293b;
      border: 1px solid #334155;
      padding: 10px 12px;
      border-radius: 8px;
      color: #ffffff;
      font-size: 13px;
    }

    .campo input:focus, .campo select:focus {
      outline: none;
      border-color: #0284c7;
      box-shadow: 0 0 0 2px rgba(2, 132, 199, 0.2);
    }

    .mensaje-exito {
      background: rgba(34, 197, 94, 0.15);
      border: 1px solid rgba(34, 197, 94, 0.3);
      color: #4ade80;
      padding: 10px;
      border-radius: 8px;
      font-size: 12px;
      text-align: center;
    }

    .mensaje-error {
      background: rgba(239, 68, 68, 0.15);
      border: 1px solid rgba(239, 68, 68, 0.3);
      color: #f87171;
      padding: 10px;
      border-radius: 8px;
      font-size: 12px;
      text-align: center;
    }

    .footer-modal {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 10px;
      padding-top: 14px;
      border-top: 1px solid #1e293b;
    }

    .btn-cancelar {
      background: #1e293b;
      border: 1px solid #334155;
      color: #cbd5e1;
      padding: 8px 16px;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 600;
      cursor: pointer;
    }

    .btn-cancelar:hover { background: #334155; }

    .btn-guardar {
      background: #0284c7;
      border: none;
      color: #ffffff;
      padding: 8px 18px;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 700;
      cursor: pointer;
      box-shadow: 0 0 12px rgba(2, 132, 199, 0.3);
    }

    .btn-guardar:hover:not([disabled]) { background: #0369a1; }
    .btn-guardar[disabled] { opacity: 0.6; cursor: not-allowed; }
  `]
})
export class RegistrarBusModalComponent {
  @Output() cerrar = new EventEmitter<void>();
  @Output() onCerrar = new EventEmitter<void>();
  @Output() busRegistrado = new EventEmitter<any>();

  nuevoBus: Partial<Bus> = {
    numeroUnidad: '',
    placa: '',
    modelo: 'Mercedes-Benz O500',
    capacidad: 40,
    estado: 'Activo'
  };

  guardando = false;
  mensaje = '';
  error = '';

  constructor(private apiService: ApiService) {}

  guardarBus() {
    this.mensaje = '';
    this.error = '';
    this.guardando = true;

    this.apiService.registrarBus(this.nuevoBus).subscribe({
      next: (res: any) => {
        this.guardando = false;
        this.mensaje = 'Unidad de bus registrada con éxito en la flota.';
        this.busRegistrado.emit(res);
        setTimeout(() => this.cerrarModal(), 1200);
      },
      error: (e: any) => {
        this.guardando = false;
        this.error = e.error?.mensaje || e.error?.error || 'Error al guardar la unidad';
      }
    });
  }

  cerrarModal() {
    this.cerrar.emit();
    this.onCerrar.emit();
  }
}
