export interface Usuario {
  idUsuario: number;
  nombreUsuario: string;
  rol: string;
  estado: string;
  mensaje?: string;
}

export interface Bus {
  idBus: number;
  placa: string;
  numeroUnidad: string;
  modelo: string;
  capacidad: number;
  estado: string;
}

export interface Alerta {
  idAlerta?: number;
  idConductor: number;
  idBus: number;
  idOperador?: number;
  fechaHora?: string;
  tipoActivacion: string;
  estado: string;
  descripcion?: string;
}

export interface LoginResponse {
  idUsuario: number;
  nombreUsuario: string;
  rol: string;
  estado: string;
  mensaje: string;
}

export interface Conductor {
  idConductor?: number;
  nombres: string;
  apellidos: string;
  dni: string;
  telefono: string;
  licencia: string;
  estado?: string;
}

export interface PaginaResponse<T> {
  contenido: T[];
  paginaActual: number;
  totalPaginas: number;
  totalElementos: number;
  tamanioPagina: number;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  mensaje: string;
  ruta: string;
  errores?: Record<string, string>;
}

export interface CambioEstadoEvent {
  idAlerta: number;
  nuevoEstado: string;
}

export interface ConductorLoginRequest {
  dni: string;
  contrasena: string;
  idBus?: number;
  codigoUnidad?: string;
}

export interface ConductorLoginResponse {
  idConductor: number;
  nombres: string;
  apellidos: string;
  dni: string;
  licencia: string;
  idBus: number;
  codigoUnidad: string;
  placa: string;
  mensaje: string;
}

export interface Comisaria {
  idComisaria?: number;
  nombre: string;
  direccion: string;
  telefono: string;
  latitud?: number;
  longitud?: number;
}

export interface AsignacionTurno {
  idAsignacion?: number;
  idConductor: number;
  idBus: number;
  fecha: string;
  horaInicio: string;
  horaFin?: string;
  estado?: string;
  nombreConductor?: string;
  dniConductor?: string;
  codigoUnidad?: string;
  placaBus?: string;
}


