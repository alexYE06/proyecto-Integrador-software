package vista;

import controlador.LoginController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginCentralView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;
    private JButton btnIngresar;
    private LoginController controller;

    public LoginCentralView() {
        setTitle("SAT-Carmen - Portal de Despacho");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 16, 29)); // Fondo oscuro institucional

        initComponentes();
        this.controller = new LoginController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        JPanel pnlContenedor = new JPanel();
        pnlContenedor.setBackground(new Color(16, 23, 38));
        pnlContenedor.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlContenedor.setLayout(new GridLayout(8, 1, 8, 8));

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

        pnlContenedor.add(lblBadge);
        pnlContenedor.add(lblTitulo);
        pnlContenedor.add(lblUser);
        pnlContenedor.add(txtUsuario);
        pnlContenedor.add(lblPass);
        pnlContenedor.add(txtPassword);
        pnlContenedor.add(lblRol);
        pnlContenedor.add(cbRol);

        add(pnlContenedor, BorderLayout.CENTER);
        add(btnIngresar, BorderLayout.SOUTH);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public String getRol() {
        return (String) cbRol.getSelectedItem();
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    // DISPARADOR DE PRUEBA RÁPIDA (No altera el código de tus compañeros)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginCentralView().setVisible(true);
        });
    }
}
