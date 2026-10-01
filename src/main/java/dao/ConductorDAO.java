package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import conexion.ConexionBD;
import modelo.Conductor;

public class ConductorDAO {

    /**
     * Registra un nuevo conductor en la base de datos.
     */
    public boolean registrarConductor(Conductor conductor) {
        String sql = "INSERT INTO conductor (nombres, apellidos, dni, telefono, licencia, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, conductor.getNombres());
            ps.setString(2, conductor.getApellidos());
            ps.setString(3, conductor.getDni());
            ps.setString(4, conductor.getTelefono());
            ps.setString(5, conductor.getLicencia());
            ps.setString(6, conductor.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en ConductorDAO.registrarConductor: " + e.getMessage());
            return false;
        }
    }
}
