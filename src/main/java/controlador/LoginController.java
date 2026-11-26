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
        Usuario u;
        try {
            u = usuarioDAO.validarAcceso(user, pass);
        } catch (IllegalStateException ex) {
            String mensaje = "No se pudo leer la base de datos. Tu cuenta del programa no ha sido comprobada.";
            if (ex.getCause() instanceof java.sql.SQLException sql) {
                String estado = sql.getSQLState();
                if ("28000".equals(estado)) {
                    mensaje = "MySQL no aceptó la clave de conexión.\n"
                            + "Pulsa Configurar MySQL para corregir la conexión guardada en este equipo.\n"
                            + "Esto no significa que la cuenta operador1 esté mal.";
                } else if (estado != null && estado.startsWith("08")) {
                    mensaje = "No se pudo comunicar con MySQL.\n"
                            + "Comprueba que puedes abrir tu conexión en MySQL Workbench.\n"
                            + "Tu cuenta del programa todavía no ha podido comprobarse.";
                }
                mensaje += "\nCódigo de diagnóstico: " + sql.getErrorCode() + " / " + estado;
            }
            int opcion = JOptionPane.showOptionDialog(view, mensaje, "No se pudo abrir MySQL", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"Configurar MySQL", "Cerrar"}, "Configurar MySQL");
            if (opcion == 0) new vista.ConfiguracionBDView(view).setVisible(true);
            return;
        }

        if (u != null) {
            JOptionPane.showMessageDialog(view,
                    "Bienvenido a la Central de Despacho: " + u.getNombreUsuario(),
                    "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
            new vista.MainCentralFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view,
                    "Se pudo leer MySQL, pero el usuario o la contraseña no coinciden, o la cuenta está inactiva.",
                    "Fallo de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
