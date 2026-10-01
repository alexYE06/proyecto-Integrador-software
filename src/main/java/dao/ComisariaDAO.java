package dao;

import conexion.ConexionBD;
import modelo.Comisaria;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class ComisariaDAO {

    private static final List<Comisaria> MEMORIA_COMISARIAS = new CopyOnWriteArrayList<>();

    static {
        MEMORIA_COMISARIAS.add(new Comisaria(1, "Comisaría Carmen de la Legua", "Av. Morales Duárez 1250", "014567890", new BigDecimal("-12.0420000"), new BigDecimal("-77.0900000")));
        MEMORIA_COMISARIAS.add(new Comisaria(2, "Comisaría Alipio Ponce - Callao", "Jr. Apurímac 450", "014290311", new BigDecimal("-12.0560000"), new BigDecimal("-77.1350000")));
        MEMORIA_COMISARIAS.add(new Comisaria(3, "Comisaría Bellavista", "Av. Colonial 2800", "014512345", new BigDecimal("-12.0620000"), new BigDecimal("-77.1120000")));
    }

    public List<Comisaria> listarComisarias() {
        List<Comisaria> lista = new ArrayList<>();
        String sql = "SELECT id_comisaria, nombre, direccion, telefono, latitud, longitud FROM comisaria";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Comisaria c = new Comisaria();
                c.setIdComisaria(rs.getInt("id_comisaria"));
                c.setNombre(rs.getString("nombre"));
                c.setDireccion(rs.getString("direccion"));
                c.setTelefono(rs.getString("telefono"));
                c.setLatitud(rs.getBigDecimal("latitud"));
                c.setLongitud(rs.getBigDecimal("longitud"));
                lista.add(c);
            }
            return lista;
        } catch (Exception e) {
            System.err.println("Aviso ComisariaDAO.listarComisarias: " + e.getMessage() + ". Retornando dependencias en memoria.");
            return new ArrayList<>(MEMORIA_COMISARIAS);
        }
    }

    public boolean registrarComisaria(Comisaria comisaria) {
        int nuevoId = MEMORIA_COMISARIAS.size() + 1;
        comisaria.setIdComisaria(nuevoId);
        MEMORIA_COMISARIAS.add(comisaria);

        String sql = "INSERT INTO comisaria (nombre, direccion, telefono, latitud, longitud) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, comisaria.getNombre());
            ps.setString(2, comisaria.getDireccion());
            ps.setString(3, comisaria.getTelefono());
            ps.setBigDecimal(4, comisaria.getLatitud());
            ps.setBigDecimal(5, comisaria.getLongitud());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        comisaria.setIdComisaria(rs.getInt(1));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Aviso ComisariaDAO: MySQL no disponible (" + e.getMessage() + "). Guardado en memoria.");
            return true;
        }
    }

    public Comisaria obtenerPorId(int idComisaria) {
        for (Comisaria c : MEMORIA_COMISARIAS) {
            if (c.getIdComisaria() == idComisaria) return c;
        }
        return null;
    }
}
