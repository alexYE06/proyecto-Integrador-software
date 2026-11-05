package controlador;

import conexion.ConexionBD;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ReporteController {

    public static void generarReporteIncidencias() {
        try (Connection con = ConexionBD.obtenerConexion()) {
            if (con == null || con.isClosed()) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos 'sistema_alertas'.", 
                        "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar el archivo .jrxml desde resources
            InputStream stream = ReporteController.class.getResourceAsStream("/reportes/Reporte_01_Incidencias_SAT_CARMEN.jrxml");
            if (stream == null) {
                JOptionPane.showMessageDialog(null, 
                        "No se encontró la plantilla en: /reportes/Reporte_01_Incidencias_SAT_CARMEN.jrxml\n" +
                        "Verifica que el archivo esté dentro de src/main/resources/reportes/", 
                        "Plantilla No Encontrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Compilar la plantilla JRXML en tiempo de ejecución
            JasperReport jasperReport = JasperCompileManager.compileReport(stream);

            // 2. Parámetros vacíos (la consulta SQL está embebida en el JRXML)
            Map<String, Object> parameters = new HashMap<>();

            // 3. Llenar el reporte ejecutando el SQL con la conexión JDBC
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

            // 4. Desplegar el visor nativo de JasperReports
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("SAT-CARMEN - Reporte de Incidencias");
            viewer.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage(), 
                    "Error JasperReports", JOptionPane.ERROR_MESSAGE);
        }
    }
}