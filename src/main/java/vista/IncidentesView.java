package vista;

import controlador.AlertaController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class IncidentesView extends JFrame {

    private JComboBox<String> cbEstadoAlerta;
    private JButton btnActualizarEstado;
    private JButton btnDespacharPNP;
    private AlertaController alertaController;

    public IncidentesView() {
        setTitle("SAT-Carmen — Gestión de Incidente Activo (BUS-051)");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(10, 16, 29));

        initComponentes();
        this.alertaController = new AlertaController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(15, 15));

        // Banner Superior de Emergencia Crítica
        JPanel pnlBanner = new JPanel(new BorderLayout());
        pnlBanner.setBackground(new Color(225, 29, 72));
        pnlBanner.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lblEmergencia = new JLabel("🚨 ALERTA CRÍTICA: BUS-051 (EXTORSIÓN / ASALTO EN CURSO)");
        lblEmergencia.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblEmergencia.setForeground(Color.WHITE);
        pnlBanner.add(lblEmergencia, BorderLayout.WEST);
        add(pnlBanner, BorderLayout.NORTH);

        // Centro: Cuadrícula con Datos Técnicos y Evidencia
        JPanel pnlDetalles = new JPanel(new GridLayout(2, 2, 15, 15));
        pnlDetalles.setBackground(new Color(10, 16, 29));
        pnlDetalles.setBorder(new EmptyBorder(10, 20, 10, 20));

        pnlDetalles.add(crearCajaInfo("FICHA DEL VEHÍCULO",
                "<html>Placa: ZZ7-801<br>Conductor: Elena Castillo Bravo<br>Velocidad: 27 km/h<br>Batería móvil: 61%</html>"));

        pnlDetalles.add(crearCajaInfo("GEOLOCALIZACIÓN Y HAVERSINE",
                "<html>Coordenadas: -12.04582, -77.09461<br>Comisaría más cercana: <b>Comisaría Callao</b><br>Distancia ortodrómica: <b>1.2 km (Haversine)</b><br>ETA patrulla: 3 minutos</html>"));

        pnlDetalles.add(crearCajaInfo("CANAL DE EVIDENCIAS EN VIVO",
                "<html>• Micrófono ambiental: <b>TRANSMITIENDO</b><br>• Video frontal: <b>ACTIVO (1080p adaptable)</b><br>• Cifrado: AES-256 + Marca de agua temporal</html>"));

        // Caja de Acciones de Operador
        JPanel pnlAccionOperador = new JPanel(new GridLayout(3, 1, 8, 8));
        pnlAccionOperador.setBackground(new Color(16, 23, 38));
        pnlAccionOperador.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)), "RESOLUCIÓN DEL OPERADOR", 0, 0,
                new Font("SansSerif", Font.BOLD, 11), new Color(250, 204, 21)));

        cbEstadoAlerta = new JComboBox<>(new String[] {
                "Recibida", "En Evaluacion", "Auxilio Despachado", "Atendida (Cerrada)", "Falsa Alarma (PIN)"
        });
        cbEstadoAlerta.setBackground(new Color(5, 8, 15));
        cbEstadoAlerta.setForeground(Color.WHITE);

        btnActualizarEstado = new JButton("ACTUALIZAR ESTADO EN BD");
        btnActualizarEstado.setBackground(new Color(37, 99, 235));
        btnActualizarEstado.setForeground(Color.WHITE);
        btnActualizarEstado.setFont(new Font("SansSerif", Font.BOLD, 11));

        btnDespacharPNP = new JButton("🚔 DESPACHAR A CENTRAL PNP 105");
        btnDespacharPNP.setBackground(new Color(16, 185, 129));
        btnDespacharPNP.setForeground(Color.WHITE);
        btnDespacharPNP.setFont(new Font("SansSerif", Font.BOLD, 11));

        pnlAccionOperador.add(cbEstadoAlerta);
        pnlAccionOperador.add(btnActualizarEstado);
        pnlAccionOperador.add(btnDespacharPNP);

        pnlDetalles.add(pnlAccionOperador);
        add(pnlDetalles, BorderLayout.CENTER);
    }

    private JPanel crearCajaInfo(String titulo, String contenidoHtml) {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBackground(new Color(16, 23, 38));
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)), titulo, 0, 0,
                new Font("SansSerif", Font.BOLD, 11), Color.WHITE));

        JLabel lbl = new JLabel(contenidoHtml);
        lbl.setForeground(new Color(203, 213, 225));
        lbl.setBorder(new EmptyBorder(8, 10, 8, 10));
        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }

    public String getEstadoSeleccionado() {
        return (String) cbEstadoAlerta.getSelectedItem();
    }

    public JButton getBtnActualizarEstado() {
        return btnActualizarEstado;
    }

    public JButton getBtnDespacharPNP() {
        return btnDespacharPNP;
    }
}