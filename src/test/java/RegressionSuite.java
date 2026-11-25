import conexion.ConexionBD;
import dao.*;
import modelo.*;
import modelo.Consulta.*;
import servicio.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import net.sf.jasperreports.engine.*;

/** Ejecutar exclusivamente en la base nueva creada con database/demo/crear_demo.sql. */
public class RegressionSuite {
 static int checks;
 static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 interface Action {void run() throws Exception;}
 static void rejects(Action a,String name)throws Exception{boolean failed=false;try{a.run();}catch(IllegalArgumentException|SQLException|java.time.DateTimeException e){failed=true;}check(failed,name);}
 static Filtro filter(String from,String to,String state,String plate,boolean or){return new Filtro(from,to,state,plate,"Contiene","",or,0,true);}
 static int count(Filtro f)throws Exception{return ConsultaDAO.ejecutar(f).filas().size();}
 static String pdf(Path path)throws Exception{try(PdfReader r=new PdfReader(path.toString())){check(r.getNumberOfPages()>0,"PDF legible: "+path.getFileName());StringBuilder b=new StringBuilder();PdfTextExtractor e=new PdfTextExtractor(r);for(int i=1;i<=r.getNumberOfPages();i++)b.append(e.getTextFromPage(i));return b.toString();}}
 public static void main(String[] args)throws Exception{
  Path out=Path.of("target/evidencias");Files.createDirectories(out);
  try(Connection c=ConexionBD.obtenerConexion()){
   if(!"sat_carmen_integracion_prueba".equals(c.getCatalog()))throw new IllegalStateException("Solo se permite la base aislada de pruebas");
   try(var q=c.createStatement();var r=q.executeQuery("SELECT COUNT(*) FROM alerta")){r.next();check(r.getInt(1)==50,"fixture inicial de 50 alertas");}
  }
  Filtro all=filter("","","Todos","",false);
  Resultado result=ConsultaDAO.ejecutar(all);
  check(result.filas().size()==50,"consulta general 50 filas");
  check(result.filas().stream().map(a->a[0]).distinct().count()==50,"sin duplicación de alertas");
  check(count(filter("","","En Evaluación","AEE-000",false))==1,"filtros AND");
  check(count(filter("","","En Evaluación","AEE-000",true))==11,"filtros OR");
  check(count(filter("2099-01-01","2099-12-31","Todos","",false))==0,"resultado vacío");
  check(count(filter("","","Todos","' OR 1=1 --",false))==0,"inyección tratada como texto");
  check(count(filter("","","Todos","%",false))==0,"porcentaje literal");
  check(count(filter("","","Todos","_",false))==0,"guion bajo literal");
  rejects(()->ConsultaDAO.construir(filter("2026-02-30","","Todos","",false)),"fecha imposible");
  rejects(()->ConsultaDAO.construir(filter("2026-12-01","2026-11-01","Todos","",false)),"rango invertido");
  rejects(()->new FiltroReporte(Timestamp.valueOf("2026-12-01 00:00:00"),Timestamp.valueOf("2026-11-01 00:00:00"),null,null,null),"rango de reportes inválido");
  var day=ConsultaDAO.ejecutar(filter("2026-11-08","2026-11-08","Todos","",false));
  check(!day.filas().isEmpty()&&day.filas().stream().allMatch(a->a[1].toString().startsWith("2026-11-08")),"fecha final inclusiva");
  for(String mode:List.of("Empieza en","Termina en")){
   var r=ConsultaDAO.ejecutar(new Filtro("","","Todos",mode.equals("Empieza en")?"AE":"000",mode,"",false,1,false));
   check(!r.filas().isEmpty()&&r.filas().stream().allMatch(a->mode.equals("Empieza en")?a[2].toString().startsWith("AE"):a[2].toString().endsWith("000")),"LIKE "+mode);
  }
  var ascending=ConsultaDAO.ejecutar(new Filtro("","","Todos","","Contiene","",false,0,false));
  for(int i=1;i<ascending.filas().size();i++)if(ascending.filas().get(i-1)[1].toString().compareTo(ascending.filas().get(i)[1].toString())>0)throw new AssertionError("orden");
  check(true,"orden ascendente");
  ConsultaService.pdf(result,out.resolve("Consulta01.pdf"));check(pdf(out.resolve("Consulta01.pdf")).contains("50"),"PDF consulta contiene cantidad");
  ConsultaService.pdf(ConsultaDAO.ejecutar(filter("2099-01-01","","Todos","",false)),out.resolve("Consulta01-vacia.pdf"));
  check(pdf(out.resolve("Consulta01-vacia.pdf")).contains("Sin coincidencias"),"PDF vacío informativo");
  ReporteService reports=new ReporteService();
  for(String type:List.of("Incidencias","Estados","Modalidades")){
   JasperPrint print=reports.generarReporte(type,new FiltroReporte(null,null,null,null,null));
   check(!print.getPages().isEmpty(),"Jasper genera "+type);
   Path dest=out.resolve(type+".pdf");reports.exportarAPdf(print,dest.toFile());check(!pdf(dest).isBlank(),"PDF Jasper con texto "+type);
   var empty=reports.generarReporte(type,new FiltroReporte(Timestamp.valueOf("2099-01-01 00:00:00"),null,null,null,null));
   check(!empty.getPages().isEmpty(),"Jasper sin datos "+type);
   var selected=reports.generarReporte(type,new FiltroReporte(null,null,"En Evaluación",null,null));
   check(!selected.getPages().isEmpty(),"Jasper filtro estado "+type);
  }
  try(var in=RegressionSuite.class.getResourceAsStream("/reports/Consulta01.jrxml");var c=ConexionBD.obtenerConexion()){
   var template=JasperCompileManager.compileReport(in);var print=JasperFillManager.fillReport(template,new HashMap<>(),c);
   JasperExportManager.exportReportToPdfFile(print,out.resolve("Consulta01-Studio.pdf").toString());pdf(out.resolve("Consulta01-Studio.pdf"));
  }
  Consulta02.Filtro c02All=new Consulta02.Filtro("","","Todas","Todos","","Contiene","",false,0,true);
  Consulta02.Resultado r02=Consulta02DAO.ejecutar(c02All);
  check(r02.filas().size()==50,"Consulta 02 general 50 despachos");
  check(r02.filas().stream().map(a->a[0]).distinct().count()==50,"Consulta 02 sin duplicación de despachos");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Comisaría ficticia 001","Simulado","DEMO-001","Contiene","Ficticio 001",false,0,true)).filas().size()==1,"Consulta 02 filtros AND");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Comisaría ficticia 001","Todos","AEE-000","Contiene","",true,0,true)).filas().size()==2,"Consulta 02 filtros OR");
  var c02Day=Consulta02DAO.ejecutar(new Consulta02.Filtro("2026-11-08","2026-11-08","Todas","Todos","","Contiene","",false,0,true));
  check(c02Day.filas().size()==3&&c02Day.filas().stream().allMatch(a->a[2].toString().startsWith("2026-11-08")),"Consulta 02 fecha final inclusiva");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Todas","Todos","AE","Empieza en","",false,2,false)).filas().size()==1,"Consulta 02 LIKE Empieza en");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Todas","Todos","050","Termina en","",false,2,false)).filas().size()==1,"Consulta 02 LIKE Termina en unidad");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Todas","Todos","","Contiene","Ficticio 025",false,3,false)).filas().size()==1,"Consulta 02 filtro operador");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Todas","Todos","' OR 1=1 --","Contiene","",false,0,true)).filas().isEmpty(),"Consulta 02 inyección tratada como texto");
  check(Consulta02DAO.ejecutar(new Consulta02.Filtro("","","Todas","Todos","%","Contiene","_",false,0,true)).filas().isEmpty(),"Consulta 02 comodines literales");
  rejects(()->Consulta02DAO.construir(new Consulta02.Filtro("2026-02-30","","Todas","Todos","","Contiene","",false,0,true)),"Consulta 02 fecha imposible");
  rejects(()->Consulta02DAO.construir(new Consulta02.Filtro("2026-12-01","2026-11-01","Todas","Todos","","Contiene","",false,0,true)),"Consulta 02 rango invertido");
  var cat02=Consulta02DAO.cargarCatalogos();
  check(cat02.comisarias().size()==50&&cat02.estadosDespacho().contains("Simulado"),"Consulta 02 catálogos dinámicos");
  Consulta02Service.pdf(c02All,out.resolve("Consulta02.pdf"));
  check(pdf(out.resolve("Consulta02.pdf")).contains("50"),"PDF Jasper Consulta 02 contiene cantidad");
  Consulta02Service.pdf(new Consulta02.Filtro("","","Comisaría ficticia 001","Simulado","TST-001","Contiene","Ficticio 001",false,0,true),out.resolve("Consulta02-filtrada.pdf"));
  check(pdf(out.resolve("Consulta02-filtrada.pdf")).contains("TST-001"),"PDF Jasper Consulta 02 filtrada");
  Consulta02Service.pdf(new Consulta02.Filtro("2099-01-01","2099-12-31","Todas","Todos","","Contiene","",false,0,true),out.resolve("Consulta02-vacia.pdf"));
  check(pdf(out.resolve("Consulta02-vacia.pdf")).contains("Sin coincidencias"),"PDF Jasper Consulta 02 vacío informativo");
  try(var in=RegressionSuite.class.getResourceAsStream("/reports/Consulta02.jrxml");var c=ConexionBD.obtenerConexion()){
   var template=JasperCompileManager.compileReport(in);var print=JasperFillManager.fillReport(template,new HashMap<>(),c);
   JasperExportManager.exportReportToPdfFile(print,out.resolve("Consulta02-Studio.pdf").toString());
   check(pdf(out.resolve("Consulta02-Studio.pdf")).contains("50"),"Consulta02.jrxml compatible con Studio");
  }
  var service=new EstadoAlertaService();var incident=service.listar().getFirst();String next=incident.estado().equals("Atendida")?"Recibida":"Atendida";
  service.actualizar(incident.id(),incident.estado(),next);
  check(new AlertaDAO().obtenerPorId(incident.id()).getEstado().equals(next),"estado persistido");
  rejects(()->service.actualizar(incident.id(),incident.estado(),next),"conflicto concurrente detectado");
  service.actualizar(incident.id(),next,incident.estado());
  rejects(()->service.actualizar(incident.id(),incident.estado(),"INVALIDO"),"estado inválido rechazado");
  Conductor driver=new Conductor(null,"Prueba","Integración","99999001","000000000","TEST-01","Activo");
  check(new ConductorDAO().registrarConductor(driver),"registro conductor DAO");
  Bus bus=new Bus();bus.setPlaca("TQA-001");bus.setNumeroUnidad("TEST-001");bus.setModelo("Prueba");bus.setCapacidad(20);bus.setEstado("Activo");
  check(new BusDAO().registrarBus(bus),"registro bus DAO");
  check(!new BusDAO().registrarBus(bus),"placa duplicada rechazada");
  Operador operator=new Operador();operator.setNombres("Prueba");operator.setApellidos("Integración");operator.setTelefono("000000000");operator.setCargo("Pruebas");
  check(new OperadorDAO().registrarOperador(operator,"test_integracion","solo-datos-ficticios"),"registro transaccional operador");
  check(new UsuarioDAO().validarAcceso("test_integracion","solo-datos-ficticios")!=null,"login central real");
  check(new UsuarioDAO().validarAcceso("test_integracion","incorrecta")==null,"login inválido");
  Alerta alert=new Alerta(0,1,1,null,Timestamp.valueOf("2026-11-24 12:00:00"),"Reporte manual de central","Recibida","Prueba aislada");
  int id=new AlertaDAO().registrarAlerta(alert);check(id>0&&new AlertaDAO().obtenerPorId(id)!=null,"registro y lectura alerta DAO");
  System.out.println("RESULTADO: "+checks+" comprobaciones correctas. Solo datos académicos.");
 }
}
