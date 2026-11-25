# Consolidación y auditoría de semanas 14 y 15

Revisión del 24 de noviembre de 2026, zona America/Lima. El proyecto conserva la arquitectura Java Swing, MVC y DAO. Los reportes y la consulta ya forman parte del menú central. La validación se realizó con MySQL 26.7 en una instancia aislada, no con la base habitual del usuario.

## Resultado y procedencia

Se contrastaron las entregas originales S14 y S15, el repositorio local y `origin/main` en `023868c`. El informe revisado fue el adjunto `Proyecto_Integrador_Grupo3_S15_COMPLETADO(1).docx`, recuperado desde la conversación anterior. Se extrajo su contenido para el contraste funcional; no se certificó su paginación ni su bibliografía.

La copia local y GitHub tienen historiales sin antecesor común. Se preservó el cambio sin commit en `DemoSelectorView` mediante `ddaede9` y se reunieron ambos historiales en una rama exclusivamente local. Se respaldaron los refs iniciales en un bundle y se copiaron los fuentes, scripts SQL y configuración antes de editar.

Se detectaron contraseñas fijas en commits locales anteriores. Por eso la publicación se prepara como un commit consolidado cuyo padre es `origin/main`: conserva el resultado del trabajo local y remoto sin publicar los antiguos commits locales sensibles. La rama local completa y el respaldo quedan disponibles. No se reescribe ni se fusiona `main`, ni se usa force push. La eliminación del valor en el código actual no sanea los secretos que pudieran estar publicados previamente: revisar y rotar esas credenciales.

## Integración realizada

- Tres plantillas JasperReports 6.21.3 con filtros y exportación: incidencias, estados y modalidades. Se incorporan las correcciones de S14: fecha final exclusiva, GPS más reciente sin multiplicar alertas, porcentajes y nombre real de la base.
- Consulta 01 de S15: filtros Y/O, fechas, placa, conductor, orden estable, tabla de resultados y PDF de la misma instantánea. La consulta se separó en `modelo.Consulta`, `dao.ConsultaDAO`, `servicio.ConsultaService` y `vista.Consulta01`.
- Se conserva `Consulta01.jrxml` para Jaspersoft Studio. El botón PDF de Consulta 01 usa OpenPDF; no utiliza esa plantilla. Las tres opciones de Reportes sí utilizan JasperReports. Ambas rutas se compilaron y exportaron.
- Conexión compartida para todos los módulos mediante `ConexionBD`, sin contraseña incrustada. Se mantienen las variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`; se añaden `DB_SSL_MODE` y `DB_ALLOW_PUBLIC_KEY_RETRIEVAL`.
- Menú Registros para conductor, bus, operador y alerta. Se conserva el arreglo remoto que registra los buses como `Activo`.
- Gestión real de incidentes añadida como pantalla propia, con actualización persistida y detección de cambios concurrentes. Se conserva el diseño anterior y se identifica como simulación, corrigiendo sus mensajes que antes afirmaban guardar o despachar a la Policía.
- Esquema vacío principal aditivo con `IF NOT EXISTS` y carga académica separada. No se importaron directorios físicos MySQL, ni se modificaron las bases existentes. `IF NOT EXISTS` no migra columnas incompatibles de una base previa.

## Contraste con el informe y pendientes de semana 16

| Área | Evidencia en el código y pruebas | Pendiente concreto |
|---|---|---|
| Login central | `UsuarioDAO` consulta MySQL; caso correcto e incorrecto probados | Migrar contraseñas de usuarios a hash con sal, controlar roles y sesión. El esquema actual conserva texto plano por compatibilidad |
| Login conductor | Abre el dashboard sin validar credenciales | Implementar autenticación y asociación usuario-conductor; por ahora se rotula simulación |
| Registros | DAO de conductor, bus, operador y alerta probados; las cuatro ventanas se abren desde el menú | Validación completa de formularios por el equipo; probar roles, errores de red y duplicados concurrentes de usuario |
| Gestión de estados | Persiste en MySQL y rechaza estado inválido/cambio concurrente | Agregar bitácora, reglas de transición y autorización por rol |
| Tres reportes, cap. 6.1 | Jasper, filtros, vacíos y PDF probados; totales de 50 en los PDF académicos | Validar sobre copia autorizada de la base habitual y catálogo real de estados/modalidades |
| Consulta 01, cap. 6.2 | 50 filas sin duplicación, Y/O 1/11, fechas, LIKE literal y PDF | El reporte Studio y el PDF OpenPDF son dos implementaciones: mantener equivalencia de filtros con pruebas |
| GPS y flota | Vistas con mapa y métricas dibujadas; consultas leen telemetría almacenada | No equivale a GPS en vivo ni a 80 buses conectados |
| Haversine | Referencias visuales/textuales; no existe `HaversineAlgorithm.java` en los fuentes revisados | Implementar y probar distancias; no prometer rutas ni tiempos de llegada solo por distancia geodésica |
| WebSockets y frontend | `frontend` contiene tres archivos vacíos, sin proyecto ejecutable | Backend, cliente, protocolo y pruebas de extremo a extremo pendientes |
| PNP, SMS y multimedia | Mensajes/prototipos; no se encontró integración operativa | No afirmar despacho policial, streaming, cifrado AES-256 ni cadena de custodia reales |
| Android y botón físico | Simulación Swing | App/dispositivo, segundo plano, PIN, permisos y telemetría pendientes |
| KPIs, Excel y mapas de calor | Etiquetas de interfaz; PDF operativo | Medir latencia; implementar Excel/mapas. No presentar cifras de pantalla como mediciones |

Prioridad S16: (1) conexión a una copia de la base habitual y revisión del esquema; (2) autenticación/roles/contraseñas y bitácora; (3) acordar qué prototipos quedan fuera del alcance; (4) demostración reproducible del recorrido login → registro → consulta → reporte; (5) revisión del cambio en GitHub antes de fusionar. La integración académica está validada; no constituye una certificación para uso operativo ante emergencias.

## Validación realizada

JDK 26.0.1 compilando con `--release 21`, MySQL 26.7.0, Connector/J 26.7.0, JasperReports 6.21.3 y OpenPDF 1.3.32. Se usaron las bibliotecas locales de S15. Maven no estaba instalado y no se verificó una descarga limpia de dependencias con Maven.

La batería `RegressionSuite` completó 46 comprobaciones: cantidad y unicidad, filtros AND/OR, fechas válidas/inválidas, orden, LIKE de inicio/final, `%` y `_` literales, texto de inyección, salida vacía, tres reportes generales/filtrados/vacíos, PDF legibles, plantilla Studio, actualización persistida, conflicto concurrente, estados inválidos, registro de conductor/bus/operador/alerta, placa duplicada y login correcto/incorrecto. Se inspeccionaron visualmente las 17 páginas de seis PDF producidos. La primera validación usó los datos académicos de la entrega. Para publicar se sustituyó el nuevo fixture por datos completamente ficticios (nombres Demo/Ficticio, teléfonos cero, vehículos TST, coordenadas cero), y se repitió la batería. No se publica una nueva copia del dataset personal de la entrega. Las entregas originales y las bases existentes se conservan intactas.

`UiSmoke` abrió las diez opciones de los menús, incluidas las cuatro ventanas de registro, y capturó las pantallas integradas. Es una prueba programática de apertura/navegación; no certifica todos los flujos manuales. Los DAO fueron probados directamente, no rellenando todos los formularios con ratón y teclado.

Advertencias no bloqueantes observadas: API interna obsoleta de OpenPDF con JDK 26 y atributo Jasper `isStretchWithOverflow` obsoleto. La instancia habitual del puerto 3307 rechazó la conexión sin contraseña; no se inspeccionó ni modificó su contenido. La instancia temporal de prueba usa solo loopback y se detiene al finalizar la auditoría.

## Compilar y abrir

Desde la raíz del proyecto, usar JDK 21 o superior. La carpeta `lib` local contiene las bibliotecas originales de S15 y está excluida de Git. En otro equipo, instalar Maven y ejecutar `mvn dependency:copy-dependencies -DoutputDirectory=lib` o copiar las bibliotecas de la entrega S15. No versionar credenciales ni datos físicos de MySQL.

```powershell
.\COMPILAR.ps1
.\INICIAR.ps1 -DbHost 127.0.0.1 -Port 3306 -Database sistema_alertas -DbUser root
```

El lanzador solicita la contraseña sin escribirla en disco. Para usar una base existente, comprobar primero sus tablas/columnas con `database/verificar_esquema.sql`. No cargar `datos_prueba.sql` ni la demo sobre datos reales. `database/crear_base_datos.sql` crea el esquema vacío; no corrige automáticamente tablas que ya existan. `database/init.sql` permanece como archivo histórico vacío.

## Repetir las pruebas sin tocar datos existentes

Crear un directorio **nuevo** y una instancia MySQL exclusiva para pruebas. No reutilizar un directorio de producción. Sustituir la ruta del ejecutable por la instalación local:

```powershell
$mysqlBin = 'C:\Program Files\MySQL\MySQL Server 26.7\bin'
$testData = Join-Path $PWD 'target\mysql-prueba-nueva'
& "$mysqlBin\mysqld.exe" --no-defaults --initialize-insecure "--datadir=$testData"
# En una terminal separada (mantener abierta):
& "$mysqlBin\mysqld.exe" --no-defaults "--datadir=$testData" --port=3317 --bind-address=127.0.0.1 --mysqlx=OFF --skip-log-bin --console
# En la terminal del proyecto:
.\database\demo\CARGAR_DEMO.ps1 -Mysql "$mysqlBin\mysql.exe" -Port 3317
$env:DB_HOST='127.0.0.1'; $env:DB_PORT='3317'
$env:DB_NAME='sat_carmen_integracion_prueba'; $env:DB_USER='root'; $env:DB_PASSWORD=''
.\PROBAR.ps1
& "$mysqlBin\mysqladmin.exe" --host=127.0.0.1 --port=3317 --user=root shutdown
```

El cargador rechaza una base de pruebas ya existente. La batería añade datos ficticios y exige las 50 alertas iniciales; para repetirla se prepara otra instancia nueva, conservando la anterior. Nunca borrar datos para hacer pasar la prueba. Los PDF quedan en `target/evidencias`, excluidos de Git. No usar las cuentas académicas como cuentas de producción.

Para probar las ventanas, con la base de pruebas activa:

```powershell
javac -encoding UTF-8 --release 21 -cp 'target/classes;lib/*' -d target/test-classes src/test/java/UiSmoke.java
java -cp 'target/test-classes;target/classes;lib/*' UiSmoke
```
