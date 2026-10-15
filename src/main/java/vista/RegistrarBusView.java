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

import controlador.BusController;

public class RegistrarBusView extends JFrame {

    private JTextField txtPlaca;
    private JTextField txtCodigoUnidad;
    private JTextField txtModelo;
    private JTextField txtCapacidad;
    private JButton btnRegistrar;
    private BusController controller;

    public RegistrarBusView() {
        setTitle("SAT-Carmen - Registrar Bus");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 16, 29));

        initComponentes();
        this.controller = new BusController(this);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel pnlFormulario = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlFormulario.setBackground(new Color(16, 23, 38));
        pnlFormulario.setBorder(new EmptyBorder(25, 30, 20, 30));

        txtPlaca = crearCampo();
        txtCodigoUnidad = crearCampo();
        txtModelo = crearCampo();
        txtCapacidad = crearCampo();

        pnlFormulario.add(crearEtiqueta("PLACA:"));
        pnlFormulario.add(txtPlaca);
        pnlFormulario.add(crearEtiqueta("CÓDIGO UNIDAD (PADRÓN):"));
        pnlFormulario.add(txtCodigoUnidad);
        pnlFormulario.add(crearEtiqueta("MODELO:"));
        pnlFormulario.add(txtModelo);
        pnlFormulario.add(crearEtiqueta("CAPACIDAD (PASAJEROS):"));
        pnlFormulario.add(txtCapacidad);

        JLabel lblTitulo = new JLabel("REGISTRO DE NUEVA UNIDAD", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(20, 10, 0, 10));

        btnRegistrar = new JButton("REGISTRAR BUS");
        btnRegistrar.setBackground(new Color(37, 99, 235)); // Azul para diferenciar del verde de chofer
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

    public String getPlaca() {
        return txtPlaca.getText().trim();
    }

    public String getCodigoUnidad() {
        return txtCodigoUnidad.getText().trim();
    }

    public String getModelo() {
        return txtModelo.getText().trim();
    }

    public String getCapacidad() {
        return txtCapacidad.getText().trim();
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public void limpiarFormulario() {
        txtPlaca.setText("");
        txtCodigoUnidad.setText("");
        txtModelo.setText("");
        txtCapacidad.setText("");
        txtPlaca.requestFocus();
    }
}
