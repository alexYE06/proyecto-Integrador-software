-- DATOS TOTALMENTE FICTICIOS GENERADOS PARA REGRESION. No proceden de personas ni vehículos reales.
-- Solo base nueva; no usar --force. Sin DROP, DELETE ni TRUNCATE.
CREATE DATABASE sat_carmen_integracion_prueba CHARACTER SET utf8mb4;
USE sat_carmen_integracion_prueba;
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS conductor (
    id_conductor INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    licencia VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS operador (
    id_operador INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    cargo VARCHAR(50),
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);
CREATE TABLE IF NOT EXISTS bus (
    id_bus INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    codigo_unidad VARCHAR(20) NOT NULL UNIQUE,
    modelo VARCHAR(50),
    capacidad INT,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS comisaria (
    id_comisaria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(15),
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7)
);
CREATE TABLE IF NOT EXISTS asignacion_turno (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_conductor INT NOT NULL,
    id_bus INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_conductor) REFERENCES conductor(id_conductor),
    FOREIGN KEY (id_bus) REFERENCES bus(id_bus)
);
CREATE TABLE IF NOT EXISTS alerta (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    id_conductor INT NOT NULL,
    id_bus INT NOT NULL,
    id_operador INT,
    fecha_hora DATETIME NOT NULL,
    tipo_activacion VARCHAR(50) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_conductor) REFERENCES conductor(id_conductor),
    FOREIGN KEY (id_bus) REFERENCES bus(id_bus),
    FOREIGN KEY (id_operador) REFERENCES operador(id_operador)
);
CREATE TABLE IF NOT EXISTS telemetria_gps (
    id_telemetria INT AUTO_INCREMENT PRIMARY KEY,
    id_bus INT NOT NULL,
    id_alerta INT,
    latitud DECIMAL(10,7) NOT NULL,
    longitud DECIMAL(10,7) NOT NULL,
    velocidad DECIMAL(6,2),
    fecha_hora DATETIME NOT NULL,
    FOREIGN KEY (id_bus) REFERENCES bus(id_bus),
    FOREIGN KEY (id_alerta) REFERENCES alerta(id_alerta)
);
CREATE TABLE IF NOT EXISTS evidencia_multimedia (
    id_evidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_alerta INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    ruta_archivo VARCHAR(255) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_alerta) REFERENCES alerta(id_alerta)
);
CREATE TABLE IF NOT EXISTS despacho_auxilio (
    id_despacho INT AUTO_INCREMENT PRIMARY KEY,
    id_alerta INT NOT NULL,
    id_comisaria INT NOT NULL,
    id_operador INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(30) NOT NULL,
    observacion VARCHAR(255),
    FOREIGN KEY (id_alerta) REFERENCES alerta(id_alerta),
    FOREIGN KEY (id_comisaria) REFERENCES comisaria(id_comisaria),
    FOREIGN KEY (id_operador) REFERENCES operador(id_operador)
);
CREATE TABLE IF NOT EXISTS cerco_preventivo (
    id_cerco INT AUTO_INCREMENT PRIMARY KEY,
    id_alerta INT NOT NULL,
    id_comisaria INT NOT NULL,
    descripcion VARCHAR(255),
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME,
    estado VARCHAR(30) NOT NULL,
    FOREIGN KEY (id_alerta) REFERENCES alerta(id_alerta),
    FOREIGN KEY (id_comisaria) REFERENCES comisaria(id_comisaria)
);
CREATE TABLE IF NOT EXISTS notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_alerta INT NOT NULL,
    id_usuario INT NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_alerta) REFERENCES alerta(id_alerta),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);
SHOW TABLES;


INSERT INTO usuario VALUES (1, 'demo_usuario_001', 'SOLO_DEMO_001', 'Operador', 'Activo');
INSERT INTO usuario VALUES (2, 'demo_usuario_002', 'SOLO_DEMO_002', 'Operador', 'Activo');
INSERT INTO usuario VALUES (3, 'demo_usuario_003', 'SOLO_DEMO_003', 'Operador', 'Activo');
INSERT INTO usuario VALUES (4, 'demo_usuario_004', 'SOLO_DEMO_004', 'Operador', 'Activo');
INSERT INTO usuario VALUES (5, 'demo_usuario_005', 'SOLO_DEMO_005', 'Operador', 'Activo');
INSERT INTO usuario VALUES (6, 'demo_usuario_006', 'SOLO_DEMO_006', 'Operador', 'Activo');
INSERT INTO usuario VALUES (7, 'demo_usuario_007', 'SOLO_DEMO_007', 'Operador', 'Activo');
INSERT INTO usuario VALUES (8, 'demo_usuario_008', 'SOLO_DEMO_008', 'Operador', 'Activo');
INSERT INTO usuario VALUES (9, 'demo_usuario_009', 'SOLO_DEMO_009', 'Operador', 'Activo');
INSERT INTO usuario VALUES (10, 'demo_usuario_010', 'SOLO_DEMO_010', 'Operador', 'Activo');
INSERT INTO usuario VALUES (11, 'demo_usuario_011', 'SOLO_DEMO_011', 'Operador', 'Activo');
INSERT INTO usuario VALUES (12, 'demo_usuario_012', 'SOLO_DEMO_012', 'Operador', 'Activo');
INSERT INTO usuario VALUES (13, 'demo_usuario_013', 'SOLO_DEMO_013', 'Operador', 'Activo');
INSERT INTO usuario VALUES (14, 'demo_usuario_014', 'SOLO_DEMO_014', 'Operador', 'Activo');
INSERT INTO usuario VALUES (15, 'demo_usuario_015', 'SOLO_DEMO_015', 'Operador', 'Activo');
INSERT INTO usuario VALUES (16, 'demo_usuario_016', 'SOLO_DEMO_016', 'Operador', 'Activo');
INSERT INTO usuario VALUES (17, 'demo_usuario_017', 'SOLO_DEMO_017', 'Operador', 'Activo');
INSERT INTO usuario VALUES (18, 'demo_usuario_018', 'SOLO_DEMO_018', 'Operador', 'Activo');
INSERT INTO usuario VALUES (19, 'demo_usuario_019', 'SOLO_DEMO_019', 'Operador', 'Activo');
INSERT INTO usuario VALUES (20, 'demo_usuario_020', 'SOLO_DEMO_020', 'Operador', 'Activo');
INSERT INTO usuario VALUES (21, 'demo_usuario_021', 'SOLO_DEMO_021', 'Operador', 'Activo');
INSERT INTO usuario VALUES (22, 'demo_usuario_022', 'SOLO_DEMO_022', 'Operador', 'Activo');
INSERT INTO usuario VALUES (23, 'demo_usuario_023', 'SOLO_DEMO_023', 'Operador', 'Activo');
INSERT INTO usuario VALUES (24, 'demo_usuario_024', 'SOLO_DEMO_024', 'Operador', 'Activo');
INSERT INTO usuario VALUES (25, 'demo_usuario_025', 'SOLO_DEMO_025', 'Operador', 'Activo');
INSERT INTO usuario VALUES (26, 'demo_usuario_026', 'SOLO_DEMO_026', 'Operador', 'Activo');
INSERT INTO usuario VALUES (27, 'demo_usuario_027', 'SOLO_DEMO_027', 'Operador', 'Activo');
INSERT INTO usuario VALUES (28, 'demo_usuario_028', 'SOLO_DEMO_028', 'Operador', 'Activo');
INSERT INTO usuario VALUES (29, 'demo_usuario_029', 'SOLO_DEMO_029', 'Operador', 'Activo');
INSERT INTO usuario VALUES (30, 'demo_usuario_030', 'SOLO_DEMO_030', 'Operador', 'Activo');
INSERT INTO usuario VALUES (31, 'demo_usuario_031', 'SOLO_DEMO_031', 'Operador', 'Activo');
INSERT INTO usuario VALUES (32, 'demo_usuario_032', 'SOLO_DEMO_032', 'Operador', 'Activo');
INSERT INTO usuario VALUES (33, 'demo_usuario_033', 'SOLO_DEMO_033', 'Operador', 'Activo');
INSERT INTO usuario VALUES (34, 'demo_usuario_034', 'SOLO_DEMO_034', 'Operador', 'Activo');
INSERT INTO usuario VALUES (35, 'demo_usuario_035', 'SOLO_DEMO_035', 'Operador', 'Activo');
INSERT INTO usuario VALUES (36, 'demo_usuario_036', 'SOLO_DEMO_036', 'Operador', 'Activo');
INSERT INTO usuario VALUES (37, 'demo_usuario_037', 'SOLO_DEMO_037', 'Operador', 'Activo');
INSERT INTO usuario VALUES (38, 'demo_usuario_038', 'SOLO_DEMO_038', 'Operador', 'Activo');
INSERT INTO usuario VALUES (39, 'demo_usuario_039', 'SOLO_DEMO_039', 'Operador', 'Activo');
INSERT INTO usuario VALUES (40, 'demo_usuario_040', 'SOLO_DEMO_040', 'Operador', 'Activo');
INSERT INTO usuario VALUES (41, 'demo_usuario_041', 'SOLO_DEMO_041', 'Operador', 'Activo');
INSERT INTO usuario VALUES (42, 'demo_usuario_042', 'SOLO_DEMO_042', 'Operador', 'Activo');
INSERT INTO usuario VALUES (43, 'demo_usuario_043', 'SOLO_DEMO_043', 'Operador', 'Activo');
INSERT INTO usuario VALUES (44, 'demo_usuario_044', 'SOLO_DEMO_044', 'Operador', 'Activo');
INSERT INTO usuario VALUES (45, 'demo_usuario_045', 'SOLO_DEMO_045', 'Operador', 'Activo');
INSERT INTO usuario VALUES (46, 'demo_usuario_046', 'SOLO_DEMO_046', 'Operador', 'Activo');
INSERT INTO usuario VALUES (47, 'demo_usuario_047', 'SOLO_DEMO_047', 'Operador', 'Activo');
INSERT INTO usuario VALUES (48, 'demo_usuario_048', 'SOLO_DEMO_048', 'Operador', 'Activo');
INSERT INTO usuario VALUES (49, 'demo_usuario_049', 'SOLO_DEMO_049', 'Operador', 'Activo');
INSERT INTO usuario VALUES (50, 'demo_usuario_050', 'SOLO_DEMO_050', 'Operador', 'Activo');
INSERT INTO usuario VALUES (51, 'demo_usuario_051', 'SOLO_DEMO_051', 'Operador', 'Activo');
INSERT INTO usuario VALUES (52, 'demo_usuario_052', 'SOLO_DEMO_052', 'Operador', 'Activo');
INSERT INTO conductor VALUES (1, 'Conductor demo', 'Ficticio 001', '99000001', '000000000', 'DEMO-LIC-001', 'Activo');
INSERT INTO operador VALUES (1, 1, 'Operador demo', 'Ficticio 001', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (1, 'TST-001', 'DEMO-001', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (1, 'Comisaría ficticia 001', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (1, 1, 1, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (1, 1, 1, 1, '2026-11-06 01:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (1, 1, 1, 0, 0, 0, '2026-11-06 01:00:00');
INSERT INTO evidencia_multimedia VALUES (1, 1, 'simulada', 'demo/no-existe-1.txt', '2026-11-06 01:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (1, 1, 1, 1, '2026-11-06 01:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (1, 1, 1, 'Simulación académica', '2026-11-06 01:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (1, 1, 1, 'Mensaje ficticio no enviado', '2026-11-06 01:00:00', 'Simulado');
INSERT INTO conductor VALUES (2, 'Conductor demo', 'Ficticio 002', '99000002', '000000000', 'DEMO-LIC-002', 'Activo');
INSERT INTO operador VALUES (2, 2, 'Operador demo', 'Ficticio 002', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (2, 'TST-002', 'DEMO-002', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (2, 'Comisaría ficticia 002', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (2, 2, 2, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (2, 2, 2, 2, '2026-11-06 02:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (2, 2, 2, 0, 0, 0, '2026-11-06 02:00:00');
INSERT INTO evidencia_multimedia VALUES (2, 2, 'simulada', 'demo/no-existe-2.txt', '2026-11-06 02:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (2, 2, 2, 2, '2026-11-06 02:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (2, 2, 2, 'Simulación académica', '2026-11-06 02:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (2, 2, 2, 'Mensaje ficticio no enviado', '2026-11-06 02:00:00', 'Simulado');
INSERT INTO conductor VALUES (3, 'Conductor demo', 'Ficticio 003', '99000003', '000000000', 'DEMO-LIC-003', 'Activo');
INSERT INTO operador VALUES (3, 3, 'Operador demo', 'Ficticio 003', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (3, 'TST-003', 'DEMO-003', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (3, 'Comisaría ficticia 003', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (3, 3, 3, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (3, 3, 3, 3, '2026-11-06 03:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (3, 3, 3, 0, 0, 0, '2026-11-06 03:00:00');
INSERT INTO evidencia_multimedia VALUES (3, 3, 'simulada', 'demo/no-existe-3.txt', '2026-11-06 03:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (3, 3, 3, 3, '2026-11-06 03:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (3, 3, 3, 'Simulación académica', '2026-11-06 03:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (3, 3, 3, 'Mensaje ficticio no enviado', '2026-11-06 03:00:00', 'Simulado');
INSERT INTO conductor VALUES (4, 'Conductor demo', 'Ficticio 004', '99000004', '000000000', 'DEMO-LIC-004', 'Activo');
INSERT INTO operador VALUES (4, 4, 'Operador demo', 'Ficticio 004', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (4, 'TST-004', 'DEMO-004', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (4, 'Comisaría ficticia 004', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (4, 4, 4, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (4, 4, 4, 4, '2026-11-06 04:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (4, 4, 4, 0, 0, 0, '2026-11-06 04:00:00');
INSERT INTO evidencia_multimedia VALUES (4, 4, 'simulada', 'demo/no-existe-4.txt', '2026-11-06 04:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (4, 4, 4, 4, '2026-11-06 04:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (4, 4, 4, 'Simulación académica', '2026-11-06 04:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (4, 4, 4, 'Mensaje ficticio no enviado', '2026-11-06 04:00:00', 'Simulado');
INSERT INTO conductor VALUES (5, 'Conductor demo', 'Ficticio 005', '99000005', '000000000', 'DEMO-LIC-005', 'Activo');
INSERT INTO operador VALUES (5, 5, 'Operador demo', 'Ficticio 005', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (5, 'TST-005', 'DEMO-005', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (5, 'Comisaría ficticia 005', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (5, 5, 5, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (5, 5, 5, 5, '2026-11-06 05:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (5, 5, 5, 0, 0, 0, '2026-11-06 05:00:00');
INSERT INTO evidencia_multimedia VALUES (5, 5, 'simulada', 'demo/no-existe-5.txt', '2026-11-06 05:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (5, 5, 5, 5, '2026-11-06 05:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (5, 5, 5, 'Simulación académica', '2026-11-06 05:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (5, 5, 5, 'Mensaje ficticio no enviado', '2026-11-06 05:00:00', 'Simulado');
INSERT INTO conductor VALUES (6, 'Conductor demo', 'Ficticio 006', '99000006', '000000000', 'DEMO-LIC-006', 'Activo');
INSERT INTO operador VALUES (6, 6, 'Operador demo', 'Ficticio 006', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (6, 'TST-006', 'DEMO-006', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (6, 'Comisaría ficticia 006', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (6, 6, 6, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (6, 6, 6, 6, '2026-11-06 06:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (6, 6, 6, 0, 0, 0, '2026-11-06 06:00:00');
INSERT INTO evidencia_multimedia VALUES (6, 6, 'simulada', 'demo/no-existe-6.txt', '2026-11-06 06:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (6, 6, 6, 6, '2026-11-06 06:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (6, 6, 6, 'Simulación académica', '2026-11-06 06:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (6, 6, 6, 'Mensaje ficticio no enviado', '2026-11-06 06:00:00', 'Simulado');
INSERT INTO conductor VALUES (7, 'Conductor demo', 'Ficticio 007', '99000007', '000000000', 'DEMO-LIC-007', 'Activo');
INSERT INTO operador VALUES (7, 7, 'Operador demo', 'Ficticio 007', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (7, 'TST-007', 'DEMO-007', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (7, 'Comisaría ficticia 007', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (7, 7, 7, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (7, 7, 7, 7, '2026-11-06 07:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (7, 7, 7, 0, 0, 0, '2026-11-06 07:00:00');
INSERT INTO evidencia_multimedia VALUES (7, 7, 'simulada', 'demo/no-existe-7.txt', '2026-11-06 07:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (7, 7, 7, 7, '2026-11-06 07:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (7, 7, 7, 'Simulación académica', '2026-11-06 07:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (7, 7, 7, 'Mensaje ficticio no enviado', '2026-11-06 07:00:00', 'Simulado');
INSERT INTO conductor VALUES (8, 'Conductor demo', 'Ficticio 008', '99000008', '000000000', 'DEMO-LIC-008', 'Activo');
INSERT INTO operador VALUES (8, 8, 'Operador demo', 'Ficticio 008', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (8, 'TST-008', 'DEMO-008', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (8, 'Comisaría ficticia 008', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (8, 8, 8, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (8, 8, 8, 8, '2026-11-06 08:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (8, 8, 8, 0, 0, 0, '2026-11-06 08:00:00');
INSERT INTO evidencia_multimedia VALUES (8, 8, 'simulada', 'demo/no-existe-8.txt', '2026-11-06 08:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (8, 8, 8, 8, '2026-11-06 08:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (8, 8, 8, 'Simulación académica', '2026-11-06 08:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (8, 8, 8, 'Mensaje ficticio no enviado', '2026-11-06 08:00:00', 'Simulado');
INSERT INTO conductor VALUES (9, 'Conductor demo', 'Ficticio 009', '99000009', '000000000', 'DEMO-LIC-009', 'Activo');
INSERT INTO operador VALUES (9, 9, 'Operador demo', 'Ficticio 009', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (9, 'TST-009', 'DEMO-009', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (9, 'Comisaría ficticia 009', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (9, 9, 9, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (9, 9, 9, 9, '2026-11-06 09:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (9, 9, 9, 0, 0, 0, '2026-11-06 09:00:00');
INSERT INTO evidencia_multimedia VALUES (9, 9, 'simulada', 'demo/no-existe-9.txt', '2026-11-06 09:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (9, 9, 9, 9, '2026-11-06 09:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (9, 9, 9, 'Simulación académica', '2026-11-06 09:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (9, 9, 9, 'Mensaje ficticio no enviado', '2026-11-06 09:00:00', 'Simulado');
INSERT INTO conductor VALUES (10, 'Conductor demo', 'Ficticio 010', '99000010', '000000000', 'DEMO-LIC-010', 'Activo');
INSERT INTO operador VALUES (10, 10, 'Operador demo', 'Ficticio 010', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (10, 'TST-010', 'DEMO-010', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (10, 'Comisaría ficticia 010', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (10, 10, 10, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (10, 10, 10, 10, '2026-11-06 10:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (10, 10, 10, 0, 0, 0, '2026-11-06 10:00:00');
INSERT INTO evidencia_multimedia VALUES (10, 10, 'simulada', 'demo/no-existe-10.txt', '2026-11-06 10:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (10, 10, 10, 10, '2026-11-06 10:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (10, 10, 10, 'Simulación académica', '2026-11-06 10:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (10, 10, 10, 'Mensaje ficticio no enviado', '2026-11-06 10:00:00', 'Simulado');
INSERT INTO conductor VALUES (11, 'Conductor demo', 'Ficticio 011', '99000011', '000000000', 'DEMO-LIC-011', 'Activo');
INSERT INTO operador VALUES (11, 11, 'Operador demo', 'Ficticio 011', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (11, 'TST-011', 'DEMO-011', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (11, 'Comisaría ficticia 011', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (11, 11, 11, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (11, 11, 11, 11, '2026-11-06 11:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (11, 11, 11, 0, 0, 0, '2026-11-06 11:00:00');
INSERT INTO evidencia_multimedia VALUES (11, 11, 'simulada', 'demo/no-existe-11.txt', '2026-11-06 11:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (11, 11, 11, 11, '2026-11-06 11:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (11, 11, 11, 'Simulación académica', '2026-11-06 11:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (11, 11, 11, 'Mensaje ficticio no enviado', '2026-11-06 11:00:00', 'Simulado');
INSERT INTO conductor VALUES (12, 'Conductor demo', 'Ficticio 012', '99000012', '000000000', 'DEMO-LIC-012', 'Activo');
INSERT INTO operador VALUES (12, 12, 'Operador demo', 'Ficticio 012', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (12, 'TST-012', 'DEMO-012', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (12, 'Comisaría ficticia 012', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (12, 12, 12, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (12, 12, 12, 12, '2026-11-06 12:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (12, 12, 12, 0, 0, 0, '2026-11-06 12:00:00');
INSERT INTO evidencia_multimedia VALUES (12, 12, 'simulada', 'demo/no-existe-12.txt', '2026-11-06 12:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (12, 12, 12, 12, '2026-11-06 12:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (12, 12, 12, 'Simulación académica', '2026-11-06 12:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (12, 12, 12, 'Mensaje ficticio no enviado', '2026-11-06 12:00:00', 'Simulado');
INSERT INTO conductor VALUES (13, 'Conductor demo', 'Ficticio 013', '99000013', '000000000', 'DEMO-LIC-013', 'Activo');
INSERT INTO operador VALUES (13, 13, 'Operador demo', 'Ficticio 013', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (13, 'TST-013', 'DEMO-013', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (13, 'Comisaría ficticia 013', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (13, 13, 13, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (13, 13, 13, 13, '2026-11-06 13:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (13, 13, 13, 0, 0, 0, '2026-11-06 13:00:00');
INSERT INTO evidencia_multimedia VALUES (13, 13, 'simulada', 'demo/no-existe-13.txt', '2026-11-06 13:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (13, 13, 13, 13, '2026-11-06 13:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (13, 13, 13, 'Simulación académica', '2026-11-06 13:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (13, 13, 13, 'Mensaje ficticio no enviado', '2026-11-06 13:00:00', 'Simulado');
INSERT INTO conductor VALUES (14, 'Conductor demo', 'Ficticio 014', '99000014', '000000000', 'DEMO-LIC-014', 'Activo');
INSERT INTO operador VALUES (14, 14, 'Operador demo', 'Ficticio 014', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (14, 'TST-014', 'DEMO-014', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (14, 'Comisaría ficticia 014', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (14, 14, 14, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (14, 14, 14, 14, '2026-11-06 14:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (14, 14, 14, 0, 0, 0, '2026-11-06 14:00:00');
INSERT INTO evidencia_multimedia VALUES (14, 14, 'simulada', 'demo/no-existe-14.txt', '2026-11-06 14:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (14, 14, 14, 14, '2026-11-06 14:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (14, 14, 14, 'Simulación académica', '2026-11-06 14:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (14, 14, 14, 'Mensaje ficticio no enviado', '2026-11-06 14:00:00', 'Simulado');
INSERT INTO conductor VALUES (15, 'Conductor demo', 'Ficticio 015', '99000015', '000000000', 'DEMO-LIC-015', 'Activo');
INSERT INTO operador VALUES (15, 15, 'Operador demo', 'Ficticio 015', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (15, 'TST-015', 'DEMO-015', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (15, 'Comisaría ficticia 015', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (15, 15, 15, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (15, 15, 15, 15, '2026-11-06 15:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (15, 15, 15, 0, 0, 0, '2026-11-06 15:00:00');
INSERT INTO evidencia_multimedia VALUES (15, 15, 'simulada', 'demo/no-existe-15.txt', '2026-11-06 15:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (15, 15, 15, 15, '2026-11-06 15:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (15, 15, 15, 'Simulación académica', '2026-11-06 15:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (15, 15, 15, 'Mensaje ficticio no enviado', '2026-11-06 15:00:00', 'Simulado');
INSERT INTO conductor VALUES (16, 'Conductor demo', 'Ficticio 016', '99000016', '000000000', 'DEMO-LIC-016', 'Activo');
INSERT INTO operador VALUES (16, 16, 'Operador demo', 'Ficticio 016', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (16, 'TST-016', 'DEMO-016', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (16, 'Comisaría ficticia 016', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (16, 16, 16, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (16, 16, 16, 16, '2026-11-06 16:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (16, 16, 16, 0, 0, 0, '2026-11-06 16:00:00');
INSERT INTO evidencia_multimedia VALUES (16, 16, 'simulada', 'demo/no-existe-16.txt', '2026-11-06 16:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (16, 16, 16, 16, '2026-11-06 16:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (16, 16, 16, 'Simulación académica', '2026-11-06 16:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (16, 16, 16, 'Mensaje ficticio no enviado', '2026-11-06 16:00:00', 'Simulado');
INSERT INTO conductor VALUES (17, 'Conductor demo', 'Ficticio 017', '99000017', '000000000', 'DEMO-LIC-017', 'Activo');
INSERT INTO operador VALUES (17, 17, 'Operador demo', 'Ficticio 017', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (17, 'TST-017', 'DEMO-017', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (17, 'Comisaría ficticia 017', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (17, 17, 17, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (17, 17, 17, 17, '2026-11-06 17:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (17, 17, 17, 0, 0, 0, '2026-11-06 17:00:00');
INSERT INTO evidencia_multimedia VALUES (17, 17, 'simulada', 'demo/no-existe-17.txt', '2026-11-06 17:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (17, 17, 17, 17, '2026-11-06 17:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (17, 17, 17, 'Simulación académica', '2026-11-06 17:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (17, 17, 17, 'Mensaje ficticio no enviado', '2026-11-06 17:00:00', 'Simulado');
INSERT INTO conductor VALUES (18, 'Conductor demo', 'Ficticio 018', '99000018', '000000000', 'DEMO-LIC-018', 'Activo');
INSERT INTO operador VALUES (18, 18, 'Operador demo', 'Ficticio 018', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (18, 'TST-018', 'DEMO-018', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (18, 'Comisaría ficticia 018', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (18, 18, 18, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (18, 18, 18, 18, '2026-11-06 18:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (18, 18, 18, 0, 0, 0, '2026-11-06 18:00:00');
INSERT INTO evidencia_multimedia VALUES (18, 18, 'simulada', 'demo/no-existe-18.txt', '2026-11-06 18:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (18, 18, 18, 18, '2026-11-06 18:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (18, 18, 18, 'Simulación académica', '2026-11-06 18:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (18, 18, 18, 'Mensaje ficticio no enviado', '2026-11-06 18:00:00', 'Simulado');
INSERT INTO conductor VALUES (19, 'Conductor demo', 'Ficticio 019', '99000019', '000000000', 'DEMO-LIC-019', 'Activo');
INSERT INTO operador VALUES (19, 19, 'Operador demo', 'Ficticio 019', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (19, 'TST-019', 'DEMO-019', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (19, 'Comisaría ficticia 019', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (19, 19, 19, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (19, 19, 19, 19, '2026-11-06 19:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (19, 19, 19, 0, 0, 0, '2026-11-06 19:00:00');
INSERT INTO evidencia_multimedia VALUES (19, 19, 'simulada', 'demo/no-existe-19.txt', '2026-11-06 19:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (19, 19, 19, 19, '2026-11-06 19:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (19, 19, 19, 'Simulación académica', '2026-11-06 19:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (19, 19, 19, 'Mensaje ficticio no enviado', '2026-11-06 19:00:00', 'Simulado');
INSERT INTO conductor VALUES (20, 'Conductor demo', 'Ficticio 020', '99000020', '000000000', 'DEMO-LIC-020', 'Activo');
INSERT INTO operador VALUES (20, 20, 'Operador demo', 'Ficticio 020', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (20, 'TST-020', 'DEMO-020', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (20, 'Comisaría ficticia 020', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (20, 20, 20, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (20, 20, 20, 20, '2026-11-06 20:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (20, 20, 20, 0, 0, 0, '2026-11-06 20:00:00');
INSERT INTO evidencia_multimedia VALUES (20, 20, 'simulada', 'demo/no-existe-20.txt', '2026-11-06 20:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (20, 20, 20, 20, '2026-11-06 20:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (20, 20, 20, 'Simulación académica', '2026-11-06 20:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (20, 20, 20, 'Mensaje ficticio no enviado', '2026-11-06 20:00:00', 'Simulado');
INSERT INTO conductor VALUES (21, 'Conductor demo', 'Ficticio 021', '99000021', '000000000', 'DEMO-LIC-021', 'Activo');
INSERT INTO operador VALUES (21, 21, 'Operador demo', 'Ficticio 021', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (21, 'TST-021', 'DEMO-021', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (21, 'Comisaría ficticia 021', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (21, 21, 21, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (21, 21, 21, 21, '2026-11-06 21:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (21, 21, 21, 0, 0, 0, '2026-11-06 21:00:00');
INSERT INTO evidencia_multimedia VALUES (21, 21, 'simulada', 'demo/no-existe-21.txt', '2026-11-06 21:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (21, 21, 21, 21, '2026-11-06 21:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (21, 21, 21, 'Simulación académica', '2026-11-06 21:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (21, 21, 21, 'Mensaje ficticio no enviado', '2026-11-06 21:00:00', 'Simulado');
INSERT INTO conductor VALUES (22, 'Conductor demo', 'Ficticio 022', '99000022', '000000000', 'DEMO-LIC-022', 'Activo');
INSERT INTO operador VALUES (22, 22, 'Operador demo', 'Ficticio 022', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (22, 'TST-022', 'DEMO-022', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (22, 'Comisaría ficticia 022', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (22, 22, 22, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (22, 22, 22, 22, '2026-11-06 22:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (22, 22, 22, 0, 0, 0, '2026-11-06 22:00:00');
INSERT INTO evidencia_multimedia VALUES (22, 22, 'simulada', 'demo/no-existe-22.txt', '2026-11-06 22:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (22, 22, 22, 22, '2026-11-06 22:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (22, 22, 22, 'Simulación académica', '2026-11-06 22:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (22, 22, 22, 'Mensaje ficticio no enviado', '2026-11-06 22:00:00', 'Simulado');
INSERT INTO conductor VALUES (23, 'Conductor demo', 'Ficticio 023', '99000023', '000000000', 'DEMO-LIC-023', 'Activo');
INSERT INTO operador VALUES (23, 23, 'Operador demo', 'Ficticio 023', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (23, 'TST-023', 'DEMO-023', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (23, 'Comisaría ficticia 023', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (23, 23, 23, '2026-11-06', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (23, 23, 23, 23, '2026-11-06 23:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (23, 23, 23, 0, 0, 0, '2026-11-06 23:00:00');
INSERT INTO evidencia_multimedia VALUES (23, 23, 'simulada', 'demo/no-existe-23.txt', '2026-11-06 23:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (23, 23, 23, 23, '2026-11-06 23:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (23, 23, 23, 'Simulación académica', '2026-11-06 23:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (23, 23, 23, 'Mensaje ficticio no enviado', '2026-11-06 23:00:00', 'Simulado');
INSERT INTO conductor VALUES (24, 'Conductor demo', 'Ficticio 024', '99000024', '000000000', 'DEMO-LIC-024', 'Activo');
INSERT INTO operador VALUES (24, 24, 'Operador demo', 'Ficticio 024', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (24, 'TST-024', 'DEMO-024', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (24, 'Comisaría ficticia 024', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (24, 24, 24, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (24, 24, 24, 24, '2026-11-07 00:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (24, 24, 24, 0, 0, 0, '2026-11-07 00:00:00');
INSERT INTO evidencia_multimedia VALUES (24, 24, 'simulada', 'demo/no-existe-24.txt', '2026-11-07 00:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (24, 24, 24, 24, '2026-11-07 00:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (24, 24, 24, 'Simulación académica', '2026-11-07 00:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (24, 24, 24, 'Mensaje ficticio no enviado', '2026-11-07 00:00:00', 'Simulado');
INSERT INTO conductor VALUES (25, 'Conductor demo', 'Ficticio 025', '99000025', '000000000', 'DEMO-LIC-025', 'Activo');
INSERT INTO operador VALUES (25, 25, 'Operador demo', 'Ficticio 025', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (25, 'TST-025', 'DEMO-025', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (25, 'Comisaría ficticia 025', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (25, 25, 25, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (25, 25, 25, 25, '2026-11-07 01:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (25, 25, 25, 0, 0, 0, '2026-11-07 01:00:00');
INSERT INTO evidencia_multimedia VALUES (25, 25, 'simulada', 'demo/no-existe-25.txt', '2026-11-07 01:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (25, 25, 25, 25, '2026-11-07 01:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (25, 25, 25, 'Simulación académica', '2026-11-07 01:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (25, 25, 25, 'Mensaje ficticio no enviado', '2026-11-07 01:00:00', 'Simulado');
INSERT INTO conductor VALUES (26, 'Conductor demo', 'Ficticio 026', '99000026', '000000000', 'DEMO-LIC-026', 'Activo');
INSERT INTO operador VALUES (26, 26, 'Operador demo', 'Ficticio 026', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (26, 'TST-026', 'DEMO-026', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (26, 'Comisaría ficticia 026', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (26, 26, 26, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (26, 26, 26, 26, '2026-11-07 02:00:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (26, 26, 26, 0, 0, 0, '2026-11-07 02:00:00');
INSERT INTO evidencia_multimedia VALUES (26, 26, 'simulada', 'demo/no-existe-26.txt', '2026-11-07 02:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (26, 26, 26, 26, '2026-11-07 02:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (26, 26, 26, 'Simulación académica', '2026-11-07 02:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (26, 26, 26, 'Mensaje ficticio no enviado', '2026-11-07 02:00:00', 'Simulado');
INSERT INTO conductor VALUES (27, 'Conductor demo', 'Ficticio 027', '99000027', '000000000', 'DEMO-LIC-027', 'Activo');
INSERT INTO operador VALUES (27, 27, 'Operador demo', 'Ficticio 027', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (27, 'TST-027', 'DEMO-027', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (27, 'Comisaría ficticia 027', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (27, 27, 27, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (27, 27, 27, 27, '2026-11-07 03:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (27, 27, 27, 0, 0, 0, '2026-11-07 03:00:00');
INSERT INTO evidencia_multimedia VALUES (27, 27, 'simulada', 'demo/no-existe-27.txt', '2026-11-07 03:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (27, 27, 27, 27, '2026-11-07 03:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (27, 27, 27, 'Simulación académica', '2026-11-07 03:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (27, 27, 27, 'Mensaje ficticio no enviado', '2026-11-07 03:00:00', 'Simulado');
INSERT INTO conductor VALUES (28, 'Conductor demo', 'Ficticio 028', '99000028', '000000000', 'DEMO-LIC-028', 'Activo');
INSERT INTO operador VALUES (28, 28, 'Operador demo', 'Ficticio 028', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (28, 'TST-028', 'DEMO-028', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (28, 'Comisaría ficticia 028', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (28, 28, 28, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (28, 28, 28, 28, '2026-11-07 04:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (28, 28, 28, 0, 0, 0, '2026-11-07 04:00:00');
INSERT INTO evidencia_multimedia VALUES (28, 28, 'simulada', 'demo/no-existe-28.txt', '2026-11-07 04:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (28, 28, 28, 28, '2026-11-07 04:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (28, 28, 28, 'Simulación académica', '2026-11-07 04:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (28, 28, 28, 'Mensaje ficticio no enviado', '2026-11-07 04:00:00', 'Simulado');
INSERT INTO conductor VALUES (29, 'Conductor demo', 'Ficticio 029', '99000029', '000000000', 'DEMO-LIC-029', 'Activo');
INSERT INTO operador VALUES (29, 29, 'Operador demo', 'Ficticio 029', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (29, 'TST-029', 'DEMO-029', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (29, 'Comisaría ficticia 029', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (29, 29, 29, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (29, 29, 29, 29, '2026-11-07 05:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (29, 29, 29, 0, 0, 0, '2026-11-07 05:00:00');
INSERT INTO evidencia_multimedia VALUES (29, 29, 'simulada', 'demo/no-existe-29.txt', '2026-11-07 05:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (29, 29, 29, 29, '2026-11-07 05:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (29, 29, 29, 'Simulación académica', '2026-11-07 05:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (29, 29, 29, 'Mensaje ficticio no enviado', '2026-11-07 05:00:00', 'Simulado');
INSERT INTO conductor VALUES (30, 'Conductor demo', 'Ficticio 030', '99000030', '000000000', 'DEMO-LIC-030', 'Activo');
INSERT INTO operador VALUES (30, 30, 'Operador demo', 'Ficticio 030', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (30, 'TST-030', 'DEMO-030', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (30, 'Comisaría ficticia 030', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (30, 30, 30, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (30, 30, 30, 30, '2026-11-07 06:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (30, 30, 30, 0, 0, 0, '2026-11-07 06:00:00');
INSERT INTO evidencia_multimedia VALUES (30, 30, 'simulada', 'demo/no-existe-30.txt', '2026-11-07 06:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (30, 30, 30, 30, '2026-11-07 06:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (30, 30, 30, 'Simulación académica', '2026-11-07 06:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (30, 30, 30, 'Mensaje ficticio no enviado', '2026-11-07 06:00:00', 'Simulado');
INSERT INTO conductor VALUES (31, 'Conductor demo', 'Ficticio 031', '99000031', '000000000', 'DEMO-LIC-031', 'Activo');
INSERT INTO operador VALUES (31, 31, 'Operador demo', 'Ficticio 031', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (31, 'TST-031', 'DEMO-031', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (31, 'Comisaría ficticia 031', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (31, 31, 31, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (31, 31, 31, 31, '2026-11-07 07:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (31, 31, 31, 0, 0, 0, '2026-11-07 07:00:00');
INSERT INTO evidencia_multimedia VALUES (31, 31, 'simulada', 'demo/no-existe-31.txt', '2026-11-07 07:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (31, 31, 31, 31, '2026-11-07 07:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (31, 31, 31, 'Simulación académica', '2026-11-07 07:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (31, 31, 31, 'Mensaje ficticio no enviado', '2026-11-07 07:00:00', 'Simulado');
INSERT INTO conductor VALUES (32, 'Conductor demo', 'Ficticio 032', '99000032', '000000000', 'DEMO-LIC-032', 'Activo');
INSERT INTO operador VALUES (32, 32, 'Operador demo', 'Ficticio 032', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (32, 'TST-032', 'DEMO-032', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (32, 'Comisaría ficticia 032', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (32, 32, 32, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (32, 32, 32, 32, '2026-11-07 08:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (32, 32, 32, 0, 0, 0, '2026-11-07 08:00:00');
INSERT INTO evidencia_multimedia VALUES (32, 32, 'simulada', 'demo/no-existe-32.txt', '2026-11-07 08:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (32, 32, 32, 32, '2026-11-07 08:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (32, 32, 32, 'Simulación académica', '2026-11-07 08:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (32, 32, 32, 'Mensaje ficticio no enviado', '2026-11-07 08:00:00', 'Simulado');
INSERT INTO conductor VALUES (33, 'Conductor demo', 'Ficticio 033', '99000033', '000000000', 'DEMO-LIC-033', 'Activo');
INSERT INTO operador VALUES (33, 33, 'Operador demo', 'Ficticio 033', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (33, 'TST-033', 'DEMO-033', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (33, 'Comisaría ficticia 033', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (33, 33, 33, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (33, 33, 33, 33, '2026-11-07 09:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (33, 33, 33, 0, 0, 0, '2026-11-07 09:00:00');
INSERT INTO evidencia_multimedia VALUES (33, 33, 'simulada', 'demo/no-existe-33.txt', '2026-11-07 09:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (33, 33, 33, 33, '2026-11-07 09:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (33, 33, 33, 'Simulación académica', '2026-11-07 09:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (33, 33, 33, 'Mensaje ficticio no enviado', '2026-11-07 09:00:00', 'Simulado');
INSERT INTO conductor VALUES (34, 'Conductor demo', 'Ficticio 034', '99000034', '000000000', 'DEMO-LIC-034', 'Activo');
INSERT INTO operador VALUES (34, 34, 'Operador demo', 'Ficticio 034', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (34, 'TST-034', 'DEMO-034', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (34, 'Comisaría ficticia 034', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (34, 34, 34, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (34, 34, 34, 34, '2026-11-07 10:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (34, 34, 34, 0, 0, 0, '2026-11-07 10:00:00');
INSERT INTO evidencia_multimedia VALUES (34, 34, 'simulada', 'demo/no-existe-34.txt', '2026-11-07 10:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (34, 34, 34, 34, '2026-11-07 10:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (34, 34, 34, 'Simulación académica', '2026-11-07 10:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (34, 34, 34, 'Mensaje ficticio no enviado', '2026-11-07 10:00:00', 'Simulado');
INSERT INTO conductor VALUES (35, 'Conductor demo', 'Ficticio 035', '99000035', '000000000', 'DEMO-LIC-035', 'Activo');
INSERT INTO operador VALUES (35, 35, 'Operador demo', 'Ficticio 035', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (35, 'TST-035', 'DEMO-035', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (35, 'Comisaría ficticia 035', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (35, 35, 35, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (35, 35, 35, 35, '2026-11-07 11:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (35, 35, 35, 0, 0, 0, '2026-11-07 11:00:00');
INSERT INTO evidencia_multimedia VALUES (35, 35, 'simulada', 'demo/no-existe-35.txt', '2026-11-07 11:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (35, 35, 35, 35, '2026-11-07 11:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (35, 35, 35, 'Simulación académica', '2026-11-07 11:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (35, 35, 35, 'Mensaje ficticio no enviado', '2026-11-07 11:00:00', 'Simulado');
INSERT INTO conductor VALUES (36, 'Conductor demo', 'Ficticio 036', '99000036', '000000000', 'DEMO-LIC-036', 'Activo');
INSERT INTO operador VALUES (36, 36, 'Operador demo', 'Ficticio 036', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (36, 'TST-036', 'DEMO-036', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (36, 'Comisaría ficticia 036', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (36, 36, 36, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (36, 36, 36, 36, '2026-11-07 12:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (36, 36, 36, 0, 0, 0, '2026-11-07 12:00:00');
INSERT INTO evidencia_multimedia VALUES (36, 36, 'simulada', 'demo/no-existe-36.txt', '2026-11-07 12:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (36, 36, 36, 36, '2026-11-07 12:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (36, 36, 36, 'Simulación académica', '2026-11-07 12:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (36, 36, 36, 'Mensaje ficticio no enviado', '2026-11-07 12:00:00', 'Simulado');
INSERT INTO conductor VALUES (37, 'Conductor demo', 'Ficticio 037', '99000037', '000000000', 'DEMO-LIC-037', 'Activo');
INSERT INTO operador VALUES (37, 37, 'Operador demo', 'Ficticio 037', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (37, 'TST-037', 'DEMO-037', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (37, 'Comisaría ficticia 037', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (37, 37, 37, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (37, 37, 37, 37, '2026-11-07 13:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (37, 37, 37, 0, 0, 0, '2026-11-07 13:00:00');
INSERT INTO evidencia_multimedia VALUES (37, 37, 'simulada', 'demo/no-existe-37.txt', '2026-11-07 13:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (37, 37, 37, 37, '2026-11-07 13:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (37, 37, 37, 'Simulación académica', '2026-11-07 13:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (37, 37, 37, 'Mensaje ficticio no enviado', '2026-11-07 13:00:00', 'Simulado');
INSERT INTO conductor VALUES (38, 'Conductor demo', 'Ficticio 038', '99000038', '000000000', 'DEMO-LIC-038', 'Activo');
INSERT INTO operador VALUES (38, 38, 'Operador demo', 'Ficticio 038', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (38, 'TST-038', 'DEMO-038', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (38, 'Comisaría ficticia 038', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (38, 38, 38, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (38, 38, 38, 38, '2026-11-07 14:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (38, 38, 38, 0, 0, 0, '2026-11-07 14:00:00');
INSERT INTO evidencia_multimedia VALUES (38, 38, 'simulada', 'demo/no-existe-38.txt', '2026-11-07 14:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (38, 38, 38, 38, '2026-11-07 14:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (38, 38, 38, 'Simulación académica', '2026-11-07 14:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (38, 38, 38, 'Mensaje ficticio no enviado', '2026-11-07 14:00:00', 'Simulado');
INSERT INTO conductor VALUES (39, 'Conductor demo', 'Ficticio 039', '99000039', '000000000', 'DEMO-LIC-039', 'Activo');
INSERT INTO operador VALUES (39, 39, 'Operador demo', 'Ficticio 039', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (39, 'TST-039', 'DEMO-039', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (39, 'Comisaría ficticia 039', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (39, 39, 39, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (39, 39, 39, 39, '2026-11-07 15:00:00', 'Reporte manual de central', 'Atendida', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (39, 39, 39, 0, 0, 0, '2026-11-07 15:00:00');
INSERT INTO evidencia_multimedia VALUES (39, 39, 'simulada', 'demo/no-existe-39.txt', '2026-11-07 15:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (39, 39, 39, 39, '2026-11-07 15:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (39, 39, 39, 'Simulación académica', '2026-11-07 15:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (39, 39, 39, 'Mensaje ficticio no enviado', '2026-11-07 15:00:00', 'Simulado');
INSERT INTO conductor VALUES (40, 'Conductor demo', 'Ficticio 040', '99000040', '000000000', 'DEMO-LIC-040', 'Activo');
INSERT INTO operador VALUES (40, 40, 'Operador demo', 'Ficticio 040', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (40, 'TST-040', 'DEMO-040', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (40, 'Comisaría ficticia 040', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (40, 40, 40, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (40, 40, 40, 40, '2026-11-07 16:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (40, 40, 40, 0, 0, 0, '2026-11-07 16:00:00');
INSERT INTO evidencia_multimedia VALUES (40, 40, 'simulada', 'demo/no-existe-40.txt', '2026-11-07 16:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (40, 40, 40, 40, '2026-11-07 16:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (40, 40, 40, 'Simulación académica', '2026-11-07 16:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (40, 40, 40, 'Mensaje ficticio no enviado', '2026-11-07 16:00:00', 'Simulado');
INSERT INTO conductor VALUES (41, 'Conductor demo', 'Ficticio 041', '99000041', '000000000', 'DEMO-LIC-041', 'Activo');
INSERT INTO operador VALUES (41, 41, 'Operador demo', 'Ficticio 041', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (41, 'TST-041', 'DEMO-041', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (41, 'Comisaría ficticia 041', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (41, 41, 41, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (41, 41, 41, 41, '2026-11-07 17:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (41, 41, 41, 0, 0, 0, '2026-11-07 17:00:00');
INSERT INTO evidencia_multimedia VALUES (41, 41, 'simulada', 'demo/no-existe-41.txt', '2026-11-07 17:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (41, 41, 41, 41, '2026-11-07 17:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (41, 41, 41, 'Simulación académica', '2026-11-07 17:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (41, 41, 41, 'Mensaje ficticio no enviado', '2026-11-07 17:00:00', 'Simulado');
INSERT INTO conductor VALUES (42, 'Conductor demo', 'Ficticio 042', '99000042', '000000000', 'DEMO-LIC-042', 'Activo');
INSERT INTO operador VALUES (42, 42, 'Operador demo', 'Ficticio 042', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (42, 'TST-042', 'DEMO-042', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (42, 'Comisaría ficticia 042', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (42, 42, 42, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (42, 42, 42, 42, '2026-11-07 18:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (42, 42, 42, 0, 0, 0, '2026-11-07 18:00:00');
INSERT INTO evidencia_multimedia VALUES (42, 42, 'simulada', 'demo/no-existe-42.txt', '2026-11-07 18:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (42, 42, 42, 42, '2026-11-07 18:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (42, 42, 42, 'Simulación académica', '2026-11-07 18:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (42, 42, 42, 'Mensaje ficticio no enviado', '2026-11-07 18:00:00', 'Simulado');
INSERT INTO conductor VALUES (43, 'Conductor demo', 'Ficticio 043', '99000043', '000000000', 'DEMO-LIC-043', 'Activo');
INSERT INTO operador VALUES (43, 43, 'Operador demo', 'Ficticio 043', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (43, 'TST-043', 'DEMO-043', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (43, 'Comisaría ficticia 043', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (43, 43, 43, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (43, 43, 43, 43, '2026-11-07 19:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (43, 43, 43, 0, 0, 0, '2026-11-07 19:00:00');
INSERT INTO evidencia_multimedia VALUES (43, 43, 'simulada', 'demo/no-existe-43.txt', '2026-11-07 19:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (43, 43, 43, 43, '2026-11-07 19:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (43, 43, 43, 'Simulación académica', '2026-11-07 19:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (43, 43, 43, 'Mensaje ficticio no enviado', '2026-11-07 19:00:00', 'Simulado');
INSERT INTO conductor VALUES (44, 'Conductor demo', 'Ficticio 044', '99000044', '000000000', 'DEMO-LIC-044', 'Activo');
INSERT INTO operador VALUES (44, 44, 'Operador demo', 'Ficticio 044', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (44, 'TST-044', 'DEMO-044', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (44, 'Comisaría ficticia 044', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (44, 44, 44, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (44, 44, 44, 44, '2026-11-07 20:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (44, 44, 44, 0, 0, 0, '2026-11-07 20:00:00');
INSERT INTO evidencia_multimedia VALUES (44, 44, 'simulada', 'demo/no-existe-44.txt', '2026-11-07 20:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (44, 44, 44, 44, '2026-11-07 20:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (44, 44, 44, 'Simulación académica', '2026-11-07 20:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (44, 44, 44, 'Mensaje ficticio no enviado', '2026-11-07 20:00:00', 'Simulado');
INSERT INTO conductor VALUES (45, 'Conductor demo', 'Ficticio 045', '99000045', '000000000', 'DEMO-LIC-045', 'Activo');
INSERT INTO operador VALUES (45, 45, 'Operador demo', 'Ficticio 045', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (45, 'TST-045', 'DEMO-045', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (45, 'Comisaría ficticia 045', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (45, 45, 45, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (45, 45, 45, 45, '2026-11-07 21:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (45, 45, 45, 0, 0, 0, '2026-11-07 21:00:00');
INSERT INTO evidencia_multimedia VALUES (45, 45, 'simulada', 'demo/no-existe-45.txt', '2026-11-07 21:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (45, 45, 45, 45, '2026-11-07 21:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (45, 45, 45, 'Simulación académica', '2026-11-07 21:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (45, 45, 45, 'Mensaje ficticio no enviado', '2026-11-07 21:00:00', 'Simulado');
INSERT INTO conductor VALUES (46, 'Conductor demo', 'Ficticio 046', '99000046', '000000000', 'DEMO-LIC-046', 'Activo');
INSERT INTO operador VALUES (46, 46, 'Operador demo', 'Ficticio 046', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (46, 'TST-046', 'DEMO-046', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (46, 'Comisaría ficticia 046', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (46, 46, 46, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (46, 46, 46, 46, '2026-11-07 22:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (46, 46, 46, 0, 0, 0, '2026-11-07 22:00:00');
INSERT INTO evidencia_multimedia VALUES (46, 46, 'simulada', 'demo/no-existe-46.txt', '2026-11-07 22:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (46, 46, 46, 46, '2026-11-07 22:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (46, 46, 46, 'Simulación académica', '2026-11-07 22:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (46, 46, 46, 'Mensaje ficticio no enviado', '2026-11-07 22:00:00', 'Simulado');
INSERT INTO conductor VALUES (47, 'Conductor demo', 'Ficticio 047', '99000047', '000000000', 'DEMO-LIC-047', 'Activo');
INSERT INTO operador VALUES (47, 47, 'Operador demo', 'Ficticio 047', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (47, 'TST-047', 'DEMO-047', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (47, 'Comisaría ficticia 047', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (47, 47, 47, '2026-11-07', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (47, 47, 47, 47, '2026-11-07 23:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (47, 47, 47, 0, 0, 0, '2026-11-07 23:00:00');
INSERT INTO evidencia_multimedia VALUES (47, 47, 'simulada', 'demo/no-existe-47.txt', '2026-11-07 23:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (47, 47, 47, 47, '2026-11-07 23:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (47, 47, 47, 'Simulación académica', '2026-11-07 23:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (47, 47, 47, 'Mensaje ficticio no enviado', '2026-11-07 23:00:00', 'Simulado');
INSERT INTO conductor VALUES (48, 'Conductor demo', 'Ficticio 048', '99000048', '000000000', 'DEMO-LIC-048', 'Activo');
INSERT INTO operador VALUES (48, 48, 'Operador demo', 'Ficticio 048', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (48, 'TST-048', 'DEMO-048', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (48, 'Comisaría ficticia 048', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (48, 48, 48, '2026-11-08', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (48, 48, 48, 48, '2026-11-08 00:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (48, 48, 48, 0, 0, 0, '2026-11-08 00:00:00');
INSERT INTO evidencia_multimedia VALUES (48, 48, 'simulada', 'demo/no-existe-48.txt', '2026-11-08 00:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (48, 48, 48, 48, '2026-11-08 00:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (48, 48, 48, 'Simulación académica', '2026-11-08 00:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (48, 48, 48, 'Mensaje ficticio no enviado', '2026-11-08 00:00:00', 'Simulado');
INSERT INTO conductor VALUES (49, 'Conductor demo', 'Ficticio 049', '99000049', '000000000', 'DEMO-LIC-049', 'Activo');
INSERT INTO operador VALUES (49, 49, 'Operador demo', 'Ficticio 049', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (49, 'TST-049', 'DEMO-049', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (49, 'Comisaría ficticia 049', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (49, 49, 49, '2026-11-08', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (49, 49, 49, 49, '2026-11-08 01:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (49, 49, 49, 0, 0, 0, '2026-11-08 01:00:00');
INSERT INTO evidencia_multimedia VALUES (49, 49, 'simulada', 'demo/no-existe-49.txt', '2026-11-08 01:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (49, 49, 49, 49, '2026-11-08 01:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (49, 49, 49, 'Simulación académica', '2026-11-08 01:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (49, 49, 49, 'Mensaje ficticio no enviado', '2026-11-08 01:00:00', 'Simulado');
INSERT INTO conductor VALUES (50, 'Conductor demo', 'Ficticio 050', '99000050', '000000000', 'DEMO-LIC-050', 'Activo');
INSERT INTO operador VALUES (50, 50, 'Operador demo', 'Ficticio 050', '000000000', 'Pruebas', 'Activo');
INSERT INTO bus VALUES (50, 'AEE-000', 'DEMO-050', 'Vehículo ficticio', 20, 'Activo');
INSERT INTO comisaria VALUES (50, 'Comisaría ficticia 050', 'Dirección de prueba sin ubicación real', '000000000', 0, 0);
INSERT INTO asignacion_turno VALUES (50, 50, 50, '2026-11-08', '00:00:00', '23:59:00', 'Activo');
INSERT INTO alerta VALUES (50, 50, 50, 50, '2026-11-08 02:00:00', 'Reporte manual de central', 'En Evaluación', 'Evento ficticio de regresión');
INSERT INTO telemetria_gps VALUES (50, 50, 50, 0, 0, 0, '2026-11-08 02:00:00');
INSERT INTO evidencia_multimedia VALUES (50, 50, 'simulada', 'demo/no-existe-50.txt', '2026-11-08 02:00:00', 'Referencia ficticia: no contiene archivos ni evidencia real');
INSERT INTO despacho_auxilio VALUES (50, 50, 50, 50, '2026-11-08 02:00:00', 'Simulado', 'No se envía ninguna comunicación');
INSERT INTO cerco_preventivo VALUES (50, 50, 50, 'Simulación académica', '2026-11-08 02:00:00', NULL, 'Simulado');
INSERT INTO notificacion VALUES (50, 50, 50, 'Mensaje ficticio no enviado', '2026-11-08 02:00:00', 'Simulado');
