package vista;
import modelo.Consulta.*;
import static dao.ConsultaDAO.*;
import static servicio.ConsultaService.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/** Consulta multitabla de Semana 15. No modifica datos. */
public class Consulta01 extends JFrame {
    public static final String[] COLUMNAS={"Alerta", "Fecha y hora", "Placa", "Unidad", "Conductor", "Modalidad", "Estado"};
    private final JTextField desde=new JTextField(), hasta=new JTextField(), placa=new JTextField(), conductor=new JTextField();
    private final JComboBox<String> estado=new JComboBox<>(new String[]{"Todos"});
    private final JComboBox<String> modo=new JComboBox<>(new String[]{"Contiene", "Empieza en", "Termina en"});
    private final JComboBox<String> logica=new JComboBox<>(new String[]{"Todas (Y)", "Cualquiera (O)"});
    private final JComboBox<String> orden=new JComboBox<>(new String[]{"Fecha y hora", "Placa", "Conductor", "Estado"});
    private final JCheckBox descendente=new JCheckBox("Descendente",true);
    private final JButton consultar=new JButton("Consultar y abrir resultados");
    private final JLabel mensaje=new JLabel("Cargando estados de la base de datos...");

    public static Connection conexion() throws SQLException { return conexion.ConexionBD.obtenerConexion(); }
    public Consulta01(){
        super("SAT-CARMEN | Consulta 01 | Filtros");setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel panel=new JPanel(new GridLayout(0,2,12,12));panel.setBorder(BorderFactory.createEmptyBorder(22,24,22,24));
        agregar(panel,"Desde (AAAA-MM-DD, opcional)",desde);agregar(panel,"Hasta (incluye todo el día)",hasta);
        agregar(panel,"Estado de alerta",estado);agregar(panel,"Placa",placa);agregar(panel,"Coincidencia de placa",modo);
        agregar(panel,"Nombre del conductor contiene",conductor);agregar(panel,"Combinar filtros activos",logica);agregar(panel,"Ordenar por",orden);
        panel.add(descendente);JButton limpiar=new JButton("Limpiar filtros");panel.add(limpiar);
        JPanel pie=new JPanel(new BorderLayout(8,8));pie.setBorder(BorderFactory.createEmptyBorder(0,24,20,24));pie.add(consultar,BorderLayout.NORTH);pie.add(mensaje,BorderLayout.SOUTH);
        add(new JLabel("   Consulta multitabla: alerta + bus + conductor",SwingConstants.LEFT),BorderLayout.NORTH);add(panel);add(pie,BorderLayout.SOUTH);
        limpiar.addActionListener(e->{desde.setText("");hasta.setText("");placa.setText("");conductor.setText("");estado.setSelectedIndex(0);modo.setSelectedIndex(0);logica.setSelectedIndex(0);orden.setSelectedIndex(0);descendente.setSelected(true);});
        consultar.addActionListener(e->buscar());setSize(780,530);setLocationRelativeTo(null);
        new SwingWorker<List<String>,Void>(){
            protected List<String> doInBackground()throws Exception{List<String> v=new ArrayList<>();try(Connection c=conexion();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT DISTINCT estado FROM alerta ORDER BY estado")){while(r.next())v.add(r.getString(1));}return v;}
            protected void done(){try{for(String s:get())estado.addItem(s);mensaje.setText("Conexión lista. Los campos vacíos no restringen la consulta.");}catch(Exception ex){mensaje.setText("No se cargaron estados. Comprueba la conexión y vuelve a abrir.");}}
        }.execute();
    }
    private void agregar(JPanel p,String s,JComponent c){p.add(new JLabel(s));p.add(c);}
    private void buscar(){
        Filtro f=new Filtro(desde.getText().trim(),hasta.getText().trim(),(String)estado.getSelectedItem(),placa.getText().trim(),(String)modo.getSelectedItem(),conductor.getText().trim(),logica.getSelectedIndex()==1,orden.getSelectedIndex(),descendente.isSelected());
        try{construir(f);}catch(Exception e){JOptionPane.showMessageDialog(this,"Revisa las fechas (AAAA-MM-DD) y el intervalo.\n"+e.getMessage());return;}
        consultar.setEnabled(false);mensaje.setText("Consultando...");
        new SwingWorker<Resultado,Void>(){
            protected Resultado doInBackground() throws Exception{return ejecutar(f);}
            protected void done(){consultar.setEnabled(true);try{Resultado r=get();new Resultados(r).setVisible(true);mensaje.setText("Consulta completada: "+r.filas().size()+" registros.");}catch(Exception e){mensaje.setText("No se pudo consultar.");JOptionPane.showMessageDialog(Consulta01.this,"Revisa conexión, base y permisos.\n"+e.getCause());}}
        }.execute();
    }
    public static class Resultados extends JFrame{
        public Resultados(Resultado r){
            super("SAT-CARMEN | Consulta 01 | Resultados y exportación");setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            DefaultTableModel model=new DefaultTableModel(COLUMNAS,0){public boolean isCellEditable(int row,int col){return false;}};
            for(Object[] fila:r.filas())model.addRow(fila);JTable tabla=new JTable(model);tabla.setRowHeight(28);
            JTextArea cab=new JTextArea(resumen(r.filtro())+"\n"+r.filas().size()+" registros"+(r.filas().isEmpty()?". Sin coincidencias.":""));cab.setEditable(false);cab.setLineWrap(true);cab.setWrapStyleWord(true);cab.setRows(3);cab.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            add(cab,BorderLayout.NORTH);add(new JScrollPane(tabla));
            JPanel botones=new JPanel();JButton exportar=new JButton("Exportar PDF"),sql=new JButton("Ver SQL y parámetros"),cerrar=new JButton("Volver a filtros");botones.add(exportar);botones.add(sql);botones.add(cerrar);add(botones,BorderLayout.SOUTH);
            cerrar.addActionListener(e->dispose());sql.addActionListener(e->{JTextArea a=new JTextArea(r.sentencia().sql()+"\n\nParámetros: "+r.sentencia().valores(),12,70);a.setLineWrap(true);a.setWrapStyleWord(true);a.setEditable(false);JOptionPane.showMessageDialog(this,new JScrollPane(a),"Consulta parametrizada",JOptionPane.INFORMATION_MESSAGE);});
            exportar.addActionListener(e->{JFileChooser fc=new JFileChooser();fc.setSelectedFile(new File("Consulta01.pdf"));if(fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;Path p=fc.getSelectedFile().toPath();if(!p.toString().toLowerCase().endsWith(".pdf"))p=Path.of(p+".pdf");if(Files.exists(p)&&JOptionPane.showConfirmDialog(this,"¿Reemplazar el PDF existente?","Archivo existente",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;final Path destino=p;exportar.setEnabled(false);
                new SwingWorker<Void,Void>(){protected Void doInBackground()throws Exception{pdf(r,destino);return null;}protected void done(){exportar.setEnabled(true);try{get();JOptionPane.showMessageDialog(Resultados.this,"PDF guardado en:\n"+destino.toAbsolutePath());}catch(Exception ex){JOptionPane.showMessageDialog(Resultados.this,"No se pudo guardar el PDF: "+ex.getCause());}}}.execute();});
            setSize(1180,620);setLocationRelativeTo(null);
        }
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new Consulta01().setVisible(true));}
}
