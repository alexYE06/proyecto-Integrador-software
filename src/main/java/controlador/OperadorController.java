package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import dao.OperadorDAO;
import modelo.Operador;
import vista.RegistrarOperadorView;

public class OperadorController implements ActionListener {

    private final RegistrarOperadorView view;
    private final OperadorDAO operadorDAO;

    public OperadorController(RegistrarOperadorView view) {
        this.view = view;
        this.operadorDAO = new OperadorDAO();
        this.view.getBtnRegistrar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nombres = view.getNombres();
        String apellidos = view.getApellidos();
        String telefono = view.getTelefono();
        String cargo = view.getCargo();
        String usuario = view.getUsuario();
        String contrasena = view.getContrasena();

        if (nombres.isEmpty() || apellidos.isEmpty() || cargo.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Complete nombres, apellidos, cargo, usuario y contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nombres.length() > 100 || apellidos.length() > 100 || telefono.length() > 15
                || cargo.length() > 50 || usuario.length() > 50 || contrasena.length() > 100) {
            JOptionPane.showMessageDialog(view,
                    "Uno o más campos exceden el máximo permitido por la base de datos.",
                    "Datos demasiado largos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (usuario.length() < 3 || contrasena.length() < 6) {
            JOptionPane.showMessageDialog(view,
                    "El usuario debe tener al menos 3 caracteres y la contraseña al menos 6.",
                    "Credenciales inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Operador operador = new Operador();
        operador.setNombres(nombres);
        operador.setApellidos(apellidos);
        operador.setTelefono(telefono);
        operador.setCargo(cargo);
        operador.setEstado("Activo");

        if (operadorDAO.registrarOperador(operador, usuario, contrasena)) {
            JOptionPane.showMessageDialog(view,
                    "El operador y su cuenta de acceso se registraron correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(view,
                    "No se pudo registrar el operador. Verifique que el usuario no esté repetido y que MySQL esté disponible.",
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
