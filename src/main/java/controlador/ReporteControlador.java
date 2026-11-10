package controlador;

import conexion.ConexionBD;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.JOptionPane;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

public class ReporteControlador {

    public static void generarYMostrarReporte() {
        try {
            // 1. Cargar la plantilla desde resources
            InputStream reportStream = ReporteControlador.class.getResourceAsStream("/reports/reporte_alertas.jrxml");
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null,
                    "No se encontró el archivo 'reporte_alertas.jrxml' en src/main/resources/reports/",
                    "Error de Plantilla",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Conectar a MySQL y llenar el reporte
            try (Connection conn = ConexionBD.obtenerConexion()) {
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

                // 3. Desplegar la ventana interactiva del reporte en pantalla
                // 'false' evita que al cerrar el reporte se cierre toda la aplicación
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
}