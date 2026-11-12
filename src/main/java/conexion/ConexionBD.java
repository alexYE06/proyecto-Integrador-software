package conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor centralizado de conexión a la base de datos MySQL para el Sistema de Alertas.
 * Permite configuración externa mediante:
 * 1. Propiedades del sistema (-Ddb.host, -Ddb.port, etc.)
 * 2. Variables de entorno (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD)
 * 3. Archivo opcional local db.properties (en raíz o config/)
 * 4. Valores por defecto (fallback) para mantener compatibilidad hacia atrás.
 */
public class ConexionBD {

    // Valores por defecto (fallback) del proyecto
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3307";
    private static final String DEFAULT_DATABASE = "sistema_alertas";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "123456";

    // Parámetros de conexión JDBC recomendados para MySQL Connector/J
    // Se agregan connectTimeout y socketTimeout (5s) para evitar que el hilo quede bloqueado indefinidamente
    private static final String JDBC_PARAMS = "useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&connectTimeout=5000&socketTimeout=5000";

    /**
     * Carga las propiedades desde un archivo local db.properties si existe.
     * Busca primero en la raíz del proyecto, luego en config/db.properties y finalmente en el classpath.
     */
    private static Properties cargarPropiedadesArchivo() {
        Properties props = new Properties();

        File archivoRaiz = new File("db.properties");
        if (archivoRaiz.exists() && archivoRaiz.isFile()) {
            try (FileInputStream fis = new FileInputStream(archivoRaiz)) {
                props.load(fis);
                return props;
            } catch (IOException e) {
                System.err.println("Advertencia [ConexionBD]: Error al leer db.properties en raíz: " + e.getMessage());
            }
        }

        File archivoConfig = new File("config/db.properties");
        if (archivoConfig.exists() && archivoConfig.isFile()) {
            try (FileInputStream fis = new FileInputStream(archivoConfig)) {
                props.load(fis);
                return props;
            } catch (IOException e) {
                System.err.println("Advertencia [ConexionBD]: Error al leer config/db.properties: " + e.getMessage());
            }
        }

        try (InputStream is = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
                return props;
            }
        } catch (IOException ignored) {
        }

        return props;
    }

    /**
     * Resuelve un valor de configuración respetando la jerarquía:
     * 1. Propiedad del sistema Java (-D...)
     * 2. Variable de entorno del sistema operativo
     * 3. Clave en db.properties
     * 4. Valor por defecto (fallback)
     */
    private static String resolverConfiguracion(String propiedadSistema, String variableEntorno,
                                                String claveArchivo, Properties props, String valorPorDefecto) {
        // 1. Propiedad del sistema por nombre exacto o nombre de variable de entorno
        String valor = System.getProperty(propiedadSistema);
        if (valor == null || valor.trim().isEmpty()) {
            valor = System.getProperty(variableEntorno);
        }

        // 2. Variable de entorno del sistema operativo
        if (valor == null || valor.trim().isEmpty()) {
            valor = System.getenv(variableEntorno);
        }

        // 3. Archivo db.properties
        if ((valor == null || valor.trim().isEmpty()) && props != null) {
            valor = props.getProperty(claveArchivo);
            if (valor == null || valor.trim().isEmpty()) {
                valor = props.getProperty(variableEntorno);
            }
            if (valor == null || valor.trim().isEmpty()) {
                valor = props.getProperty(propiedadSistema);
            }
        }

        // 4. Fallback por defecto
        if (valor == null || valor.trim().isEmpty()) {
            return valorPorDefecto;
        }

        return valor.trim();
    }

    public static String getHost() {
        Properties props = cargarPropiedadesArchivo();
        String host = resolverConfiguracion("db.host", "DB_HOST", "db.host", props, DEFAULT_HOST);
        // En Windows, 127.0.0.1 evita demoras o bloqueos por resolución IPv6 (::1) si MySQL solo escucha en IPv4
        if ("localhost".equalsIgnoreCase(host)) {
            return "127.0.0.1";
        }
        return host;
    }

    public static String getPuerto() {
        Properties props = cargarPropiedadesArchivo();
        return resolverConfiguracion("db.port", "DB_PORT", "db.port", props, DEFAULT_PORT);
    }

    public static String getBaseDatos() {
        Properties props = cargarPropiedadesArchivo();
        String valor = resolverConfiguracion("db.name", "DB_NAME", "db.name", props, DEFAULT_DATABASE);
        if (valor.equals(DEFAULT_DATABASE)) {
            String alt = props.getProperty("db.database");
            if (alt != null && !alt.trim().isEmpty()) {
                valor = alt.trim();
            }
        }
        return valor;
    }

    public static String getUsuario() {
        Properties props = cargarPropiedadesArchivo();
        String valor = resolverConfiguracion("db.user", "DB_USER", "db.user", props, DEFAULT_USER);
        if (valor.equals(DEFAULT_USER)) {
            String alt = props.getProperty("db.username");
            if (alt != null && !alt.trim().isEmpty()) {
                valor = alt.trim();
            }
        }
        return valor;
    }

    private static String getPassword() {
        // 1. Propiedad de sistema
        String sysProp = System.getProperty("db.password");
        if (sysProp != null) {
            return sysProp;
        }
        sysProp = System.getProperty("DB_PASSWORD");
        if (sysProp != null) {
            return sysProp;
        }

        // 2. Variable de entorno
        String env = System.getenv("DB_PASSWORD");
        if (env != null) {
            return env;
        }

        // 3. Archivo db.properties (permite contraseña vacía si se especificó explícitamente "db.password=")
        Properties props = cargarPropiedadesArchivo();
        if (props.containsKey("db.password")) {
            return props.getProperty("db.password", "");
        }
        if (props.containsKey("password")) {
            return props.getProperty("password", "");
        }

        // 4. Fallback por defecto si no se definió nada
        return DEFAULT_PASSWORD;
    }

    public static String getUrl() {
        return String.format("jdbc:mysql://%s:%s/%s?%s",
                getHost(), getPuerto(), getBaseDatos(), JDBC_PARAMS);
    }

    /**
     * Devuelve un resumen legible de los parámetros de conexión actuales.
     * Por motivos estrictos de seguridad, NUNCA expone la contraseña.
     */
    public static String obtenerResumenConfiguracion() {
        return String.format("Host: %s | Puerto: %s | Base de Datos: %s | Usuario: %s",
                getHost(), getPuerto(), getBaseDatos(), getUsuario());
    }

    /**
     * Obtiene una nueva conexión a la base de datos MySQL.
     * Mantiene intacta la firma requerida por todos los DAOs del proyecto.
     *
     * @return Objeto Connection activo
     * @throws SQLException si ocurre un error de conexión con el motor MySQL
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(getUrl(), getUsuario(), getPassword());
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: No se encontró el driver de MySQL Connector/J en el classpath.", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Prueba de Conexión a Base de Datos - SAT Carmen ");
        System.out.println("=================================================");
        System.out.println("Configuración activa: " + obtenerResumenConfiguracion());
        System.out.println("Intentando conectar con MySQL...");

        try (Connection conn = obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("¡CONEXIÓN EXITOSA a la base de datos '" + getBaseDatos() + "'!");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            System.err.println("Código SQLState: " + e.getSQLState() + " | Código de Error: " + e.getErrorCode());
            System.err.println("Mensaje: " + e.getMessage());
        }
    }
}
