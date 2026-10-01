import { Injectable, NgZone } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Alerta, CambioEstadoEvent } from '../models/sistema.models';

@Injectable({
  providedIn: 'root'
})
export class SseService {
  private readonly streamUrl = 'http://localhost:8080/api/alertas/stream';
  private eventSource: EventSource | null = null;

  private alertaSubject = new Subject<Alerta>();
  private cambioEstadoSubject = new Subject<CambioEstadoEvent>();
  private conectadoSubject = new Subject<boolean>();

  public readonly alertaRecibida$ = this.alertaSubject.asObservable();
  public readonly cambioEstado$ = this.cambioEstadoSubject.asObservable();
  public readonly conectado$ = this.conectadoSubject.asObservable();

  constructor(private zone: NgZone) {
    this.conectar();
  }

  conectar(): void {
    if (this.eventSource) {
      this.eventSource.close();
    }

    try {
      this.eventSource = new EventSource(this.streamUrl);

      this.eventSource.addEventListener('CONEXION_ESTABLECIDA', (event: MessageEvent) => {
        this.zone.run(() => {
          console.log('[SSE SAT-Carmen] Conexión establecida con la central:', event.data);
          this.conectadoSubject.next(true);
        });
      });

      this.eventSource.addEventListener('NUEVA_ALERTA', (event: MessageEvent) => {
        this.zone.run(() => {
          try {
            const alerta: Alerta = JSON.parse(event.data);
            console.warn('[SSE SAT-Carmen] ¡NUEVA ALERTA RECIBIDA EN TIEMPO REAL!', alerta);
            this.alertaSubject.next(alerta);
          } catch (e) {
            console.error('Error parseando alerta SSE:', e);
          }
        });
      });

      this.eventSource.addEventListener('CAMBIO_ESTADO', (event: MessageEvent) => {
        this.zone.run(() => {
          try {
            const data: CambioEstadoEvent = JSON.parse(event.data);
            console.log('[SSE SAT-Carmen] Cambio de estado de alerta en tiempo real:', data);
            this.cambioEstadoSubject.next(data);
          } catch (e) {
            console.error('Error parseando cambio de estado SSE:', e);
          }
        });
      });

      this.eventSource.onerror = (err) => {
        this.zone.run(() => {
          console.warn('[SSE SAT-Carmen] Reconectando canal en tiempo real...');
          this.conectadoSubject.next(false);
        });
      };
    } catch (err) {
      console.error('Error al inicializar EventSource:', err);
    }
  }

  desconectar(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      this.conectadoSubject.next(false);
    }
  }
}
