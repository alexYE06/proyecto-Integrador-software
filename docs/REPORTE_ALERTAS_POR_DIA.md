# Reporte estadístico de alertas por día

Plantilla JasperReports 6.21.3: `src/main/resources/reports/reporte_alertas_por_dia.jrxml`.
El logo institucional empleado por el encabezado está en `src/main/resources/reports/logo_proyecto.png`.

## Funcionalidad

El reporte agrupa los registros de `alerta` por `DATE(fecha_hora)` y presenta un resumen por día, cuatro indicadores para el periodo y una gráfica apilada. Las fechas sin alertas no se dibujan como días con cero: la base entregada no define una tabla calendario y el reporte no agrega tablas.

Estados incluidos en las categorías, exactamente como los define el requerimiento:

- Atendidas: `Atendida`.
- Falsas alarmas: `Falsa Alarma`.
- Abiertas: `Recibida`, `En Evaluación`, `Auxilio Despachado`.

El total diario cuenta todos los registros de alerta del día. Por ello, si la base contiene otros estados, estos forman parte del total, pero no de esas tres categorías.

## Consulta principal

La consulta de la plantilla agrega primero el día y sus conteos; luego, funciones de ventana suman los conteos diarios para dar valores del periodo a las tarjetas, sin volver a unir filas ni duplicar alertas. Sintaxis compatible con MySQL 8 o superior (el proyecto usa MySQL 26.7.0):

```sql
SELECT d.*,
       SUM(d.total_alertas) OVER () AS periodo_total,
       SUM(d.atendidas) OVER () AS periodo_atendidas,
       SUM(d.falsas_alarmas) OVER () AS periodo_falsas_alarmas,
       SUM(d.abiertas) OVER () AS periodo_abiertas
FROM (
    SELECT DATE(a.fecha_hora) AS dia,
           COUNT(*) AS total_alertas,
           COUNT(CASE WHEN a.estado = 'Atendida' THEN 1 END) AS atendidas,
           COUNT(CASE WHEN a.estado = 'Falsa Alarma' THEN 1 END) AS falsas_alarmas,
           COUNT(CASE WHEN a.estado IN
               ('Recibida', 'En Evaluación', 'Auxilio Despachado') THEN 1 END) AS abiertas
    FROM alerta a
    WHERE ($P{FECHA_INICIO} IS NULL OR a.fecha_hora >= $P{FECHA_INICIO})
      AND ($P{FECHA_FIN} IS NULL OR a.fecha_hora < $P{FECHA_FIN})
    GROUP BY DATE(a.fecha_hora)
) d
ORDER BY d.dia;
```

La consulta se repite en el subdataset `GraficoDiario` con los mismos filtros, para que el chart reciba una serie diaria independiente. El único campo de fecha usado es `alerta.fecha_hora`; no se usan relaciones ni columnas adicionales.

## Parámetros, fields y cálculos

| Parámetro / field | Clase Java | Uso |
|---|---|---|
| `FECHA_INICIO` | `java.sql.Timestamp` | Incluye la fecha/hora indicada (`>=`). Nulo elimina el límite inferior. |
| `FECHA_FIN` | `java.sql.Timestamp` | Excluye el instante indicado (`<`). Nulo elimina el límite superior. |
| `dia` | `java.sql.Date` | Día del resumen. |
| `total_alertas`, `atendidas`, `falsas_alarmas`, `abiertas` | `java.lang.Long` | Conteos del día; MySQL `COUNT` devuelve entero de 64 bits. |
| `periodo_total`, `periodo_atendidas`, `periodo_falsas_alarmas`, `periodo_abiertas` | `java.math.BigDecimal` | Sumas de ventana usadas por las cuatro tarjetas. MySQL `SUM` sobre enteros devuelve decimal. |

No hace falta crear variables de suma en Jasper: las tarjetas se muestran en el encabezado del grupo `ResumenPeriodo`, antes de las filas del detalle, usando los totales de ventana que entrega la misma consulta. Las variables `Sum` de detalle no estarían calculadas todavía en ese encabezado. Así se evita una consulta KPI adicional.

La pantalla de reportes convierte el último día seleccionado al inicio del día siguiente y lo manda como `FECHA_FIN`. Por ejemplo, para incluir del 01 al 25 de noviembre, use `FECHA_INICIO=2026-11-01 00:00:00` y `FECHA_FIN=2026-11-26 00:00:00`. De esta forma se incluye todo el día 25, sin usar `23:59:59`.

## Diseño y bandas

- **Title:** logo a la izquierda, título y subtítulo; rango elegido y fecha/hora de emisión.
- **Group Header `ResumenPeriodo`:** cuatro tarjetas de KPI y gráfico de barras apiladas. El grupo constante se imprime una sola vez, pues el dataset devuelve una fila por día.
- **Column Header:** encabezados Día, Total, Atendidas, Falsas alarmas y Abiertas.
- **Detail:** un renglón por fecha, en orden cronológico; fondo alterno y formato `dd/MM/yyyy`.
- **Page Footer:** texto institucional y número de página.
- **NoData:** mensaje claro si no hay alertas en el rango.

Colores de la gráfica: verde para atendidas, ámbar para falsas alarmas y rojo para abiertas. La leyenda queda debajo de la gráfica; las etiquetas de categoría son `dd/MM/yy` para distinguir días de años diferentes. La página se configura en A4 horizontal.

## Logo y apertura en Jaspersoft Studio

El JRXML carga `reports/logo_proyecto.png` desde el classpath de la aplicación y mantiene sus proporciones (`RetainShape`). En Jaspersoft Studio, importe el proyecto Maven o añada `src/main/resources` al classpath del proyecto; verifique que el recurso aparezca como `reports/logo_proyecto.png`. No cambie el PNG ni reconstruya el logo.

Para conectar Studio, cree una conexión JDBC MySQL al esquema que contiene la tabla `alerta` y use la opción **Dataset and Query** para revisar la consulta principal. En el Outline, los parámetros ya están declarados como `java.sql.Timestamp`. Si Studio solicita valores, use valores timestamp; el extremo final debe ser el inicio del día siguiente al último día que se quiere incluir. Para dejar un extremo abierto, deje el parámetro nulo.

El gráfico está en el grupo y usa el subdataset `GraficoDiario`, los campos `dia_etiqueta`, `atendidas`, `falsas_alarmas` y `abiertas`, junto con la misma conexión JDBC del reporte. La tabla y el gráfico usan las mismas reglas de filtro y categorías.

## Integración y validación

La aplicación muestra la plantilla como **Alertas por día**, mapea la fecha final exclusiva y deshabilita los filtros de estado, bus y modalidad para este reporte, ya que no aplican a la consulta. Se verificó que `database/crear_base_datos.sql` declara los campos `fecha_hora` y `estado` en `alerta`, y que JasperReports 6.21.3 compila el JRXML. La compilación del código Maven requiere JDK 21, configurado como versión objetivo del proyecto.
