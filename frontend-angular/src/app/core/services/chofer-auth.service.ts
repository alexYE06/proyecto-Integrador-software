import { Injectable, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ConductorLoginResponse } from '../models/sistema.models';

@Injectable({
  providedIn: 'root'
})
export class ChoferAuthService {
  private readonly STORAGE_KEY = 'sat_chofer_session';
  private router = inject(Router);
  private currentChoferSignal = signal<ConductorLoginResponse | null>(this.obtenerSesionGuardada());

  readonly currentChofer = this.currentChoferSignal.asReadonly();

  guardarSesion(conductor: ConductorLoginResponse): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(conductor));
    this.currentChoferSignal.set(conductor);
  }

  cerrarSesion(): void {
    localStorage.removeItem(this.STORAGE_KEY);
    this.currentChoferSignal.set(null);
    this.router.navigate(['/login-chofer']);
  }

  estaAutenticado(): boolean {
    return this.currentChoferSignal() !== null;
  }

  obtenerSesion(): ConductorLoginResponse | null {
    return this.currentChoferSignal();
  }

  private obtenerSesionGuardada(): ConductorLoginResponse | null {
    const raw = localStorage.getItem(this.STORAGE_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as ConductorLoginResponse;
    } catch {
      localStorage.removeItem(this.STORAGE_KEY);
      return null;
    }
  }
}
