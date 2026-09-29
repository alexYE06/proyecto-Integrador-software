package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import dao.ConductorDAO;
import modelo.Conductor;
import vista.RegistrarConductorView;

public class ConductorController implements ActionListener {

    private final RegistrarConductorView view;
    private final ConductorDAO conductorDAO;

    public ConductorController(RegistrarConductorView view) {
        this.view = view;
        this.conductorDAO = new ConductorDAO();
        this.view.getBtnRegistrar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != view.getBtnRegistrar()) {
            return;
        }

        String nombres = view.getNombres();
        String apellidos = view.getApellidos();
        String dni = view.getDni();
        String telefono = view.getTelefono();
        String licencia = view.getLicencia();

        if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || licencia.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Complete nombres, apellidos, DNI y licencia.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(view,
                    "El DNI debe contener exactamente 8 dígitos.",
                    "DNI inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Conductor conductor = new Conductor();
        conductor.setNombres(nombres);
        conductor.setApellidos(apellidos);
        conductor.setDni(dni);
        conductor.setTelefono(telefono);
        conductor.setLicencia(licencia);
        conductor.setEstado("Activo");

        if (conductorDAO.registrarConductor(conductor)) {
            JOptionPane.showMessageDialog(view,
                    "Chofer registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(view,
                    "No se pudo registrar el chofer. Verifique que el DNI no esté repetido y que MySQL esté disponible.",
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
