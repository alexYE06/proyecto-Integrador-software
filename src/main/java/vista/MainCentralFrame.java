package vista;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainCentralFrame extends JFrame {

    private static MainCentralFrame instance;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainCentralFrame() {
        instance = this;
        setTitle("SAT-Carmen - Central de Operaciones");
        setSize(1366, 768);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barra superior de navegación (permite abrir Reportes sin editar IncidentesView)
        JMenuBar menuBar = new JMenuBar();
        JMenu menuNavegacion = new JMenu("Módulos");

        JMenuItem itemMonitoreo = new JMenuItem("Monitoreo de flota");
        itemMonitoreo.addActionListener(e -> cambiarPantalla("Monitoreo"));

        JMenuItem itemIncidentes = new JMenuItem("Incidente activo");
        itemIncidentes.addActionListener(e -> cambiarPantalla("Incidentes"));

        JMenuItem itemReportes = new JMenuItem("Reportes PDF (Filtros)");
        itemReportes.addActionListener(e -> cambiarPantalla("Reportes"));

        JMenuItem itemBackend = new JMenuItem("Servicios backend");
        itemBackend.addActionListener(e -> cambiarPantalla("Backend"));

        menuNavegacion.add(itemMonitoreo);
        menuNavegacion.add(itemIncidentes);
        menuNavegacion.add(itemReportes);
        menuNavegacion.add(itemBackend);
        menuBar.add(menuNavegacion);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Añadir las vistas como tarjetas
        mainPanel.add(new MonitoreoFlotaView(), "Monitoreo");
        mainPanel.add(new IncidentesView(), "Incidentes");
        mainPanel.add(new ReportesView(), "Reportes");
        mainPanel.add(new ServiciosBackendView(), "Backend");

        add(mainPanel);
    }

    public static MainCentralFrame getInstance() {
        return instance;
    }

    public void cambiarPantalla(String nombrePantalla) {
        cardLayout.show(mainPanel, nombrePantalla);
    }
}