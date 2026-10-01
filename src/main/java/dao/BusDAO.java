package dao;

import conexion.ConexionBD;
import modelo.Bus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class BusDAO {

    private static final List<Bus> MEMORIA_BUSES = new CopyOnWriteArrayList<>();

    static {
        MEMORIA_BUSES.add(new Bus(1, "ABC-123", "BUS-001", "Mercedes-Benz", 40, "Activo"));
        MEMORIA_BUSES.add(new Bus(2, "DEF-456", "BUS-002", "Volvo", 45, "Activo"));
        MEMORIA_BUSES.add(new Bus(3, "GHI-789", "BUS-051", "Scania", 50, "En Alerta"));
        MEMORIA_BUSES.add(new Bus(4, "JKL-012", "BUS-014", "Hyundai", 35, "Activo"));
    }

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
            return lista;
        } catch (Exception e) {
            System.err.println("Aviso BusDAO.listarBuses: " + e.getMessage() + ". Retornando flota operativa en memoria.");
            return new ArrayList<>(MEMORIA_BUSES);
        }
    }

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
        } catch (Exception e) {
            // Fallback a memoria
        }

        for (Bus b : MEMORIA_BUSES) {
            if (b.getIdBus() == idBus) return b;
        }

        return null;
    }

    public boolean registrarBus(Bus bus) {
        int nuevoId = MEMORIA_BUSES.size() + 1;
        bus.setIdBus(nuevoId);
        MEMORIA_BUSES.add(bus);

        String sql = "INSERT INTO bus (placa, codigo_unidad, modelo, capacidad, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, bus.getPlaca());
            ps.setString(2, bus.getNumeroUnidad());
            ps.setString(3, bus.getModelo());
            ps.setInt(4, bus.getCapacidad());
            ps.setString(5, bus.getEstado());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        bus.setIdBus(rs.getInt(1));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Aviso BusDAO.registrarBus: MySQL no disponible (" + e.getMessage() + "). Guardado en memoria.");
            return true;
        }
    }

    public boolean actualizarEstado(int idBus, String nuevoEstado) {
        for (Bus b : MEMORIA_BUSES) {
            if (b.getIdBus() == idBus) {
                b.setEstado(nuevoEstado);
                break;
            }
        }

        String sql = "UPDATE bus SET estado = ? WHERE id_bus = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idBus);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return true;
        }
    }
}