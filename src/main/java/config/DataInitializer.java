package config;

import conexion.ConexionBD;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("────────────────────────────────────────────────────────────");
        System.out.println("  [DataInitializer] Verificando datos iniciales del sistema");
        System.out.println("────────────────────────────────────────────────────────────");

        try (Connection conn = ConexionBD.obtenerConexion()) {
            if (conn == null) {
                System.out.println("  [DataInitializer] MySQL no disponible en este momento. Se utilizará modo contingencia.");
                return;
            }

            inicializarUsuarios(conn);
            inicializarBuses(conn);
            inicializarConductores(conn);
            inicializarAlertas(conn);

            System.out.println("  [DataInitializer] Verificación completada con éxito.");
            System.out.println("────────────────────────────────────────────────────────────");

        } catch (Exception e) {
            System.err.println("  [DataInitializer] Aviso al inicializar datos: " + e.getMessage());
        }
    }

    private void inicializarUsuarios(Connection conn) {
        try {
            String checkSql = "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = 'admin.central'";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        ps.setString(1, "admin.central");
                        ps.setString(2, "admin123");
                        ps.setString(3, "Administrador");
                        ps.setString(4, "Activo");
                        ps.executeUpdate();
                        System.out.println("  ✓ Usuario 'admin.central' creado exitosamente.");
                    }
                }
            }
        } catch (Exception e) {
            // Ignorar si la tabla no existe o error menor
        }
    }

    private void inicializarBuses(Connection conn) {
        try {
            String checkSql = "SELECT COUNT(*) FROM bus";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado) VALUES "
                            + "('A1A-710', 'BUS-071', 'Mercedes-Benz O500', 40, 'Activo'), "
                            + "('B2B-711', 'BUS-072', 'Volvo B8R', 45, 'Activo'), "
                            + "('C3C-712', 'BUS-073', 'Scania K250', 50, 'En Alerta')";
                    try (Statement stInsert = conn.createStatement()) {
                        stInsert.executeUpdate(insertSql);
                        System.out.println("  ✓ 3 Buses de la Línea Carmen de la Punta creados.");
                    }
                }
            }
        } catch (Exception e) {
            // Ignorar si la tabla no existe o error menor
        }
    }

    private void inicializarConductores(Connection conn) {
        try {
            String checkSql = "SELECT COUNT(*) FROM conductor";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado) VALUES "
                            + "('Carlos', 'Mendoza Ruiz', '71234567', '987654321', 'Q12345678', 'Activo'), "
                            + "('Luis', 'Quispe Torres', '73456789', '986543210', 'Q87654321', 'Activo'), "
                            + "('Jorge', 'Salazar Peña', '74567890', '971234567', 'Q90123456', 'Activo')";
                    try (Statement stInsert = conn.createStatement()) {
                        stInsert.executeUpdate(insertSql);
                        System.out.println("  ✓ 3 Conductores registrados en el padrón.");
                    }
                }
            }
        } catch (Exception e) {
            // Ignorar si la tabla no existe o error menor
        }
    }

    private void inicializarAlertas(Connection conn) {
        try {
            String checkSql = "SELECT COUNT(*) FROM alerta";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO alerta (id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion) VALUES "
                            + "(1, 3, 1, NOW(), 'BOTON_PANICO_3_PULSOS', 'En Evaluación', 'Posible extorsión reportada silenciosamente en Av. Faucett'), "
                            + "(2, 1, 1, NOW(), 'BOTON_PANICO_3_PULSOS', 'Auxilio Despachado', 'Patrulla PNP despachada a intersección Callao')";
                    try (Statement stInsert = conn.createStatement()) {
                        stInsert.executeUpdate(insertSql);
                        System.out.println("  ✓ 2 Alertas de demostración inicializadas.");
                    }
                }
            }
        } catch (Exception e) {
            // Ignorar si la tabla no existe o error menor
        }
    }
}
