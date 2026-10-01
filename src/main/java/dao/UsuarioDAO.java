package dao;

import conexion.ConexionBD;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {

    /**
     * Valida las credenciales de inicio de sesión del operador o administrador.
     * Retorna el objeto Usuario si las credenciales son correctas y está 'Activo',
     * o null si falla.
     */
    public Usuario validarAcceso(String nombreUsuario, String contrasena) {
        String sql = "SELECT id_usuario, nombre_usuario, contrasena, rol, estado "
                + "FROM usuario "
                + "WHERE nombre_usuario = ? AND contrasena = ? AND estado = 'Activo'";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setEstado(rs.getString("estado"));
                    return usuario;
                }
            }
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo conectar a MySQL (" + e.getMessage() + ").");
        }

        // Usuario administrador de respaldo en caso de contingencia o prueba local
        if ("admin.central".equals(nombreUsuario) && "admin123".equals(contrasena)) {
            Usuario backup = new Usuario();
            backup.setIdUsuario(1);
            backup.setNombreUsuario("admin.central");
            backup.setRol("Administrador");
            backup.setEstado("Activo");
            return backup;
        }

        return null;
    }

    /**
     * Lista todos los usuarios registrados en la base de datos.
     */
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_usuario, contrasena, rol, estado FROM usuario";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.listarUsuarios: " + e.getMessage());
        }
        return lista;
    }

    // Prueba rápida para verificar que UsuarioDAO valida correctamente con los
    // datos de prueba
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.validarAcceso("operador1", "op123");
        if (u != null) {
            System.out.println(
                    "Login exitoso en UsuarioDAO! Bienvenido: " + u.getNombreUsuario() + " | Rol: " + u.getRol());
        } else {
            System.out.println("Credenciales incorrectas o usuario no encontrado.");
        }
    }
}