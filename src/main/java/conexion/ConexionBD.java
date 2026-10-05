package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3307/sistema_alertas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Pickford01.8";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
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