package vista;

import controlador.LoginController;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginCentralView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private LoginController controller;

    public LoginCentralView() {
        setTitle("SAT-Carmen - Consola de la Base");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(244, 247, 250)); // Fondo muy claro

        initComponentes();
        this.controller = new LoginController(this);
    }

    private void initComponentes() {
        setLayout(null);

        // --- TOP LEFT LOGO ---
        JPanel pnlLogoTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLogoTop.setBounds(40, 30, 400, 50);
        pnlLogoTop.setOpaque(false);

        JPanel iconLogo = crearIconoEscudo(40, 40);
        
        JPanel pnlTextLogo = new JPanel(new GridLayout(2, 1));
        pnlTextLogo.setOpaque(false);
        JLabel lblSat = new JLabel("SAT-CARMEN");
        lblSat.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSat.setForeground(new Color(15, 23, 42));
        JLabel lblSatSub = new JLabel("CENTRAL DE DESPACHO • CALLAO • LIMA METROPOLITANA");
        lblSatSub.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblSatSub.setForeground(new Color(148, 163, 184));
        
        pnlTextLogo.add(lblSat);
        pnlTextLogo.add(lblSatSub);

        pnlLogoTop.add(iconLogo);
        pnlLogoTop.add(pnlTextLogo);
        add(pnlLogoTop);

        // --- LEFT CONTENT (INFO & STATS) ---
        JPanel pnlIzquierdo = new JPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        pnlIzquierdo.setBounds(80, 200, 500, 300);
        pnlIzquierdo.setOpaque(false);

        // Pill badge
        JPanel pnlPill = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(204, 241, 248)); // cyan muy claro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        pnlPill.setOpaque(false);
        pnlPill.setMaximumSize(new Dimension(270, 25));
        pnlPill.setLayout(new GridBagLayout());
        JLabel lblPill = new JLabel("PROTOTIPO • CONEXIÓN EN VIVO CON EL MÓVIL");
        lblPill.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblPill.setForeground(new Color(14, 116, 144)); // cyan oscuro
        pnlPill.add(lblPill);
        pnlPill.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlIzquierdo.add(pnlPill);
        pnlIzquierdo.add(Box.createRigidArea(new Dimension(0, 15)));

        // Big Title
        JLabel lblBigTitle = new JLabel("Consola de la base");
        lblBigTitle.setFont(new Font("SansSerif", Font.BOLD, 46));
        lblBigTitle.setForeground(new Color(15, 23, 42));
        lblBigTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlIzquierdo.add(lblBigTitle);
        pnlIzquierdo.add(Box.createRigidArea(new Dimension(0, 25)));

        // Flowchart
        JPanel pnlFlow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFlow.setOpaque(false);
        pnlFlow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlFlow.add(crearCajaFlow("App móvil", false));
        pnlFlow.add(crearFlecha());
        pnlFlow.add(crearCajaFlow("Alerta silenciosa", true));
        pnlFlow.add(crearFlecha());
        pnlFlow.add(crearCajaFlow("Consola central", true));
        pnlFlow.add(crearFlecha());
        pnlFlow.add(crearCajaFlow("PNP / Gerencia", false));
        pnlIzquierdo.add(pnlFlow);
        pnlIzquierdo.add(Box.createRigidArea(new Dimension(0, 30)));

        // Stats
        JPanel pnlStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlStats.setOpaque(false);
        pnlStats.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlStats.add(crearCardStat("80", "BUSES"));
        pnlStats.add(crearCardStat("120", "CONDUCTORES"));
        pnlStats.add(crearCardStat("5", "OPERADORES"));
        pnlIzquierdo.add(pnlStats);

        add(pnlIzquierdo);

        // --- RIGHT CONTENT (LOGIN CARD) ---
        JPanel pnlLogin = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra suave (simulada)
                g2.setColor(new Color(226, 232, 240));
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 30, 30);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 30, 30);
                g2.dispose();
            }
        };
        pnlLogin.setLayout(null);
        pnlLogin.setOpaque(false);
        pnlLogin.setBounds(620, 100, 320, 420);

        // Header Login Card
        JPanel iconLogin = crearIconoEscudo(36, 36);
        iconLogin.setBounds(25, 25, 36, 36);
        pnlLogin.add(iconLogin);

        JLabel lblLogTit = new JLabel("Iniciar sesión");
        lblLogTit.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblLogTit.setForeground(new Color(15, 23, 42));
        lblLogTit.setBounds(75, 25, 200, 20);
        pnlLogin.add(lblLogTit);

        JLabel lblLogSub = new JLabel("Acceso de la central • CU-01");
        lblLogSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblLogSub.setForeground(new Color(148, 163, 184));
        lblLogSub.setBounds(75, 45, 200, 15);
        pnlLogin.add(lblLogSub);

        // Roles tabs
        JPanel pnlRoles = new JPanel(new GridLayout(1, 3, 5, 0));
        pnlRoles.setOpaque(false);
        pnlRoles.setBounds(25, 80, 270, 50);
        
        pnlRoles.add(crearRoleTab("Operador", "🛰️", true));
        pnlRoles.add(crearRoleTab("Admin", "🛡️", false));
        pnlRoles.add(crearRoleTab("Móvil*", "🚍", false));
        pnlLogin.add(pnlRoles);

        // Form
        JLabel lblUser = new JLabel("USUARIO");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblUser.setForeground(new Color(100, 116, 139));
        lblUser.setBounds(25, 145, 270, 15);
        pnlLogin.add(lblUser);

        txtUsuario = new JTextField("lramos");
        txtUsuario.setBounds(25, 165, 270, 40);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        pnlLogin.add(txtUsuario);

        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblPass.setForeground(new Color(100, 116, 139));
        lblPass.setBounds(25, 215, 270, 15);
        pnlLogin.add(lblPass);

        txtPassword = new JPasswordField("demo123");
        txtPassword.setBounds(25, 235, 270, 40);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        pnlLogin.add(txtPassword);

        btnIngresar = new JButton("Ingresar a la consola");
        btnIngresar.setBounds(25, 295, 270, 40);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setOpaque(true);
        btnIngresar.setBackground(new Color(34, 211, 238)); // cyan
        btnIngresar.setForeground(new Color(15, 23, 42));
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlLogin.add(btnIngresar);

        // Footers
        JLabel lblDemo = new JLabel("<html><center>Demo: <b>lramos</b> (operador) o <b>admin</b> • clave <b>demo123</b><br><span style='color:#94a3b8;'>* El conductor no usa este formulario; entra por el móvil.</span></center></html>");
        lblDemo.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblDemo.setForeground(new Color(100, 116, 139));
        lblDemo.setBounds(25, 345, 270, 40);
        pnlLogin.add(lblDemo);
        
        JButton btnVolver = new JButton("Volver al inicio");
        btnVolver.setBounds(25, 385, 270, 20);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setForeground(new Color(148, 163, 184));
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 10));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            dispose();
            new DemoSelectorView().setVisible(true);
        });
        pnlLogin.add(btnVolver);

        add(pnlLogin);
    }

    private JPanel crearIconoEscudo(int w, int h) {
        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 23, 42)); // oscuro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(225, 29, 72)); // rojo escudo
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth("🛡")) / 2;
                int ty = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString("🛡", tx, ty);
                g2.dispose();
            }
        };
        icon.setOpaque(false);
        icon.setPreferredSize(new Dimension(w, h));
        return icon;
    }

    private JPanel crearRoleTab(String text, String icon, boolean active) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    g2.setColor(new Color(204, 241, 248)); // cyan muy claro
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(new Color(34, 211, 238)); // border cyan
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(new Color(226, 232, 240));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                }
                g2.dispose();
            }
        };
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lblIco = new JLabel(icon);
        lblIco.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblTxt = new JLabel(text);
        lblTxt.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (active) {
            lblTxt.setForeground(new Color(14, 116, 144)); // cyan oscuro
        } else {
            lblTxt.setForeground(new Color(148, 163, 184)); // gris
            if (text.equals("Móvil*")) {
                lblTxt.setForeground(new Color(203, 213, 225)); // gris mas claro
            }
        }
        
        pnl.add(lblIco);
        pnl.add(lblTxt);
        return pnl;
    }

    private JPanel crearCajaFlow(String text, boolean highlighted) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (highlighted) {
                    g2.setColor(new Color(204, 241, 248));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(new Color(34, 211, 238));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(new Color(226, 232, 240));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                }
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        pnl.setLayout(new GridBagLayout());
        pnl.setPreferredSize(new Dimension(100, 30));
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 9));
        if (highlighted) lbl.setForeground(new Color(14, 116, 144));
        else lbl.setForeground(new Color(100, 116, 139));
        
        pnl.add(lbl);
        return pnl;
    }

    private JLabel crearFlecha() {
        JLabel lbl = new JLabel("→");
        lbl.setForeground(new Color(203, 213, 225));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        return lbl;
    }

    private JPanel crearCardStat(String val, String title) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        pnl.setPreferredSize(new Dimension(100, 80));
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblVal.setForeground(new Color(15, 23, 42));
        
        JLabel lblTit = new JLabel(title);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblTit.setForeground(new Color(148, 163, 184));
        
        pnl.add(lblVal);
        pnl.add(lblTit);
        return pnl;
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginCentralView().setVisible(true);
        });
    }
}
