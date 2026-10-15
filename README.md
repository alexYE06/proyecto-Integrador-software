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