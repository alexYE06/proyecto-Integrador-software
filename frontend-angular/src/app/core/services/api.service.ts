import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse, Bus, Alerta, ConductorLoginResponse, Conductor, Comisaria, AsignacionTurno } from '../models/sistema.models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api';

  // Autenticación Central
  login(usuario: string, contrasena: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, {
      usuario,
      contrasena
    });
  }

  // Autenticación Conductor Móvil
  conductorLogin(dni: string, contrasena: string, idBus?: number, codigoUnidad?: string): Observable<ConductorLoginResponse> {
    return this.http.post<ConductorLoginResponse>(`${this.baseUrl}/auth/conductor-login`, {
      dni,
      contrasena,
      idBus,
      codigoUnidad
    });
  }

  // 1. TABLA CONDUCTORES
  getConductores(): Observable<Conductor[]> {
    return this.http.get<Conductor[]>(`${this.baseUrl}/conductores`);
  }

  registrarConductor(conductor: Conductor): Observable<any> {
    return this.http.post(`${this.baseUrl}/conductores`, conductor);
  }

  // 2. TABLA BUSES (FLOTA)
  getBuses(): Observable<Bus[]> {
    return this.http.get<Bus[]>(`${this.baseUrl}/buses`);
  }

  getBusPorId(id: number): Observable<Bus> {
    return this.http.get<Bus>(`${this.baseUrl}/buses/${id}`);
  }

  registrarBus(bus: Partial<Bus>): Observable<Bus> {
    return this.http.post<Bus>(`${this.baseUrl}/buses`, bus);
  }

  // 3. TABLA COMISARÍAS Y AUXILIO
  getComisarias(): Observable<Comisaria[]> {
    return this.http.get<Comisaria[]>(`${this.baseUrl}/comisarias`);
  }

  registrarComisaria(comisaria: Comisaria): Observable<Comisaria> {
    return this.http.post<Comisaria>(`${this.baseUrl}/comisarias`, comisaria);
  }

  // 4. TABLA ASIGNACIÓN DE TURNOS
  getTurnos(): Observable<AsignacionTurno[]> {
    return this.http.get<AsignacionTurno[]>(`${this.baseUrl}/turnos`);
  }

  registrarTurno(turno: AsignacionTurno): Observable<AsignacionTurno> {
    return this.http.post<AsignacionTurno>(`${this.baseUrl}/turnos`, turno);
  }

  // Alertas e incidentes
  getAlertas(): Observable<Alerta[]> {
    return this.http.get<Alerta[]>(`${this.baseUrl}/alertas`);
  }

  getAlertasPaginadas(page = 0, size = 10): Observable<import('../models/sistema.models').PaginaResponse<Alerta>> {
    return this.http.get<import('../models/sistema.models').PaginaResponse<Alerta>>(
      `${this.baseUrl}/alertas/paginado?page=${page}&size=${size}`
    );
  }

  registrarAlerta(alerta: Alerta): Observable<Alerta> {
    return this.http.post<Alerta>(`${this.baseUrl}/alertas`, alerta);
  }

  actualizarEstadoAlerta(idAlerta: number, nuevoEstado: string, idOperador?: number): Observable<any> {
    return this.http.patch(`${this.baseUrl}/alertas/${idAlerta}/estado`, {
      nuevoEstado,
      idOperador
    });
  }
}
