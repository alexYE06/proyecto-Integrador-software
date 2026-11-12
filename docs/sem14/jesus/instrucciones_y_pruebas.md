# Plan de la Semana 14: Configuración, Diagnóstico y Ejecución
**Rol:** Jesús Miguel Horna Ticlayauri  
**Proyecto:** Sistema de Alertas en Tiempo Real - Carmen de la Punta S.A.  
**Rama:** `sem14/jesus-ejecucion`

---

## 1. Resumen de Objetivos Implementados

1. **Parametrización externa de la base de datos (`ConexionBD.java`):**
   - Lectura jerárquica: Propiedades de sistema (`-Ddb.host`, etc.) > Variables de entorno (`DB_HOST`, etc.) > Archivo `db.properties` > Fallback por defecto (`localhost:3307`, `sistema_alertas`, `root`, `123456`).
   - Conservación estricta de la firma `public static Connection obtenerConexion() throws SQLException` para no alterar ningún DAO existente.
   - Método auxiliar `obtenerResumenConfiguracion()` que expone host, puerto, base y usuario, protegiendo absolutamente la contraseña.
   - Creación de `db.properties.example` y protección de `db.properties` en `.gitignore`.

2. **Herramienta de Diagnóstico (`diagnostico.VerificarEntorno.java`):**
   - Inspección previa de configuración activa sin mostrar contraseñas.
   - Prueba de apertura de conexión con captura de `SQLException`, diagnóstico de causas probables y salida con código de error si falla (sin simular datos).
   - Auditoría de las **12 tablas oficiales** del modelo de datos:
     1. `usuario`
     2. `conductor`
     3. `operador`
     4. `bus`
     5. `comisaria`
     6. `asignacion_turno`
     7. `alerta`
     8. `telemetria_gps`
     9. `evidencia_multimedia`
     10. `despacho_auxilio`
     11. `cerco_preventivo`
     12. `notificacion`
   - Conteo real de registros por tabla (`SELECT COUNT(*)`) indicando si cumple la meta de la Semana 14 (`[OK >= 50]` o `[PENDIENTE: faltan X para 50]`).
   - Resumen final en consola indicando porcentaje de existencia de tablas y cumplimiento de registros.

---

## 2. Preparación del Entorno

### Requisitos:
- Java JDK 21
- Apache Maven 3.8+
- Servidor MySQL 8.0+ / MariaDB 10.4+

### Compilación limpia del proyecto:
```bash
mvn clean compile
```

---

## 3. Pruebas de los Tres Escenarios Requeridos

### ESCENARIO 1: Conexión Válida y Auditoría de Entorno

#### Objetivo:
Verificar que cuando el servidor MySQL está activo y la base de datos `sistema_alertas` existe con sus 12 tablas, el diagnóstico se conecta, audita las tablas y muestra los registros reales.

#### Procedimiento:
1. Asegurarse de que MySQL esté encendido y que se haya ejecutado el script `database/crear_base_datos.sql`.
2. Si su MySQL utiliza el puerto 3306 (estándar) o una contraseña personalizada, crear `db.properties` a partir de `db.properties.example` con los datos correspondientes.
3. Ejecutar:
```bash
mvn exec:java -Dexec.mainClass=diagnostico.VerificarEntorno
```

#### Salida esperada en consola:
```text
================================================================================
 SISTEMA DE ALERTAS EN TIEMPO REAL - CARMEN DE LA PUNTA S.A.
 DIAGNÓSTICO Y VERIFICACIÓN DEL ENTORNO (SEMANA 14)
 Rol: Jesús - Configuración, Diagnóstico y Ejecución
================================================================================

[1] CONFIGURACIÓN DE CONEXIÓN:
    Host: localhost | Puerto: 3306 | Base de Datos: sistema_alertas | Usuario: root
    URL JDBC: jdbc:mysql://localhost:3306/sistema_alertas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

[2] PROBANDO CONEXIÓN CON MYSQL...
    -> ¡CONEXIÓN EXITOSA! Motor MySQL respondiendo correctamente.
    -> Catálogo actual: sistema_alertas

[3] AUDITORÍA DE TABLAS Y REGISTROS REALES (SEMANA 14)
+----+-----------------------+----------+--------------------+----------------------------------+
| #  | Nombre de la Tabla    | ¿Existe? | Registros Reales   | Estado para Semana 14            |
+----+-----------------------+----------+--------------------+----------------------------------+
| 1  | usuario               | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
| 2  | conductor             | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
| 3  | operador              | SÍ       | 1                  | [PENDIENTE: faltan 49 para 50]   |
| 4  | bus                   | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
| 5  | comisaria             | SÍ       | 1                  | [PENDIENTE: faltan 49 para 50]   |
| 6  | asignacion_turno      | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
| 7  | alerta                | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
| 8  | telemetria_gps        | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
| 9  | evidencia_multimedia  | SÍ       | 1                  | [PENDIENTE: faltan 49 para 50]   |
| 10 | despacho_auxilio      | SÍ       | 1                  | [PENDIENTE: faltan 49 para 50]   |
| 11 | cerco_preventivo      | SÍ       | 1                  | [PENDIENTE: faltan 49 para 50]   |
| 12 | notificacion          | SÍ       | 2                  | [PENDIENTE: faltan 48 para 50]   |
+----+-----------------------+----------+--------------------+----------------------------------+

================================================================================
 RESUMEN GENERAL DEL ENTORNO - SEMANA 14
================================================================================
  * Total de tablas analizadas            : 12
  * Tablas existentes en la base de datos : 12 / 12 (100.0%)
  * Tablas con meta >= 50 registros       : 0 / 12 (0.0%)
--------------------------------------------------------------------------------
  ESTADO FINAL: [ESTRUCTURA OK - POBLACIÓN PENDIENTE]
  Las 12 tablas existen en la base de datos.
  Actualmente 12 tablas tienen menos de 50 filas (muestran registros reales actuales).
  Siguiente paso: Ejecutar script de carga masiva de datos de prueba para alcanzar la meta.
================================================================================
```
*Código de salida: 0 (Éxito de conexión y tablas presentes).*

---

### ESCENARIO 2: Puerto Incorrecto o Servicio Detenido

#### Objetivo:
Verificar que la herramienta capture el fallo de red/puerto, muestre los parámetros probados sin filtrar contraseñas, emita un diagnóstico en español explicando la causa probable y termine con código de error (distinto de cero) sin inventar datos.

#### Procedimiento:
Ejecutar pasando un puerto inválido (por ejemplo, `9999`) mediante propiedad del sistema o modificando temporalmente `db.port` en `db.properties`:
```bash
mvn exec:java -Dexec.mainClass=diagnostico.VerificarEntorno -Ddb.port=9999
```

#### Salida esperada en consola:
```text
================================================================================
 SISTEMA DE ALERTAS EN TIEMPO REAL - CARMEN DE LA PUNTA S.A.
 DIAGNÓSTICO Y VERIFICACIÓN DEL ENTORNO (SEMANA 14)
 Rol: Jesús - Configuración, Diagnóstico y Ejecución
================================================================================

[1] CONFIGURACIÓN DE CONEXIÓN:
    Host: localhost | Puerto: 9999 | Base de Datos: sistema_alertas | Usuario: root
    URL JDBC: jdbc:mysql://localhost:9999/sistema_alertas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

[2] PROBANDO CONEXIÓN CON MYSQL...

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 ERROR DE CONEXIóN CON LA BASE DE DATOS
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
Detalles técnicos:
  * Código SQLState : 08S01
  * Código de Error : 0
  * Mensaje         : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.

Diagnóstico y Causa Probable:
  -> FALLO DE COMUNICACIÓN (Communications link failure):
     No se puede conectar al servidor MySQL en localhost:9999.
  -> Solución:
     1. Asegúrese de que el servicio de MySQL / MariaDB esté ENCENDIDO.
     2. Si su MySQL corre en el puerto 3306 (estándar) y el proyecto busca 3307,
        cree un archivo 'db.properties' con la línea: db.port=3306
        o defina la variable de entorno DB_PORT=3306.
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
```
*Código de salida: 1 (Fallo controlado).*

---

### ESCENARIO 3: Base de Datos Inexistente

#### Objetivo:
Verificar que cuando el servidor MySQL está activo pero se solicita una base de datos que no existe, la herramienta capture el error SQL 1049 (`Unknown database`), muestre la causa en español y sugiera ejecutar el script de creación.

#### Procedimiento:
Ejecutar pasando una base de datos que no exista (por ejemplo, `base_inexistente_xyz`):
```bash
mvn exec:java -Dexec.mainClass=diagnostico.VerificarEntorno -Ddb.name=base_inexistente_xyz
```

#### Salida esperada en consola:
```text
================================================================================
 SISTEMA DE ALERTAS EN TIEMPO REAL - CARMEN DE LA PUNTA S.A.
 DIAGNÓSTICO Y VERIFICACIÓN DEL ENTORNO (SEMANA 14)
 Rol: Jesús - Configuración, Diagnóstico y Ejecución
================================================================================

[1] CONFIGURACIÓN DE CONEXIÓN:
    Host: localhost | Puerto: 3306 | Base de Datos: base_inexistente_xyz | Usuario: root
    URL JDBC: jdbc:mysql://localhost:3306/base_inexistente_xyz?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

[2] PROBANDO CONEXIÓN CON MYSQL...

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 ERROR DE CONEXIÓN CON LA BASE DE DATOS
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
Detalles técnicos:
  * Código SQLState : 42000
  * Código de Error : 1049
  * Mensaje         : Unknown database 'base_inexistente_xyz'

Diagnóstico y Causa Probable:
  -> BASE DE DATOS INEXISTENTE (Error 1049): No existe 'base_inexistente_xyz'.
  -> Solución: Abra MySQL Workbench o terminal y ejecute el script 'database/crear_base_datos.sql'.
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
```
*Código de salida: 1 (Fallo controlado).*

---

## 4. Conclusión de la Ejecución

- Todos los DAOs y vistas existentes mantienen compatibilidad total al usar `ConexionBD.obtenerConexion()`.
- Ninguna contraseña se almacena ni se expone en repositorios públicos.
- El diagnóstico permite a cualquier integrante o docente validar inmediatamente el estado real del entorno local y de las tablas de datos para la entrega de la Semana 14.
