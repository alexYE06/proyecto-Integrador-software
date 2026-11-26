package app;

import conexion.ConexionBD;
import vista.ConfiguracionBDView;
import vista.DemoSelectorView;
import javax.swing.SwingUtilities;
import java.nio.file.Files;

/** Punto de inicio del proyecto para VS Code, otros IDE y el acceso directo. */
public final class SatCarmen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (!Files.exists(ConexionBD.archivoLocal()) && System.getenv("DB_PASSWORD") == null && System.getProperty("DB_PASSWORD") == null) {
                new ConfiguracionBDView(null).setVisible(true);
                if (!Files.exists(ConexionBD.archivoLocal())) return;
            }
            new DemoSelectorView().setVisible(true);
        });
    }
}
