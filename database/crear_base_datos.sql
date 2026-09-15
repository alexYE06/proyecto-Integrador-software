CREATE DATABASE sistema_alertas;
USE sistema_alertas;
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE conductor (
    id_conductor INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    licencia VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE operador (
    id_operador INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    cargo VARCHAR(50),
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);
CREATE TABLE bus (
    id_bus INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    codigo_unidad VARCHAR(20) NOT NULL UNIQUE,
    modelo VARCHAR(50),
    capacidad INT,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE comisaria (
    id_comisaria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(15),
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7)
);
CREATE TABLE asignacion_turno (
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
CREATE TABLE alerta (
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
CREATE TABLE telemetria_gps (
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
CREATE TABLE evidencia_multimedia (
    id_evidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_alerta INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    ruta_archivo VARCHAR(255) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_alerta) REFERENCES alerta(id_alerta)
);
CREATE TABLE despacho_auxilio (
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
CREATE TABLE cerco_preventivo (
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
CREATE TABLE notificacion (
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

