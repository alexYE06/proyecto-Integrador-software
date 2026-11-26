# Presentar SAT-CARMEN en clase

## 1. Abrir el programa desde VS Code

1. Abre la carpeta completa `proyecto-Integrador-software` en VS Code.
2. Guarda los archivos que tengas abiertos. Cierra las ventanas viejas de SAT-CARMEN.
3. En **Ejecutar y depurar** (Ctrl+Mayús+D), selecciona **SAT-CARMEN (programa actualizado)** y pulsa F5.
4. Espera a que compile. Entra en **Entorno Central** y usa `operador1` / `op123` en la base demo de Lian.

El programa se vuelve a compilar antes de abrirse: un pull ya no deja funcionando la copia anterior. También funciona ABRIR_PROGRAMA.cmd. El archivo de inicio del código es `src/main/java/app/SatCarmen.java`; en otros editores, ejecuta esa clase con la carpeta raíz como directorio de trabajo.

La contraseña de MySQL se guarda una vez en este equipo. La cuenta del operador es otra cosa: sirve para entrar a la central y se mantiene. En otro equipo, cada integrante debe configurar su propia conexión desde **Configurar MySQL**. No cambies las claves de MySQL ni borres tu base.

## 2. Mostrar las consultas funcionando

- Menú superior **Consultas → Consulta 02**. Primero consulta sin fechas para ver datos. Después filtra por estado de alerta. Muestra la tabla y **Exportar PDF**.
- **Consultas → Consulta 03**. Primero deja fechas vacías, pulsa **Consultar**, luego **Previsualizar** y **Exportar PDF**. Si quieres una prueba de un día en la base demo, usa 2026-11-08 en ambas fechas.
- Consulta 03 muestra todos los buses en la tabla. Su gráfico por bus muestra los diez con más alertas para mantener legibles los nombres; no elimina las demás filas.
- El reporte adicional **Alertas por día** está en **Módulos → Reportes PDF (Filtros)**. Agrupa por fecha; no es la tabla por bus de Consulta 03.

## 3. Mostrar Jasper Studio

Abre Jaspersoft Studio 6.21.x. Las plantillas que debes enseñar están en:

- `src/main/resources/reports/Consulta01.jrxml`
- `src/main/resources/reports/Consulta02.jrxml`
- `src/main/resources/reports/Consulta03.jrxml`
- `src/main/resources/reports/reporte_alertas_por_dia.jrxml` (reporte adicional)

Usa **File → Open File** para abrir Consulta02 y Consulta03. En **Design** se ve el diseño. En **Source** se ven sus parámetros, SQL y campos. En **Preview** selecciona un adaptador MySQL: **One Empty Record** no sirve para consultar esta base.

En el equipo de Lian está preparado `.local/SAT-CARMEN.xml`. Impórtalo desde **Repository Explorer → Data Adapters** y selecciona **SAT-CARMEN local** al previsualizar. Ese archivo contiene la conexión privada y NO se comparte con el equipo. Si Studio pide una contraseña, usa la de MySQL Workbench. En otros equipos, `PREPARAR_JASPER.ps1` genera su propio adaptador tras configurar la conexión.

Para Consulta02 deja los parámetros en sus valores iniciales; para Consulta03 deja FECHA_INICIO y FECHA_FIN nulos para mostrar todo. FECHA_FIN representa el comienzo del día siguiente al último día deseado: para incluir el 08/11/2026 completo, usa 09/11/2026 00:00:00 en Studio. El formulario del programa hace esa conversión por ti.

Los archivos en `docs/jasper-originales` son copias históricas recibidas del equipo. No los uses para presentar: uno es anterior y el otro está en formato Jasper 7. Las plantillas activas son las de `resources/reports`.

## 4. Mostrar MySQL Workbench

En el equipo de Lian, abre **Local instance MySQL80**, puerto **3307**, base **sat_carmen_sem14_demo**. No hace falta sustituir esta base.

Abre `database/PRESENTACION_MYSQL.sql`. Ejecuta por bloques: identificación de base/puerto, tablas, cantidades, consulta 02 y consulta 03. Todas son lecturas: no modifican los datos. Los integrantes que usen otra base deben cambiar el USE de la primera línea.

Puedes explicar: «Java muestra y filtra los datos; Jasper prepara el reporte; MySQL guarda los registros». La base demo contiene datos académicos: los despachos/cercos marcados Simulado no prueban una conexión real con la policía.

## Verificación y trabajo en equipo

`VERIFICAR_PRESENTACION.ps1` comprueba las siete plantillas y el PDF de Consulta01 sin MySQL. Con `-MySQL` también lee la base configurada, ejecuta las tres consultas, exporta sus PDF y los cuatro reportes, y comprueba la cuenta académica operador1/op123. No modifica MySQL. Los archivos de prueba quedan en `target/evidencias-presentacion`.

Validación local del 26/11/2026: compilación correcta, siete plantillas compiladas, tres consultas con datos, PDF de consultas y reportes legibles, casos vacíos de Consulta01/03 correctos, operador1 aceptado y 50 alertas en sat_carmen_sem14_demo. No se automatizó la vista Preview de Studio ni F5 en VS Code; estos pasos deben ensayarse antes de clase.

Los cambios se entregan en una rama de revisión. Después de revisar y fusionar el PR, el equipo puede actualizar main y ejecutar con F5. Cada integrante conserva su configuración local: `.local`, `lib` y `target` no se suben. Para una copia nueva se necesita JDK 21 o superior y Maven para descargar las bibliotecas mediante COMPILAR.ps1.
