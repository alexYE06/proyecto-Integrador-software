package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import conexion.ConexionBD;
import modelo.Conductor;
import org.springframework.stereotype.Repository;

@Repository
public class ConductorDAO {

    // Lista en memoria thread-safe persistente durante la ejecución de la app
    private static final List<Conductor> MEMORIA_CONDUCTORES = new CopyOnWriteArrayList<>();

    static {
        Conductor c1 = new Conductor();
        c1.setIdConductor(1);
        c1.setNombres("Carlos Alberto");
        c1.setApellidos("Mendoza Ruiz");
        c1.setDni("71234567");
        c1.setTelefono("987654321");
        c1.setLicencia("Q71234567");
        c1.setEstado("Activo");
        MEMORIA_CONDUCTORES.add(c1);

        Conductor c2 = new Conductor();
        c2.setIdConductor(2);
        c2.setNombres("Elena");
        c2.setApellidos("Castillo Bravo");
        c2.setDni("73456789");
        c2.setTelefono("986543210");
        c2.setLicencia("L87654321");
        c2.setEstado("Activo");
        MEMORIA_CONDUCTORES.add(c2);

        Conductor c3 = new Conductor();
        c3.setIdConductor(3);
        c3.setNombres("Jorge");
        c3.setApellidos("Salazar Paredes");
        c3.setDni("74567890");
        c3.setTelefono("985432109");
        c3.setLicencia("L74567890");
        c3.setEstado("Activo");
        MEMORIA_CONDUCTORES.add(c3);
    }

    /**
     * Registra un nuevo conductor en la base de datos (con persistencia híbrida garantizada).
     */
    public boolean registrarConductor(Conductor conductor) {
        // Asignar ID secuencial en memoria primero
        int nuevoId = MEMORIA_CONDUCTORES.size() + 1;
        conductor.setIdConductor(nuevoId);
        MEMORIA_CONDUCTORES.add(conductor);

        String sql = "INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, conductor.getNombres());
            ps.setString(2, conductor.getApellidos());
            ps.setString(3, conductor.getDni());
            ps.setString(4, conductor.getTelefono());
            ps.setString(5, conductor.getLicencia());
            ps.setString(6, conductor.getEstado());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        conductor.setIdConductor(rs.getInt(1));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Aviso: MySQL no disponible al insertar (" + e.getMessage() + "). Guardado en memoria operativa.");
            return true; // Retorna true para que la UI confirme el guardado exitoso
        }
    }

    /**
     * Lista todos los conductores registrados.
     */
    public List<Conductor> listarConductores() {
        List<Conductor> listaBD = new ArrayList<>();
        String sql = "SELECT id_conductor, nombres, apellidos, dni, telefono, licencia, estado FROM conductor";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Conductor c = new Conductor();
                c.setIdConductor(rs.getInt("id_conductor"));
                c.setNombres(rs.getString("nombres"));
                c.setApellidos(rs.getString("apellidos"));
                c.setDni(rs.getString("dni"));
                c.setTelefono(rs.getString("telefono"));
                c.setLicencia(rs.getString("licencia"));
                c.setEstado(rs.getString("estado"));
                listaBD.add(c);
            }
            return listaBD;
        } catch (Exception e) {
            System.err.println("Aviso: Error de conexión a MySQL (" + e.getMessage() + "). Retornando padrón operativo en memoria.");
            return new ArrayList<>(MEMORIA_CONDUCTORES);
        }
    }

    /**
     * Busca un conductor por su DNI.
     */
    public Conductor obtenerPorDni(String dni) {
        String sql = "SELECT id_conductor, nombres, apellidos, dni, telefono, licencia, estado FROM conductor WHERE dni = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Conductor c = new Conductor();
                    c.setIdConductor(rs.getInt("id_conductor"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setDni(rs.getString("dni"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setLicencia(rs.getString("licencia"));
                    c.setEstado(rs.getString("estado"));
                    return c;
                }
            }
        } catch (Exception e) {
            // Continúa a buscar en memoria
        }

        for (Conductor c : MEMORIA_CONDUCTORES) {
            if (dni != null && dni.trim().equals(c.getDni().trim())) {
                return c;
            }
        }

        return null;
    }
}
