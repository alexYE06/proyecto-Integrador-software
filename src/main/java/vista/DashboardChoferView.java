package vista;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardChoferView extends JFrame {

    public DashboardChoferView() {
        setTitle("SAT Alerta - Mi Jornada (Chofer)");
        setSize(360, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 360, 720, 40, 40));

        getContentPane().setBackground(new Color(248, 250, 252)); // Fondo claro (slate-50)

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

        JLabel lblTime = new JLabel("12:34:10");
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
        pnlHeader.setMaximumSize(new Dimension(320, 50));

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1));
        pnlTitulos.setOpaque(false);
        JLabel lblTitulo = new JLabel("Mi jornada");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("BUS-024 • Callao – Centro Histórico");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(100, 116, 139));
        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);

        // Avatar circular con la letra J (Botón interactivo)
        JButton btnAvatar = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 165, 233));
                g2.fillOval(0, 0, 36, 36);
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

        // --- MAIN CARD (UNIDAD EN SERVICIO) ---
        JPanel pnlUnidad = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(21, 128, 141)); // Teal oscuro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        pnlUnidad.setLayout(new BoxLayout(pnlUnidad, BoxLayout.Y_AXIS));
        pnlUnidad.setOpaque(false);
        pnlUnidad.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlUnidad.setMaximumSize(new Dimension(320, 100));

        JLabel lblUnidadTit = new JLabel("UNIDAD EN SERVICIO");
        lblUnidadTit.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblUnidadTit.setForeground(new Color(165, 243, 252));

        JLabel lblUnidadVal = new JLabel("Ruta R-01 • ZY4-906");
        lblUnidadVal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblUnidadVal.setForeground(Color.WHITE);

        JPanel pnlUnidadFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlUnidadFooter.setOpaque(false);
        JLabel f1 = new JLabel("🕒 Turno B");
        f1.setForeground(Color.WHITE);
        f1.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JLabel f2 = new JLabel("🧭 N");
        f2.setForeground(Color.WHITE);
        f2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JLabel f3 = new JLabel("⚡ 70%");
        f3.setForeground(Color.WHITE);
        f3.setFont(new Font("SansSerif", Font.PLAIN, 11));
        pnlUnidadFooter.add(f1);
        pnlUnidadFooter.add(f2);
        pnlUnidadFooter.add(f3);

        pnlUnidad.add(lblUnidadTit);
        pnlUnidad.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlUnidad.add(lblUnidadVal);
        pnlUnidad.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlUnidad.add(pnlUnidadFooter);

        pnlPrincipal.add(pnlUnidad);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- STATS CARDS ---
        JPanel pnlStats = new JPanel(new GridLayout(1, 3, 10, 0));
        pnlStats.setOpaque(false);
        pnlStats.setMaximumSize(new Dimension(320, 70));
        pnlStats.add(crearCardStat("12/28", "PARADAS"));
        pnlStats.add(crearCardStat("23", "KM/H"));
        pnlStats.add(crearCardStat("4.6 h", "RESTANTES"));
        pnlPrincipal.add(pnlStats);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- LIST VIEW (DIAGNOSTICO, PROTECCION, PROX PARADA) ---
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
        pnlLista.setMaximumSize(new Dimension(320, 200));

        JPanel filaDiag = crearFilaLista("◎", "Diagnóstico del sistema", "Red, GPS y servicio en 2.° plano", ">");
        filaDiag.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filaDiag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new DiagnosticoView().setVisible(true);
            }
        });
        pnlLista.add(filaDiag);
        pnlLista.add(crearFilaLista("🛡️", "Protección activa", "Monitoreo discreto en segundo plano", "ON"));
        pnlLista.add(crearFilaLista("≡", "Próxima parada", "Plaza Miguel Grau", "06:38"));

        pnlPrincipal.add(pnlLista);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- INFO RECORDATORIO ---
        JPanel pnlInfo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(241, 245, 249)); // slate-100
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlInfo.setMaximumSize(new Dimension(320, 100));

        JLabel lblInfoTit = new JLabel("RECORDATORIO DE SEGURIDAD");
        lblInfoTit.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblInfoTit.setForeground(new Color(100, 116, 139));

        JTextArea txtInfo = new JTextArea(
                "Ante una amenaza, el botón de encendido del teléfono activa la alerta de emergencia de forma silenciosa. No desbloquee la pantalla.");
        txtInfo.setWrapStyleWord(true);
        txtInfo.setLineWrap(true);
        txtInfo.setOpaque(false);
        txtInfo.setEditable(false);
        txtInfo.setFocusable(false);
        txtInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtInfo.setForeground(new Color(100, 116, 139));

        pnlInfo.add(lblInfoTit);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlInfo.add(txtInfo);

        pnlPrincipal.add(pnlInfo);
        pnlPrincipal.add(Box.createVerticalGlue());

        // --- BOTTOM NAVIGATION ---
        JPanel pnlBottomNav = new JPanel(new GridLayout(1, 3));
        pnlBottomNav.setBackground(Color.WHITE);
        pnlBottomNav.setPreferredSize(new Dimension(360, 60));
        pnlBottomNav.setMaximumSize(new Dimension(360, 60));

        JPanel btnInicio = crearBotonNav("⌂", "Inicio", true);
        
        JPanel btnMiRuta = crearBotonNav("⇄", "Mi ruta", false);
        btnMiRuta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMiRuta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new MiRutaView().setVisible(true);
            }
        });

        JPanel btnMensajes = crearBotonNav("✉", "Mensajes", false);
        btnMensajes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMensajes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new MensajesView().setVisible(true);
            }
        });

        pnlBottomNav.add(btnInicio);
        pnlBottomNav.add(btnMiRuta);
        pnlBottomNav.add(btnMensajes);

        add(pnlPrincipal, BorderLayout.CENTER);
        add(pnlBottomNav, BorderLayout.SOUTH);
    }

    private void configurarBotonBarra(JButton boton) {
        boton.setPreferredSize(new Dimension(28, 24));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel crearCardStat(String valor, String label) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblVal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLab = new JLabel(label);
        lblLab.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblLab.setForeground(new Color(148, 163, 184));
        lblLab.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnl.add(lblVal);
        pnl.add(Box.createRigidArea(new Dimension(0, 5)));
        pnl.add(lblLab);

        return pnl;
    }

    private JPanel crearFilaLista(String icon, String titulo, String sub, String valorDer) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblIcon.setForeground(new Color(14, 165, 233));
        lblIcon.setPreferredSize(new Dimension(30, 30));

        JPanel pnlCentro = new JPanel(new GridLayout(2, 1));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTit.setForeground(new Color(15, 23, 42));

        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(148, 163, 184));

        pnlCentro.add(lblTit);
        pnlCentro.add(lblSub);

        JLabel lblDer = new JLabel(valorDer);
        lblDer.setFont(new Font("SansSerif", Font.BOLD, 13));
        if (valorDer.equals("ON")) {
            lblDer.setForeground(new Color(34, 197, 94)); // Verde
        } else if (valorDer.equals(">")) {
            lblDer.setForeground(new Color(148, 163, 184)); // Gris
        } else {
            lblDer.setForeground(new Color(15, 23, 42)); // Oscuro para hora
        }

        pnl.add(lblIcon, BorderLayout.WEST);
        pnl.add(pnlCentro, BorderLayout.CENTER);
        pnl.add(lblDer, BorderLayout.EAST);

        return pnl;
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
