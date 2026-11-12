# Sistema de alerta en tiempo real para optimizar la respuesta ante extorsiones en Carmen de la Punta S.A.

## Proyecto Integrador I

Sistema orientado al monitoreo y gestión de alertas de emergencia para apoyar la respuesta ante situaciones de extorsión que afecten a conductores y unidades de transporte.

La función principal del sistema considera la generación silenciosa de una alerta mediante tres pulsaciones consecutivas del botón de encendido del dispositivo del conductor.

## Integrantes

- Alex Jeanpierre Mejia Huanuqueño
- Leonardo Duran Chavez Alfonso
- Josef Amderson Chavez Bernuy
- Piero Alexis Yauri Esteban
- Jesús Miguel Horna Ticlayauri
- Nayely Merarí Ramirez Garcia

## Requisitos del Sistema

Para compilar y ejecutar el proyecto se requiere:

1. **Java Development Kit (JDK):** Versión 21 (o superior con soporte para `--release 21`).
2. **Apache Maven:** Versión 3.8.0 o superior.
3. **Servidor MySQL:** MySQL Server 8.0+ o MariaDB 10.4+.
4. **Base de Datos:** Base de datos `sistema_alertas` inicializada con el script `database/crear_base_datos.sql`.

## Configuración Externa de Base de Datos (Semana 14)

El sistema soporta configuración externa de conexión sin modificar el código fuente Java y evitando subir credenciales personales a Git.

### Jerarquía de Prioridad:
1. **Propiedades del sistema JVM:** `-Ddb.host=... -Ddb.port=... -Ddb.name=... -Ddb.user=... -Ddb.password=...`
2. **Variables de entorno:** `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.
3. **Archivo local `db.properties`:** Ubicado en la raíz del proyecto o en `config/db.properties`.
4. **Valores por defecto (Fallback):** Host `localhost`, Puerto `3307`, Base de datos `sistema_alertas`, Usuario `root`, Clave `123456`.

### Pasos para configurar en otra laptop:
1. En la raíz del proyecto, copia el archivo de plantilla:
   ```bash
   cp db.properties.example db.properties
   ```
   *(En Windows PowerShell: `Copy-Item db.properties.example db.properties`)*
2. Abre `db.properties` y ajusta los valores de tu entorno local:
   ```properties
   db.host=localhost
   db.port=3306
   db.name=sistema_alertas
   db.user=root
   db.password=tu_contraseña_local
   ```
3. El archivo `db.properties` ya está añadido a `.gitignore`, por lo que tus contraseñas nunca se subirán al repositorio.

## Comandos de Construcción y Ejecución con Maven

Todos los comandos deben ejecutarse desde la carpeta raíz del proyecto. Se realiza una construcción limpia para asegurar que no dependa de binarios previos en `target/`.

### 1. Limpieza y compilación limpia:
```bash
mvn clean compile
```

### 2. Ejecución del Diagnóstico del Entorno (Verificación Semana 14):
Valida la conectividad con MySQL, comprueba la existencia de las 12 tablas oficiales del modelo de datos y muestra el conteo real de registros auditando la meta de >= 50 filas:
```bash
mvn exec:java -Dexec.mainClass=diagnostico.VerificarEntorno
```
> *Nota para PowerShell:* Si se pasan argumentos con comillas, ejecute:  
> `mvn exec:java "-Dexec.mainClass=diagnostico.VerificarEntorno"`

### 3. Ejecución de la Aplicación Principal (Consola de la Central / Login):
```bash
mvn exec:java -Dexec.mainClass=vista.LoginCentralView
```

### 4. Ejecución del Selector de Demostración:
```bash
mvn exec:java -Dexec.mainClass=vista.DemoSelectorView
```

## Base de datos

La base de datos principal del sistema se denomina:

`sistema_alertas`

Los scripts SQL se encuentran en la carpeta:

`database/`

Archivos principales:

- `crear_base_datos.sql`: Creación de la base de datos y las 12 tablas relacionales.
- `datos_prueba.sql`: Inserción de registros iniciales de prueba.

### Las 12 tablas oficiales:
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

## Arquitectura del proyecto

El sistema utiliza una arquitectura MVC complementada con el patrón DAO.

```text
src/main/java/
├── modelo/          # Entidades de negocio
├── dao/             # Acceso a datos y consultas SQL
├── conexion/        # ConexionBD centralizada y parametrizable
├── controlador/     # Lógica de flujo y control
├── vista/           # Vistas e interfaces gráficas Swing
└── diagnostico/     # Herramienta de verificación de entorno y tablas
```

## Formularios de la central

Desde el menú lateral de la central se puede abrir:

- **Registrar alerta:** selecciona un conductor y una unidad activos, permite asignar
  opcionalmente un operador y guarda el tipo de activación, estado y descripción en
  la tabla `alerta`.
- **Registrar operador:** guarda el perfil en `operador` y crea su cuenta con rol
  `Operador` en `usuario` dentro de una única transacción.

Para registrar una alerta deben existir conductores y buses con estado `Activo`.
Los dos formularios utilizan la conexión JDBC configurada en `ConexionBD`.

## Documentación y Pruebas de la Semana 14

Para ver la guía de validación de los tres escenarios de prueba (Conexión exitosa, Puerto incorrecto y Base de datos inexistente), consulte:
- [Guía de Pruebas Semana 14 - Rol Jesús](docs/sem14/jesus/instrucciones_y_pruebas.md)
