package dao;

import conexion.ConexionBD;
import modelo.Bus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    /**
     * Lista todos los buses registrados en la flota de Carmen de la Punta S.A.
     */
    public List<Bus> listarBuses() {
        List<Bus> lista = new ArrayList<>();
        String sql = "SELECT id_bus, placa, codigo_unidad, modelo, capacidad, estado FROM bus";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bus bus = new Bus();
                bus.setIdBus(rs.getInt("id_bus"));
                bus.setPlaca(rs.getString("placa"));
                bus.setNumeroUnidad(rs.getString("codigo_unidad"));
                bus.setModelo(rs.getString("modelo"));
                bus.setCapacidad(rs.getInt("capacidad"));
                bus.setEstado(rs.getString("estado"));
                lista.add(bus);
            }
        } catch (SQLException e) {
            System.err.println("Error en BusDAO.listarBuses: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un bus específico por su ID.
     */
    public Bus obtenerPorId(int idBus) {
        String sql = "SELECT id_bus, placa, codigo_unidad, modelo, capacidad, estado FROM bus WHERE id_bus = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBus);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bus bus = new Bus();
                    bus.setIdBus(rs.getInt("id_bus"));
                    bus.setPlaca(rs.getString("placa"));
                    bus.setNumeroUnidad(rs.getString("codigo_unidad"));
                    bus.setModelo(rs.getString("modelo"));
                    bus.setCapacidad(rs.getInt("capacidad"));
                    bus.setEstado(rs.getString("estado"));
                    return bus;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en BusDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Registra un nuevo bus en la base de datos (Operación CRUD - Create).
     */
    public boolean registrarBus(Bus bus) {
        String sql = "INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bus.getPlaca());
            ps.setString(2, bus.getNumeroUnidad());
            ps.setString(3, bus.getModelo());
            ps.setInt(4, bus.getCapacidad());
            ps.setString(5, bus.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en BusDAO.registrarBus: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el estado operativo de un bus (Operación CRUD - Update).
     */
    public boolean actualizarEstado(int idBus, String nuevoEstado) {
        String sql = "UPDATE bus SET estado = ? WHERE id_bus = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idBus);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en BusDAO.actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    // Prueba rápida para verificar que lista los buses de datos_prueba.sql
    public static void main(String[] args) {
        BusDAO dao = new BusDAO();
        List<Bus> buses = dao.listarBuses();
        System.out.println("Total de buses encontrados: " + buses.size());
        for (Bus b : buses) {
            System.out.println("Bus ID: " + b.getIdBus() + " | Placa: " + b.getPlaca() 
                            + " | Unidad: " + b.getNumeroUnidad() + " | Estado: " + b.getEstado());
        }
    }
}