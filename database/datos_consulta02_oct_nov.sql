USE sistema_alertas;

DROP TEMPORARY TABLE IF EXISTS tmp_consulta02_demo;
CREATE TEMPORARY TABLE tmp_consulta02_demo (
    dni VARCHAR(8) NOT NULL,
    placa VARCHAR(10) NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL,
    operador_nombres VARCHAR(100) NOT NULL,
    operador_apellidos VARCHAR(100) NOT NULL,
    nombre_comisaria VARCHAR(100) NOT NULL,
    fecha_alerta DATETIME NOT NULL,
    tipo_activacion VARCHAR(50) NOT NULL,
    estado_alerta VARCHAR(30) NOT NULL,
    descripcion_alerta VARCHAR(255) NOT NULL,
    fecha_despacho DATETIME NOT NULL,
    estado_despacho VARCHAR(30) NOT NULL,
    observacion VARCHAR(255) NOT NULL,
    fecha_inicio_cerco DATETIME NOT NULL,
    estado_cerco VARCHAR(30) NOT NULL,
    descripcion_cerco VARCHAR(255) NOT NULL
);

INSERT INTO tmp_consulta02_demo VALUES
('71234567', 'ABC-123', 'operador1', 'Andrea', 'Flores Ramos',
 'Comisaría Carmen de la Legua', '2026-10-05 09:00:00',
 'Tres pulsaciones botón encendido', 'Recibida',
 'Alerta de prueba para filtro de octubre',
 '2026-10-05 09:10:00', 'Despachado',
 'Despacho de prueba para filtro de octubre',
 '2026-10-05 09:15:00', 'Activo',
 'Cerco preventivo de prueba para octubre'),
('73456789', 'DEF-456', 'operador1', 'Andrea', 'Flores Ramos',
 'Comisaría Carmen de la Legua', '2026-10-20 15:00:00',
 'Tres pulsaciones botón encendido', 'En Evaluación',
 'Alerta de prueba para filtro de octubre',
 '2026-10-20 15:10:00', 'Despachado',
 'Despacho de prueba para filtro de octubre',
 '2026-10-20 15:20:00', 'Activo',
 'Cerco preventivo de prueba para octubre'),
('71234567', 'ABC-123', 'operador1', 'Andrea', 'Flores Ramos',
 'Comisaría Carmen de la Legua', '2026-11-15 11:30:00',
 'Tres pulsaciones botón encendido', 'Recibida',
 'Alerta de prueba para filtro de noviembre',
 '2026-11-15 11:40:00', 'Despachado',
 'Despacho de prueba para filtro de noviembre',
 '2026-11-15 11:50:00', 'Activo',
 'Cerco preventivo de prueba para noviembre');

START TRANSACTION;

INSERT INTO alerta
    (id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion)
SELECT c.id_conductor, b.id_bus, o.id_operador, t.fecha_alerta,
       t.tipo_activacion, t.estado_alerta, t.descripcion_alerta
FROM tmp_consulta02_demo t
JOIN conductor c ON c.dni = t.dni
JOIN bus b ON b.placa = t.placa
JOIN operador o ON o.id_operador = (
    SELECT o2.id_operador
    FROM operador o2
    JOIN usuario u2 ON u2.id_usuario = o2.id_usuario
    WHERE u2.nombre_usuario = t.nombre_usuario
      AND o2.nombres = t.operador_nombres
      AND o2.apellidos = t.operador_apellidos
    ORDER BY o2.id_operador
    LIMIT 1
)
WHERE NOT EXISTS (
    SELECT 1
    FROM alerta ax
    WHERE ax.id_conductor = c.id_conductor
      AND ax.id_bus = b.id_bus
      AND ax.fecha_hora = t.fecha_alerta
      AND ax.tipo_activacion = t.tipo_activacion
);

INSERT INTO despacho_auxilio
    (id_alerta, id_comisaria, id_operador, fecha_hora, estado, observacion)
SELECT a.id_alerta, cm.id_comisaria, o.id_operador,
       t.fecha_despacho, t.estado_despacho, t.observacion
FROM tmp_consulta02_demo t
JOIN conductor c ON c.dni = t.dni
JOIN bus b ON b.placa = t.placa
JOIN alerta a ON a.id_conductor = c.id_conductor
             AND a.id_bus = b.id_bus
             AND a.fecha_hora = t.fecha_alerta
             AND a.tipo_activacion = t.tipo_activacion
JOIN comisaria cm ON cm.id_comisaria = (
    SELECT cm2.id_comisaria
    FROM comisaria cm2
    WHERE cm2.nombre = t.nombre_comisaria
    ORDER BY cm2.id_comisaria
    LIMIT 1
)
JOIN operador o ON o.id_operador = (
    SELECT o2.id_operador
    FROM operador o2
    JOIN usuario u2 ON u2.id_usuario = o2.id_usuario
    WHERE u2.nombre_usuario = t.nombre_usuario
      AND o2.nombres = t.operador_nombres
      AND o2.apellidos = t.operador_apellidos
    ORDER BY o2.id_operador
    LIMIT 1
)
WHERE NOT EXISTS (
    SELECT 1
    FROM despacho_auxilio d
    WHERE d.id_alerta = a.id_alerta
      AND d.fecha_hora = t.fecha_despacho
);

INSERT INTO cerco_preventivo
    (id_alerta, id_comisaria, descripcion, fecha_inicio, fecha_fin, estado)
SELECT a.id_alerta, cm.id_comisaria, t.descripcion_cerco,
       t.fecha_inicio_cerco, NULL, t.estado_cerco
FROM tmp_consulta02_demo t
JOIN conductor c ON c.dni = t.dni
JOIN bus b ON b.placa = t.placa
JOIN alerta a ON a.id_conductor = c.id_conductor
             AND a.id_bus = b.id_bus
             AND a.fecha_hora = t.fecha_alerta
             AND a.tipo_activacion = t.tipo_activacion
JOIN comisaria cm ON cm.id_comisaria = (
    SELECT cm2.id_comisaria
    FROM comisaria cm2
    WHERE cm2.nombre = t.nombre_comisaria
    ORDER BY cm2.id_comisaria
    LIMIT 1
)
WHERE NOT EXISTS (
    SELECT 1
    FROM cerco_preventivo cp
    WHERE cp.id_alerta = a.id_alerta
      AND cp.fecha_inicio = t.fecha_inicio_cerco
);

COMMIT;
DROP TEMPORARY TABLE IF EXISTS tmp_consulta02_demo;
