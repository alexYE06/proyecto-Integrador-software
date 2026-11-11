USE
sistema_alertas1;
-- 1. USUARIOS ORIGEN (IDs 1 y 2)
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado)
VALUES ('admin.central', 'admin123', 'Administrador', 'Activo'),
       ('operador1', 'op123', 'Operador', 'Activo');

-- 2. USUARIOS PARA OPERADORES (IDs del 3 al 22)
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado)
VALUES ('carlos.gutierrez', 'operador123', 'Operador', 'Activo'),
       ('maria.fernandez', 'operador123', 'Operador', 'Activo'),
       ('roberto.sanchez', 'operador123', 'Operador', 'Activo'),
       ('lucia.benitez', 'operador123', 'Operador', 'Activo'),
       ('daniel.alvarez', 'operador123', 'Operador', 'Activo'),
       ('patricia.navarro', 'operador123', 'Operador', 'Activo'),
       ('fernando.torres', 'operador123', 'Operador', 'Activo'),
       ('sofia.paredes', 'operador123', 'Operador', 'Activo'),
       ('javier.salazar', 'operador123', 'Operador', 'Activo'),
       ('carmen.herrera', 'operador123', 'Operador', 'Activo'),
       ('alejandro.vargas', 'operador123', 'Operador', 'Activo'),
       ('valeria.rojas', 'operador123', 'Operador', 'Activo'),
       ('gonzalo.espinoza', 'operador123', 'Operador', 'Activo'),
       ('elena.campos', 'operador123', 'Operador', 'Activo'),
       ('raul.guerrero', 'operador123', 'Operador', 'Activo'),
       ('diana.villanueva', 'operador123', 'Operador', 'Activo'),
       ('martin.soto', 'operador123', 'Operador', 'Activo'),
       ('camila.cardenas', 'operador123', 'Operador', 'Activo'),
       ('esteban.perez', 'operador123', 'Operador', 'Activo'),
       ('fiorella.lopez', 'operador123', 'Operador', 'Activo');

-- 3. OPERADORES VINCULADOS A USUARIOS (id_usuario corregidos del 3 al 22)
INSERT INTO operador (id_usuario, nombres, apellidos, telefono, cargo, estado)
VALUES (3, 'Carlos', 'Gutiérrez Mendoza', '911223344', 'Operador de Central', 'Activo'),
       (4, 'María', 'Fernández Vega', '922334455', 'Operador de Central', 'Activo'),
       (5, 'Roberto', 'Sánchez Palacios', '933445566', 'Operador de Central', 'Activo'),
       (6, 'Lucía', 'Benítez Ríos', '944556677', 'Supervisor de Turno', 'Activo'),
       (7, 'Daniel', 'Álvarez Castro', '955667788', 'Operador de Central', 'Activo'),
       (8, 'Patricia', 'Navarro Medina', '966778899', 'Analista de Monitoreo', 'Activo'),
       (9, 'Fernando', 'Torres Guzmán', '977889900', 'Operador de Central', 'Activo'),
       (10, 'Sofía', 'Paredes Rivas', '988990011', 'Operador de Central', 'Activo'),
       (11, 'Javier', 'Salazar Reyes', '999001122', 'Supervisor de Turno', 'Activo'),
       (12, 'Carmen', 'Herrera Bustamante', '900112233', 'Operador de Central', 'Activo'),
       (13, 'Alejandro', 'Vargas Delgado', '912345670', 'Analista de Monitoreo', 'Activo'),
       (14, 'Valeria', 'Rojas Flores', '923456781', 'Operador de Central', 'Activo'),
       (15, 'Gonzalo', 'Espinoza Ramos', '934567892', 'Operador de Central', 'Activo'),
       (16, 'Elena', 'Campos Silva', '945678903', 'Supervisor de Turno', 'Activo'),
       (17, 'Raúl', 'Guerrero Morales', '956789014', 'Operador de Central', 'Activo'),
       (18, 'Diana', 'Villanueva Ríos', '967890125', 'Analista de Monitoreo', 'Activo'),
       (19, 'Martín', 'Soto Maldonado', '978901236', 'Operador de Central', 'Activo'),
       (20, 'Camila', 'Cárdenas Rivas', '989012347', 'Operador de Central', 'Activo'),
       (21, 'Esteban', 'Pérez Peralta', '990123458', 'Supervisor de Turno', 'Activo'),
       (22, 'Fiorella', 'López Córdova', '901234569', 'Operador de Central', 'Activo');

-- 4. CONDUCTORES (IDs del 1 al 20)
INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado)
VALUES ('Jorge', 'Gómez Peralta', '74123890', '912345678', 'Q74123890', 'Activo'),
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

-- 5. BUSES (IDs del 1 al 20)
INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado)
VALUES ('GHI-789', 'BUS-006', 'Mercedes-Benz', 40, 'Activo'),
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

-- 6. ALERTAS (IDs del 1 al 20)
INSERT INTO alerta (id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion)
VALUES (1, 1, 1, '2026-09-02 08:15:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Posible extorsión reportada silenciosamente en ruta'),
       (2, 2, 2, '2026-09-06 14:20:00', 'Reporte manual de central', 'Atendida',
        'Reporte de presencia de sujetos sospechosos en paradero'),
       (3, 3, 3, '2026-09-10 11:05:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Alerta silenciosa enviada por conductor por cobro de cupo'),
       (4, 4, 4, '2026-09-14 17:40:00', 'Reporte manual de central', 'Atendida',
        'Intento de amedrentamiento a unidad en ruta'),
       (5, 5, 5, '2026-09-18 09:30:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Activación discreta ante amenaza verbal de extorsionadores'),
       (6, 6, 6, '2026-09-22 19:15:00', 'Reporte manual de central', 'Atendida',
        'Alerta emitida por central tras seguimiento en cámara'),
       (7, 7, 7, '2026-09-26 07:45:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Posible extorsión detectada por botón de pánico'),
       (8, 8, 8, '2026-09-30 12:10:00', 'Reporte manual de central', 'Atendida',
        'Reporte preventivo de extorsión en tramo crítico'),
       (9, 9, 9, '2026-10-04 15:50:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Notificación de emergencia silenciosa desde cabina'),
       (10, 10, 10, '2026-10-08 10:20:00', 'Reporte manual de central', 'Atendida',
        'Sospecha de seguimiento por delincuentes a la unidad'),
       (11, 11, 11, '2026-10-12 18:05:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Alerta de extorsión activada mediante botón de encendido'),
       (12, 12, 12, '2026-10-16 06:30:00', 'Reporte manual de central', 'Atendida',
        'Aviso de cobro de cupos reportado por inspección'),
       (13, 13, 13, '2026-10-20 13:15:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Posible extorsión reportada silenciosamente por chofer'),
       (14, 14, 14, '2026-10-24 21:00:00', 'Reporte manual de central', 'Atendida',
        'Incidente de riesgo registrado y atendido por despacho'),
       (15, 15, 15, '2026-10-28 11:45:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Alerta silenciosa por intercepción de sujetos no identificados'),
       (16, 16, 16, '2026-11-01 16:30:00', 'Reporte manual de central', 'Atendida',
        'Alerta confirmada de intimidación a la flota'),
       (17, 17, 17, '2026-11-03 09:10:00', 'Tres pulsaciones botón encendido', 'Atendida',
        'Disparo de alerta discreta por amedrentamiento directo'),
       (18, 18, 18, '2026-11-05 08:30:00', 'Tres pulsaciones botón encendido', 'En Evaluación',
        'Posible extorsión en proceso reportada desde cabina'),
       (19, 19, 19, '2026-11-05 10:15:00', 'Reporte manual de central', 'En Evaluación',
        'Alerta de emergencia en evaluación por la central'),
       (20, 20, 20, '2026-11-05 11:00:00', 'Tres pulsaciones botón encendido', 'En Evaluación',
        'Activación silenciosa de botón de pánico en ruta actual');

-- 7. TELEMETRÍA GPS (IDs del 1 al 20)
INSERT INTO telemetria_gps (id_bus, id_alerta, latitud, longitud, velocidad, fecha_hora)
VALUES (1, 1, -12.0435000, -77.0920000, 34.20, '2026-09-02 08:15:05'),
       (2, 2, -12.0478000, -77.0985000, 29.50, '2026-09-06 14:20:04'),
       (3, 3, -12.0512000, -77.1030000, 31.00, '2026-09-10 11:05:05'),
       (4, 4, -12.0441000, -77.0895000, 22.80, '2026-09-14 17:40:04'),
       (5, 5, -12.0489000, -77.1012000, 35.60, '2026-09-18 09:30:05'),
       (6, 6, -12.0535000, -77.1054000, 18.40, '2026-09-22 19:15:05'),
       (7, 7, -12.0418000, -77.0872000, 40.10, '2026-09-26 07:45:04'),
       (8, 8, -12.0463000, -77.0948000, 27.30, '2026-09-30 12:10:05'),
       (9, 9, -12.0520000, -77.1021000, 33.50, '2026-10-04 15:50:04'),
       (10, 10, -12.0455000, -77.0915000, 25.00, '2026-10-08 10:20:05'),
       (11, 11, -12.0498000, -77.0990000, 30.20, '2026-10-12 18:05:04'),
       (12, 12, -12.0542000, -77.1068000, 21.70, '2026-10-16 06:30:05'),
       (13, 13, -12.0429000, -77.0888000, 36.80, '2026-10-20 13:15:04'),
       (14, 14, -12.0470000, -77.0962000, 0.00, '2026-10-24 21:00:05'),
       (15, 15, -12.0508000, -77.1018000, 28.90, '2026-10-28 11:45:04'),
       (16, 16, -12.0448000, -77.0932000, 32.10, '2026-11-01 16:30:05'),
       (17, 17, -12.0482000, -77.0975000, 24.60, '2026-11-03 09:10:04'),
       (18, 18, -12.0528000, -77.1042000, 15.30, '2026-11-05 08:30:05'),
       (19, 19, -12.0461000, -77.0908000, 33.00, '2026-11-05 10:15:04'),
       (20, 20, -12.0515000, -77.1025000, 0.00, '2026-11-05 11:00:05');

-- 8. CONSULTAS DE VERIFICACIÓN
-- Relaciona el perfil del operador con su cuenta de usuario
SELECT o.id_operador,
       u.id_usuario,
       u.nombre_usuario,
       u.contrasena,
       CONCAT(o.nombres, ' ', o.apellidos) AS operador,
       o.cargo,
       u.rol,
       o.estado
FROM operador o
         INNER JOIN usuario u ON o.id_usuario = u.id_usuario
ORDER BY u.id_usuario ASC;

-- Consulta de Alertas con Bus y Conductor
SELECT a.id_alerta,
       a.fecha_hora,
       b.codigo_unidad,
       b.placa,
       CONCAT(c.nombres, ' ', c.apellidos) AS conductor,
       a.estado
FROM alerta a
         INNER JOIN bus b ON a.id_bus = b.id_bus
         INNER JOIN conductor c ON a.id_conductor = c.id_conductor
ORDER BY a.fecha_hora DESC;

-- Consulta de Registro Telemétrico GPS
SELECT t.id_telemetria,
       t.id_bus,
       t.id_alerta,
       t.latitud,
       t.longitud,
       t.velocidad,
       t.fecha_hora
FROM telemetria_gps t
ORDER BY t.id_telemetria ASC;