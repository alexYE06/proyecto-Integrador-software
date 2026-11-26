package servicio;
import modelo.Consulta.*;
import java.awt.Color;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
public class ConsultaService {
    public static final String[] COLUMNAS={"Alerta", "Fecha y hora", "Placa", "Unidad", "Conductor", "Modalidad", "Estado"};
    public static String resumen(Filtro f){
        return "Desde: "+(f.desde().isBlank()?"sin límite":f.desde())+" | Hasta: "+(f.hasta().isBlank()?"sin límite":f.hasta())+
            " | Estado: "+f.estado()+" | Placa: "+(f.placa().isBlank()?"todas":f.modo()+" "+f.placa())+
            " | Conductor contiene: "+(f.conductor().isBlank()?"todos":f.conductor())+" | Unión: "+(f.cualquiera()?"O":"Y")+
            " | Orden: "+new String[]{"Fecha","Placa","Conductor","Estado"}[f.orden()]+(f.desc()?" descendente":" ascendente");
    }
    public static void pdf(Resultado r, Path destino) throws Exception {
        // Jasper recibe la misma instantánea visible: no repite SQL al exportar.
        java.util.Collection<java.util.Map<String, ?>> filas = new java.util.ArrayList<>();
        String[] campos = {"id_alerta", "fecha_hora", "placa", "codigo_unidad", "conductor", "tipo_activacion", "estado"};
        for (Object[] fila : r.filas()) {
            java.util.Map<String, Object> registro = new java.util.HashMap<>();
            for (int i = 0; i < campos.length; i++) registro.put(campos[i], fila[i]);
            filas.add(registro);
        }
        Filtro f = r.filtro();
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("DESDE", f.desde()); params.put("HASTA", f.hasta());
        params.put("ESTADO", f.estado()); params.put("PLACA", f.placa());
        params.put("CONDUCTOR", f.conductor()); params.put("MODO_PLACA", f.modo());
        params.put("CUALQUIERA", f.cualquiera()); params.put("DESCENDENTE", f.desc());
        params.put("ORDEN", new String[]{"Fecha y hora", "Placa", "Conductor", "Estado"}[f.orden()]);
        Path parent = destino.toAbsolutePath().getParent(); Files.createDirectories(parent);
        Path tmp = Files.createTempFile(parent, "consulta01-", ".pdf");
        try (InputStream in = ConsultaService.class.getResourceAsStream("/reports/Consulta01.jrxml")) {
            if (in == null) throw new IllegalStateException("No se encontró Consulta01.jrxml");
            var reporte = net.sf.jasperreports.engine.JasperCompileManager.compileReport(in);
            var datos = new net.sf.jasperreports.engine.data.JRMapCollectionDataSource(filas);
            var print = net.sf.jasperreports.engine.JasperFillManager.fillReport(reporte, params, datos);
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(print, tmp.toString());
            Files.move(tmp, destino, StandardCopyOption.REPLACE_EXISTING);
        } finally { Files.deleteIfExists(tmp); }
    }
}
