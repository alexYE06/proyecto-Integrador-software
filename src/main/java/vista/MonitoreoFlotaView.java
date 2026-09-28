package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MonitoreoFlotaView extends JFrame {

    private JTable tblFlota;
    private DefaultTableModel modeloTabla;
    private JButton btnVerIncidente;
    private JButton btnSimularAlertaFisica; // Función estrella (Sección 4 del Plan)

    public MonitoreoFlotaView() {
        setTitle("SAT-Carmen — Panel General de Flota (80 Unidades)");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(10, 16, 29));

        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Panel Superior: KPIs de la Flota
        JPanel pnlKPIs = new JPanel(new GridLayout(1, 4, 15, 10));
        pnlKPIs.setBackground(new Color(16, 23, 38));
        pnlKPIs.setBorder(new EmptyBorder(15, 20, 15, 20));

        pnlKPIs.add(crearTarjetaKPI("UNIDADES ACTIVAS", "76 / 80", new Color(16, 185, 129)));
        pnlKPIs.add(crearTarjetaKPI("ALERTAS ACTIVAS", "1 CRÍTICA", new Color(239, 68, 68)));
        pnlKPIs.add(crearTarjetaKPI("TIEMPO RESPUESTA", "6.8s (Meta <= 10s)", new Color(250, 204, 21)));
        pnlKPIs.add(crearTarjetaKPI("ESTADO SERVICIO", "WebSockets OK", new Color(56, 189, 248)));

        add(pnlKPIs, BorderLayout.NORTH);

        // Centro: Tabla de Unidades en Ruta
        String[] columnas = { "Padrón", "Placa", "Conductor", "Ruta Actual", "Velocidad", "Estado" };
        modeloTabla = new DefaultTableModel(columnas, 0);

        // Datos iniciales de demostración
        modeloTabla.addRow(
                new Object[] { "BUS-024", "ZY4-906", "J. Horna", "Callao - Centro Histórico", "23 km/h", "NORMAL" });
        modeloTabla.addRow(new Object[] { "BUS-051", "ZZ7-801", "Elena Castillo", "Ventanilla - La Perla", "27 km/h",
                "EN ALERTA (SOS)" });
        modeloTabla.addRow(
                new Object[] { "BUS-1305", "A8D-712", "A. Duran", "Ate - La Punta (Línea 71A)", "38 km/h", "NORMAL" });

        tblFlota = new JTable(modeloTabla);
        tblFlota.setBackground(new Color(16, 23, 38));
        tblFlota.setForeground(Color.WHITE);
        tblFlota.setRowHeight(28);
        tblFlota.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(tblFlota);
        scroll.getViewport().setBackground(new Color(10, 16, 29));
        scroll.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // Panel Inferior de Botones de Acción
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        pnlAcciones.setBackground(new Color(16, 23, 38));

        btnSimularAlertaFisica = new JButton("⚡ SIMULAR 3 PULSACIONES (CHOFER)");
        btnSimularAlertaFisica.setBackground(new Color(180, 83, 9));
        btnSimularAlertaFisica.setForeground(Color.WHITE);
        btnSimularAlertaFisica.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnVerIncidente = new JButton("GESTIONAR INCIDENTE ACTIVO →");
        btnVerIncidente.setBackground(new Color(225, 29, 72));
        btnVerIncidente.setForeground(Color.WHITE);
        btnVerIncidente.setFont(new Font("SansSerif", Font.BOLD, 12));

        pnlAcciones.add(btnSimularAlertaFisica);
        pnlAcciones.add(btnVerIncidente);
        add(pnlAcciones, BorderLayout.SOUTH);

        // Eventos básicos de navegación
        btnVerIncidente.addActionListener(e -> new IncidentesView().setVisible(true));
        btnSimularAlertaFisica.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "[SIMULACIÓN DE HARDWARE]: Se detectaron 3 pulsaciones consecutivas en el botón físico.\n" +
                            "Alerta silenciosa generada para el BUS-051 y transmitida a MySQL.",
                    "Disparo de Alerta Silenciosa", JOptionPane.WARNING_MESSAGE);
        });
    }

    private JPanel crearTarjetaKPI(String titulo, String valor, Color colorValor) {
        JPanel pnl = new JPanel(new GridLayout(2, 1));
        pnl.setBackground(new Color(5, 8, 15));
        pnl.setBorder(BorderFactory.createLineBorder(new Color(30, 41, 59), 1));
        JLabel lblT = new JLabel("  " + titulo);
        lblT.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblT.setForeground(new Color(148, 163, 184));
        JLabel lblV = new JLabel("  " + valor);
        lblV.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblV.setForeground(colorValor);
        pnl.add(lblT);
        pnl.add(lblV);
        return pnl;
    }
}