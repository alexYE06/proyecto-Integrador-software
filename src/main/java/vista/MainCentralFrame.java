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

        JMenuItem itemMonitoreo = new JMenuItem("Monitoreo de flota (prototipo)");
        itemMonitoreo.addActionListener(e -> cambiarPantalla("Monitoreo"));

        JMenuItem itemIncidentes = new JMenuItem("Incidente (simulación)");
        itemIncidentes.addActionListener(e -> cambiarPantalla("Incidentes"));

        JMenuItem itemReportes = new JMenuItem("Reportes PDF (Filtros)");
        itemReportes.addActionListener(e -> cambiarPantalla("Reportes"));

        JMenuItem itemBackend = new JMenuItem("Servicios backend (prototipo)");
        itemBackend.addActionListener(e -> cambiarPantalla("Backend"));

        menuNavegacion.add(itemMonitoreo);
        menuNavegacion.add(itemIncidentes);
        JMenuItem gestion = new JMenuItem("Gestión de incidentes (MySQL)");
        gestion.addActionListener(e -> cambiarPantalla("Gestion"));
        menuNavegacion.add(gestion);
        JMenu registros = new JMenu("Registros");
        JMenuItem conductor = new JMenuItem("Conductor"), bus = new JMenuItem("Bus"), operador = new JMenuItem("Operador"), alerta = new JMenuItem("Alerta");
        conductor.addActionListener(e -> new RegistrarConductorView().setVisible(true));
        bus.addActionListener(e -> new RegistrarBusView().setVisible(true));
        operador.addActionListener(e -> new RegistrarOperadorView().setVisible(true));
        alerta.addActionListener(e -> new RegistrarAlertaView().setVisible(true));
        registros.add(conductor); registros.add(bus); registros.add(operador); registros.add(alerta);
        menuBar.add(registros);
        menuNavegacion.add(itemReportes);
        JMenuItem itemConsulta = new JMenuItem("Consulta 01 (Semana 15)");
        itemConsulta.addActionListener(e -> new Consulta01().setVisible(true));
        menuNavegacion.add(itemConsulta);
        menuNavegacion.add(itemBackend);
        menuBar.add(menuNavegacion);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Añadir las vistas como tarjetas
        mainPanel.add(new MonitoreoFlotaView(), "Monitoreo");
        mainPanel.add(new IncidentesView(), "Incidentes");
        mainPanel.add(new GestionIncidentesView(), "Gestion");
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
