import { Injectable, signal } from '@angular/core';

export interface Notificacion {
  id: string;
  tipo: 'critica' | 'exito' | 'advertencia' | 'info';
  titulo: string;
  mensaje: string;
  tiempo: Date;
}

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private notificacionesSignal = signal<Notificacion[]>([]);
  public readonly notificaciones = this.notificacionesSignal.asReadonly();

  mostrar(tipo: Notificacion['tipo'], titulo: string, mensaje: string, duracionMs = 5000): void {
    const id = Date.now().toString() + Math.random().toString(36).substring(2, 5);
    const nueva: Notificacion = {
      id,
      tipo,
      titulo,
      mensaje,
      tiempo: new Date()
    };

    this.notificacionesSignal.update((lista) => [nueva, ...lista]);

    if (tipo === 'critica') {
      this.reproducirTonoAlerta();
    }

    if (duracionMs > 0) {
      setTimeout(() => {
        this.cerrar(id);
      }, duracionMs);
    }
  }

  alertaCritica(titulo: string, mensaje: string): void {
    this.mostrar('critica', titulo, mensaje, 8000);
  }

  exito(titulo: string, mensaje: string): void {
    this.mostrar('exito', titulo, mensaje, 4000);
  }

  advertencia(titulo: string, mensaje: string): void {
    this.mostrar('advertencia', titulo, mensaje, 5000);
  }

  info(titulo: string, mensaje: string): void {
    this.mostrar('info', titulo, mensaje, 4000);
  }

  cerrar(id: string): void {
    this.notificacionesSignal.update((lista) => lista.filter((n) => n.id !== id));
  }

  private reproducirTonoAlerta(): void {
    try {
      const audioCtx = new (window.AudioContext || (window as any).webkitAudioContext)();
      const osc = audioCtx.createOscillator();
      const gain = audioCtx.createGain();

      osc.type = 'sawtooth';
      osc.frequency.setValueAtTime(880, audioCtx.currentTime); // La (A5)
      osc.frequency.exponentialRampToValueAtTime(440, audioCtx.currentTime + 0.3);

      gain.gain.setValueAtTime(0.15, audioCtx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.01, audioCtx.currentTime + 0.35);

      osc.connect(gain);
      gain.connect(audioCtx.destination);

      osc.start();
      osc.stop(audioCtx.currentTime + 0.35);
    } catch {
      // Si el navegador bloquea audio sin interacción previa, se ignora silenciosamente
    }
  }
}
