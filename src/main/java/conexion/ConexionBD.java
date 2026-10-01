package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/sistema_alertas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&connectTimeout=5000&socketTimeout=10000";
    private static final String USER = "root";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 1. Probar con contraseña 123456 (configurada en el MySQL / MariaDB local)
            try {
                return DriverManager.getConnection(URL, USER, "123456");
            } catch (SQLException e1) {
                // 2. Si falla por contraseña, probar con contraseña vacía (estándar alternativo)
                try {
                    return DriverManager.getConnection(URL, USER, "");
                } catch (SQLException e2) {
                    throw e1;
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: No se encontró el driver de MySQL Connector/J.", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión a MySQL...");
        try (Connection conn = obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("CONEXION EXITOSA a la base de datos 'sistema_alertas'!");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            System.err.println(e.getMessage());
        }
    }
}