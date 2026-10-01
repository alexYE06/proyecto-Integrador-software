import { Injectable, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginResponse } from '../models/sistema.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'sat_auth_user';
  private router = inject(Router);
  private currentUserSignal = signal<LoginResponse | null>(this.obtenerSesionGuardada());

  // Signal reactivo para consumir el usuario en cualquier componente
  readonly currentUser = this.currentUserSignal.asReadonly();

  guardarSesion(usuario: LoginResponse): void {
    localStorage.setItem(this.TOKEN_KEY, JSON.stringify(usuario));
    this.currentUserSignal.set(usuario);
  }

  cerrarSesion(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.currentUserSignal.set(null);
    this.router.navigate(['/login-central']);
  }

  estaAutenticado(): boolean {
    return this.currentUserSignal() !== null;
  }

  obtenerUsuario(): LoginResponse | null {
    return this.currentUserSignal();
  }

  private obtenerSesionGuardada(): LoginResponse | null {
    const raw = localStorage.getItem(this.TOKEN_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as LoginResponse;
    } catch {
      localStorage.removeItem(this.TOKEN_KEY);
      return null;
    }
  }
}
