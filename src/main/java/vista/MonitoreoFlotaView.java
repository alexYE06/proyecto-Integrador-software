package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MonitoreoFlotaView extends JPanel {

    public MonitoreoFlotaView() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // slate-50
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        // --- SIDEBAR (IZQUIERDA) ---
        JPanel pnlSidebar = new JPanel(new BorderLayout());
        pnlSidebar.setBackground(Color.WHITE);
        pnlSidebar.setPreferredSize(new Dimension(240, getHeight()));
        pnlSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(226, 232, 240))); // borde derecho

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

        pnlMenu.add(crearItemMenu("■", "Monitoreo de flota", true, false));
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
        
        JPanel itemBackend = crearItemMenu("⚙", "Servicios backend", false, false);
        itemBackend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainCentralFrame.getInstance().cambiarPantalla("Backend");
            }
        });
        pnlMenu.add(itemBackend);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblSeccion2 = new JLabel("CASOS DE USO");
        lblSeccion2.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblSeccion2.setForeground(new Color(148, 163, 184));
        pnlMenu.add(lblSeccion2);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        // Pills CU
        JPanel pnlCUs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlCUs.setBackground(Color.WHITE);
        pnlCUs.setMaximumSize(new Dimension(210, 150));
        String[] cus = {"CU-01", "CU-09", "CU-10", "CU-11", "CU-12", "CU-13", "CU-14", "CU-15"};
        for (String cu : cus) {
            pnlCUs.add(crearCUPill(cu));
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

        // Cabecera Central
        JPanel pnlHeaderCentral = new JPanel(new BorderLayout());
        pnlHeaderCentral.setOpaque(false);
        
        JPanel pnlHeaderTit = new JPanel(new GridLayout(2, 1));
        pnlHeaderTit.setOpaque(false);
        JLabel lblTitulo = new JLabel("Monitoreo en tiempo real");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("80 unidades · latencia WebSocket < 100 ms · Callao");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(148, 163, 184));
        pnlHeaderTit.add(lblTitulo);
        pnlHeaderTit.add(lblSub);
        
        JPanel pnlHeaderBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlHeaderBtns.setOpaque(false);
        pnlHeaderBtns.add(crearBotonBusqueda("Buscar unidad, conductor o placa..."));
        
        JButton btnRegChofer = crearBotonPeligro("+ Registrar Chofer");
        btnRegChofer.setBackground(new Color(16, 185, 129)); // Verde
        btnRegChofer.addActionListener(e -> new RegistrarConductorView().setVisible(true));
        pnlHeaderBtns.add(btnRegChofer);
        
        pnlHeaderBtns.add(crearBotonIcono("🔔", "15:27:16"));
        
        JButton btnSimular = crearBotonPeligro("⚠ Simular alerta");
        btnSimular.addActionListener(e -> JOptionPane.showMessageDialog(this, "[SIMULACIÓN] Alerta física disparada."));
        pnlHeaderBtns.add(btnSimular);
        
        pnlHeaderBtns.add(crearBotonAccion("App móvil"));
        
        JButton btnSalir = crearBotonAccion("Salir central");
        btnSalir.addActionListener(e -> {
            System.exit(0);
        });
        pnlHeaderBtns.add(btnSalir);
        
        pnlHeaderCentral.add(pnlHeaderTit, BorderLayout.WEST);
        pnlHeaderCentral.add(pnlHeaderBtns, BorderLayout.EAST);
        
        pnlCentral.add(pnlHeaderCentral, BorderLayout.NORTH);

        // Cuerpo Central (KPIs + Mapa + Alertas)
        JPanel pnlCuerpo = new JPanel(new BorderLayout(20, 20));
        pnlCuerpo.setOpaque(false);
        pnlCuerpo.setBorder(new EmptyBorder(20, 0, 0, 0));

        // KPIs
        JPanel pnlKPIs = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlKPIs.setOpaque(false);
        pnlKPIs.setPreferredSize(new Dimension(0, 80));
        pnlKPIs.add(crearCardKPI("UNIDADES ACTIVAS", "76", "de 80 buses en ruta - RF-17", new Color(16, 185, 129))); // verde
        pnlKPIs.add(crearCardKPI("ALERTAS ACTIVAS", "1", "prioridad máxima en consola - RF-13", new Color(225, 29, 72))); // rojo
        pnlKPIs.add(crearCardKPI("EN ATENCIÓN", "1", "evaluación o auxilio despachado", new Color(245, 158, 11))); // naranja
        pnlKPIs.add(crearCardKPI("NOTIFICACIÓN PROMEDIO", "6.8s", "meta KPI < 10 s - WebSockets", new Color(15, 23, 42))); // oscuro
        pnlCuerpo.add(pnlKPIs, BorderLayout.NORTH);

        // Mapa y Alertas
        JPanel pnlAbajo = new JPanel(new BorderLayout(20, 0));
        pnlAbajo.setOpaque(false);

        // Mapa Simulado (Izquierda)
        JPanel pnlMapa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo oscuro azul
                g2.setColor(new Color(15, 23, 42));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Lineas de ruta
                g2.setColor(new Color(30, 58, 138));
                g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(150, 150, 600, 170);
                g2.drawLine(200, 300, 700, 200);
                g2.drawLine(100, 250, 180, 500);
                g2.drawLine(250, 450, 650, 300);

                // Puntos normales (verdes)
                g2.setColor(new Color(52, 211, 153));
                for(int i=0; i<8; i++) {
                    g2.fillOval(200 + (i*40), 155 + (i*2), 12, 12);
                    g2.fillOval(250 + (i*50), 280 - (i*10), 12, 12);
                }

                // Punto en alerta (naranja/rojo)
                g2.setColor(new Color(249, 115, 22)); // naranja
                g2.fillOval(350, 350, 16, 16);
                
                // Textos simulados del mapa
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.setColor(new Color(148, 163, 184));
                g2.drawString("Av. Faucett", 600, 140);
                g2.drawString("Av. Argentina", 550, 250);
                g2.drawString("Océano Pacífico", 50, 600);

                // Tooltip simulado
                g2.setColor(new Color(15, 23, 42, 230)); // translúcido
                g2.fillRoundRect(400, 220, 150, 50, 10, 10);
                g2.setColor(new Color(56, 189, 248)); // borde
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(400, 220, 150, 50, 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString("BUS-051", 410, 240);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.setColor(new Color(203, 213, 225));
                g2.drawString("27 km/h · R-04 · E", 410, 258);
                
                g2.dispose();
            }
        };
        pnlMapa.setLayout(new BorderLayout());
        pnlMapa.setOpaque(false);
        
        // Header del mapa (filtros blancos)
        JPanel pnlMapaHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlMapaHeader.setOpaque(false);
        pnlMapaHeader.add(crearFiltroMapa("CU-07", true));
        pnlMapaHeader.add(crearFiltroMapa("Todas las rutas", false));
        pnlMapaHeader.add(crearFiltroMapa("Todo estado", false));
        pnlMapa.add(pnlMapaHeader, BorderLayout.NORTH);

        pnlAbajo.add(pnlMapa, BorderLayout.CENTER);

        // Sidebar de Alertas (Derecha)
        JPanel pnlAlertas = new JPanel(new BorderLayout());
        pnlAlertas.setOpaque(false);
        pnlAlertas.setPreferredSize(new Dimension(320, 0));
        
        JPanel pnlCardAlertas = new JPanel();
        pnlCardAlertas.setLayout(new BoxLayout(pnlCardAlertas, BoxLayout.Y_AXIS));
        pnlCardAlertas.setBackground(Color.WHITE);
        pnlCardAlertas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Header de Alertas Recientes
        JPanel pnlAleHeader = new JPanel(new BorderLayout());
        pnlAleHeader.setOpaque(false);
        pnlAleHeader.setMaximumSize(new Dimension(300, 30));
        JLabel lblAleTit = new JLabel("Alertas recientes CU-08");
        lblAleTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblAleTit.setForeground(new Color(15, 23, 42));
        JLabel lblAleCount = new JLabel("2", SwingConstants.CENTER);
        lblAleCount.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblAleCount.setOpaque(true);
        lblAleCount.setBackground(new Color(241, 245, 249));
        lblAleCount.setPreferredSize(new Dimension(20, 20));
        pnlAleHeader.add(lblAleTit, BorderLayout.WEST);
        pnlAleHeader.add(lblAleCount, BorderLayout.EAST);
        pnlCardAlertas.add(pnlAleHeader);
        pnlCardAlertas.add(Box.createRigidArea(new Dimension(0, 15)));

        // Items de alertas
        pnlCardAlertas.add(crearItemAlerta("BUS-051", "En Evaluación", "Av. La Marina (tramo campo)", "Elena Castillo Bravo · Ventanilla - La Perla", "hace 23 min", true));
        pnlCardAlertas.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlCardAlertas.add(crearItemAlerta("BUS-009", "Atendida", "Av. Cónegos", "Ariana Castillo Bravo · Av. Argentina - La Marina", "hace 35 min", false));
        
        pnlAlertas.add(pnlCardAlertas, BorderLayout.NORTH);

        pnlAbajo.add(pnlAlertas, BorderLayout.EAST);
        pnlCuerpo.add(pnlAbajo, BorderLayout.CENTER);

        pnlCentral.add(pnlCuerpo, BorderLayout.CENTER);
        add(pnlCentral, BorderLayout.CENTER);
    }

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
                    g2.setColor(new Color(241, 245, 249)); // slate-100 bg
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
            }
        };
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(8, 10, 8, 10));
        pnl.setMaximumSize(new Dimension(210, 35));
        if(!active) pnl.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color col = active ? new Color(14, 145, 165) : new Color(100, 116, 139); // teal o gris
        
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
                    g2.setColor(new Color(225, 29, 72)); // rojo
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

    private JPanel crearCUPill(String cu) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(new Color(241, 245, 249));
        pnl.setBorder(new EmptyBorder(4, 8, 4, 8));
        JLabel lbl = new JLabel(cu);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setForeground(new Color(14, 116, 144));
        pnl.add(lbl, BorderLayout.CENTER);
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

    private JPanel crearBotonBusqueda(String txt) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        JLabel lbl = new JLabel("🔍 " + txt);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(new Color(148, 163, 184));
        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel crearBotonIcono(String icon, String txt) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        JLabel lbl = new JLabel(icon + "  " + txt);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        pnl.add(lbl);
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

    private JPanel crearCardKPI(String tit, String val, String sub, Color col) {
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
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(12, 15, 12, 15));
        
        JLabel lblTit = new JLabel(tit);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblTit.setForeground(new Color(100, 116, 139));
        
        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblVal.setForeground(new Color(15, 23, 42));
        
        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblSub.setForeground(new Color(148, 163, 184));

        pnl.add(lblTit);
        pnl.add(lblVal);
        pnl.add(lblSub);
        
        return pnl;
    }

    private JPanel crearFiltroMapa(String txt, boolean cian) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setForeground(cian ? new Color(14, 145, 165) : new Color(100, 116, 139));
        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel crearItemAlerta(String bus, String badge, String loc, String subloc, String time, boolean activo) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 245, 249), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Fila 1: Titulo y badge
        JPanel f1 = new JPanel(new BorderLayout());
        f1.setOpaque(false);
        JLabel lblBus = new JLabel(bus);
        lblBus.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        JLabel lblBadge = new JLabel(" " + badge + " ", SwingConstants.CENTER);
        lblBadge.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblBadge.setOpaque(true);
        if(activo) {
            lblBadge.setBackground(new Color(254, 243, 199)); // amarillo claro
            lblBadge.setForeground(new Color(217, 119, 6)); // naranja
        } else {
            lblBadge.setBackground(new Color(209, 250, 229)); // verde claro
            lblBadge.setForeground(new Color(5, 150, 105)); // verde oscuro
        }
        f1.add(lblBus, BorderLayout.WEST);
        f1.add(lblBadge, BorderLayout.EAST);
        
        // Fila 2: Textos
        JLabel lblLoc = new JLabel(loc);
        lblLoc.setFont(new Font("SansSerif", Font.BOLD, 10));
        JLabel lblSub = new JLabel(subloc);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblSub.setForeground(new Color(148, 163, 184));
        
        // Fila 3: Tiempo y boton
        JPanel f3 = new JPanel(new BorderLayout());
        f3.setOpaque(false);
        JLabel lblTime = new JLabel(time);
        lblTime.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblTime.setForeground(new Color(148, 163, 184));
        f3.add(lblTime, BorderLayout.WEST);
        
        if (activo) {
            JButton btnVer = new JButton("Ver incidente");
            btnVer.setContentAreaFilled(false);
            btnVer.setBorderPainted(false);
            btnVer.setFocusPainted(false);
            btnVer.setForeground(new Color(225, 29, 72)); // rojo
            btnVer.setFont(new Font("SansSerif", Font.BOLD, 10));
            btnVer.setMargin(new Insets(0,0,0,0));
            btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnVer.addActionListener(e -> new IncidentesView().setVisible(true));
            f3.add(btnVer, BorderLayout.EAST);
        }
        
        pnl.add(f1);
        pnl.add(Box.createRigidArea(new Dimension(0, 5)));
        pnl.add(lblLoc);
        pnl.add(lblSub);
        pnl.add(Box.createRigidArea(new Dimension(0, 5)));
        pnl.add(f3);
        
        return pnl;
    }

}