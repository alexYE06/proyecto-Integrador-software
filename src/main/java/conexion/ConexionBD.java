package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.nio.file.*;
import java.io.*;

/**
 * Configuración compartida por formularios, DAO, consultas y reportes.
 */
public final class ConexionBD {
    private ConexionBD() {
    }

    public static Path archivoLocal() {
        return Path.of(System.getProperty("sat.config", ".local/database.properties")).toAbsolutePath();
    }

    public static Properties configuracion() {
        Properties p = new Properties();
        p.setProperty("DB_HOST", "127.0.0.1");
        p.setProperty("DB_PORT", "3306");
        p.setProperty("DB_NAME", "sistema_alertas");
        p.setProperty("DB_USER", "root");
        p.setProperty("DB_PASSWORD", "");
        if (Files.exists(archivoLocal())) {
            try (Reader r = Files.newBufferedReader(archivoLocal())) { p.load(r); }
            catch (IOException ex) { throw new IllegalStateException("No se pudo leer la configuración local de MySQL.", ex); }
        }
        for (String key : new String[]{"DB_HOST", "DB_PORT", "DB_NAME", "DB_USER", "DB_PASSWORD", "DB_SSL_MODE", "DB_ALLOW_PUBLIC_KEY_RETRIEVAL"}) {
            String value = System.getProperty(key, System.getenv(key));
            if (value != null) p.setProperty(key, value);
        }
        return p;
    }

    public static void guardar(Properties p) throws IOException {
        Path path = archivoLocal();
        Files.createDirectories(path.getParent());
        Path tmp = Files.createTempFile(path.getParent(), "conexion-", ".tmp");
        try {
            try (Writer w = Files.newBufferedWriter(tmp)) { p.store(w, "Solo para este equipo. No compartir ni subir a GitHub."); }
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
        } finally { Files.deleteIfExists(tmp); }
    }

    public static Connection obtenerConexion() throws SQLException {
        return conectar(configuracion());
    }

    public static Connection conectar(Properties config) throws SQLException {
        String url = "jdbc:mysql://" + config.getProperty("DB_HOST") + ":" + config.getProperty("DB_PORT") + "/" + config.getProperty("DB_NAME");
        Properties p = new Properties();
        p.setProperty("user", config.getProperty("DB_USER", "root"));
        p.setProperty("password", config.getProperty("DB_PASSWORD", ""));
        p.setProperty("serverTimezone", "America/Lima");
        p.setProperty("connectTimeout", "5000");
        p.setProperty("socketTimeout", "30000");
        p.setProperty("sslMode", config.getProperty("DB_SSL_MODE", "PREFERRED"));
        p.setProperty("allowPublicKeyRetrieval", config.getProperty("DB_ALLOW_PUBLIC_KEY_RETRIEVAL", "false"));
        return DriverManager.getConnection(url, p);
    }
}
