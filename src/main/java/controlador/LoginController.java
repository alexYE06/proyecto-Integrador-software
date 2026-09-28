package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.LoginCentralView;
import vista.MonitoreoFlotaView;

public class LoginController implements ActionListener {

    private LoginCentralView view;
    private UsuarioDAO usuarioDAO;

    public LoginController(LoginCentralView view) {
        this.view = view;
        this.usuarioDAO = new UsuarioDAO(); // Instancia el DAO que subió Piero
        this.view.getBtnIngresar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = view.getUsuario();
        String pass = view.getPassword();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamada al método de validación de Piero contra MySQL
        Usuario u = usuarioDAO.validarAcceso(user, pass);

        if (u != null) {
            JOptionPane.showMessageDialog(view,
                    "Bienvenido a la Central de Despacho: " + u.getNombreUsuario(),
                    "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
            new MonitoreoFlotaView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view,
                    "Credenciales incorrectas o usuario no activo.",
                    "Fallo de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}