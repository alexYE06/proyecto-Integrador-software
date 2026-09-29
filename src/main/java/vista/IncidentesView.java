package vista;

import controlador.AlertaController;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class IncidentesView extends JPanel {

    private JComboBox<String> cbEstadoAlerta;
    private JButton btnActualizarEstado;
    private JButton btnDespacharPNP;
    private AlertaController alertaController;

    public IncidentesView() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // slate-50

        initComponentes();
        this.alertaController = new AlertaController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        // --- SIDEBAR (IZQUIERDA) ---
        JPanel pnlSidebar = new JPanel(new BorderLayout());
        pnlSidebar.setBackground(Color.WHITE);
        pnlSidebar.setPreferredSize(new Dimension(240, getHeight()));
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
        
        // Ahora Incidente es activo
        pnlMenu.add(crearItemMenu("⚠", "Incidente activo", true, true));
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
        JLabel lblTitulo = new JLabel("Gestión del incidente");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        JLabel lblSub = new JLabel("ALT-2026-0923-001 · BUS-051");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(148, 163, 184));
        pnlHeaderTit.add(lblTitulo);
        pnlHeaderTit.add(lblSub);
        
        JPanel pnlHeaderBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlHeaderBtns.setOpaque(false);
        pnlHeaderBtns.add(crearBotonAccion("🔍 Buscar unidad, conductor o placa..."));
        
        JButton btnRegChofer = crearBotonPeligro("+ Registrar Chofer");
        btnRegChofer.setBackground(new Color(16, 185, 129)); // Verde
        btnRegChofer.addActionListener(e -> new RegistrarConductorView().setVisible(true));
        pnlHeaderBtns.add(btnRegChofer);
        
        pnlHeaderBtns.add(crearBotonAccion("🔔  15:28:18"));
        
        JButton btnSimular = crearBotonPeligro("⚠ Simular alerta");
        pnlHeaderBtns.add(btnSimular);
        
        pnlHeaderBtns.add(crearBotonAccion("App móvil"));
        JButton btnSalir = crearBotonAccion("Salir central");
        btnSalir.addActionListener(e -> System.exit(0));
        pnlHeaderBtns.add(btnSalir);
        
        pnlHeaderCentral.add(pnlHeaderTit, BorderLayout.WEST);
        pnlHeaderCentral.add(pnlHeaderBtns, BorderLayout.EAST);
        pnlCentral.add(pnlHeaderCentral, BorderLayout.NORTH);

        // --- CUERPO (Banner + 2 Columnas) ---
        JPanel pnlCuerpo = new JPanel(new BorderLayout(15, 15));
        pnlCuerpo.setOpaque(false);
        pnlCuerpo.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Top Banner del Bus
        JPanel pnlBannerBus = crearCardBlanca();
        pnlBannerBus.setLayout(new BorderLayout());
        pnlBannerBus.setPreferredSize(new Dimension(0, 65));
        
        JPanel pnlBannerIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        pnlBannerIzq.setOpaque(false);
        JLabel lblBusTit = new JLabel("BUS-051 - Alerta silenciosa");
        lblBusTit.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBusTit.setForeground(new Color(15, 23, 42));
        pnlBannerIzq.add(lblBusTit);
        pnlBannerIzq.add(crearPill("ALTA", true, false));
        pnlBannerIzq.add(crearPill("CU-18", false, true));
        
        JLabel lblBannerSub = new JLabel("  ALT-2026-0923-001 · activada hace 23 min · conductor Elena Castillo Bravo · R-04 — Ventanilla - La Perla - Av. La Marina (tramo campo)");
        lblBannerSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblBannerSub.setForeground(new Color(148, 163, 184));
        
        JPanel pnlBannerInfo = new JPanel(new BorderLayout());
        pnlBannerInfo.setOpaque(false);
        pnlBannerInfo.add(pnlBannerIzq, BorderLayout.NORTH);
        pnlBannerInfo.add(lblBannerSub, BorderLayout.CENTER);
        
        JPanel pnlBannerDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        pnlBannerDer.setOpaque(false);
        pnlBannerDer.add(crearPill("● EN VIVO · 23:59", true, true)); // rojo
        
        cbEstadoAlerta = new JComboBox<>(new String[] {
            "En Evaluación", "Recibida", "Auxilio Despachado", "Atendida (Cerrada)"
        });
        cbEstadoAlerta.setBackground(Color.WHITE);
        cbEstadoAlerta.setFont(new Font("SansSerif", Font.PLAIN, 11));
        pnlBannerDer.add(cbEstadoAlerta);
        
        btnActualizarEstado = crearBotonAccion("+ Monitoreo");
        pnlBannerDer.add(btnActualizarEstado);
        
        pnlBannerBus.add(pnlBannerInfo, BorderLayout.CENTER);
        pnlBannerBus.add(pnlBannerDer, BorderLayout.EAST);
        
        pnlCuerpo.add(pnlBannerBus, BorderLayout.NORTH);

        // Centro con 2 Columnas
        JPanel pnlSplit = new JPanel(new GridBagLayout());
        pnlSplit.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        
        // Columna Izquierda (Mapa + Rutas de auxilio)
        JPanel pnlColIzq = new JPanel();
        pnlColIzq.setLayout(new BoxLayout(pnlColIzq, BoxLayout.Y_AXIS));
        pnlColIzq.setOpaque(false);
        
        JPanel pnlMapa = crearCardBlanca();
        pnlMapa.setLayout(new BorderLayout());
        pnlMapa.setPreferredSize(new Dimension(800, 350));
        
        JPanel pnlMapaHeader = new JPanel(new BorderLayout());
        pnlMapaHeader.setOpaque(false);
        pnlMapaHeader.setBorder(new EmptyBorder(10, 15, 10, 15));
        JPanel pnlMapaHeaderIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlMapaHeaderIzq.setOpaque(false);
        pnlMapaHeaderIzq.add(crearPill("CU-09", false, true));
        JLabel lblMapaTit = new JLabel("Posición del vehículo en riesgo");
        lblMapaTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        pnlMapaHeaderIzq.add(lblMapaTit);
        pnlMapaHeader.add(pnlMapaHeaderIzq, BorderLayout.WEST);
        
        JLabel lblCoords = new JLabel("lat -12.04582 · lon -77.09461");
        lblCoords.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblCoords.setForeground(new Color(148, 163, 184));
        pnlMapaHeader.add(lblCoords, BorderLayout.EAST);
        
        JPanel pnlMapaDraw = crearMapaCanvas();
        pnlMapa.add(pnlMapaHeader, BorderLayout.NORTH);
        pnlMapa.add(pnlMapaDraw, BorderLayout.CENTER);
        
        pnlColIzq.add(pnlMapa);
        pnlColIzq.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Panel Rutas Auxilio
        JPanel pnlRutas = crearCardBlanca();
        pnlRutas.setLayout(new BoxLayout(pnlRutas, BoxLayout.Y_AXIS));
        
        JPanel pnlRutasHeader = new JPanel(new BorderLayout());
        pnlRutasHeader.setOpaque(false);
        pnlRutasHeader.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        JPanel pnlRH = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlRH.setOpaque(false);
        JLabel lblRutasTit = new JLabel("Ruta de auxilio optimizada");
        lblRutasTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        pnlRH.add(lblRutasTit);
        pnlRH.add(crearPill("CU-34", false, true));
        pnlRutasHeader.add(pnlRH, BorderLayout.WEST);
        pnlRutasHeader.add(crearPill("Haversine · CU-16", false, true), BorderLayout.EAST);
        
        pnlRutas.add(pnlRutasHeader);
        
        pnlRutas.add(crearItemRuta(1, "Hospital A. Sabogal", "Hospital · (01) 537-1616", "1.71 km", "ETA ≤ 3 min", true));
        pnlRutas.add(crearItemRuta(2, "Serenazgo Callao - GOP", "Serenazgo · (01) 452-9911", "2.22 km", "ETA ≈ 4 min", false));
        pnlRutas.add(crearItemRuta(3, "Hospital D. A. Carrión (Callao)", "Hospital · (01) 452-9700", "2.78 km", "ETA ≤ 5 min", false));
        
        JPanel pnlRutasBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlRutasBtns.setOpaque(false);
        btnDespacharPNP = crearBotonPeligro("Despachar apoyo recomendado");
        pnlRutasBtns.add(btnDespacharPNP);
        pnlRutasBtns.add(crearBotonAccion("Recalcular (en movimiento)"));
        pnlRutas.add(pnlRutasBtns);
        
        pnlColIzq.add(pnlRutas);
        
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 15);
        pnlSplit.add(pnlColIzq, gbc);

        // Columna Derecha (Ficha, Video, Notificaciones)
        JPanel pnlColDer = new JPanel();
        pnlColDer.setLayout(new BoxLayout(pnlColDer, BoxLayout.Y_AXIS));
        pnlColDer.setOpaque(false);
        
        // Ficha Tecnica
        JPanel pnlFicha = crearCardBlanca();
        pnlFicha.setLayout(new BoxLayout(pnlFicha, BoxLayout.Y_AXIS));
        pnlFicha.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel pnlFichaH = new JPanel(new BorderLayout());
        pnlFichaH.setOpaque(false);
        JPanel pnlFH = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlFH.setOpaque(false);
        JLabel lblFichaTit = new JLabel("Ficha técnica en vivo");
        lblFichaTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        pnlFH.add(lblFichaTit);
        pnlFH.add(crearPill("CU-11", false, true));
        pnlFichaH.add(pnlFH, BorderLayout.WEST);
        pnlFichaH.add(crearPill("PRIORIDAD", true, false), BorderLayout.EAST); // rojo texto
        
        pnlFicha.add(pnlFichaH);
        pnlFicha.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel pnlFData = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlFData.setOpaque(false);
        pnlFData.add(crearDataInfo("PLACA / UNIDAD", "BUS-051 - ZZ7-801"));
        pnlFData.add(crearDataInfo("PADRÓN / MODELO", "904-2021 - Zhongtong LCK6127"));
        pnlFData.add(crearDataInfo("CONDUCTOR", "Elena Castillo Bravo"));
        pnlFData.add(crearDataInfo("RUTA", "R-04 - Ventanilla - La Perla"));
        pnlFData.add(crearDataInfo("VELOCIDAD / RUMBO", "27 km/h"));
        
        JPanel pnlBat = new JPanel(new BorderLayout());
        pnlBat.setOpaque(false);
        JLabel lblBatT = new JLabel("BATERÍA DEL SMARTPHONE");
        lblBatT.setFont(new Font("SansSerif", Font.BOLD, 8));
        lblBatT.setForeground(new Color(148, 163, 184));
        JLabel lblBatV = new JLabel("61%");
        lblBatV.setFont(new Font("SansSerif", Font.BOLD, 11));
        pnlBat.add(lblBatT, BorderLayout.NORTH);
        pnlBat.add(lblBatV, BorderLayout.CENTER);
        JPanel pnlBar = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(226, 232, 240));
                g.fillRect(0, 0, getWidth(), 4);
                g.setColor(new Color(52, 211, 153));
                g.fillRect(0, 0, (int)(getWidth()*0.61), 4);
            }
        };
        pnlBar.setPreferredSize(new Dimension(100, 4));
        pnlBat.add(pnlBar, BorderLayout.SOUTH);
        pnlFData.add(pnlBat);
        
        pnlFData.add(crearDataInfo("SEÑAL CELULAR", "3/5 - WebSockets"));
        
        pnlFicha.add(pnlFData);
        pnlColDer.add(pnlFicha);
        pnlColDer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Video
        JPanel pnlVid = crearCardBlanca();
        pnlVid.setLayout(new BorderLayout());
        pnlVid.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel pnlVidH = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlVidH.setOpaque(false);
        JLabel lblVidTit = new JLabel("Streaming de audio / video");
        lblVidTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        pnlVidH.add(lblVidTit);
        pnlVidH.add(crearPill("CU-12", false, true));
        pnlVid.add(pnlVidH, BorderLayout.NORTH);
        
        JPanel pnlBlack = new JPanel(new BorderLayout());
        pnlBlack.setBackground(new Color(15, 23, 42));
        pnlBlack.setPreferredSize(new Dimension(0, 200));
        JPanel pnlBTop = new JPanel(new BorderLayout());
        pnlBTop.setOpaque(false);
        pnlBTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblRec = new JLabel("● REC · BUS-051");
        lblRec.setForeground(new Color(225, 29, 72));
        lblRec.setFont(new Font("SansSerif", Font.BOLD, 10));
        JLabel lblQ = new JLabel("1080p · 420 kbps (adaptativo BT-12)");
        lblQ.setForeground(Color.WHITE);
        lblQ.setFont(new Font("SansSerif", Font.PLAIN, 9));
        pnlBTop.add(lblRec, BorderLayout.WEST);
        pnlBTop.add(lblQ, BorderLayout.EAST);
        pnlBlack.add(pnlBTop, BorderLayout.NORTH);
        
        pnlVid.add(pnlBlack, BorderLayout.CENTER);
        
        pnlColDer.add(pnlVid);
        pnlColDer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Notificaciones
        JPanel pnlNoti = crearCardBlanca();
        pnlNoti.setLayout(new BoxLayout(pnlNoti, BoxLayout.Y_AXIS));
        pnlNoti.setBorder(new EmptyBorder(15, 15, 15, 15));
        JPanel pnlNotiH = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlNotiH.setOpaque(false);
        JLabel lblNotiTit = new JLabel("Notificaciones multicanal");
        lblNotiTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        pnlNotiH.add(lblNotiTit);
        pnlNotiH.add(crearPill("CU-37", false, true));
        pnlNoti.add(pnlNotiH);
        pnlNoti.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlNoti.add(crearNotiItem("S", "SMS → Policía Nacional del Perú", "Enlace de ubicación satelital del BUS-051", "✓ Acuse"));
        pnlNoti.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlNoti.add(crearNotiItem("@", "Correo SMTP → Gerencia", "Ficha completa del incidente ALT-2026-0923-001", "✓ Acuse"));
        pnlNoti.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlNoti.add(crearNotiItem("☁", "Respaldo de evidencias cifradas", "Sincronizado a S3 con bucket inmutable", "✓ 3 archivos"));
        pnlColDer.add(pnlNoti);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlSplit.add(pnlColDer, gbc);
        
        pnlCuerpo.add(pnlSplit, BorderLayout.CENTER);
        pnlCentral.add(pnlCuerpo, BorderLayout.CENTER);

        add(pnlCentral, BorderLayout.CENTER);
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

    private JPanel crearMapaCanvas() {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                g2.setColor(new Color(15, 23, 42)); // oscuro azul
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0); // esquinas inferiores rectas
                
                // Rutas oscuras (cruzadas)
                g2.setColor(new Color(30, 58, 138));
                g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(100, 100, 600, 120);
                g2.drawLine(50, 200, 400, 250);
                g2.drawLine(200, 150, 250, 400);
                g2.drawLine(150, 280, 500, 180);
                
                // Puntos naranjas diamantes (riesgos)
                g2.setColor(new Color(239, 68, 68, 100)); // semi transparente rojo/naranja
                int[][] polyX = {{300, 310, 300, 290}};
                int[][] polyY = {{150, 160, 170, 160}};
                for (int i=0; i<3; i++) {
                    g2.fillPolygon(new int[]{300+(i*80), 310+(i*80), 300+(i*80), 290+(i*80)}, 
                                   new int[]{150+(i*60), 160+(i*60), 170+(i*60), 160+(i*60)}, 4);
                    g2.setColor(new Color(239, 68, 68));
                    g2.drawPolygon(new int[]{300+(i*80), 310+(i*80), 300+(i*80), 290+(i*80)}, 
                                   new int[]{150+(i*60), 160+(i*60), 170+(i*60), 160+(i*60)}, 4);
                }

                // Punto en alerta crítico
                g2.setColor(new Color(52, 211, 153)); // circulo verde de tracking
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
                g2.drawOval(480, 180, 30, 30);
                g2.setColor(new Color(52, 211, 153)); // solido
                g2.fillOval(490, 190, 10, 10);
                
                // Textos
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.setColor(new Color(239, 68, 68));
                g2.drawString("ZONA DE RIESGO - Z3", 250, 260);
                
                // Tooltip central simulado
                g2.setColor(new Color(15, 23, 42, 240));
                g2.fillRoundRect(380, 160, 140, 50, 10, 10);
                g2.setColor(new Color(148, 163, 184));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(380, 160, 140, 50, 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("BUS-051", 390, 180);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.setColor(new Color(203, 213, 225));
                g2.drawString("27 km/h · R-04 · E", 390, 198);
                
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        return pnl;
    }

    private JPanel crearDataInfo(String tit, String val) {
        JPanel pnl = new JPanel(new GridLayout(2, 1));
        pnl.setOpaque(false);
        JLabel lT = new JLabel(tit);
        lT.setFont(new Font("SansSerif", Font.BOLD, 8));
        lT.setForeground(new Color(148, 163, 184));
        JLabel lV = new JLabel(val);
        lV.setFont(new Font("SansSerif", Font.BOLD, 11));
        lV.setForeground(new Color(15, 23, 42));
        pnl.add(lT);
        pnl.add(lV);
        return pnl;
    }

    private JPanel crearNotiItem(String ic, String t1, String t2, String status) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JPanel pnlIco = new JPanel();
        pnlIco.setBackground(new Color(241, 245, 249));
        pnlIco.setPreferredSize(new Dimension(24, 24));
        pnlIco.add(new JLabel(ic));
        
        JPanel pnlText = new JPanel(new GridLayout(2, 1));
        pnlText.setOpaque(false);
        pnlText.setBorder(new EmptyBorder(0, 10, 0, 0));
        JLabel l1 = new JLabel(t1);
        l1.setFont(new Font("SansSerif", Font.BOLD, 10));
        JLabel l2 = new JLabel(t2);
        l2.setFont(new Font("SansSerif", Font.PLAIN, 9));
        l2.setForeground(new Color(148, 163, 184));
        pnlText.add(l1);
        pnlText.add(l2);
        
        JLabel l3 = new JLabel(status);
        l3.setFont(new Font("SansSerif", Font.BOLD, 9));
        l3.setForeground(new Color(16, 185, 129));
        
        pnl.add(pnlIco, BorderLayout.WEST);
        pnl.add(pnlText, BorderLayout.CENTER);
        pnl.add(l3, BorderLayout.EAST);
        return pnl;
    }

    private JPanel crearItemRuta(int num, String tit, String sub, String dist, String eta, boolean active) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, new Color(241, 245, 249)),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        if (active) {
            pnl.setBackground(new Color(240, 253, 250)); // fondo muy clarito cyan
            pnl.setOpaque(true);
        }
        
        JPanel pnlIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlIzq.setOpaque(false);
        
        JLabel lblNum = new JLabel(num+"", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    g.setColor(new Color(14, 145, 165));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    setForeground(Color.WHITE);
                } else {
                    g.setColor(new Color(241, 245, 249));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    setForeground(new Color(100, 116, 139));
                }
                super.paintComponent(g);
            }
        };
        lblNum.setPreferredSize(new Dimension(24, 24));
        lblNum.setFont(new Font("SansSerif", Font.BOLD, 10));
        
        JPanel pnlTxt = new JPanel(new GridLayout(2, 1));
        pnlTxt.setOpaque(false);
        JLabel lTit = new JLabel(tit);
        lTit.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel lSub = new JLabel(sub);
        lSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lSub.setForeground(new Color(148, 163, 184));
        pnlTxt.add(lTit);
        pnlTxt.add(lSub);
        
        pnlIzq.add(lblNum);
        pnlIzq.add(pnlTxt);
        
        JPanel pnlDer = new JPanel(new GridLayout(2, 1));
        pnlDer.setOpaque(false);
        JLabel lDist = new JLabel(dist, SwingConstants.RIGHT);
        lDist.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel lEta = new JLabel(eta, SwingConstants.RIGHT);
        lEta.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lEta.setForeground(new Color(148, 163, 184));
        pnlDer.add(lDist);
        pnlDer.add(lEta);
        
        pnl.add(pnlIzq, BorderLayout.WEST);
        pnl.add(pnlDer, BorderLayout.EAST);
        
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

    // Reuse helper methods from Monitoreo
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

    public String getEstadoSeleccionado() { return (String) cbEstadoAlerta.getSelectedItem(); }
    public JButton getBtnActualizarEstado() { return btnActualizarEstado; }
    public JButton getBtnDespacharPNP() { return btnDespacharPNP; }

}