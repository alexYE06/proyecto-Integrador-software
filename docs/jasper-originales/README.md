# Originales recibidos del equipo

Las plantillas activas están en `src/main/resources/reports/`.

- `Consulta02-anterior.jrxml`: original recibido en la carpeta suelta. Es anterior a la versión integrada: parte de despacho y no incluye los filtros nuevos de estado de alerta y cerco. No reemplazar la plantilla activa con este archivo.
- `Consulta03-diario-Studio7.jrxml`: original en formato JasperReports 7. La aplicación usa JasperReports 6.21.3 y el equipo local tiene Studio 6.21.5. Su SQL se conserva en la adaptación activa `reporte_alertas_por_dia.jrxml`, accesible desde Reportes → Alertas por día. El diseño se adaptó al formato 6.
- `Consulta03.jrxml` activa corresponde al resumen por bus. El reporte diario es una opción adicional, no un reemplazo de la consulta por bus.

Consulta 01 utiliza ahora su plantilla Jasper al exportar las mismas filas visibles, sin volver a consultar la base.
