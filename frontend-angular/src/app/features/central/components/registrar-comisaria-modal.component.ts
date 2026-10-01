import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { Comisaria } from '../../../core/models/sistema.models';

@Component({
  selector: 'app-registrar-comisaria-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="overlay-modal" (click)="cerrarModal()">
      <div class="ventana-modal" (click)="$event.stopPropagation()">
        <header class="header-modal">
          <div class="titulo-grupo">
            <span class="icono">🚓</span>
            <div>
              <h3>Registrar Dependencia Policial</h3>
              <p>Punto de enlace y coordinación para auxilio rápido en ruta</p>
            </div>
          </div>
          <button class="btn-cerrar" (click)="cerrarModal()">✕</button>
        </header>

        <form (ngSubmit)="guardarComisaria()" class="formulario-modal">
          <div class="campo">
            <label>Nombre de la Comisaría / Base Policial</label>
            <input 
              type="text" 
              [(ngModel)]="nuevaComisaria.nombre" 
              name="nombre" 
              placeholder="Ej. Comisaría PNP Bellavista" 
              required 
            />
          </div>

          <div class="campo">
            <label>Dirección / Jurisdicción</label>
            <input 
              type="text" 
              [(ngModel)]="nuevaComisaria.direccion" 
              name="direccion" 
              placeholder="Ej. Av. Óscar R. Benavides 2800, Callao" 
              required 
            />
          </div>

          <div class="campo">
            <label>Teléfono de Emergencia / Central 105</label>
            <input 
              type="text" 
              [(ngModel)]="nuevaComisaria.telefono" 
              name="telefono" 
              placeholder="Ej. 01-4512345" 
              required 
            />
          </div>

          <div class="fila-doble">
            <div class="campo">
              <label>Latitud GPS</label>
              <input 
                type="number" 
                step="any"
                [(ngModel)]="nuevaComisaria.latitud" 
                name="latitud" 
                placeholder="-12.0420000" 
              />
            </div>
            <div class="campo">
              <label>Longitud GPS</label>
              <input 
                type="number" 
                step="any"
                [(ngModel)]="nuevaComisaria.longitud" 
                name="longitud" 
                placeholder="-77.0900000" 
              />
            </div>
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
              {{ guardando ? 'Guardando en MySQL...' : 'Guardar Comisaría' }}
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
      background: rgba(239, 68, 68, 0.15);
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

    .campo input {
      background: #1e293b;
      border: 1px solid #334155;
      padding: 10px 12px;
      border-radius: 8px;
      color: #ffffff;
      font-size: 13px;
    }

    .campo input:focus {
      outline: none;
      border-color: #ef4444;
      box-shadow: 0 0 0 2px rgba(239, 68, 68, 0.2);
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
      background: #ef4444;
      border: none;
      color: #ffffff;
      padding: 8px 18px;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 700;
      cursor: pointer;
      box-shadow: 0 0 12px rgba(239, 68, 68, 0.3);
    }

    .btn-guardar:hover:not([disabled]) { background: #dc2626; }
    .btn-guardar[disabled] { opacity: 0.6; cursor: not-allowed; }
  `]
})
export class RegistrarComisariaModalComponent {
  @Output() cerrar = new EventEmitter<void>();
  @Output() onCerrar = new EventEmitter<void>();
  @Output() comisariaRegistrada = new EventEmitter<any>();

  nuevaComisaria: Comisaria = {
    nombre: '',
    direccion: '',
    telefono: '',
    latitud: -12.0420000,
    longitud: -77.0900000
  };

  guardando = false;
  mensaje = '';
  error = '';

  constructor(private apiService: ApiService) {}

  guardarComisaria() {
    this.mensaje = '';
    this.error = '';
    this.guardando = true;

    this.apiService.registrarComisaria(this.nuevaComisaria).subscribe({
      next: (res: any) => {
        this.guardando = false;
        this.mensaje = 'Comisaría registrada con éxito en el directorio de auxilio.';
        this.comisariaRegistrada.emit(res);
        setTimeout(() => this.cerrarModal(), 1200);
      },
      error: (e: any) => {
        this.guardando = false;
        this.error = e.error?.mensaje || e.error?.error || 'Error al guardar la comisaría';
      }
    });
  }

  cerrarModal() {
    this.cerrar.emit();
    this.onCerrar.emit();
  }
}
