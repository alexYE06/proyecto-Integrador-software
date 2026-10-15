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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.ConductorController;

public class RegistrarConductorView extends JFrame {

    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtLicencia;
    private JButton btnRegistrar;
    private ConductorController controller;

    public RegistrarConductorView() {
        setTitle("SAT-Carmen - Registrar Chofer");
        setSize(420, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 16, 29));

        initComponentes();
        this.controller = new ConductorController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel pnlFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlFormulario.setBackground(new Color(16, 23, 38));
        pnlFormulario.setBorder(new EmptyBorder(25, 30, 20, 30));

        txtNombres = crearCampo();
        txtApellidos = crearCampo();
        txtDni = crearCampo();
        txtTelefono = crearCampo();
        txtLicencia = crearCampo();

        pnlFormulario.add(crearEtiqueta("NOMBRES:"));
        pnlFormulario.add(txtNombres);
        pnlFormulario.add(crearEtiqueta("APELLIDOS:"));
        pnlFormulario.add(txtApellidos);
        pnlFormulario.add(crearEtiqueta("DNI (8 DÍGITOS):"));
        pnlFormulario.add(txtDni);
        pnlFormulario.add(crearEtiqueta("TELÉFONO:"));
        pnlFormulario.add(txtTelefono);
        pnlFormulario.add(crearEtiqueta("LICENCIA:"));
        pnlFormulario.add(txtLicencia);

        JLabel lblTitulo = new JLabel("REGISTRO DE NUEVO CHOFER", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(20, 10, 0, 10));

        btnRegistrar = new JButton("REGISTRAR CHOFER");
        btnRegistrar.setBackground(new Color(16, 185, 129));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        add(lblTitulo, BorderLayout.NORTH);
        add(pnlFormulario, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(5, 8, 15));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        return campo;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(148, 163, 184));
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 11));
        return etiqueta;
    }

    public String getNombres() {
        return txtNombres.getText().trim();
    }

    public String getApellidos() {
        return txtApellidos.getText().trim();
    }

    public String getDni() {
        return txtDni.getText().trim();
    }

    public String getTelefono() {
        return txtTelefono.getText().trim();
    }

    public String getLicencia() {
        return txtLicencia.getText().trim();
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public void limpiarFormulario() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtLicencia.setText("");
        txtNombres.requestFocus();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new RegistrarConductorView().setVisible(true));
    }
}
