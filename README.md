# Sistema de alerta en tiempo real para optimizar la respuesta ante extorsiones en Carmen de la Punta S.A.

## Inicio actualizado y presentación de semana 16

Abre la carpeta completa en VS Code, selecciona **SAT-CARMEN (programa actualizado)** en Ejecutar y depurar y pulsa **F5**. Configura MySQL una sola vez desde el programa. Las consultas 01, 02 y 03 están en el menú **Consultas**. Todas exportan con JasperReports; el reporte adicional por día está en Reportes PDF.

Consulta [la guía para presentar en clase](docs/PRESENTAR_EN_CLASE.md), con el orden Java → Jasper Studio → MySQL. La conexión privada se guarda en `.local/` y no se sube a GitHub. `ABRIR_PROGRAMA.cmd` también recompila antes de iniciar; ya no reutiliza una compilación antigua tras un pull.

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

## Tecnologías

- Java
- Maven
- MySQL Server
- MySQL Workbench
- JDBC
- MySQL Connector/J
- Arquitectura MVC
- Patrón DAO
- Git
- GitHub

## Base de datos

La base de datos principal del sistema se denomina:

`sistema_alertas`

Los scripts SQL se encuentran en la carpeta:

`database/`

Archivos principales:

- `crear_base_datos.sql`
- `datos_prueba.sql`

## Arquitectura del proyecto

El sistema utiliza una arquitectura MVC complementada con el patrón DAO.

```text
src/main/java/
├── modelo/
├── dao/
├── conexion/
├── controlador/
└── vista/
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
## Consolidación de semanas 14 y 15

Reportes Jasper, Consulta 01 y registros integrados. Consulta docs/CONSOLIDACION.md para iniciar, probar, revisar las brechas y preparar semana 16. Ejecuta COMPILAR.ps1 e INICIAR.ps1 desde la raíz. Las funciones móviles, mapas y servicios en vivo siguen siendo prototipos; no son una plataforma operativa de emergencias.
