package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.OperadorController;

public class RegistrarOperadorView extends JFrame {

    private final JTextField txtNombres = crearCampo();
    private final JTextField txtApellidos = crearCampo();
    private final JTextField txtTelefono = crearCampo();
    private final JTextField txtCargo = crearCampo();
    private final JTextField txtUsuario = crearCampo();
    private final JPasswordField txtContrasena = new JPasswordField();
    private final JButton btnRegistrar = new JButton("REGISTRAR OPERADOR");

    public RegistrarOperadorView() {
        setTitle("SAT-Carmen - Registrar operador");
        setSize(480, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 16, 29));
        initComponentes();
        new OperadorController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("REGISTRO DE OPERADOR DE DESPACHO", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(20, 10, 0, 10));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 12));
        formulario.setBackground(new Color(16, 23, 38));
        formulario.setBorder(new EmptyBorder(25, 30, 20, 30));
        agregarCampo(formulario, "NOMBRES:", txtNombres);
        agregarCampo(formulario, "APELLIDOS:", txtApellidos);
        agregarCampo(formulario, "TELÉFONO (OPCIONAL):", txtTelefono);
        agregarCampo(formulario, "CARGO:", txtCargo);
        agregarCampo(formulario, "NOMBRE DE USUARIO:", txtUsuario);
        txtContrasena.setBackground(new Color(5, 8, 15));
        txtContrasena.setForeground(Color.WHITE);
        txtContrasena.setCaretColor(Color.WHITE);
        agregarCampo(formulario, "CONTRASEÑA (MÍN. 6):", txtContrasena);

        btnRegistrar.setBackground(new Color(16, 185, 129));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        add(titulo, BorderLayout.NORTH);
        add(formulario, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }

    private static JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(5, 8, 15));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        return campo;
    }

    private static void agregarCampo(JPanel formulario, String texto, java.awt.Component campo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(148, 163, 184));
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 11));
        formulario.add(etiqueta);
        formulario.add(campo);
    }

    public String getNombres() { return txtNombres.getText().trim(); }
    public String getApellidos() { return txtApellidos.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getCargo() { return txtCargo.getText().trim(); }
    public String getUsuario() { return txtUsuario.getText().trim(); }
    public String getContrasena() { return new String(txtContrasena.getPassword()); }
    public JButton getBtnRegistrar() { return btnRegistrar; }

    public void limpiarFormulario() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtCargo.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtNombres.requestFocus();
    }
}
