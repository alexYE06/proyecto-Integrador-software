package controlador;

import conexion.ConexionBD;
import dao.BusDAO;
import modelo.Bus;
import modelo.FiltroReporte;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import servicio.ReporteService;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ReporteControlador {

    private final ReporteService reporteService = new ReporteService();
    private final BusDAO busDAO = new BusDAO();

    // Método existente conservado para no romper el botón actual de IncidentesView
    public static void generarYMostrarReporte() {
        try {
            InputStream reportStream = ReporteControlador.class.getResourceAsStream("/reports/reporte_alertas.jrxml");
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró el archivo 'reporte_alertas.jrxml' en src/main/resources/reports/",
                        "Error de Plantilla",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = ConexionBD.obtenerConexion()) {
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setTitle("Reporte de Incidencias - SAT-Carmen");
                viewer.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al generar el reporte: " + e.getMessage(),
                    "Error JasperReports",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Bus> listarBusesActivos() {
        try {
            return busDAO.listarActivos();
        } catch (SQLException e) {
            System.err.println("No se pudieron cargar los buses activos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public JasperPrint generar(String tipoReporte, FiltroReporte filtro) throws Exception {
        return reporteService.generarReporte(tipoReporte, filtro);
    }

    public void guardarPdf(String tipoReporte, FiltroReporte filtro, File destino) throws Exception {
        JasperPrint print = reporteService.generarReporte(tipoReporte, filtro);
        reporteService.exportarAPdf(print, destino);
    }
}