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
    public static void pdf(Resultado r,Path destino) throws Exception {
        // Se exporta la misma instantánea mostrada; no se ejecuta nuevamente SQL.
        Path tmp=Files.createTempFile(destino.toAbsolutePath().getParent(),"consulta-",".pdf");
        try{
            com.lowagie.text.Document doc=new com.lowagie.text.Document(PageSize.A4.rotate(),28,28,32,32);
            try(OutputStream out=Files.newOutputStream(tmp)){
                PdfWriter writer=PdfWriter.getInstance(doc,out);
                writer.setPageEvent(new PdfPageEventHelper(){public void onEndPage(PdfWriter w,com.lowagie.text.Document d){ColumnText.showTextAligned(w.getDirectContent(),Element.ALIGN_RIGHT,new Phrase("Página "+w.getPageNumber(),FontFactory.getFont(FontFactory.HELVETICA,8)),d.right(),18,0);}});
                doc.open();
                doc.add(new Paragraph("SAT-CARMEN | Consulta 01 de alertas",FontFactory.getFont(FontFactory.HELVETICA_BOLD,16)));
                doc.add(new Paragraph("Emitido: "+LocalDateTime.now()+" | Registros: "+r.filas().size(),FontFactory.getFont(FontFactory.HELVETICA,9)));
                doc.add(new Paragraph(resumen(r.filtro()),FontFactory.getFont(FontFactory.HELVETICA,9)));
                PdfPTable tabla=new PdfPTable(new float[]{.5f,1.2f,.75f,.7f,1.7f,1.8f,1.05f}); tabla.setWidthPercentage(100);tabla.setSpacingBefore(12);tabla.setHeaderRows(1);
                for(String c:COLUMNAS){PdfPCell cell=new PdfPCell(new Phrase(c,FontFactory.getFont(FontFactory.HELVETICA_BOLD,9)));cell.setBackgroundColor(new Color(225,233,240));cell.setPadding(5);tabla.addCell(cell);}
                for(Object[] fila:r.filas())for(Object v:fila){PdfPCell cell=new PdfPCell(new Phrase(v==null?"":v.toString(),FontFactory.getFont(FontFactory.HELVETICA,9)));cell.setPadding(5);tabla.addCell(cell);}
                doc.add(tabla);if(r.filas().isEmpty())doc.add(new Paragraph("Sin coincidencias para los filtros seleccionados."));doc.close();
            }
            Files.move(tmp,destino,StandardCopyOption.REPLACE_EXISTING);
        }finally{Files.deleteIfExists(tmp);}
    }
}
