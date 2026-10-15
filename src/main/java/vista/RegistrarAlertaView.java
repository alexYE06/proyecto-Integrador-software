package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.RegistrarAlertaController;
import modelo.Bus;
import modelo.Conductor;
import modelo.Operador;

public class RegistrarAlertaView extends JFrame {

    private final JComboBox<Conductor> cbConductor = new JComboBox<>();
    private final JComboBox<Bus> cbBus = new JComboBox<>();
    private final JComboBox<Operador> cbOperador = new JComboBox<>();
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[] {
            "Tres pulsaciones botón encendido", "Reporte manual de central", "Llamada de emergencia"
    });
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[] {
            "Recibida", "En Evaluación", "Auxilio Despachado", "Atendida", "Falsa Alarma"
    });
    private final JTextArea txtDescripcion = new JTextArea(4, 20);
    private final JButton btnRegistrar = new JButton("REGISTRAR ALERTA");

    public RegistrarAlertaView() {
        setTitle("SAT-Carmen - Registrar alerta");
        setSize(560, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 16, 29));
        initComponentes();
        new RegistrarAlertaController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("REGISTRO DE ALERTA DE EMERGENCIA", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(20, 10, 0, 10));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 12));
        formulario.setBackground(new Color(16, 23, 38));
        formulario.setBorder(new EmptyBorder(25, 30, 20, 30));
        agregarCampo(formulario, "CONDUCTOR:", cbConductor);
        agregarCampo(formulario, "BUS / UNIDAD:", cbBus);
        agregarCampo(formulario, "OPERADOR ASIGNADO:", cbOperador);
        agregarCampo(formulario, "TIPO DE ACTIVACIÓN:", cbTipo);
        agregarCampo(formulario, "ESTADO:", cbEstado);

        JLabel etiquetaDescripcion = new JLabel("DESCRIPCIÓN (OPCIONAL):");
        etiquetaDescripcion.setForeground(new Color(148, 163, 184));
        etiquetaDescripcion.setFont(new Font("SansSerif", Font.BOLD, 11));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        formulario.add(etiquetaDescripcion);
        formulario.add(txtDescripcion);

        btnRegistrar.setBackground(new Color(225, 29, 72));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        add(titulo, BorderLayout.NORTH);
        add(formulario, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }

    private static void agregarCampo(JPanel formulario, String texto, java.awt.Component campo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(148, 163, 184));
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 11));
        formulario.add(etiqueta);
        formulario.add(campo);
    }

    public void cargarOpciones(List<Conductor> conductores, List<Bus> buses, List<Operador> operadores) {
        for (Conductor conductor : conductores) cbConductor.addItem(conductor);
        for (Bus bus : buses) cbBus.addItem(bus);
        cbOperador.addItem(null);
        for (Operador operador : operadores) cbOperador.addItem(operador);
        cbOperador.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value == null ? "Sin asignar" : value.toString());
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });
    }

    public Conductor getConductorSeleccionado() { return (Conductor) cbConductor.getSelectedItem(); }
    public Bus getBusSeleccionado() { return (Bus) cbBus.getSelectedItem(); }
    public Operador getOperadorSeleccionado() { return (Operador) cbOperador.getSelectedItem(); }
    public String getTipoSeleccionado() { return (String) cbTipo.getSelectedItem(); }
    public String getEstadoSeleccionado() { return (String) cbEstado.getSelectedItem(); }
    public String getDescripcion() { return txtDescripcion.getText().trim(); }
    public JButton getBtnRegistrar() { return btnRegistrar; }

    public void deshabilitarRegistro() {
        btnRegistrar.setEnabled(false);
    }

    public void limpiarFormulario() {
        cbConductor.setSelectedIndex(0);
        cbBus.setSelectedIndex(0);
        cbOperador.setSelectedIndex(0);
        cbTipo.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        txtDescripcion.setText("");
    }
}
