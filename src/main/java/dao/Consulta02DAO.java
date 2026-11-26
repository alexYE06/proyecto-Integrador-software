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
 * DAO parametrizado de solo lectura para la Consulta 02 (6 tablas con LEFT JOIN desde alerta):
 * alerta + bus + despacho_auxilio + comisaria + operador + cerco_preventivo.
 */
public class Consulta02DAO {

    private static final String SQL_BASE =
            "SELECT a.id_alerta, "
            + "COALESCE(d.fecha_hora, a.fecha_hora) AS fecha_referencia, "
            + "CONCAT(b.codigo_unidad, ' / ', b.placa) AS unidad_placa, "
            + "a.estado AS estado_alerta, "
            + "COALESCE(cm.nombre, 'Sin comisaría') AS comisaria, "
            + "COALESCE(CONCAT(o.nombres, ' ', o.apellidos), 'Sin operador') AS operador, "
            + "COALESCE(d.estado, 'Sin despacho') AS estado_despacho, "
            + "COALESCE(cp.estado, 'Sin cerco') AS estado_cerco "
            + "FROM alerta a "
            + "INNER JOIN bus b ON b.id_bus = a.id_bus "
            + "LEFT JOIN despacho_auxilio d ON d.id_despacho = ("
            + "SELECT d2.id_despacho FROM despacho_auxilio d2 "
            + "WHERE d2.id_alerta = a.id_alerta "
            + "ORDER BY d2.fecha_hora DESC, d2.id_despacho DESC LIMIT 1) "
            + "LEFT JOIN comisaria cm ON cm.id_comisaria = d.id_comisaria "
            + "LEFT JOIN operador o ON o.id_operador = COALESCE(d.id_operador, a.id_operador) "
            + "LEFT JOIN cerco_preventivo cp ON cp.id_cerco = ("
            + "SELECT cp2.id_cerco FROM cerco_preventivo cp2 "
            + "WHERE cp2.id_alerta = a.id_alerta "
            + "ORDER BY (d.id_comisaria IS NOT NULL AND cp2.id_comisaria = d.id_comisaria) DESC, cp2.fecha_inicio DESC, cp2.id_cerco DESC LIMIT 1)";

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
        String estadoAlerta = (f.estadoAlerta() == null || f.estadoAlerta().isBlank()) ? "Todos" : f.estadoAlerta().trim();
        String comisaria = (f.comisaria() == null || f.comisaria().isBlank()) ? "Todas" : f.comisaria().trim();
        String estadoDespacho = (f.estadoDespacho() == null || f.estadoDespacho().isBlank()) ? "Todos" : f.estadoDespacho().trim();
        String estadoCerco = (f.estadoCerco() == null || f.estadoCerco().isBlank()) ? "Todos" : f.estadoCerco().trim();
        String bus = f.bus() == null ? "" : f.bus().trim();
        String modoBus = (f.modoBus() == null || f.modoBus().isBlank()) ? "Contiene" : f.modoBus().trim();
        String operador = f.operador() == null ? "" : f.operador().trim();

        LocalDate ini = desdeTxt.isBlank() ? null : LocalDate.parse(desdeTxt);
        LocalDate fin = hastaTxt.isBlank() ? null : LocalDate.parse(hastaTxt);
        if (ini != null && fin != null && ini.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicial debe ser menor o igual que la final.");
        }

        List<String> condiciones = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();
        String sql = SQL_BASE;

        // El intervalo de fechas es una sola condición lógica: nunca se separan sus extremos con OR.
        if (ini != null && fin != null) {
            condiciones.add("(COALESCE(d.fecha_hora, a.fecha_hora) >= ? AND COALESCE(d.fecha_hora, a.fecha_hora) < ?)");
            parametros.add(Timestamp.valueOf(ini.atStartOfDay()));
            parametros.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));
        } else if (ini != null) {
            condiciones.add("COALESCE(d.fecha_hora, a.fecha_hora) >= ?");
            parametros.add(Timestamp.valueOf(ini.atStartOfDay()));
        } else if (fin != null) {
            condiciones.add("COALESCE(d.fecha_hora, a.fecha_hora) < ?");
            parametros.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));
        }

        if (!estadoAlerta.equals("Todos")) {
            condiciones.add("a.estado = ?");
            parametros.add(estadoAlerta);
        }

        if (!comisaria.equals("Todas")) {
            condiciones.add("COALESCE(cm.nombre, 'Sin comisaría') = ?");
            parametros.add(comisaria);
        }

        if (!estadoDespacho.equals("Todos")) {
            condiciones.add("COALESCE(d.estado, 'Sin despacho') = ?");
            parametros.add(estadoDespacho);
        }

        if (!estadoCerco.equals("Todos")) {
            condiciones.add("COALESCE(cp.estado, 'Sin cerco') = ?");
            parametros.add(estadoCerco);
        }

        if (!bus.isBlank()) {
            String patBus = patron(bus, modoBus);
            condiciones.add("(b.placa LIKE ? ESCAPE '!' OR b.codigo_unidad LIKE ? ESCAPE '!')");
            parametros.add(patBus);
            parametros.add(patBus);
        }

        if (!operador.isBlank()) {
            condiciones.add("COALESCE(CONCAT(o.nombres, ' ', o.apellidos), 'Sin operador') LIKE ? ESCAPE '!'");
            parametros.add(patron(operador, "Contiene"));
        }

        if (!condiciones.isEmpty()) {
            sql += " WHERE (" + String.join(f.cualquiera() ? " OR " : " AND ", condiciones) + ")";
        }

        String campoOrden = switch (f.orden()) {
            case 0 -> "fecha_referencia";
            case 1 -> "comisaria";
            case 2 -> "unidad_placa";
            case 3 -> "operador";
            case 4 -> "a.estado";
            case 5 -> "estado_despacho";
            case 6 -> "estado_cerco";
            default -> throw new IllegalArgumentException("Criterio de ordenamiento inválido.");
        };

        sql += " ORDER BY " + campoOrden + (f.desc() ? " DESC" : " ASC") + ", a.id_alerta ASC";
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
                        fila[i] = (i == 1) ? rs.getTimestamp(i + 1) : rs.getObject(i + 1);
                    }
                    filas.add(fila);
                }
            }
        }
        return new Resultado(f, sentencia, List.copyOf(filas));
    }

    public static Catalogos cargarCatalogos() throws SQLException {
        List<String> estadosAlerta = new ArrayList<>();
        List<String> comisarias = new ArrayList<>();
        List<String> estadosDespacho = new ArrayList<>();
        List<String> estadosCerco = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             Statement st = cn.createStatement()) {
            st.setQueryTimeout(15);
            try (ResultSet rs = st.executeQuery("SELECT DISTINCT estado FROM alerta ORDER BY estado")) {
                while (rs.next()) estadosAlerta.add(rs.getString(1));
            }
            try (ResultSet rs = st.executeQuery("SELECT DISTINCT nombre FROM comisaria ORDER BY nombre")) {
                while (rs.next()) comisarias.add(rs.getString(1));
            }
            try (ResultSet rs = st.executeQuery("SELECT DISTINCT estado FROM despacho_auxilio ORDER BY estado")) {
                while (rs.next()) estadosDespacho.add(rs.getString(1));
            }
            try (ResultSet rs = st.executeQuery("SELECT DISTINCT estado FROM cerco_preventivo ORDER BY estado")) {
                while (rs.next()) estadosCerco.add(rs.getString(1));
            }
        }
        return new Catalogos(
                List.copyOf(estadosAlerta),
                List.copyOf(comisarias),
                List.copyOf(estadosDespacho),
                List.copyOf(estadosCerco)
        );
    }
}
