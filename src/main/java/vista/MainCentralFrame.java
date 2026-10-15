package vista;

import java.awt.CardLayout;
import javax.swing.JFrame;
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

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Añadir las vistas como tarjetas
        mainPanel.add(new MonitoreoFlotaView(), "Monitoreo");
        mainPanel.add(new IncidentesView(), "Incidentes");
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
