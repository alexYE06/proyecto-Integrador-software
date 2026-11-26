package dao;

import conexion.ConexionBD;
import modelo.Consulta03.Filtro;
import modelo.Consulta03.Resultado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Lectura agregada de alertas por unidad; no escribe datos en MySQL. */
public final class Consulta03DAO {
    private static final String SQL = """
            SELECT b.id_bus, b.codigo_unidad, b.placa,
                   COUNT(*) AS total_alertas,
                   COUNT(CASE WHEN a.estado = 'Atendida' THEN 1 END) AS atendidas,
                   COUNT(CASE WHEN a.estado = 'Falsa Alarma' THEN 1 END) AS falsas_alarmas,
                   COUNT(CASE WHEN a.estado = 'Auxilio Despachado' THEN 1 END) AS auxilio_despachado
            FROM alerta a
            INNER JOIN bus b ON b.id_bus = a.id_bus
            WHERE (? IS NULL OR a.fecha_hora >= ?)
              AND (? IS NULL OR a.fecha_hora < ?)
            GROUP BY b.id_bus, b.codigo_unidad, b.placa
            ORDER BY total_alertas DESC, b.codigo_unidad ASC
            """;

    private Consulta03DAO() {}

    public static void validar(Filtro f) {
        if (f == null) throw new IllegalArgumentException("El filtro no puede ser nulo.");
        LocalDate desde = (f.desde() == null || f.desde().isBlank()) ? null : LocalDate.parse(f.desde().trim());
        LocalDate hasta = (f.hasta() == null || f.hasta().isBlank()) ? null : LocalDate.parse(f.hasta().trim());
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial debe ser menor o igual que la final.");
        }
    }

    public static Resultado ejecutar(Filtro f) throws Exception {
        validar(f);
        LocalDate desde = (f.desde() == null || f.desde().isBlank()) ? null : LocalDate.parse(f.desde().trim());
        LocalDate hasta = (f.hasta() == null || f.hasta().isBlank()) ? null : LocalDate.parse(f.hasta().trim());
        Timestamp inicio = desde == null ? null : Timestamp.valueOf(desde.atStartOfDay());
        Timestamp finExclusivo = hasta == null ? null : Timestamp.valueOf(hasta.plusDays(1).atStartOfDay());
        List<Object[]> filas = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(SQL)) {
            ps.setQueryTimeout(30);
            ps.setTimestamp(1, inicio);
            ps.setTimestamp(2, inicio);
            ps.setTimestamp(3, finExclusivo);
            ps.setTimestamp(4, finExclusivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{rs.getInt("id_bus"), rs.getString("codigo_unidad"),
                            rs.getString("placa"), rs.getLong("total_alertas"),
                            rs.getLong("atendidas"), rs.getLong("falsas_alarmas"),
                            rs.getLong("auxilio_despachado")});
                }
            }
        }
        return new Resultado(f, List.copyOf(filas));
    }
}
