USE sistema_alertas;

INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES
('admin.central', 'admin123', 'Administrador', 'Activo'),
('operador1', 'op123', 'Operador', 'Activo');

INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado) VALUES
('Carlos', 'Mendoza Ruiz', '71234567', '987654321', 'L12345678', 'Activo'),
('Luis', 'Quispe Torres', '73456789', '986543210', 'L87654321', 'Activo');
Aquí tienes la instrucción **`INSERT` con 20 registros adicionales** para la tabla `conductor`. En cada registro se ha colocado en el campo `licencia` la letra **`Q` seguida del DNI correspondiente**:

/* se agregaron mas conductoores */
INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado) VALUES
('Jorge', 'Gómez Peralta', '74123890', '912345678', 'Q74123890', 'Activo'),
('Miguel', 'Rojas Flores', '75234901', '923456789', 'Q75234901', 'Activo'),
('Juan', 'Pérez Silva', '76345012', '934567890', 'Q76345012', 'Activo'),
('José', 'Fernández Castro', '77456123', '945678901', 'Q77456123', 'Activo'),
('Pedro', 'Sánchez Romero', '78567234', '956789012', 'Q78567234', 'Activo'),
('Manuel', 'Vargas Delgado', '79678345', '967890123', 'Q79678345', 'Activo'),
('Ramiro', 'Guerrero Morales', '70789456', '978901234', 'Q70789456', 'Activo'),
('Fernando', 'Chávez Palacios', '71890567', '989012345', 'Q71890567', 'Activo'),
('Hugo', 'Salazar Gutiérrez', '72901678', '990123456', 'Q72901678', 'Activo'),
('Mario', 'Espinoza Ramos', '73012789', '901234567', 'Q73012789', 'Activo'),
('Christian', 'Aguilar Reyes', '74123891', '912345679', 'Q74123891', 'Activo'),
('Diego', 'Navarro Medina', '75234902', '923456780', 'Q75234902', 'Activo'),
('Gabriel', 'Campos Paredes', '76345013', '934567891', 'Q76345013', 'Activo'),
('Raúl', 'Vega Benítez', '77456124', '945678902', 'Q77456124', 'Activo'),
('Ángel', 'Villanueva Ríos', '78567235', '956789013', 'Q78567235', 'Activo'),
('Víctor', 'Soto Maldonado', '79678346', '967890124', 'Q79678346', 'Activo'),
('Ricardo', 'López Córdova', '70789457', '978901235', 'Q70789457', 'Activo'),
('César', 'Herrera Bustamante', '71890568', '989012346', 'Q71890568', 'Activo'),
('Gustavo', 'Cárdenas Rivas', '72901679', '990123457', 'Q72901679', 'Activo'),
('Javier', 'Paredes Mendoza', '73012790', '901234568', 'Q73012790', 'Activo');

INSERT INTO operador (id_usuario, nombres, apellidos, telefono, cargo, estado) VALUES
(2, 'Andrea', 'Flores Ramos', '955667788', 'Operador de Central', 'Activo');



INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado) VALUES
('ABC-123', 'BUS-001', 'Mercedes-Benz', 40, 'Activo'),
('DEF-456', 'BUS-002', 'Volvo', 45, 'Activo');

/* se agregaron mas buses */
INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado) VALUES
('GHI-789', 'BUS-006', 'Mercedes-Benz', 40, 'Activo'),
('JKL-012', 'BUS-007', 'Volvo', 45, 'Activo'),
('MNO-345', 'BUS-008', 'Scania', 50, 'Activo'),
('PQR-678', 'BUS-009', 'Hyundai', 35, 'Activo'),
('STU-901', 'BUS-010', 'Volkswagen', 40, 'Activo'),
('VWX-234', 'BUS-011', 'Mercedes-Benz', 42, 'Activo'),
('YZA-567', 'BUS-012', 'Mitsubishi', 38, 'Activo'),
('BCD-890', 'BUS-013', 'Volvo', 45, 'Activo'),
('EFG-123', 'BUS-014', 'Scania', 50, 'Activo'),
('HIJ-456', 'BUS-015', 'Hyundai', 35, 'Activo'),
('KLM-789', 'BUS-016', 'Mercedes-Benz', 40, 'Activo'),
('NOP-012', 'BUS-017', 'Volkswagen', 42, 'Activo'),
('QRS-345', 'BUS-018', 'Volvo', 45, 'Activo'),
('TUV-678', 'BUS-019', 'Scania', 48, 'Activo'),
('WXY-901', 'BUS-020', 'Mitsubishi Fuso', 36, 'Activo'),
('ZAB-234', 'BUS-021', 'Mercedes-Benz', 40, 'Activo'),
('CDE-567', 'BUS-022', 'Hyundai', 38, 'Activo'),
('FGH-890', 'BUS-023', 'Volvo', 45, 'Activo'),
('IJK-123', 'BUS-024', 'Scania', 50, 'Activo'),
('LMN-456', 'BUS-025', 'Mitsubishi', 40, 'Activo');

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

/* se agregaron mas alertas */
INSERT INTO alerta
(id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion) VALUES
(1, 1, 1, '2026-09-02 08:15:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Posible extorsión reportada silenciosamente en ruta'),
(2, 2, 1, '2026-09-06 14:20:00', 'Reporte manual de central', 'Atendida', 'Reporte de presencia de sujetos sospechosos en paradero'),
(52, 52, 1, '2026-09-10 11:05:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Alerta silenciosa enviada por conductor por cobro de cupo'),
(53, 53, 1, '2026-09-14 17:40:00', 'Reporte manual de central', 'Atendida', 'Intento de amedrentamiento a unidad en ruta'),
(54, 54, 1, '2026-09-18 09:30:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Activación discreta ante amenaza verbal de extorsionadores'),
(55, 56, 1, '2026-09-22 19:15:00', 'Reporte manual de central', 'Atendida', 'Alerta emitida por central tras seguimiento en cámara'),
(56, 57, 1, '2026-09-26 07:45:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Posible extorsión detectada por botón de pánico'),
(57, 58, 1, '2026-09-30 12:10:00', 'Reporte manual de central', 'Atendida', 'Reporte preventivo de extorsión en tramo crítico'),
(58, 59, 1, '2026-10-04 15:50:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Notificación de emergencia silenciosa desde cabina'),
(59, 60, 1, '2026-10-08 10:20:00', 'Reporte manual de central', 'Atendida', 'Sospecha de seguimiento por delincuentes a la unidad'),
(60, 61, 1, '2026-10-12 18:05:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Alerta de extorsión activada mediante botón de encendido'),
(61, 62, 1, '2026-10-16 06:30:00', 'Reporte manual de central', 'Atendida', 'Aviso de cobro de cupos reportado por inspección'),
(62, 63, 1, '2026-10-20 13:15:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Posible extorsión reportada silenciosamente por chofer'),
(63, 64, 1, '2026-10-24 21:00:00', 'Reporte manual de central', 'Atendida', 'Incidente de riesgo registrado y atendido por despacho'),
(64, 65, 1, '2026-10-28 11:45:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Alerta silenciosa por intercepción de sujetos no identificados'),
(65, 66, 1, '2026-11-01 16:30:00', 'Reporte manual de central', 'Atendida', 'Alerta confirmada de intimidación a la flota'),
(66, 67, 1, '2026-11-03 09:10:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Disparo de alerta discreta por amedrentamiento directo'),
(67, 68, 1, '2026-11-05 08:30:00', 'Tres pulsaciones botón encendido', 'En Evaluación', 'Posible extorsión en proceso reportada desde cabina'),
(68, 69, 1, '2026-11-05 10:15:00', 'Reporte manual de central', 'En Evaluación', 'Alerta de emergencia en evaluación por la central'),
(69, 70, 1, '2026-11-05 11:00:00', 'Tres pulsaciones botón encendido', 'En Evaluación', 'Activación silenciosa de botón de pánico en ruta actual');


SELECT
    a.id_alerta,
    a.fecha_hora,
    b.codigo_unidad,
    b.placa,
    CONCAT(c.nombres, ' ', c.apellidos) AS conductor,
    a.estado
FROM alerta a
INNER JOIN bus b ON a.id_bus = b.id_bus
INNER JOIN conductor c ON a.id_conductor = c.id_conductor
ORDER BY a.fecha_hora DESC;



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