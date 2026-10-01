import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificacionService, Notificacion } from '../../core/services/notificacion.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="contenedor-toasts" *ngIf="notificacionService.notificaciones().length > 0">
      <div
        *ngFor="let n of notificacionService.notificaciones()"
        class="toast-item"
        [ngClass]="'toast-' + n.tipo"
      >
        <div class="toast-icono">
          <span *ngIf="n.tipo === 'critica'">🚨</span>
          <span *ngIf="n.tipo === 'exito'">✅</span>
          <span *ngIf="n.tipo === 'advertencia'">⚠️</span>
          <span *ngIf="n.tipo === 'info'">ℹ️</span>
        </div>
        <div class="toast-contenido">
          <div class="toast-titulo">{{ n.titulo }}</div>
          <div class="toast-mensaje">{{ n.mensaje }}</div>
        </div>
        <button class="btn-cerrar" (click)="notificacionService.cerrar(n.id)">×</button>
      </div>
    </div>
  `,
  styles: [`
    .contenedor-toasts {
      position: fixed;
      top: 24px;
      right: 24px;
      z-index: 99999;
      display: flex;
      flex-direction: column;
      gap: 12px;
      max-width: 400px;
      pointer-events: none;
    }

    .toast-item {
      pointer-events: auto;
      display: flex;
      align-items: flex-start;
      gap: 12px;
      padding: 14px 16px;
      background: #0f172a;
      border-radius: 10px;
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.6), 0 0 1px 1px rgba(255, 255, 255, 0.1);
      animation: slideIn 0.3s ease-out;
      border-left: 4px solid #38bdf8;
      color: #f8fafc;
      font-family: system-ui, sans-serif;
    }

    .toast-critica {
      border-left-color: #ef4444;
      background: #180d12;
      box-shadow: 0 0 20px rgba(239, 68, 68, 0.3), 0 10px 25px -5px rgba(0, 0, 0, 0.6);
      border: 1px solid rgba(239, 68, 68, 0.4);
      border-left-width: 5px;
    }

    .toast-exito {
      border-left-color: #22c55e;
      background: #0d1a16;
      border: 1px solid rgba(34, 197, 94, 0.3);
      border-left-width: 5px;
    }

    .toast-advertencia {
      border-left-color: #f59e0b;
      background: #1c180e;
      border: 1px solid rgba(245, 158, 11, 0.3);
      border-left-width: 5px;
    }

    .toast-icono {
      font-size: 20px;
      line-height: 1;
      padding-top: 2px;
    }

    .toast-contenido {
      flex: 1;
    }

    .toast-titulo {
      font-size: 13px;
      font-weight: 700;
      margin-bottom: 2px;
      color: #ffffff;
    }

    .toast-mensaje {
      font-size: 12px;
      color: #cbd5e1;
      line-height: 1.4;
    }

    .btn-cerrar {
      background: none;
      border: none;
      color: #94a3b8;
      font-size: 18px;
      cursor: pointer;
      padding: 0;
      line-height: 1;
      margin-left: 6px;
    }

    .btn-cerrar:hover {
      color: #ffffff;
    }

    @keyframes slideIn {
      from {
        transform: translateX(100%);
        opacity: 0;
      }
      to {
        transform: translateX(0);
        opacity: 1;
      }
    }
  `]
})
export class ToastContainerComponent {
  constructor(public notificacionService: NotificacionService) {}
}
