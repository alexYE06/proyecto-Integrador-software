package dao;

import conexion.ConexionBD;
import modelo.AsignacionTurno;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class AsignacionTurnoDAO {

    private static final List<AsignacionTurno> MEMORIA_TURNOS = new CopyOnWriteArrayList<>();

    static {
        AsignacionTurno t1 = new AsignacionTurno();
        t1.setIdAsignacion(1);
        t1.setIdConductor(1);
        t1.setIdBus(1);
        t1.setFecha(Date.valueOf("2026-09-30"));
        t1.setHoraInicio(Time.valueOf("06:00:00"));
        t1.setHoraFin(Time.valueOf("14:00:00"));
        t1.setEstado("Activo");
        t1.setNombreConductor("Carlos Alberto Mendoza Ruiz");
        t1.setDniConductor("71234567");
        t1.setCodigoUnidad("BUS-001");
        t1.setPlacaBus("ABC-123");
        MEMORIA_TURNOS.add(t1);

        AsignacionTurno t2 = new AsignacionTurno();
        t2.setIdAsignacion(2);
        t2.setIdConductor(2);
        t2.setIdBus(2);
        t2.setFecha(Date.valueOf("2026-09-30"));
        t2.setHoraInicio(Time.valueOf("14:00:00"));
        t2.setHoraFin(Time.valueOf("22:00:00"));
        t2.setEstado("Activo");
        t2.setNombreConductor("Elena Castillo Bravo");
        t2.setDniConductor("73456789");
        t2.setCodigoUnidad("BUS-002");
        t2.setPlacaBus("DEF-456");
        MEMORIA_TURNOS.add(t2);
    }

    public List<AsignacionTurno> listarTurnos() {
        List<AsignacionTurno> lista = new ArrayList<>();
        String sql = "SELECT a.id_asignacion, a.id_conductor, a.id_bus, a.fecha, a.hora_inicio, a.hora_fin, a.estado, " +
                "CONCAT(c.nombres, ' ', c.apellidos) AS nombre_completo, c.dni, b.codigo_unidad, b.placa " +
                "FROM asignacion_turno a " +
                "LEFT JOIN conductor c ON a.id_conductor = c.id_conductor " +
                "LEFT JOIN bus b ON a.id_bus = b.id_bus " +
                "ORDER BY a.fecha DESC, a.hora_inicio DESC";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AsignacionTurno t = new AsignacionTurno();
                t.setIdAsignacion(rs.getInt("id_asignacion"));
                t.setIdConductor(rs.getInt("id_conductor"));
                t.setIdBus(rs.getInt("id_bus"));
                t.setFecha(rs.getDate("fecha"));
                t.setHoraInicio(rs.getTime("hora_inicio"));
                t.setHoraFin(rs.getTime("hora_fin"));
                t.setEstado(rs.getString("estado"));
                t.setNombreConductor(rs.getString("nombre_completo"));
                t.setDniConductor(rs.getString("dni"));
                t.setCodigoUnidad(rs.getString("codigo_unidad"));
                t.setPlacaBus(rs.getString("placa"));
                lista.add(t);
            }
            return lista;
        } catch (Exception e) {
            System.err.println("Aviso AsignacionTurnoDAO.listarTurnos: " + e.getMessage() + ". Retornando turnos en memoria.");
            return new ArrayList<>(MEMORIA_TURNOS);
        }
    }

    public boolean registrarTurno(AsignacionTurno turno) {
        int nuevoId = MEMORIA_TURNOS.size() + 1;
        turno.setIdAsignacion(nuevoId);
        MEMORIA_TURNOS.add(turno);

        String sql = "INSERT INTO asignacion_turno (id_conductor, id_bus, fecha, hora_inicio, hora_fin, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, turno.getIdConductor());
            ps.setInt(2, turno.getIdBus());
            ps.setDate(3, turno.getFecha());
            ps.setTime(4, turno.getHoraInicio());
            ps.setTime(5, turno.getHoraFin());
            ps.setString(6, turno.getEstado());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        turno.setIdAsignacion(rs.getInt(1));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Aviso AsignacionTurnoDAO: MySQL no disponible (" + e.getMessage() + "). Guardado en memoria.");
            return true;
        }
    }
}
