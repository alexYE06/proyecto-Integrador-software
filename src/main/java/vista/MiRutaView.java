package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MiRutaView extends JFrame {

    public MiRutaView() {
        setTitle("SAT Alerta - Mi Ruta");
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

        JLabel lblTime = new JLabel("12:34:59");
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
        JLabel lblTitulo = new JLabel("Ruta asignada");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("BUS-024 • Callao – Centro Histórico");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184)); // gris
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
        pnlLista.setMaximumSize(new Dimension(320, 350));
        pnlLista.setBorder(new EmptyBorder(10, 5, 10, 5));

        pnlLista.add(crearItemRuta("Terminal Porteña", "Programada 06:10", true));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemRuta("Av. Argentina x Cónegos", "Programada 06:24", true));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemRuta("Plaza Miguel Grau", "Programada 06:38", false));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemRuta("Av. La Marina x Gambetta", "Programada 06:52", false));
        pnlLista.add(crearSeparador());
        pnlLista.add(crearItemRuta("Campo de Marte (terminal)", "Programada 07:05", false));

        pnlPrincipal.add(pnlLista);
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

        JPanel btnMiRuta = crearBotonNav("⇄", "Mi ruta", true); // Este es activo

        JPanel btnMensajes = crearBotonNav("✉", "Mensajes", false);
        btnMensajes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMensajes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

    private JPanel crearItemRuta(String titulo, String sub, boolean visitado) {
        JPanel pnl = new JPanel(new BorderLayout(15, 0));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnl.setMaximumSize(new Dimension(320, 60));

        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (visitado) {
                    g2.setColor(new Color(236, 253, 245)); // verde claro
                    g2.fillOval(0, 0, 32, 32);
                    g2.setColor(new Color(16, 185, 129)); // check verde
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawLine(10, 16, 14, 20);
                    g2.drawLine(14, 20, 22, 12);
                } else {
                    g2.setColor(new Color(241, 245, 249)); // gris muy claro
                    g2.fillOval(0, 0, 32, 32);
                    g2.setColor(new Color(148, 163, 184)); // circulo gris
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(11, 11, 10, 10);
                }
                
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
        lblSub.setForeground(new Color(148, 163, 184));
        
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
                g.setColor(new Color(241, 245, 249)); // linea gris clara
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
