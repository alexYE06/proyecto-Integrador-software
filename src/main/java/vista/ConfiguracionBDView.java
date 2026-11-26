package vista;

import conexion.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Properties;

/** Configura la conexión una vez, desde cualquier editor o forma de inicio. */
public final class ConfiguracionBDView extends JDialog {
    public ConfiguracionBDView(Window owner) {
        super(owner, "Conectar SAT-CARMEN con MySQL", ModalityType.APPLICATION_MODAL);
        Properties actual = ConexionBD.configuracion();
        JPanel fields = new JPanel(new GridLayout(0, 2, 10, 10));
        fields.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        String[] keys = {"DB_HOST", "DB_PORT", "DB_NAME", "DB_USER", "DB_PASSWORD"};
        String[] labels = {"Equipo", "Puerto", "Base de datos", "Usuario de MySQL", "Contraseña de MySQL"};
        JTextField[] inputs = new JTextField[keys.length];
        for (int i = 0; i < keys.length; i++) {
            inputs[i] = i == 4 ? new JPasswordField(24) : new JTextField(24);
            inputs[i].setText(actual.getProperty(keys[i], ""));
            fields.add(new JLabel(labels[i])); fields.add(inputs[i]);
        }
        JLabel status = new JLabel("Se guarda solo en este equipo, fuera de GitHub.");
        JButton save = new JButton("Probar y guardar");
        JButton cancel = new JButton("Cancelar");
        cancel.addActionListener(e -> dispose());
        JPanel actions = new JPanel(); actions.add(save); actions.add(cancel);
        add(fields, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));
        bottom.add(status, BorderLayout.NORTH); bottom.add(actions, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
        save.addActionListener(e -> {
            Properties next = new Properties(); next.putAll(actual);
            for (int i = 0; i < keys.length; i++) next.setProperty(keys[i], i == 4 ? new String(((JPasswordField) inputs[i]).getPassword()) : inputs[i].getText().trim());
            try {
                int port = Integer.parseInt(next.getProperty("DB_PORT"));
                if (port < 1 || port > 65535 || !next.getProperty("DB_NAME").matches("[a-zA-Z0-9_]+")) throw new IllegalArgumentException();
            } catch (Exception ex) { status.setText("Revisa el puerto y el nombre de la base de datos."); return; }
            save.setEnabled(false); cancel.setEnabled(false); status.setText("Comprobando conexión...");
            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    try (Connection c = ConexionBD.conectar(next); var s = c.prepareStatement("SELECT id_usuario FROM usuario LIMIT 1")) { s.executeQuery().close(); }
                    ConexionBD.guardar(next); return null;
                }
                protected void done() {
                    save.setEnabled(true); cancel.setEnabled(true);
                    try { get(); dispose(); }
                    catch (Exception ex) {
                        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                        String help = "No se pudo guardar la configuración local.";
                        if (cause instanceof java.sql.SQLException sql) {
                            help = switch (sql.getErrorCode()) {
                                case 1045 -> "MySQL rechazó el usuario o la contraseña (1045). Usa la clave de Workbench, no op123.";
                                case 1049 -> "No existe esa base de datos en este puerto (1049).";
                                case 1146 -> "La base existe, pero le falta la tabla usuario (1146).";
                                default -> "Conexión fallida: " + sql.getMessage();
                            };
                        }
                        status.setText("No se guardó. Puedes corregir los datos y volver a probar.");
                        JOptionPane.showMessageDialog(ConfiguracionBDView.this, help, "Detalle de conexión", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        });
        pack(); setResizable(false); setLocationRelativeTo(owner);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConfiguracionBDView(null).setVisible(true));
    }
}
