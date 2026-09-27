package dao;

import conexion.ConexionBD;
import modelo.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    /**
     * Registra una nueva alerta silenciosa (tres pulsaciones de encendido).
     * Retorna el ID autogenerado de la alerta creada (o -1 si falla).
     */
    public int registrarAlerta(Alerta alerta) {
        String sql = "INSERT INTO alerta (id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, alerta.getIdConductor());
            ps.setInt(2, alerta.getIdBus());

            if (alerta.getIdOperador() != null && alerta.getIdOperador() > 0) {
                ps.setInt(3, alerta.getIdOperador());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (alerta.getFechaHora() != null) {
                ps.setTimestamp(4, alerta.getFechaHora());
            } else {
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }

            ps.setString(5, alerta.getTipoActivacion() != null ? alerta.getTipoActivacion() : "BOTON_PANICO_3_PULSOS");
            ps.setString(6, alerta.getEstado() != null ? alerta.getEstado() : "Recibida");
            ps.setString(7, alerta.getDescripcion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en AlertaDAO.registrarAlerta: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Lista todas las alertas ordenadas de la más reciente a la más antigua.
     */
    public List<Alerta> listarAlertas() {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT id_alerta, id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion "
                + "FROM alerta ORDER BY fecha_hora DESC";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Alerta alerta = new Alerta();
                alerta.setIdAlerta(rs.getInt("id_alerta"));
                alerta.setIdConductor(rs.getInt("id_conductor"));
                alerta.setIdBus(rs.getInt("id_bus"));
                
                int idOp = rs.getInt("id_operador");
                alerta.setIdOperador(rs.wasNull() ? null : idOp);

                alerta.setFechaHora(rs.getTimestamp("fecha_hora"));
                alerta.setTipoActivacion(rs.getString("tipo_activacion"));
                alerta.setEstado(rs.getString("estado"));
                alerta.setDescripcion(rs.getString("descripcion"));

                lista.add(alerta);
            }
        } catch (SQLException e) {
            System.err.println("Error en AlertaDAO.listarAlertas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Permite a la central cambiar el estado de la alerta y asignarle un operador.
     * Ejemplo de estados: 'Recibida', 'En Evaluación', 'Auxilio Despachado', 'Atendida', 'Falsa Alarma'.
     */
    public boolean cambiarEstado(int idAlerta, String nuevoEstado, Integer idOperador) {
        String sql = "UPDATE alerta SET estado = ?, id_operador = ? WHERE id_alerta = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            if (idOperador != null && idOperador > 0) {
                ps.setInt(2, idOperador);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, idAlerta);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en AlertaDAO.cambiarEstado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una alerta por su ID.
     */
    public Alerta obtenerPorId(int idAlerta) {
        String sql = "SELECT id_alerta, id_conductor, id_bus, id_operador, fecha_hora, tipo_activacion, estado, descripcion "
                + "FROM alerta WHERE id_alerta = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAlerta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Alerta alerta = new Alerta();
                    alerta.setIdAlerta(rs.getInt("id_alerta"));
                    alerta.setIdConductor(rs.getInt("id_conductor"));
                    alerta.setIdBus(rs.getInt("id_bus"));

                    int idOp = rs.getInt("id_operador");
                    alerta.setIdOperador(rs.wasNull() ? null : idOp);

                    alerta.setFechaHora(rs.getTimestamp("fecha_hora"));
                    alerta.setTipoActivacion(rs.getString("tipo_activacion"));
                    alerta.setEstado(rs.getString("estado"));
                    alerta.setDescripcion(rs.getString("descripcion"));

                    return alerta;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en AlertaDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    // Prueba para verificar inserción y consulta en consola
    public static void main(String[] args) {
        AlertaDAO dao = new AlertaDAO();

        System.out.println("--- Probando AlertaDAO ---");
        List<Alerta> iniciales = dao.listarAlertas();
        System.out.println("Alertas encontradas inicialmente: " + iniciales.size());

        for (Alerta a : iniciales) {
            System.out.println("Alerta ID: " + a.getIdAlerta() 
                    + " | Estado: " + a.getEstado() 
                    + " | Activación: " + a.getTipoActivacion()
                    + " | Fecha: " + a.getFechaHora());
        }
    }
}