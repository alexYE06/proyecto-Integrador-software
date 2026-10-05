package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexion.ConexionBD;
import modelo.Conductor;

public class ConductorDAO {

    public List<Conductor> listarActivos() throws SQLException {
        List<Conductor> conductores = new ArrayList<>();
        String sql = "SELECT id_conductor, nombres, apellidos, dni, telefono, licencia, estado "
                + "FROM conductor WHERE estado = 'Activo' ORDER BY apellidos, nombres";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Conductor conductor = new Conductor();
                conductor.setIdConductor(rs.getInt("id_conductor"));
                conductor.setNombres(rs.getString("nombres"));
                conductor.setApellidos(rs.getString("apellidos"));
                conductor.setDni(rs.getString("dni"));
                conductor.setTelefono(rs.getString("telefono"));
                conductor.setLicencia(rs.getString("licencia"));
                conductor.setEstado(rs.getString("estado"));
                conductores.add(conductor);
            }
        }
        return conductores;
    }

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
