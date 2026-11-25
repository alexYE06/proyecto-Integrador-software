-- Solo lectura. Seleccionar antes la base a comprobar (USE nombre_base).
SELECT DATABASE() AS base_seleccionada;
SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA=DATABASE()
ORDER BY TABLE_NAME, ORDINAL_POSITION;
-- El resultado debe estar vacío; no sustituye comparar tipos y claves del esquema canónico.
SELECT requerido.tabla AS tabla_faltante
FROM (
 SELECT 'usuario' tabla UNION ALL SELECT 'conductor' UNION ALL SELECT 'operador'
 UNION ALL SELECT 'bus' UNION ALL SELECT 'comisaria' UNION ALL SELECT 'asignacion_turno'
 UNION ALL SELECT 'alerta' UNION ALL SELECT 'telemetria_gps' UNION ALL SELECT 'evidencia_multimedia'
 UNION ALL SELECT 'despacho_auxilio' UNION ALL SELECT 'cerco_preventivo' UNION ALL SELECT 'notificacion'
) requerido
LEFT JOIN information_schema.TABLES t ON t.TABLE_SCHEMA=DATABASE() AND t.TABLE_NAME=requerido.tabla
WHERE t.TABLE_NAME IS NULL;
