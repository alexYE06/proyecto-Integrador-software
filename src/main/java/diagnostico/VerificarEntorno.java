package diagnostico;

import conexion.ConexionBD;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Herramienta de Diagnóstico y Verificación del Entorno de Base de Datos
 * Rol: Jesús - Configuración, Diagnóstico y Ejecución (Semana 14)
 *
 * Funcionalidades:
 * 1. Inspecciona y muestra los parámetros de conexión resueltos (sin revelar contraseñas).
 * 2. Prueba la conectividad real con MySQL capturando y explicando errores de forma amigable.
 * 3. Valida la existencia y el conteo real de registros en las 12 tablas del proyecto.
 * 4. Emite un informe tabular y resumen del estado de cumplimiento de la Semana 14 (meta: >= 50 registros por tabla).
 */
public class VerificarEntorno {

    // Las 12 tablas oficiales del modelo de datos de Sistema de Alertas Carmen de la Punta
    private static final String[] TABLAS_SEMANA_14 = {
        "usuario",
        "conductor",
        "operador",
        "bus",
        "comisaria",
        "asignacion_turno",
        "alerta",
        "telemetria_gps",
        "evidencia_multimedia",
        "despacho_auxilio",
        "cerco_preventivo",
        "notificacion"
    };

    private static final int META_REGISTROS_SEMANA_14 = 50;

    private static class ResultadoTabla {
        int indice;
        String nombre;
        boolean existe;
        int cantidadRegistros;
        String estado;

        ResultadoTabla(int indice, String nombre, boolean existe, int cantidadRegistros, String estado) {
            this.indice = indice;
            this.nombre = nombre;
            this.existe = existe;
            this.cantidadRegistros = cantidadRegistros;
            this.estado = estado;
        }
    }

    public static void main(String[] args) {
        imprimirEncabezado();

        // 1. Mostrar configuración resuelta (NUNCA contraseña)
        System.out.println("[1] CONFIGURACIÓN DE CONEXIÓN:");
        System.out.println("    " + ConexionBD.obtenerResumenConfiguracion());
        System.out.println("    URL JDBC: " + ConexionBD.getUrl());
        System.out.println();

        // 2. Intentar abrir conexión
        System.out.println("[2] PROBANDO CONEXIÓN CON MYSQL...");
        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            if (conn == null || conn.isClosed()) {
                System.err.println("ERROR CRÍTICO: La conexión retornó nulo o cerrado.");
                terminarConError(1);
                return;
            }
            System.out.println("    -> ¡CONEXIÓN EXITOSA! Motor MySQL respondiendo correctamente.");
            System.out.println("    -> Catálogo actual: " + conn.getCatalog());
            System.out.println();
        } catch (SQLException e) {
            manejarFalloConexion(e);
            terminarConError(1);
            return;
        }

        // 3. Verificar las 12 tablas
        System.out.println("[3] AUDITORÍA DE TABLAS Y REGISTROS REALES (SEMANA 14)");
        List<ResultadoTabla> resultados = new ArrayList<>();
        int tablasExistentes = 0;
        int tablasConMeta50 = 0;

        try {
            String schema = null;
            try {
                schema = conn.getCatalog();
            } catch (SQLException ignored) {
            }
            if (schema == null || schema.trim().isEmpty()) {
                schema = ConexionBD.getBaseDatos();
            }

            for (int i = 0; i < TABLAS_SEMANA_14.length; i++) {
                String tabla = TABLAS_SEMANA_14[i];
                boolean existe = verificarExisteTabla(conn, schema, tabla);
                int conteo = 0;
                String estado;

                if (existe) {
                    tablasExistentes++;
                    conteo = contarRegistros(conn, tabla);
                    if (conteo >= META_REGISTROS_SEMANA_14) {
                        estado = String.format("[OK >= %d]", META_REGISTROS_SEMANA_14);
                        tablasConMeta50++;
                    } else {
                        int faltan = META_REGISTROS_SEMANA_14 - conteo;
                        estado = String.format("[PENDIENTE: faltan %d para %d]", faltan, META_REGISTROS_SEMANA_14);
                    }
                } else {
                    estado = "[NO EXISTE: pendiente crear]";
                }

                resultados.add(new ResultadoTabla(i + 1, tabla, existe, conteo, estado));
            }
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }

        // 4. Imprimir tabla en consola
        imprimirTablaResultados(resultados);

        // 5. Imprimir resumen final
        imprimirResumenFinal(tablasExistentes, tablasConMeta50, TABLAS_SEMANA_14.length);

        // Cerrar hilos huérfanos de MySQL Connector/J para salir limpiamente
        apagarHilosMysql();

        if (tablasExistentes < TABLAS_SEMANA_14.length) {
            System.exit(2);
        } else {
            System.exit(0);
        }
    }

    private static void imprimirEncabezado() {
        System.out.println("================================================================================");
        System.out.println(" SISTEMA DE ALERTAS EN TIEMPO REAL - CARMEN DE LA PUNTA S.A.");
        System.out.println(" DIAGNÓSTICO Y VERIFICACIÓN DEL ENTORNO (SEMANA 14)");
        System.out.println(" Rol: Jesús - Configuración, Diagnóstico y Ejecución");
        System.out.println("================================================================================");
        System.out.println();
    }

    private static void manejarFalloConexion(SQLException e) {
        System.err.println();
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println(" ERROR DE CONEXIÓN CON LA BASE DE DATOS");
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("Detalles técnicos:");
        System.err.println("  * Código SQLState : " + e.getSQLState());
        System.err.println("  * Código de Error : " + e.getErrorCode());
        System.err.println("  * Mensaje         : " + e.getMessage());
        if (e.getCause() != null) {
            System.err.println("  * Causa interna   : " + e.getCause().getClass().getName() + " -> " + e.getCause().getMessage());
        }
        System.err.println();
        System.err.println("Diagnóstico y Causa Probable:");

        int codigo = e.getErrorCode();
        String sqlState = e.getSQLState() != null ? e.getSQLState() : "";

        if (codigo == 1045 || "28000".equals(sqlState)) {
            System.err.println("  -> ACCESO DENEGADO (Error 1045): Usuario o contraseña incorrectos.");
            System.err.println("  -> Solución: Verifique que el usuario ('" + ConexionBD.getUsuario()
                    + "') y su contraseña en 'db.properties' o variables de entorno sean válidos en su MySQL.");
        } else if (codigo == 1049 || "42000".equals(sqlState)) {
            System.err.println("  -> BASE DE DATOS INEXISTENTE (Error 1049): No existe '" + ConexionBD.getBaseDatos() + "'.");
            System.err.println("  -> Solución: Abra MySQL Workbench o terminal y ejecute el script 'database/crear_base_datos.sql'.");
        } else if (codigo == 0 || "08S01".equals(sqlState) || e.getMessage().contains("Communications link failure")) {
            System.err.println("  -> FALLO DE COMUNICACIÓN (Communications link failure):");
            System.err.println("     No se puede conectar al servidor MySQL en " + ConexionBD.getHost() + ":" + ConexionBD.getPuerto() + ".");
            System.err.println("  -> Solución:");
            System.err.println("     1. Asegúrese de que el servicio de MySQL / MariaDB esté ENCENDIDO.");
            System.err.println("     2. Si su MySQL corre en el puerto 3306 (estándar) y el proyecto busca 3307,");
            System.err.println("        cree un archivo 'db.properties' con la línea: db.port=3306");
            System.err.println("        o defina la variable de entorno DB_PORT=3306.");
        } else {
            System.err.println("  -> Error no clasificado. Verifique la configuración de red y privilegios de MySQL.");
        }

        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println();
    }

    private static boolean verificarExisteTabla(Connection conn, String schema, String tabla) {
        // Intento 1: Consultar information_schema.tables (muy confiable en MySQL)
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, tabla);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException ignored) {
        }

        // Intento 2: Usar DatabaseMetaData como fallback
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(schema, null, tabla, new String[]{"TABLE"})) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException ignored) {
        }

        // Intento 3: Probar una consulta trivial SELECT 1 FROM tabla WHERE 1=0
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1 FROM `" + tabla + "` WHERE 1 = 0")) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static int contarRegistros(Connection conn, String tabla) {
        String query = "SELECT COUNT(*) FROM `" + tabla + "`";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Advertencia: No se pudo contar registros en la tabla '" + tabla + "': " + e.getMessage());
        }
        return 0;
    }

    private static void imprimirTablaResultados(List<ResultadoTabla> resultados) {
        String formatoLinea = "+----+-----------------------+----------+--------------------+----------------------------------+";
        System.out.println(formatoLinea);
        System.out.printf("| %-2s | %-21s | %-8s | %-18s | %-32s |%n",
                "#", "Nombre de la Tabla", "¿Existe?", "Registros Reales", "Estado para Semana 14");
        System.out.println(formatoLinea);

        for (ResultadoTabla r : resultados) {
            String existeStr = r.existe ? "SÍ" : "NO";
            String cantStr = r.existe ? String.valueOf(r.cantidadRegistros) : "N/A";
            System.out.printf("| %-2d | %-21s | %-8s | %-18s | %-32s |%n",
                    r.indice, r.nombre, existeStr, cantStr, r.estado);
        }
        System.out.println(formatoLinea);
        System.out.println();
    }

    private static void imprimirResumenFinal(int tablasExistentes, int tablasConMeta50, int totalTablas) {
        System.out.println("================================================================================");
        System.out.println(" RESUMEN GENERAL DEL ENTORNO - SEMANA 14");
        System.out.println("================================================================================");
        System.out.printf("  * Total de tablas analizadas            : %d%n", totalTablas);
        System.out.printf("  * Tablas existentes en la base de datos : %d / %d (%.1f%%)%n",
                tablasExistentes, totalTablas, (tablasExistentes * 100.0 / totalTablas));
        System.out.printf("  * Tablas con meta >= 50 registros       : %d / %d (%.1f%%)%n",
                tablasConMeta50, totalTablas, (tablasConMeta50 * 100.0 / totalTablas));
        System.out.println("--------------------------------------------------------------------------------");

        if (tablasExistentes == totalTablas && tablasConMeta50 == totalTablas) {
            System.out.println("  ESTADO FINAL: [APROBADO] Todas las tablas existen y cumplen con la meta de >= 50 registros.");
        } else if (tablasExistentes == totalTablas) {
            System.out.println("  ESTADO FINAL: [ESTRUCTURA OK - POBLACIÓN PENDIENTE]");
            System.out.println("  Las 12 tablas existen en la base de datos.");
            System.out.println("  Actualmente " + (totalTablas - tablasConMeta50)
                    + " tablas tienen menos de 50 filas (muestran registros reales actuales).");
            System.out.println("  Siguiente paso: Ejecutar script de carga masiva de datos de prueba para alcanzar la meta.");
        } else {
            System.out.println("  ESTADO FINAL: [ESTRUCTURA INCOMPLETA]");
            System.out.println("  Faltan " + (totalTablas - tablasExistentes) + " tablas por crear en la base de datos.");
            System.out.println("  Siguiente paso: Ejecutar 'database/crear_base_datos.sql' para crear las tablas faltantes.");
        }
        System.out.println("================================================================================");
        System.out.println();
    }

    private static void apagarHilosMysql() {
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable ignored) {
        }
    }

    private static void terminarConError(int codigoSalida) {
        apagarHilosMysql();
        System.exit(codigoSalida);
    }
}
