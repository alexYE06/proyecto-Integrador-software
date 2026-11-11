package servicio;

import conexion.ConexionBD;
import modelo.FiltroReporte;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ReporteService {

    public static final String REPORTE_INCIDENCIAS = "Incidencias";
    public static final String REPORTE_ESTADOS = "Estados";
    public static final String REPORTE_MODALIDADES = "Modalidades";

    public String obtenerRutaPlantilla(String tipoReporte) {
        return switch (tipoReporte) {
            case REPORTE_INCIDENCIAS -> "/reports/reporte_alertas.jrxml";
            case REPORTE_ESTADOS -> "/reports/reporte_estados.jrxml";
            case REPORTE_MODALIDADES -> "/reports/reporte_modalidades.jrxml";
            default -> throw new IllegalArgumentException("Tipo de reporte no reconocido: " + tipoReporte);
        };
    }

    public JasperPrint generarReporte(String tipoReporte, FiltroReporte filtro) throws Exception {
        Map<String, Object> parametros = filtro.toParametersMap();
        String rutaRecurso = obtenerRutaPlantilla(tipoReporte);

        try (InputStream reportStream = ReporteService.class.getResourceAsStream(rutaRecurso)) {
            if (reportStream == null) {
                throw new IllegalStateException("No se encontró la plantilla en resources: " + rutaRecurso);
            }

            try (Connection conn = ConexionBD.obtenerConexion()) {
                if (conn == null || conn.isClosed()) {
                    throw new SQLException("No se pudo establecer conexión con la base de datos.");
                }
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                return JasperFillManager.fillReport(jasperReport, parametros, conn);
            }
        }
    }

    public void exportarAPdf(JasperPrint jasperPrint, File destino) throws Exception {
        if (jasperPrint == null) {
            throw new IllegalArgumentException("No hay un reporte generado para exportar.");
        }
        JasperExportManager.exportReportToPdfFile(jasperPrint, destino.getAbsolutePath());

        if (!destino.exists() || destino.length() == 0) {
            throw new IllegalStateException("El archivo PDF no se pudo escribir correctamente en: " + destino.getAbsolutePath());
        }
    }
}