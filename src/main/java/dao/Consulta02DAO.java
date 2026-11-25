package dao;

import conexion.ConexionBD;
import modelo.Consulta02.Catalogos;
import modelo.Consulta02.Filtro;
import modelo.Consulta02.Resultado;
import modelo.Consulta02.Sentencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO parametrizado de solo lectura para la Consulta 02:
 * despacho_auxilio + alerta + comisaria + operador + bus + cerco_preventivo (6 tablas).
 */
public class Consulta02DAO {

    private static final String SQL_BASE =
            "SELECT d.id_despacho, a.id_alerta, d.fecha_hora AS fecha_despacho, "
            + "CONCAT(b.codigo_unidad, ' / ', b.placa) AS unidad_placa, "
            + "cm.nombre AS comisaria, "
            + "CONCAT(o.nombres, ' ', o.apellidos) AS operador, "
            + "d.estado AS estado_despacho, "
            + "COALESCE(cp.estado, 'Sin cerco') AS estado_cerco "
            + "FROM despacho_auxilio d "
            + "INNER JOIN alerta a ON a.id_alerta = d.id_alerta "
            + "INNER JOIN bus b ON b.id_bus = a.id_bus "
            + "INNER JOIN comisaria cm ON cm.id_comisaria = d.id_comisaria "
            + "INNER JOIN operador o ON o.id_operador = d.id_operador "
            + "LEFT JOIN cerco_preventivo cp ON cp.id_cerco = ("
            + "SELECT cp2.id_cerco FROM cerco_preventivo cp2 "
            + "WHERE cp2.id_alerta = d.id_alerta "
            + "ORDER BY (cp2.id_comisaria = d.id_comisaria) DESC, cp2.fecha_inicio DESC, cp2.id_cerco DESC "
            + "LIMIT 1)";

    public static String patron(String texto, String modo) {
        String limpio = texto.replace("!", "!!").replace("%", "!%").replace("_", "!_");
        return switch (modo) {
            case "Empieza en" -> limpio + "%";
            case "Termina en" -> "%" + limpio;
            default -> "%" + limpio + "%";
        };
    }

    public static Sentencia construir(Filtro f) {
        if (f == null) {
            throw new IllegalArgumentException("El filtro no puede ser nulo.");
        }
        String desdeTxt = f.desde() == null ? "" : f.desde().trim();
        String hastaTxt = f.hasta() == null ? "" : f.hasta().trim();
        String comisaria = (f.comisaria() == null || f.comisaria().isBlank()) ? "Todas" : f.comisaria().trim();
        String estadoDespacho = (f.estadoDespacho() == null || f.estadoDespacho().isBlank()) ? "Todos" : f.estadoDespacho().trim();
        String bus = f.bus() == null ? "" : f.bus().trim();
        String modoBus = (f.modoBus() == null || f.modoBus().isBlank()) ? "Contiene" : f.modoBus().trim();
        String operador = f.operador() == null ? "" : f.operador().trim();

        LocalDate ini = desdeTxt.isBlank() ? null : LocalDate.parse(desdeTxt);
        LocalDate fin = hastaTxt.isBlank() ? null : LocalDate.parse(hastaTxt);
        if (ini != null && fin != null && ini.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicial de despacho debe ser menor o igual que la final.");
        }

        List<String> condiciones = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();
        String sql = SQL_BASE;

        // El intervalo de fechas es una sola condición lógica: nunca se separan sus extremos con OR.
        if (ini != null && fin != null) {
            condiciones.add("(d.fecha_hora >= ? AND d.fecha_hora < ?)");
            parametros.add(Timestamp.valueOf(ini.atStartOfDay()));
            parametros.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));
        } else if (ini != null) {
            condiciones.add("d.fecha_hora >= ?");
            parametros.add(Timestamp.valueOf(ini.atStartOfDay()));
        } else if (fin != null) {
            condiciones.add("d.fecha_hora < ?");
            parametros.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));
        }

        if (!comisaria.equals("Todas")) {
            condiciones.add("cm.nombre = ?");
            parametros.add(comisaria);
        }

        if (!estadoDespacho.equals("Todos")) {
            condiciones.add("d.estado = ?");
            parametros.add(estadoDespacho);
        }

        if (!bus.isBlank()) {
            String patBus = patron(bus, modoBus);
            condiciones.add("(b.placa LIKE ? ESCAPE '!' OR b.codigo_unidad LIKE ? ESCAPE '!')");
            parametros.add(patBus);
            parametros.add(patBus);
        }

        if (!operador.isBlank()) {
            condiciones.add("CONCAT(o.nombres, ' ', o.apellidos) LIKE ? ESCAPE '!'");
            parametros.add(patron(operador, "Contiene"));
        }

        if (!condiciones.isEmpty()) {
            sql += " WHERE (" + String.join(f.cualquiera() ? " OR " : " AND ", condiciones) + ")";
        }

        String campoOrden = switch (f.orden()) {
            case 0 -> "d.fecha_hora";
            case 1 -> "cm.nombre";
            case 2 -> "unidad_placa";
            case 3 -> "operador";
            case 4 -> "d.estado";
            case 5 -> "estado_cerco";
            default -> throw new IllegalArgumentException("Criterio de ordenamiento inválido.");
        };

        sql += " ORDER BY " + campoOrden + (f.desc() ? " DESC" : " ASC") + ", d.id_despacho ASC";
        return new Sentencia(sql, List.copyOf(parametros));
    }

    public static Resultado ejecutar(Filtro f) throws SQLException {
        Sentencia sentencia = construir(f);
        List<Object[]> filas = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sentencia.sql())) {
            ps.setQueryTimeout(30);
            for (int i = 0; i < sentencia.valores().size(); i++) {
                ps.setObject(i + 1, sentencia.valores().get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[8];
                    for (int i = 0; i < 8; i++) {
                        fila[i] = (i == 2) ? rs.getTimestamp(i + 1) : rs.getObject(i + 1);
                    }
                    filas.add(fila);
                }
            }
        }
        return new Resultado(f, sentencia, List.copyOf(filas));
    }

    public static Catalogos cargarCatalogos() throws SQLException {
        List<String> comisarias = new ArrayList<>();
        List<String> estados = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             Statement st = cn.createStatement()) {
            st.setQueryTimeout(15);
            try (ResultSet rs = st.executeQuery("SELECT DISTINCT nombre FROM comisaria ORDER BY nombre")) {
                while (rs.next()) {
                    comisarias.add(rs.getString(1));
                }
            }
            try (ResultSet rs = st.executeQuery("SELECT DISTINCT estado FROM despacho_auxilio ORDER BY estado")) {
                while (rs.next()) {
                    estados.add(rs.getString(1));
                }
            }
        }
        return new Catalogos(List.copyOf(comisarias), List.copyOf(estados));
    }
}
