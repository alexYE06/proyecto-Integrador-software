USE sistema_alertas;

INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES
('admin.central', 'admin123', 'Administrador', 'Activo'),
('operador1', 'op123', 'Operador', 'Activo');

INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado) VALUES
('Carlos', 'Mendoza Ruiz', '71234567', '987654321', 'L12345678', 'Activo'),
('Luis', 'Quispe Torres', '73456789', '986543210', 'L87654321', 'Activo');

INSERT INTO operador (id_usuario, nombres, apellidos, telefono, cargo, estado) VALUES
(2, 'Andrea', 'Flores Ramos', '955667788', 'Operador de Central', 'Activo');

INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado) VALUES
('ABC-123', 'BUS-001', 'Mercedes-Benz', 40, 'Activo'),
('DEF-456', 'BUS-002', 'Volvo', 45, 'Activo');

INSERT INTO comisaria (nombre, direccion, telefono, latitud, longitud) VALUES
('Comisaría Carmen de la Legua', 'Av. Principal 123', '014567890', -12.0420000, -77.0900000);

INSERT INTO asignacion_turno
(id_conductor, id_bus, fecha, hora_inicio, hora_fin, estado) VALUES
(1, 1, '2026-09-25', '08:00:00', '16:00:00', 'Activo'),
(2, 2, '2026-09-25', '09:00:00', '17:00:00', 'Activo');

INSERT INTO alerta
(id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion) VALUES
(1, 1, 1, '2026-09-25 10:15:00', 'Tres pulsaciones botón encendido', 'Recibida', 'Posible extorsión reportada silenciosamente'),
(2, 2, 1, '2026-09-25 11:30:00', 'Tres pulsaciones botón encendido', 'En Evaluación', 'Alerta silenciosa enviada por conductor');

INSERT INTO telemetria_gps
(id_bus, id_alerta, latitud, longitud, velocidad, fecha_hora) VALUES
(1, 1, -12.0450000, -77.0950000, 32.50, '2026-09-25 10:15:05'),
(2, 2, -12.0500000, -77.1000000, 28.00, '2026-09-25 11:30:04');

INSERT INTO evidencia_multimedia
(id_alerta, tipo, ruta_archivo, fecha_hora, descripcion) VALUES
(1, 'Audio', 'evidencias/audio_alerta_001.mp3', '2026-09-25 10:16:00', 'Audio capturado durante el incidente');

INSERT INTO despacho_auxilio
(id_alerta, id_comisaria, id_operador, fecha_hora, estado, observacion) VALUES
(1, 1, 1, '2026-09-25 10:20:00', 'Despachado', 'Se solicitó apoyo policial');

INSERT INTO cerco_preventivo
(id_alerta, id_comisaria, descripcion, fecha_inicio, fecha_fin, estado) VALUES
(1, 1, 'Cerco preventivo en zona cercana al incidente', '2026-09-25 10:25:00', NULL, 'Activo');

INSERT INTO notificacion
(id_alerta, id_usuario, mensaje, fecha_hora, estado) VALUES
(1, 1, 'Nueva alerta de extorsión recibida', '2026-09-25 10:15:10', 'Enviada'),
(2, 2, 'Alerta pendiente de evaluación', '2026-09-25 11:30:10', 'Enviada');