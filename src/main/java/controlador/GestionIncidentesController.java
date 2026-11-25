package controlador;
import javax.swing.*;
import vista.GestionIncidentesView;
import servicio.EstadoAlertaService;
public class GestionIncidentesController {
 private final GestionIncidentesView view;private final EstadoAlertaService servicio=new EstadoAlertaService();
 public GestionIncidentesController(GestionIncidentesView v){view=v;v.getBtnRecargar().addActionListener(e->cargar(null));v.getBtnActualizarEstado().addActionListener(e->guardar());cargar(null);}
 private void error(Exception e){Throwable c=e.getCause()==null?e:e.getCause();view.mensaje("No se completó la operación.");JOptionPane.showMessageDialog(view,c.getMessage(),"Revisar operación",JOptionPane.ERROR_MESSAGE);}
 private void cargar(Integer id){view.ocupado(true);new SwingWorker<java.util.List<EstadoAlertaService.Incidente>,Void>(){
  protected java.util.List<EstadoAlertaService.Incidente> doInBackground()throws Exception{return servicio.listar();}
  protected void done(){view.ocupado(false);try{var lista=get();view.mostrarAlertas(lista,id);view.mensaje(lista.size()+" alertas leídas desde MySQL.");}catch(Exception e){view.mostrarAlertas(java.util.List.of(),null);error(e);}}
 }.execute();}
 private void guardar(){var a=view.getAlertaSeleccionada();if(a==null){view.mensaje("Selecciona una alerta antes de guardar.");return;}String nuevo=view.getEstadoSeleccionado();view.ocupado(true);
  new SwingWorker<Void,Void>(){
   protected Void doInBackground()throws Exception{servicio.actualizar(a.id(),a.estado(),nuevo);return null;}
   protected void done(){view.ocupado(false);try{get();JOptionPane.showMessageDialog(view,"Alerta "+a.id()+": estado guardado en MySQL: "+nuevo);cargar(a.id());}catch(Exception e){error(e);}}
  }.execute();
 }
}
