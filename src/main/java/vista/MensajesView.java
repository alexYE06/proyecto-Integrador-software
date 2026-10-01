package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MensajesView extends JFrame {

    public MensajesView() {
        setTitle("SAT Alerta - Mensajes");
        setSize(360, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 360, 720, 40, 40));

        getContentPane().setBackground(new Color(248, 250, 252));

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

        JLabel lblTime = new JLabel("14:54:10");
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
        pnlHeader.setMaximumSize(new Dimension(320, 45));

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1));
        pnlTitulos.setOpaque(false);
        JLabel lblTitulo = new JLabel("Mensajes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("BUS-024 • Callao – Centro Histórico");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184));
        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);

        // Avatar (cuadro redondeado con la J)
        JButton btnAvatar = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 165, 233));
                g2.fillRoundRect(0, 0, 36, 36, 15, 15);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (36 - fm.stringWidth("J")) / 2;
                int y = ((36 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString("J", x, y);
                g2.dispose();
            }
        };
        btnAvatar.setContentAreaFilled(false);
        btnAvatar.setBorderPainted(false);
        btnAvatar.setFocusPainted(false);
        btnAvatar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAvatar.setPreferredSize(new Dimension(36, 36));
        btnAvatar.setToolTipText("Cerrar sesión");
        btnAvatar.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Deseas cerrar la sesión y volver al login?",
                    "Cerrar sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                dispose();
                new LoginChoferView().setVisible(true);
            }
        });

        pnlHeader.add(pnlTitulos, BorderLayout.CENTER);
        pnlHeader.add(btnAvatar, BorderLayout.EAST);
        pnlPrincipal.add(pnlHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- MESSAGE CARD ---
        JPanel pnlMensaje = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Sombra suave (simulada con un borde delgado gris claro)
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        pnlMensaje.setLayout(new BoxLayout(pnlMensaje, BoxLayout.Y_AXIS));
        pnlMensaje.setOpaque(false);
        pnlMensaje.setMaximumSize(new Dimension(320, 100));
        pnlMensaje.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblMensajeTit = new JLabel("CENTRAL DE DESPACHO • 14:13");
        lblMensajeTit.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblMensajeTit.setForeground(new Color(148, 163, 184));
        lblMensajeTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea txtMensaje = new JTextArea("Turno confirmado. Unidad BUS-024 operativa en Ruta 12. Buen recorrido.");
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setLineWrap(true);
        txtMensaje.setOpaque(false);
        txtMensaje.setEditable(false);
        txtMensaje.setFocusable(false);
        txtMensaje.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMensaje.setForeground(new Color(30, 41, 59));
        txtMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlMensaje.add(lblMensajeTit);
        pnlMensaje.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlMensaje.add(txtMensaje);

        pnlPrincipal.add(pnlMensaje);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // Info foot
        JLabel lblInfoUnidireccional = new JLabel("Canal unidireccional desde la consola (CU-15)");
        lblInfoUnidireccional.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblInfoUnidireccional.setForeground(new Color(148, 163, 184));
        lblInfoUnidireccional.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlPrincipal.add(lblInfoUnidireccional);

        pnlPrincipal.add(Box.createVerticalGlue());

        // --- BOTTOM NAVIGATION ---
        JPanel pnlBottomNav = new JPanel(new GridLayout(1, 3));
        pnlBottomNav.setBackground(Color.WHITE);
        pnlBottomNav.setPreferredSize(new Dimension(360, 60));
        pnlBottomNav.setMaximumSize(new Dimension(360, 60));

        JPanel btnInicio = crearBotonNav("⌂", "Inicio", false);
        btnInicio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInicio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new DashboardChoferView().setVisible(true);
            }
        });

        JPanel btnMiRuta = crearBotonNav("⇄", "Mi ruta", false);
        btnMiRuta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMiRuta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new MiRutaView().setVisible(true);
            }
        });

        JPanel btnMensajes = crearBotonNav("✉", "Mensajes", true); // Activo

        pnlBottomNav.add(btnInicio);
        pnlBottomNav.add(btnMiRuta);
        pnlBottomNav.add(btnMensajes);

        add(pnlPrincipal, BorderLayout.CENTER);
        add(pnlBottomNav, BorderLayout.SOUTH);
    }

    private JPanel crearBotonNav(String icon, String texto, boolean activo) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 0, 5, 0));
        
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
