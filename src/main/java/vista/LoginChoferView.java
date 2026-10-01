package vista;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginChoferView extends JFrame {

    private JTextField txtDni;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnCredencialesDemo;

    public LoginChoferView() {
        setTitle("SAT Alerta - Chofer");
        setSize(360, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Estilo celular
        setShape(new RoundRectangle2D.Double(0, 0, 360, 720, 40, 40));

        // Fondo oscuro para simular la app móvil
        getContentPane().setBackground(new Color(20, 40, 60)); 
        
        initComponentes();
    }

    private void initComponentes() {
        // Contenedor principal que dibujará el gradiente de fondo
        JPanel pnlFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), 0, getHeight(), new Color(22, 65, 87));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlFondo.setLayout(new BorderLayout());
        
        // Panel Superior (Barra de estado celular simulada + Logo)
        JPanel pnlTop = new JPanel();
        pnlTop.setOpaque(false);
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Barra de estado falsa
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(false);
        statusBar.setBorder(new EmptyBorder(0, 20, 20, 20));
        JLabel lblTime = new JLabel("12:30:56");
        lblTime.setForeground(new Color(100, 120, 140));
        JLabel lblIcons = new JLabel("4G III 87%");
        lblIcons.setForeground(new Color(100, 120, 140));
        statusBar.add(lblTime, BorderLayout.WEST);
        statusBar.add(lblIcons, BorderLayout.EAST);
        pnlTop.add(statusBar);

        // Logo escudo (simulado con texto)
        JLabel lblLogo = new JLabel("🛡️", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblAppTitle = new JLabel("SAT Alerta");
        lblAppTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblAppTitle.setForeground(Color.WHITE);
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAppTitle.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel lblSubTitle = new JLabel("Carmen de la Punta • Ruta Segura");
        lblSubTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubTitle.setForeground(new Color(180, 200, 220));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        pnlTop.add(lblLogo);
        pnlTop.add(lblAppTitle);
        pnlTop.add(lblSubTitle);
        
        // Panel Central (Formulario blanco)
        JPanel pnlCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlCentro.setOpaque(false);
        
        JPanel pnlCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnlCard.setOpaque(false);
        pnlCard.setPreferredSize(new Dimension(320, 380));
        pnlCard.setLayout(null);

        JLabel lblLoginTit = new JLabel("Iniciar sesión");
        lblLoginTit.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLoginTit.setForeground(new Color(30, 41, 59));
        lblLoginTit.setBounds(25, 20, 200, 25);
        pnlCard.add(lblLoginTit);

        JLabel lblLoginSub = new JLabel("Acceso del conductor • CU-01");
        lblLoginSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblLoginSub.setForeground(new Color(148, 163, 184));
        lblLoginSub.setBounds(25, 45, 200, 20);
        pnlCard.add(lblLoginSub);

        JLabel lblDni = new JLabel("DNI");
        lblDni.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblDni.setForeground(new Color(71, 85, 105));
        lblDni.setBounds(25, 80, 200, 20);
        pnlCard.add(lblDni);

        txtDni = new JTextField("");
        txtDni.setBounds(25, 100, 270, 45);
        txtDni.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(6, 182, 212), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtDni.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnlCard.add(txtDni);

        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblPass.setForeground(new Color(71, 85, 105));
        lblPass.setBounds(25, 160, 200, 20);
        pnlCard.add(lblPass);

        txtPassword = new JPasswordField("");
        txtPassword.setBounds(25, 180, 270, 45);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Permite presionar Enter estando en el campo de contraseña
        txtPassword.addActionListener(e -> {
            if (btnIngresar != null) btnIngresar.doClick();
        });
        
        pnlCard.add(txtPassword);

        btnIngresar = new JButton("Ingresar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(34, 211, 238));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnIngresar.setBounds(25, 250, 270, 45);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setForeground(new Color(15, 23, 42));
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Redirección al Dashboard
        btnIngresar.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new DashboardChoferView().setVisible(true));
        });

        pnlCard.add(btnIngresar);

        btnCredencialesDemo = new JButton("Usar credenciales demo");
        btnCredencialesDemo.setBounds(25, 310, 270, 30);
        btnCredencialesDemo.setContentAreaFilled(false);
        btnCredencialesDemo.setBorderPainted(false);
        btnCredencialesDemo.setFocusPainted(false);
        btnCredencialesDemo.setForeground(new Color(14, 116, 144));
        btnCredencialesDemo.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCredencialesDemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCredencialesDemo.addActionListener(e -> {
            txtDni.setText("70894561");
            txtPassword.setText("demo123");
        });

        pnlCard.add(btnCredencialesDemo);
        
        pnlCentro.add(pnlCard);

        // Footer
        JPanel pnlFooter = new JPanel();
        pnlFooter.setOpaque(false);
        pnlFooter.setLayout(new BoxLayout(pnlFooter, BoxLayout.Y_AXIS));
        pnlFooter.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel lblFoot1 = new JLabel("Sesión segura • RF-03 • sin conexión con la central");
        lblFoot1.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFoot1.setForeground(new Color(100, 120, 140));
        lblFoot1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblFoot2 = new JLabel("Demo: 70894561 / demo123");
        lblFoot2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFoot2.setForeground(new Color(100, 120, 140));
        lblFoot2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Boton para volver al selector
        JButton btnVolver = new JButton("Volver al Selector");
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setForeground(Color.GRAY);
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 10));
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            this.dispose();
            new DemoSelectorView().setVisible(true);
        });

        pnlFooter.add(lblFoot1);
        pnlFooter.add(lblFoot2);
        pnlFooter.add(btnVolver);

        pnlFondo.add(pnlTop, BorderLayout.NORTH);
        pnlFondo.add(pnlCentro, BorderLayout.CENTER);
        pnlFondo.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlFondo);
    }
}
