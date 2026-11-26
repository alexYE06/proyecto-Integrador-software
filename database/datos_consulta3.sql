-- Datos de prueba para la Consulta 3.
-- Agrega 20 alertas simuladas entre el 12 y el 16 de agosto de 2026:
-- 4 alertas por día; 10 Atendida, 5 Falsa Alarma y 5 Auxilio Despachado.
-- Requiere los conductores, buses y operadores 1..20 creados por los inserts previos.
-- Es idempotente: las descripciones identificadoras evitan duplicar filas al reejecutarlo.

USE sistema_alertas;

START TRANSACTION;

INSERT INTO alerta
    (id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion)
SELECT datos.id_conductor,
       datos.id_bus,
       datos.id_operador,
       datos.fecha_hora,
       datos.tipo_activacion,
       datos.estado,
       datos.descripcion
FROM (
    SELECT 1 AS id_conductor, 1 AS id_bus, 1 AS id_operador,
           '2026-08-12 08:10:00' AS fecha_hora, 'Tres pulsaciones botón encendido' AS tipo_activacion,
           'Atendida' AS estado, 'Consulta 3 - 2026-08-12 - alerta atendida 1' AS descripcion
    UNION ALL SELECT 2, 2, 2, '2026-08-12 10:25:00', 'Reporte manual de central', 'Falsa Alarma', 'Consulta 3 - 2026-08-12 - falsa alarma'
    UNION ALL SELECT 3, 3, 3, '2026-08-12 12:40:00', 'Tres pulsaciones botón encendido', 'Auxilio Despachado', 'Consulta 3 - 2026-08-12 - auxilio despachado'
    UNION ALL SELECT 4, 4, 4, '2026-08-12 15:05:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-12 - alerta atendida 2'

    UNION ALL SELECT 5, 5, 5, '2026-08-13 07:50:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-13 - alerta atendida 1'
    UNION ALL SELECT 6, 6, 6, '2026-08-13 09:35:00', 'Tres pulsaciones botón encendido', 'Falsa Alarma', 'Consulta 3 - 2026-08-13 - falsa alarma'
    UNION ALL SELECT 7, 7, 7, '2026-08-13 13:20:00', 'Reporte manual de central', 'Auxilio Despachado', 'Consulta 3 - 2026-08-13 - auxilio despachado'
    UNION ALL SELECT 8, 8, 8, '2026-08-13 17:45:00', 'Tres pulsaciones botón encendido', 'Atendida', 'Consulta 3 - 2026-08-13 - alerta atendida 2'

    UNION ALL SELECT 9, 9, 9, '2026-08-14 08:05:00', 'Tres pulsaciones botón encendido', 'Falsa Alarma', 'Consulta 3 - 2026-08-14 - falsa alarma'
    UNION ALL SELECT 10, 10, 10, '2026-08-14 10:50:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-14 - alerta atendida 1'
    UNION ALL SELECT 11, 11, 11, '2026-08-14 14:15:00', 'Tres pulsaciones botón encendido', 'Auxilio Despachado', 'Consulta 3 - 2026-08-14 - auxilio despachado'
    UNION ALL SELECT 12, 12, 12, '2026-08-14 18:30:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-14 - alerta atendida 2'

    UNION ALL SELECT 13, 13, 13, '2026-08-15 06:55:00', 'Tres pulsaciones botón encendido', 'Auxilio Despachado', 'Consulta 3 - 2026-08-15 - auxilio despachado'
    UNION ALL SELECT 14, 14, 14, '2026-08-15 09:40:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-15 - alerta atendida 1'
    UNION ALL SELECT 15, 15, 15, '2026-08-15 12:25:00', 'Tres pulsaciones botón encendido', 'Falsa Alarma', 'Consulta 3 - 2026-08-15 - falsa alarma'
    UNION ALL SELECT 16, 16, 16, '2026-08-15 16:10:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-15 - alerta atendida 2'

    UNION ALL SELECT 17, 17, 17, '2026-08-16 07:30:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-16 - alerta atendida 1'
    UNION ALL SELECT 18, 18, 18, '2026-08-16 11:15:00', 'Tres pulsaciones botón encendido', 'Auxilio Despachado', 'Consulta 3 - 2026-08-16 - auxilio despachado'
    UNION ALL SELECT 19, 19, 19, '2026-08-16 14:45:00', 'Reporte manual de central', 'Atendida', 'Consulta 3 - 2026-08-16 - alerta atendida 2'
    UNION ALL SELECT 20, 20, 20, '2026-08-16 19:20:00', 'Tres pulsaciones botón encendido', 'Falsa Alarma', 'Consulta 3 - 2026-08-16 - falsa alarma'
) AS datos
WHERE NOT EXISTS (
    SELECT 1
    FROM alerta existente
    WHERE existente.descripcion = datos.descripcion
);

COMMIT;

-- Reparte las 20 alertas entre cinco buses existentes (cuatro alertas por bus).
-- Se actualizan las filas del conjunto de Consulta 3; no se eliminan ni se duplican.
UPDATE alerta
SET id_bus = CASE descripcion
    WHEN 'Consulta 3 - 2026-08-12 - alerta atendida 1' THEN 1
    WHEN 'Consulta 3 - 2026-08-12 - falsa alarma' THEN 2
    WHEN 'Consulta 3 - 2026-08-12 - auxilio despachado' THEN 3
    WHEN 'Consulta 3 - 2026-08-12 - alerta atendida 2' THEN 4

    WHEN 'Consulta 3 - 2026-08-13 - alerta atendida 1' THEN 5
    WHEN 'Consulta 3 - 2026-08-13 - falsa alarma' THEN 1
    WHEN 'Consulta 3 - 2026-08-13 - auxilio despachado' THEN 2
    WHEN 'Consulta 3 - 2026-08-13 - alerta atendida 2' THEN 3

    WHEN 'Consulta 3 - 2026-08-14 - falsa alarma' THEN 4
    WHEN 'Consulta 3 - 2026-08-14 - alerta atendida 1' THEN 5
    WHEN 'Consulta 3 - 2026-08-14 - auxilio despachado' THEN 1
    WHEN 'Consulta 3 - 2026-08-14 - alerta atendida 2' THEN 2

    WHEN 'Consulta 3 - 2026-08-15 - auxilio despachado' THEN 3
    WHEN 'Consulta 3 - 2026-08-15 - alerta atendida 1' THEN 4
    WHEN 'Consulta 3 - 2026-08-15 - falsa alarma' THEN 5
    WHEN 'Consulta 3 - 2026-08-15 - alerta atendida 2' THEN 1

    WHEN 'Consulta 3 - 2026-08-16 - alerta atendida 1' THEN 2
    WHEN 'Consulta 3 - 2026-08-16 - auxilio despachado' THEN 3
    WHEN 'Consulta 3 - 2026-08-16 - alerta atendida 2' THEN 4
    WHEN 'Consulta 3 - 2026-08-16 - falsa alarma' THEN 5
    ELSE id_bus
END
WHERE descripcion LIKE 'Consulta 3 - 2026-08-%';

-- Verifica que quedaron 4 alertas en cada día y observa la distribución por estado.
SELECT DATE(fecha_hora) AS dia,
       estado,
       COUNT(*) AS cantidad
FROM alerta
WHERE descripcion LIKE 'Consulta 3 - 2026-08-%'
GROUP BY DATE(fecha_hora), estado
ORDER BY dia, estado;

-- Verifica la distribución de alertas por bus.
SELECT b.id_bus,
       b.codigo_unidad,
       b.placa,
       COUNT(*) AS total_alertas,
       SUM(a.estado = 'Atendida') AS atendidas,
       SUM(a.estado = 'Falsa Alarma') AS falsas_alarmas,
       SUM(a.estado = 'Auxilio Despachado') AS auxilio_despachado
FROM alerta a
JOIN bus b ON b.id_bus = a.id_bus
WHERE a.descripcion LIKE 'Consulta 3 - 2026-08-%'
GROUP BY b.id_bus, b.codigo_unidad, b.placa
ORDER BY total_alertas DESC, b.codigo_unidad;
