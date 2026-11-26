package servicio;

import conexion.ConexionBD;
import dao.Consulta03DAO;
import modelo.Consulta03.Filtro;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/** Generación Jasper y exportación PDF para Consulta 03. */
public final class Consulta03Service {
    private static final String RECURSO = "/reports/Consulta03.jrxml";
    private static volatile JasperReport plantilla;

    private Consulta03Service() {}

    private static JasperReport plantilla() throws Exception {
        if (plantilla == null) {
            synchronized (Consulta03Service.class) {
                if (plantilla == null) {
                    try (InputStream in = Consulta03Service.class.getResourceAsStream(RECURSO)) {
                        if (in == null) throw new IllegalStateException("No se encontró " + RECURSO);
                        plantilla = JasperCompileManager.compileReport(in);
                    }
                }
            }
        }
        return plantilla;
    }

    public static JasperPrint generar(Filtro f) throws Exception {
        Consulta03DAO.validar(f);
        LocalDate desde = (f.desde() == null || f.desde().isBlank()) ? null : LocalDate.parse(f.desde().trim());
        LocalDate hasta = (f.hasta() == null || f.hasta().isBlank()) ? null : LocalDate.parse(f.hasta().trim());
        Timestamp inicio = desde == null ? null : Timestamp.valueOf(desde.atStartOfDay());
        Timestamp fin = hasta == null ? null : Timestamp.valueOf(hasta.plusDays(1).atStartOfDay());
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("FECHA_INICIO", inicio);
        parametros.put("FECHA_FIN", fin);
        try (Connection cn = ConexionBD.obtenerConexion()) {
            if (cn == null || cn.isClosed()) throw new SQLException("No se pudo conectar a MySQL.");
            parametros.put("BASE_DATOS", cn.getCatalog());
            return JasperFillManager.fillReport(plantilla(), parametros, cn);
        }
    }

    public static void pdf(Filtro f, Path destino) throws Exception {
        JasperPrint print = generar(f);
        Path dir = destino.toAbsolutePath().getParent();
        if (dir != null) Files.createDirectories(dir);
        JasperExportManager.exportReportToPdfFile(print, destino.toAbsolutePath().toString());
        if (!Files.exists(destino) || Files.size(destino) == 0) {
            throw new IllegalStateException("No se pudo generar el PDF de Consulta 03.");
        }
    }
}
