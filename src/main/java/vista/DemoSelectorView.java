package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DemoSelectorView extends JFrame {

    public DemoSelectorView() {
        setTitle("SAT Alerta - Selección de Entorno (Demo)");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(15, 23, 42)); // Fondo oscuro moderno

        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Seleccione el entorno a simular", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlBotones = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlBotones.setBackground(new Color(15, 23, 42));
        pnlBotones.setBorder(new EmptyBorder(20, 50, 60, 50));

        // Botón Chofer
        JButton btnChofer = crearBotonOpcion("Entorno Chofer", "Formato móvil (Vertical)", new Color(6, 182, 212));
        btnChofer.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginChoferView().setVisible(true));
        });

        // Botón Central Nuevo
        JButton btnCentral = crearBotonOpcion("Entorno Central", "Formato PC (Nuevo)", new Color(225, 29, 72));
        btnCentral.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginCentralView().setVisible(true));
        });

        pnlBotones.add(btnChofer);
        pnlBotones.add(btnCentral);
        
        add(pnlBotones, BorderLayout.CENTER);

        // --- ENLACE SECRETO / DISCRETO AL DISEÑO ANTIGUO ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSur.setBackground(new Color(15, 23, 42));
        pnlSur.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JButton btnSecreto = new JButton("Abrir versión clásica (V1)");
        btnSecreto.setContentAreaFilled(false);
        btnSecreto.setBorderPainted(false);
        btnSecreto.setFocusPainted(false);
        btnSecreto.setForeground(new Color(51, 65, 85)); // Un gris oscuro, casi oculto en el fondo
        btnSecreto.setFont(new Font("SansSerif", Font.PLAIN, 10));
        btnSecreto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover sutil para que se den cuenta que es un botón
        btnSecreto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSecreto.setForeground(new Color(148, 163, 184)); // Se aclara un poco al pasar el mouse
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSecreto.setForeground(new Color(51, 65, 85)); // Vuelve a ocultarse
            }
        });
        
        btnSecreto.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginCentralAntiguoView().setVisible(true));
        });
        
        pnlSur.add(btnSecreto);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private JButton crearBotonOpcion(String titulo, String subtitulo, Color colorBorde) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(30, 41, 59));
                } else {
                    g2.setColor(new Color(15, 23, 42));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTit.setForeground(Color.WHITE);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.add(Box.createVerticalGlue());
        btn.add(lblTit);
        btn.add(Box.createRigidArea(new Dimension(0, 10)));
        btn.add(lblSub);
        btn.add(Box.createVerticalGlue());

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DemoSelectorView().setVisible(true));
    }
}
