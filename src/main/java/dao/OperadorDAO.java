package dao;

import conexion.ConexionBD;
import modelo.Operador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OperadorDAO {

    public boolean registrarOperador(Operador operador, String nombreUsuario, String contrasena) {
        String insertarUsuario = "INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) "
                + "VALUES (?, ?, 'Operador', 'Activo')";
        String insertarOperador = "INSERT INTO operador "
                + "(id_usuario, nombres, apellidos, telefono, cargo, estado) VALUES (?, ?, ?, ?, ?, 'Activo')";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT 1 FROM usuario WHERE nombre_usuario = ?")) {
                    ps.setString(1, nombreUsuario);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException("El nombre de usuario ya está registrado.");
                        }
                    }
                }

                int idUsuario;
                try (PreparedStatement ps = conn.prepareStatement(insertarUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, nombreUsuario);
                    ps.setString(2, contrasena);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo crear la cuenta del operador.");
                    }
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("La base de datos no devolvió el ID del usuario creado.");
                        }
                        idUsuario = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(insertarOperador)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, operador.getNombres());
                    ps.setString(3, operador.getApellidos());
                    ps.setString(4, operador.getTelefono());
                    ps.setString(5, operador.getCargo());
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo crear el registro del operador.");
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error en OperadorDAO.registrarOperador: " + e.getMessage());
            return false;
        }
    }

    public List<Operador> listarActivos() throws SQLException {
        List<Operador> operadores = new ArrayList<>();
        String sql = "SELECT o.id_operador, o.id_usuario, o.nombres, o.apellidos, o.telefono, o.cargo, o.estado "
                + "FROM operador o JOIN usuario u ON u.id_usuario = o.id_usuario "
                + "WHERE o.estado = 'Activo' AND u.estado = 'Activo' ORDER BY o.apellidos, o.nombres";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Operador operador = new Operador();
                operador.setIdOperador(rs.getInt("id_operador"));
                operador.setIdUsuario(rs.getInt("id_usuario"));
                operador.setNombres(rs.getString("nombres"));
                operador.setApellidos(rs.getString("apellidos"));
                operador.setTelefono(rs.getString("telefono"));
                operador.setCargo(rs.getString("cargo"));
                operador.setEstado(rs.getString("estado"));
                operadores.add(operador);
            }
        }
        return operadores;
    }
}
