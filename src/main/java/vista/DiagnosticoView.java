package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DiagnosticoView extends JFrame {

    public DiagnosticoView() {
        setTitle("SAT Alerta - Diagnóstico");
        setSize(360, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 360, 720, 40, 40));

        getContentPane().setBackground(new Color(248, 250, 252)); // Fondo claro slate-50

        initComponentes();
    }

    private void initComponentes() {
        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(new Color(248, 250, 252));
        pnlPrincipal.setBorder(new EmptyBorder(15, 20, 10, 20));

        // --- STATUS BAR ---
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(false);
        statusBar.setMaximumSize(new Dimension(320, 20));

        JLabel lblTime = new JLabel("12:34:45");
        lblTime.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel lblIcons = new JLabel("4G III 87%");
        lblIcons.setFont(new Font("SansSerif", Font.BOLD, 12));

        statusBar.add(lblTime, BorderLayout.WEST);
        statusBar.add(lblIcons, BorderLayout.EAST);
        pnlPrincipal.add(statusBar);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setMaximumSize(new Dimension(320, 40));

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1));
        pnlTitulos.setOpaque(false);
        JLabel lblTitulo = new JLabel("Diagnóstico");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("Última comprobación: ahora");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184)); // gris
        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVolver.setForeground(new Color(14, 145, 165)); // teal
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            dispose();
            new DashboardChoferView().setVisible(true);
        });

        pnlHeader.add(pnlTitulos, BorderLayout.CENTER);
        pnlHeader.add(btnVolver, BorderLayout.EAST);
        pnlPrincipal.add(pnlHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- MAIN LIST CARD ---
        JPanel pnlLista = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setOpaque(false);
        pnlLista.setMaximumSize(new Dimension(320, 280));
        pnlLista.setBorder(new EmptyBorder(5, 5, 5, 5));

        pnlLista.add(crearItemDiagnostico("Red móvil", "4G estable • 4/5 barras"));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemDiagnostico("GPS", "-12.0400, -77.1448"));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemDiagnostico("Servicio en segundo plano", "Activo • sin interrumpir la jornada"));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemDiagnostico("Indicadores de transmisión", "Ocultos por diseño (RNF-05)"));

        pnlPrincipal.add(pnlLista);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- BUTTON REINTENTAR ---
        JButton btnReintentar = new JButton("Reintentar diagnóstico") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnReintentar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReintentar.setMaximumSize(new Dimension(320, 45));
        btnReintentar.setPreferredSize(new Dimension(320, 45));
        btnReintentar.setContentAreaFilled(false);
        btnReintentar.setBorderPainted(false);
        btnReintentar.setFocusPainted(false);
        btnReintentar.setForeground(new Color(15, 23, 42));
        btnReintentar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnReintentar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReintentar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Realizando diagnóstico...", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        pnlPrincipal.add(btnReintentar);
        pnlPrincipal.add(Box.createVerticalGlue());

        // --- BOTTOM NAVIGATION ---
        JPanel pnlBottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlBottomNav.setBackground(Color.WHITE);
        pnlBottomNav.setPreferredSize(new Dimension(360, 60));
        pnlBottomNav.setMaximumSize(new Dimension(360, 60));

        JPanel btnInicio = crearBotonNav("⌂", "Inicio", true);
        btnInicio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInicio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new DashboardChoferView().setVisible(true);
            }
        });
        
        pnlBottomNav.add(btnInicio);

        add(pnlPrincipal, BorderLayout.CENTER);
        add(pnlBottomNav, BorderLayout.SOUTH);
    }

    private JPanel crearItemDiagnostico(String titulo, String sub) {
        JPanel pnl = new JPanel(new BorderLayout(15, 0));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnl.setMaximumSize(new Dimension(320, 60));

        // Icono check con fondo circular verde claro
        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(236, 253, 245)); // fondo verde claro
                g2.fillOval(0, 0, 32, 32);
                g2.setColor(new Color(16, 185, 129)); // check verde esmeralda
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(10, 16, 14, 20);
                g2.drawLine(14, 20, 22, 12);
                g2.dispose();
            }
        };
        pnlIcon.setPreferredSize(new Dimension(32, 32));
        pnlIcon.setOpaque(false);

        JPanel pnlTextos = new JPanel(new GridLayout(2, 1));
        pnlTextos.setOpaque(false);
        
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTit.setForeground(new Color(15, 23, 42));
        
        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184)); // gris
        
        pnlTextos.add(lblTit);
        pnlTextos.add(lblSub);

        pnl.add(pnlIcon, BorderLayout.WEST);
        pnl.add(pnlTextos, BorderLayout.CENTER);

        return pnl;
    }
    
    private JPanel crearSeparador() {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(241, 245, 249)); // linea gris muy clara
                g.drawLine(15, getHeight()/2, getWidth()-15, getHeight()/2);
            }
        };
        pnl.setOpaque(false);
        pnl.setMaximumSize(new Dimension(320, 1));
        pnl.setPreferredSize(new Dimension(320, 1));
        return pnl;
    }

    private JPanel crearBotonNav(String icon, String texto, boolean activo) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 50, 5, 50));
        
        Color color = activo ? new Color(14, 145, 165) : new Color(148, 163, 184);
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblIcon.setForeground(color);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblTexto.setForeground(color);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnl.add(lblIcon);
        pnl.add(lblTexto);
        
        return pnl;
    }
}
