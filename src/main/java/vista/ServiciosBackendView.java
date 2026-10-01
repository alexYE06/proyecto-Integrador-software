package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ServiciosBackendView extends JPanel {

    public ServiciosBackendView() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        initComponentes();
    }

    private void initComponentes() {
        // --- SIDEBAR (IZQUIERDA) ---
        JPanel pnlSidebar = new JPanel(new BorderLayout());
        pnlSidebar.setBackground(Color.WHITE);
        pnlSidebar.setPreferredSize(new Dimension(240, 0));
        pnlSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(226, 232, 240)));

        // Top Logo
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        pnlLogo.setBackground(Color.WHITE);
        pnlLogo.add(crearIconoEscudo(32, 32));
        JPanel pnlLogoText = new JPanel(new GridLayout(2, 1));
        pnlLogoText.setBackground(Color.WHITE);
        JLabel lblSat = new JLabel("SAT-CARMEN");
        lblSat.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSat.setForeground(new Color(15, 23, 42));
        JLabel lblSatSub = new JLabel("ALERTA EN TIEMPO REAL");
        lblSatSub.setFont(new Font("SansSerif", Font.BOLD, 8));
        lblSatSub.setForeground(new Color(148, 163, 184));
        pnlLogoText.add(lblSat);
        pnlLogoText.add(lblSatSub);
        pnlLogo.add(pnlLogoText);
        pnlSidebar.add(pnlLogo, BorderLayout.NORTH);

        // Menú lateral
        JPanel pnlMenu = new JPanel();
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
        pnlMenu.setBackground(Color.WHITE);
        pnlMenu.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblSeccion1 = new JLabel("CONSOLA WEB • OPERACIÓN");
        lblSeccion1.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblSeccion1.setForeground(new Color(148, 163, 184));
        pnlMenu.add(lblSeccion1);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel itemMonitoreo = crearItemMenu("■", "Monitoreo de flota", false, false);
        itemMonitoreo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainCentralFrame.getInstance().cambiarPantalla("Monitoreo");
            }
        });
        pnlMenu.add(itemMonitoreo);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPanel itemIncidente = crearItemMenu("⚠", "Incidente activo", false, true);
        itemIncidente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainCentralFrame.getInstance().cambiarPantalla("Incidentes");
            }
        });
        pnlMenu.add(itemIncidente);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Activo: Backend
        pnlMenu.add(crearItemMenu("⚙", "Servicios backend", true, false));
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblSeccion2 = new JLabel("CASOS DE USO");
        lblSeccion2.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblSeccion2.setForeground(new Color(148, 163, 184));
        pnlMenu.add(lblSeccion2);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pnlCUs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlCUs.setBackground(Color.WHITE);
        pnlCUs.setMaximumSize(new Dimension(210, 150));
        String[] cus = {"CU-01", "CU-09", "CU-10", "CU-11", "CU-12", "CU-13", "CU-14", "CU-15"};
        for (String cu : cus) {
            pnlCUs.add(crearPill(cu, false, false));
        }
        pnlMenu.add(pnlCUs);
        pnlSidebar.add(pnlMenu, BorderLayout.CENTER);

        // Perfil bottom
        JPanel pnlPerfil = new JPanel(new BorderLayout());
        pnlPerfil.setBackground(Color.WHITE);
        pnlPerfil.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel pnlUser = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlUser.setBackground(Color.WHITE);
        pnlUser.add(crearAvatar("L"));
        JPanel pnlUserText = new JPanel(new GridLayout(2, 1));
        pnlUserText.setBackground(Color.WHITE);
        JLabel lblUser = new JLabel("Luis Ramos Quispe");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel lblUserSub = new JLabel("Operador de despacho");
        lblUserSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblUserSub.setForeground(new Color(148, 163, 184));
        pnlUserText.add(lblUser);
        pnlUserText.add(lblUserSub);
        pnlUser.add(pnlUserText);
        
        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setForeground(new Color(100, 116, 139));
        btnCerrarSesion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> {
            System.exit(0);
        });

        pnlPerfil.add(pnlUser, BorderLayout.CENTER);
        pnlPerfil.add(btnCerrarSesion, BorderLayout.SOUTH);
        pnlSidebar.add(pnlPerfil, BorderLayout.SOUTH);

        add(pnlSidebar, BorderLayout.WEST);

        // --- CONTENIDO PRINCIPAL ---
        JPanel pnlCentral = new JPanel();
        pnlCentral.setLayout(new BorderLayout());
        pnlCentral.setBackground(new Color(248, 250, 252));
        pnlCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Cabecera Principal
        JPanel pnlHeaderCentral = new JPanel(new BorderLayout());
        pnlHeaderCentral.setOpaque(false);
        
        JPanel pnlHeaderTit = new JPanel(new GridLayout(2, 1));
        pnlHeaderTit.setOpaque(false);
        JLabel lblTitulo = new JLabel("Procesos backend e integraciones");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("Servicios automáticos del sistema SAT-Extorsión-Carmen");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(148, 163, 184));
        pnlHeaderTit.add(lblTitulo);
        pnlHeaderTit.add(lblSub);
        
        JPanel pnlHeaderBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlHeaderBtns.setOpaque(false);
        pnlHeaderBtns.add(crearBotonAccion("🔍 Buscar unidad, conductor o placa..."));
        
        // BOTÓN NUEVO PARA REGISTRAR CONDUCTORES EN MYSQL
        JButton btnRegChofer = crearBotonPeligro("+ Registrar Chofer");
        btnRegChofer.setBackground(new Color(16, 185, 129)); // Verde
        btnRegChofer.addActionListener(e -> new RegistrarConductorView().setVisible(true));
        pnlHeaderBtns.add(btnRegChofer);
        
        JButton btnRegBus = crearBotonPeligro("+ Registrar Bus");
        btnRegBus.setBackground(new Color(37, 99, 235)); // Azul
        btnRegBus.addActionListener(e -> new RegistrarBusView().setVisible(true));
        pnlHeaderBtns.add(btnRegBus);
        
        pnlHeaderBtns.add(crearBotonAccion("🔔  19:00:55"));
        
        JButton btnSimular = crearBotonPeligro("⚠ Simular alerta");
        pnlHeaderBtns.add(btnSimular);
        
        pnlHeaderBtns.add(crearBotonAccion("App móvil"));
        JButton btnSalir = crearBotonAccion("Salir central");
        btnSalir.addActionListener(e -> System.exit(0));
        pnlHeaderBtns.add(btnSalir);
        
        pnlHeaderCentral.add(pnlHeaderTit, BorderLayout.WEST);
        pnlHeaderCentral.add(pnlHeaderBtns, BorderLayout.EAST);
        pnlCentral.add(pnlHeaderCentral, BorderLayout.NORTH);

        // --- CUERPO ---
        JPanel pnlCuerpo = new JPanel(new BorderLayout(15, 15));
        pnlCuerpo.setOpaque(false);
        pnlCuerpo.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Top Cards
        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 100));
        
        pnlCards.add(crearServicioCard("WebSockets / telemetría", "Conectado", "80 canales · <100 ms"));
        pnlCards.add(crearServicioCard("Gateway SMS → PNP", "Operativo", "Acuse automático de entrega"));
        pnlCards.add(crearServicioCard("SMTP → Gerencia", "Operativo", "Columna de salida cifrada TLS"));
        pnlCards.add(crearServicioCard("Almacenamiento de evidencias", "Cifrado", "AES-256 · marca de agua"));
        
        pnlCuerpo.add(pnlCards, BorderLayout.NORTH);

        // Bitácora Tabla
        JPanel pnlBitacora = crearCardBlanca();
        pnlBitacora.setLayout(new BorderLayout());
        pnlBitacora.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel pnlBitH = new JPanel(new BorderLayout());
        pnlBitH.setOpaque(false);
        JPanel pnlBH = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlBH.setOpaque(false);
        JLabel lblBitTit = new JLabel("Bitácora de procesos automáticos");
        lblBitTit.setFont(new Font("SansSerif", Font.BOLD, 14));
        pnlBH.add(lblBitTit);
        pnlBH.add(crearPill("CU-16 - CU-19", false, true));
        pnlBitH.add(pnlBH, BorderLayout.WEST);
        pnlBitH.add(crearPill("6 eventos", false, false), BorderLayout.EAST);
        
        pnlBitacora.add(pnlBitH, BorderLayout.NORTH);
        
        JPanel pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setOpaque(false);
        pnlLista.add(Box.createRigidArea(new Dimension(0, 15)));
        
        pnlLista.add(crearBitacoraRow("18:25:08", "Incidente", "Estado actualizado a «En Evaluación» · ALT-2026-0923-001"));
        pnlLista.add(crearBitacoraRow("18:24:54", "Incidente", "Estado actualizado a «Recibida» · ALT-2026-0923-001"));
        pnlLista.add(crearBitacoraRow("17:52:11", "Notificaciones", "SMS a PNP · ALT-2026-0923-002 · acuse #SMS-4412"));
        pnlLista.add(crearBitacoraRow("17:52:11", "Evidencias", "Respaldo cifrado AES-256 con marca de agua · 3 archivos"));
        pnlLista.add(crearBitacoraRow("17:58:11", "Cerco virtual", "Polígono Z2 actualizado · 12 unidades notificadas"));
        pnlLista.add(crearBitacoraRow("18:04:11", "Haversine", "Ruta de auxilio recalculada · Comisaría Bellavista a 1.4 km"));
        
        pnlBitacora.add(pnlLista, BorderLayout.CENTER);

        pnlCuerpo.add(pnlBitacora, BorderLayout.CENTER);
        pnlCentral.add(pnlCuerpo, BorderLayout.CENTER);
        add(pnlCentral, BorderLayout.CENTER);
    }

    private JPanel crearServicioCard(String t1, String status, String t2) {
        JPanel pnl = crearCardBlanca();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel l1 = new JLabel(t1);
        l1.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l1.setForeground(new Color(100, 116, 139));
        pnl.add(l1);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel pSt = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pSt.setOpaque(false);
        JLabel lDot = new JLabel("●");
        lDot.setFont(new Font("SansSerif", Font.BOLD, 12));
        lDot.setForeground(new Color(16, 185, 129)); // Verde
        JLabel lSt = new JLabel(status);
        lSt.setFont(new Font("SansSerif", Font.BOLD, 14));
        lSt.setForeground(new Color(16, 185, 129)); // Verde
        pSt.add(lDot);
        pSt.add(lSt);
        pnl.add(pSt);
        
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel l2 = new JLabel(t2);
        l2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        l2.setForeground(new Color(148, 163, 184));
        pnl.add(l2);
        
        return pnl;
    }

    private JPanel crearBitacoraRow(String time, String proc, String desc) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)),
            new EmptyBorder(15, 0, 15, 0)
        ));
        
        JPanel pIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        pIzq.setOpaque(false);
        JLabel lT = new JLabel(time);
        lT.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lT.setForeground(new Color(148, 163, 184));
        JLabel lP = new JLabel(proc);
        lP.setFont(new Font("SansSerif", Font.BOLD, 11));
        lP.setPreferredSize(new Dimension(100, 15));
        JLabel lD = new JLabel(desc);
        lD.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lD.setForeground(new Color(100, 116, 139));
        
        pIzq.add(lT);
        pIzq.add(lP);
        pIzq.add(lD);
        pnl.add(pIzq, BorderLayout.WEST);
        
        JLabel lOk = new JLabel(" ✓ OK ", SwingConstants.CENTER);
        lOk.setFont(new Font("SansSerif", Font.BOLD, 9));
        lOk.setOpaque(true);
        lOk.setBackground(new Color(209, 250, 229));
        lOk.setForeground(new Color(5, 150, 105));
        pnl.add(lOk, BorderLayout.EAST);
        
        return pnl;
    }

    private JPanel crearCardBlanca() {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        return pnl;
    }

    private JPanel crearPill(String txt, boolean rojo, boolean cian) {
        JPanel pnl = new JPanel(new BorderLayout());
        if (rojo) {
            pnl.setBackground(new Color(254, 226, 226));
            pnl.setBorder(new EmptyBorder(4, 8, 4, 8));
        } else if (cian) {
            pnl.setBackground(new Color(204, 241, 248));
            pnl.setBorder(new EmptyBorder(4, 8, 4, 8));
        } else {
            pnl.setBackground(new Color(241, 245, 249));
            pnl.setBorder(new EmptyBorder(4, 8, 4, 8));
        }
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 9));
        if (rojo) lbl.setForeground(new Color(225, 29, 72));
        else if (cian) lbl.setForeground(new Color(14, 116, 144));
        else lbl.setForeground(new Color(148, 163, 184));
        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }

    // Reuse helper methods
    private JPanel crearIconoEscudo(int w, int h) {
        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(204, 241, 248)); // cyan muy claro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(14, 116, 144)); // escudo cyan oscuro
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
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
    private JPanel crearItemMenu(String icon, String title, boolean active, boolean alert) {
        JPanel pnl = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(241, 245, 249));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
            }
        };
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(8, 10, 8, 10));
        pnl.setMaximumSize(new Dimension(210, 35));
        if(!active) pnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Color col = active ? new Color(14, 145, 165) : new Color(100, 116, 139);
        JLabel lblIco = new JLabel(icon);
        lblIco.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblIco.setForeground(col);
        JLabel lblTit = new JLabel(title);
        lblTit.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 12));
        lblTit.setForeground(active ? new Color(15, 23, 42) : new Color(100, 116, 139));
        pnl.add(lblIco, BorderLayout.WEST);
        pnl.add(lblTit, BorderLayout.CENTER);
        if (alert) {
            JLabel lblAlert = new JLabel("1", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(225, 29, 72));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            lblAlert.setForeground(Color.WHITE);
            lblAlert.setFont(new Font("SansSerif", Font.BOLD, 9));
            lblAlert.setPreferredSize(new Dimension(16, 16));
            pnl.add(lblAlert, BorderLayout.EAST);
        }
        return pnl;
    }
    private JPanel crearAvatar(String letra) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 145, 165));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(letra)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(letra, x, y);
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        pnl.setPreferredSize(new Dimension(30, 30));
        return pnl;
    }
    private JButton crearBotonPeligro(String txt) {
        JButton btn = new JButton(txt);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBackground(new Color(225, 29, 72));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setBorder(new EmptyBorder(6, 15, 6, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    private JButton crearBotonAccion(String txt) {
        JButton btn = new JButton(txt);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(15, 23, 42));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
