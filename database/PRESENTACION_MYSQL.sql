-- Solo consultas: no borra ni modifica datos.
-- En otro equipo cambia únicamente el nombre de la base de la línea siguiente.
USE sat_carmen_sem14_demo;
SELECT DATABASE() AS base_actual, @@port AS puerto;
SHOW TABLES;

SELECT 'alerta' AS tabla, COUNT(*) AS cantidad FROM alerta
UNION ALL SELECT 'asignacion_turno',COUNT(*) FROM asignacion_turno
UNION ALL SELECT 'bus',COUNT(*) FROM bus
UNION ALL SELECT 'cerco_preventivo',COUNT(*) FROM cerco_preventivo
UNION ALL SELECT 'comisaria',COUNT(*) FROM comisaria
UNION ALL SELECT 'conductor',COUNT(*) FROM conductor
UNION ALL SELECT 'despacho_auxilio',COUNT(*) FROM despacho_auxilio
UNION ALL SELECT 'evidencia_multimedia',COUNT(*) FROM evidencia_multimedia
UNION ALL SELECT 'notificacion',COUNT(*) FROM notificacion
UNION ALL SELECT 'operador',COUNT(*) FROM operador
UNION ALL SELECT 'telemetria_gps',COUNT(*) FROM telemetria_gps
UNION ALL SELECT 'usuario',COUNT(*) FROM usuario;

-- Consulta 02: ver las seis tablas relacionadas, incluso alertas sin despacho.
SELECT a.id_alerta, COALESCE(d.fecha_hora,a.fecha_hora) AS fecha_referencia,
       b.codigo_unidad,b.placa,a.estado AS estado_alerta,
       cm.nombre AS comisaria,CONCAT(o.nombres,' ',o.apellidos) AS operador,
       d.estado AS estado_despacho,cp.estado AS estado_cerco
FROM alerta a
JOIN bus b ON b.id_bus=a.id_bus
LEFT JOIN despacho_auxilio d ON d.id_despacho=(SELECT d2.id_despacho FROM despacho_auxilio d2 WHERE d2.id_alerta=a.id_alerta ORDER BY d2.fecha_hora DESC,d2.id_despacho DESC LIMIT 1)
LEFT JOIN comisaria cm ON cm.id_comisaria=d.id_comisaria
LEFT JOIN operador o ON o.id_operador=COALESCE(d.id_operador,a.id_operador)
LEFT JOIN cerco_preventivo cp ON cp.id_cerco=(SELECT cp2.id_cerco FROM cerco_preventivo cp2 WHERE cp2.id_alerta=a.id_alerta ORDER BY (d.id_comisaria IS NOT NULL AND cp2.id_comisaria=d.id_comisaria) DESC,cp2.fecha_inicio DESC,cp2.id_cerco DESC LIMIT 1)
ORDER BY fecha_referencia DESC;

-- Consulta 03: mismas cantidades por bus que muestra el formulario.
SELECT b.codigo_unidad,b.placa,COUNT(*) AS total,
       SUM(a.estado='Atendida') AS atendidas,
       SUM(a.estado='Falsa Alarma') AS falsas,
       SUM(a.estado='Auxilio Despachado') AS auxilio
FROM alerta a JOIN bus b ON b.id_bus=a.id_bus
GROUP BY b.id_bus,b.codigo_unidad,b.placa
ORDER BY total DESC,b.codigo_unidad;

-- Comprobación del total y del intervalo disponible para filtrar en clase.
SELECT COUNT(*) AS alertas,MIN(fecha_hora) AS primera_fecha,MAX(fecha_hora) AS ultima_fecha FROM alerta;
