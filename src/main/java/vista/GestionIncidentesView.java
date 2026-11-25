package vista;
import controlador.GestionIncidentesController;
import servicio.EstadoAlertaService;
import servicio.EstadoAlertaService.Incidente;
import javax.swing.*;
import java.awt.*;
import java.util.List;
public class GestionIncidentesView extends JPanel {
 private final JComboBox<Incidente> alertas=new JComboBox<>();
 private final JComboBox<String> estados=new JComboBox<>(EstadoAlertaService.ESTADOS.toArray(String[]::new));
 private final JButton guardar=new JButton("Guardar estado en MySQL"),recargar=new JButton("Actualizar lista");
 private final JTextArea detalle=new JTextArea();
 private final JLabel mensaje=new JLabel("Cargando alertas…");
 public GestionIncidentesView(){
  setLayout(new BorderLayout(20,20));setBorder(BorderFactory.createEmptyBorder(30,30,30,30));setBackground(new Color(248,250,252));
  JPanel arriba=new JPanel(new GridLayout(0,1,8,8));JLabel titulo=new JLabel("Gestión de incidentes");titulo.setFont(new Font("SansSerif",Font.BOLD,26));
  arriba.add(titulo);arriba.add(new JLabel("Selecciona una alerta de la base de datos"));arriba.add(alertas);add(arriba,BorderLayout.NORTH);
  detalle.setEditable(false);detalle.setFont(new Font("SansSerif",Font.PLAIN,16));detalle.setLineWrap(true);detalle.setWrapStyleWord(true);detalle.setMargin(new Insets(20,20,20,20));add(new JScrollPane(detalle),BorderLayout.CENTER);
  JPanel pie=new JPanel(new GridLayout(0,1,8,8)),acciones=new JPanel(new FlowLayout(FlowLayout.LEFT));
  acciones.add(new JLabel("Nuevo estado:"));acciones.add(estados);acciones.add(guardar);acciones.add(recargar);pie.add(acciones);pie.add(mensaje);
  pie.add(new JLabel("Esta pantalla guarda en MySQL; no envía comunicaciones a la Policía."));add(pie,BorderLayout.SOUTH);
  alertas.addActionListener(e->mostrarSeleccion());new GestionIncidentesController(this);
 }
 private void mostrarSeleccion(){var a=getAlertaSeleccionada();if(a==null){detalle.setText("No hay alertas disponibles.");return;}
  estados.setSelectedItem(a.estado());detalle.setText("Alerta: "+a.id()+"\nFecha: "+a.fecha()+"\nUnidad: "+a.bus()+"\nConductor: "+a.conductor()+"\nModalidad: "+a.modalidad()+"\nEstado guardado: "+a.estado()+"\n\n"+a.descripcion());
 }
 public void mostrarAlertas(List<Incidente> lista,Integer id){alertas.removeAllItems();for(var a:lista){alertas.addItem(a);if(id!=null&&a.id()==id)alertas.setSelectedItem(a);}mostrarSeleccion();}
 public Incidente getAlertaSeleccionada(){return (Incidente)alertas.getSelectedItem();}
 public String getEstadoSeleccionado(){return (String)estados.getSelectedItem();}
 public JButton getBtnActualizarEstado(){return guardar;}
 public JButton getBtnRecargar(){return recargar;}
 public void ocupado(boolean b){guardar.setEnabled(!b);recargar.setEnabled(!b);alertas.setEnabled(!b);estados.setEnabled(!b);}
 public void mensaje(String s){mensaje.setText(s);}
}
