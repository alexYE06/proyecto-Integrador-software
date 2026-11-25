package servicio;

import conexion.ConexionBD;
import dao.Consulta02DAO;
import modelo.Consulta02.Filtro;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para Consulta 02 integrado con JasperReports (/reports/Consulta02.jrxml) y MySQL.
 */
public class Consulta02Service {

    public static final String RUTA_PLANTILLA = "/reports/Consulta02.jrxml";

    public static final String[] COLUMNAS = {
            "Despacho",
            "Alerta",
            "Fecha despacho",
            "Unidad / Placa",
            "Comisaría asignada",
            "Operador",
            "Estado auxilio",
            "Estado cerco"
    };

    public static final String[] ORDENES = {
            "Fecha de despacho",
            "Comisaría",
            "Unidad / Placa",
            "Operador",
            "Estado de despacho",
            "Estado de cerco"
    };

    private static volatile JasperReport plantillaCache;

    public static String resumen(Filtro f) {
        String desde = (f.desde() == null || f.desde().isBlank()) ? "sin límite" : f.desde().trim();
        String hasta = (f.hasta() == null || f.hasta().isBlank()) ? "sin límite" : f.hasta().trim();
        String comisaria = (f.comisaria() == null || f.comisaria().isBlank()) ? "Todas" : f.comisaria().trim();
        String estado = (f.estadoDespacho() == null || f.estadoDespacho().isBlank()) ? "Todos" : f.estadoDespacho().trim();
        String modoBus = (f.modoBus() == null || f.modoBus().isBlank()) ? "Contiene" : f.modoBus().trim();
        String bus = (f.bus() == null || f.bus().isBlank()) ? "todos" : modoBus + " " + f.bus().trim();
        String operador = (f.operador() == null || f.operador().isBlank()) ? "todos" : f.operador().trim();
        String ordenNombre = (f.orden() >= 0 && f.orden() < ORDENES.length) ? ORDENES[f.orden()] : ORDENES[0];

        return "Desde: " + desde
                + " | Hasta: " + hasta
                + " | Comisaría: " + comisaria
                + " | Estado auxilio: " + estado
                + " | Bus/Placa: " + bus
                + " | Operador contiene: " + operador
                + " | Unión: " + (f.cualquiera() ? "O" : "Y")
                + " | Orden: " + ordenNombre + (f.desc() ? " descendente" : " ascendente");
    }

    public static JasperReport obtenerPlantillaCompilada() throws Exception {
        JasperReport actual = plantillaCache;
        if (actual != null) {
            return actual;
        }
        synchronized (Consulta02Service.class) {
            if (plantillaCache == null) {
                try (InputStream reportStream = Consulta02Service.class.getResourceAsStream(RUTA_PLANTILLA)) {
                    if (reportStream == null) {
                        throw new IllegalStateException("No se encontró la plantilla JasperReports en resources: " + RUTA_PLANTILLA);
                    }
                    plantillaCache = JasperCompileManager.compileReport(reportStream);
                }
            }
            return plantillaCache;
        }
    }

    public static Map<String, Object> construirParametros(Filtro f, String baseDatos) {
        // Valida fechas, rango e índice de orden antes de invocar a JasperReports
        Consulta02DAO.construir(f);

        Map<String, Object> params = new HashMap<>();
        params.put("DESDE", f.desde() == null ? "" : f.desde().trim());
        params.put("HASTA", f.hasta() == null ? "" : f.hasta().trim());
        params.put("COMISARIA", (f.comisaria() == null || f.comisaria().isBlank()) ? "Todas" : f.comisaria().trim());
        params.put("ESTADO_DESPACHO", (f.estadoDespacho() == null || f.estadoDespacho().isBlank()) ? "Todos" : f.estadoDespacho().trim());
        params.put("BUS", f.bus() == null ? "" : f.bus().trim());
        params.put("MODO_BUS", (f.modoBus() == null || f.modoBus().isBlank()) ? "Contiene" : f.modoBus().trim());
        params.put("OPERADOR", f.operador() == null ? "" : f.operador().trim());
        params.put("CUALQUIERA", f.cualquiera());
        params.put("ORDEN", ORDENES[f.orden()]);
        params.put("DESCENDENTE", f.desc());
        params.put("BASE_DATOS", (baseDatos == null || baseDatos.isBlank()) ? "sistema_alertas" : baseDatos);
        return params;
    }

    /**
     * Compila /reports/Consulta02.jrxml y ejecuta su consulta SQL parametrizada en MySQL mediante JasperReports.
     */
    public static JasperPrint generarReporte(Filtro f) throws Exception {
        Consulta02DAO.construir(f);
        JasperReport jasperReport = obtenerPlantillaCompilada();
        try (Connection conn = ConexionBD.obtenerConexion()) {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }
            Map<String, Object> params = construirParametros(f, conn.getCatalog());
            return JasperFillManager.fillReport(jasperReport, params, conn);
        }
    }

    /**
     * Genera el reporte con JasperReports y lo exporta de forma atómica a un archivo PDF.
     */
    public static void pdf(Filtro f, Path destino) throws Exception {
        JasperPrint jasperPrint = generarReporte(f);
        Path dirPadre = destino.toAbsolutePath().getParent();
        if (dirPadre != null) {
            Files.createDirectories(dirPadre);
        }
        Path tmp = Files.createTempFile(dirPadre, "consulta02-", ".pdf");
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, tmp.toAbsolutePath().toString());
            if (!Files.exists(tmp) || Files.size(tmp) == 0) {
                throw new IllegalStateException("JasperReports no generó contenido válido para el PDF.");
            }
            Files.move(tmp, destino, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}
