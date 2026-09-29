package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginCentralAntiguoView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;
    private JButton btnIngresar;

    public LoginCentralAntiguoView() {
        setTitle("SAT-Carmen - Portal de Despacho (Antiguo)");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 16, 29)); // Fondo oscuro institucional

        initComponentes();
        configurarEventos();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        JPanel pnlContenedor = new JPanel();
        pnlContenedor.setBackground(new Color(16, 23, 38));
        pnlContenedor.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlContenedor.setLayout(new GridLayout(9, 1, 8, 8)); // 9 filas para añadir el boton de volver

        JLabel lblBadge = new JLabel("ETCAPSA 71A", SwingConstants.CENTER);
        lblBadge.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBadge.setForeground(new Color(250, 204, 21));

        JLabel lblTitulo = new JLabel("Central de Despacho", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("USUARIO / CÓDIGO:");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblUser.setForeground(new Color(148, 163, 184));
        txtUsuario = new JTextField("lramos");
        txtUsuario.setBackground(new Color(5, 8, 15));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);

        JLabel lblPass = new JLabel("CONTRASEÑA:");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblPass.setForeground(new Color(148, 163, 184));
        txtPassword = new JPasswordField("demo123");
        txtPassword.setBackground(new Color(5, 8, 15));
        txtPassword.setForeground(Color.WHITE);

        JLabel lblRol = new JLabel("PERFIL OPERATIVO:");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblRol.setForeground(new Color(148, 163, 184));
        cbRol = new JComboBox<>(new String[] { "Operador de Despacho", "Administrador" });
        cbRol.setBackground(new Color(5, 8, 15));
        cbRol.setForeground(Color.WHITE);

        btnIngresar = new JButton("INGRESAR A CONSOLA →");
        btnIngresar.setBackground(new Color(225, 29, 72));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnVolver = new JButton("Volver al Selector");
        btnVolver.setBackground(new Color(16, 23, 38));
        btnVolver.setForeground(new Color(148, 163, 184));
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            this.dispose();
            new DemoSelectorView().setVisible(true);
        });

        pnlContenedor.add(lblBadge);
        pnlContenedor.add(lblTitulo);
        pnlContenedor.add(lblUser);
        pnlContenedor.add(txtUsuario);
        pnlContenedor.add(lblPass);
        pnlContenedor.add(txtPassword);
        pnlContenedor.add(lblRol);
        pnlContenedor.add(cbRol);
        pnlContenedor.add(btnVolver);

        add(pnlContenedor, BorderLayout.CENTER);
        add(btnIngresar, BorderLayout.SOUTH);
    }
    
    private void configurarEventos() {
        btnIngresar.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String pass = new String(txtPassword.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario u = usuarioDAO.validarAcceso(user, pass);

            if (u != null) {
                JOptionPane.showMessageDialog(this,
                        "Bienvenido a la Central de Despacho: " + u.getNombreUsuario(),
                        "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new MainCentralFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Credenciales incorrectas o usuario no activo.",
                        "Fallo de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
