import conexion.ConexionBD;
import dao.*;
import modelo.Consulta;
import modelo.Consulta02;
import modelo.Consulta03;
import servicio.*;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.*;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import net.sf.jasperreports.engine.JasperCompileManager;

/** Sin argumentos: plantillas y PDF sin MySQL. --mysql: solo SELECT y PDFs. */
public final class PresentacionSmoke {
    static void check(boolean ok, String label) { if (!ok) throw new AssertionError(label); System.out.println("PASS " + label); }
    static String texto(Path file) throws Exception {
        try (PdfReader r = new PdfReader(file.toString())) {
            check(r.getNumberOfPages() > 0, file.getFileName().toString());
            StringBuilder text = new StringBuilder(); var extractor = new PdfTextExtractor(r);
            for (int i = 1; i <= r.getNumberOfPages(); i++) text.append(extractor.getTextFromPage(i));
            return text.toString();
        }
    }
    public static void main(String[] args) throws Exception {
        Path out = Path.of("target/evidencias-presentacion"); Files.createDirectories(out);
        for (String name : List.of("Consulta01", "Consulta02", "Consulta03", "reporte_alertas", "reporte_estados", "reporte_modalidades", "reporte_alertas_por_dia")) {
            try (var in = PresentacionSmoke.class.getResourceAsStream("/reports/" + name + ".jrxml")) {
                check(in != null && JasperCompileManager.compileReport(in) != null, "Compila Jasper " + name);
            }
        }
        var f = new Consulta.Filtro("", "", "Todos", "", "Contiene", "", false, 0, true);
        List<Object[]> rows = Collections.singletonList(new Object[]{1, Timestamp.valueOf("2026-11-26 08:00:00"), "TEST-001", "BUS-001", "Conductor de prueba", "Reporte manual de central", "Atendida"});
        ConsultaService.pdf(new Consulta.Resultado(f, new Consulta.Sentencia("", List.of()), rows), out.resolve("Consulta01-sintetica.pdf"));
        check(texto(out.resolve("Consulta01-sintetica.pdf")).contains("TEST-001"), "Jasper conserva las filas visibles");
        ConsultaService.pdf(new Consulta.Resultado(f, new Consulta.Sentencia("", List.of()), List.of()), out.resolve("Consulta01-vacia.pdf"));
        check(texto(out.resolve("Consulta01-vacia.pdf")).contains("Sin coincidencias"), "Jasper vacío informativo");
        if (args.length == 0) return;
        try (var c = ConexionBD.obtenerConexion()) {
            System.out.println("BASE VERIFICADA: " + c.getCatalog());
            try (var s = c.createStatement(); var r = s.executeQuery("SELECT COUNT(*) FROM alerta")) { r.next(); System.out.println("ALERTAS: " + r.getInt(1)); }
            String script = Files.readString(Path.of("database/PRESENTACION_MYSQL.sql")).replaceAll("(?m)^--.*$", "");
            for (String statement : script.split(";")) {
                if (statement.isBlank() || statement.strip().startsWith("USE ")) continue;
                try (var s = c.createStatement(); var r = s.executeQuery(statement)) { while (r.next()) { /* Solo comprobar ejecución. */ } }
            }
            check(true, "Guion de Workbench ejecuta sin errores");
        }
        var r1 = ConsultaDAO.ejecutar(f); check(!r1.filas().isEmpty(), "Consulta 01 obtiene datos");
        ConsultaService.pdf(r1, out.resolve("Consulta01.pdf")); texto(out.resolve("Consulta01.pdf"));
        var f2 = new Consulta02.Filtro("", "", "Todos", "Todas", "Todos", "Todos", "", "Contiene", "", false, 0, true);
        check(!Consulta02DAO.ejecutar(f2).filas().isEmpty(), "Consulta 02 obtiene datos");
        Consulta02Service.pdf(f2, out.resolve("Consulta02.pdf")); texto(out.resolve("Consulta02.pdf"));
        var f3 = new Consulta03.Filtro("", "");
        check(!Consulta03DAO.ejecutar(f3).filas().isEmpty(), "Consulta 03 obtiene datos");
        Consulta03Service.pdf(f3, out.resolve("Consulta03.pdf")); texto(out.resolve("Consulta03.pdf"));
        Consulta03Service.pdf(new Consulta03.Filtro("2099-01-01", "2099-01-02"), out.resolve("Consulta03-vacia.pdf"));
        texto(out.resolve("Consulta03-vacia.pdf"));
        var reports = new ReporteService();
        for (String type : List.of("Incidencias", "Estados", "Modalidades", "Alertas por día")) {
            var print = reports.generarReporte(type, new modelo.FiltroReporte(null, null, null, null, null));
            Path file = out.resolve(type.replace(' ', '_') + ".pdf");
            reports.exportarAPdf(print, file.toFile()); texto(file);
        }
        check(new UsuarioDAO().validarAcceso("operador1", "op123") != null, "Cuenta académica operador1");
        System.out.println("VALIDACIÓN COMPLETA: consultas, Jasper, PDFs y acceso; sin modificar MySQL.");
    }
}
