import dao.Consulta02DAO;
import modelo.Consulta02;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import servicio.Consulta02Service;
import vista.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class UiSmoke {
    private static void capture(Component c, String fileName) throws Exception {
        c.validate();
        if (c instanceof Window w) {
            w.validate();
        }
        BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = im.createGraphics();
        c.printAll(g);
        g.dispose();
        ImageIO.write(im, "png", new File("target/evidencias/" + fileName));
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("target/evidencias"));
        Consulta02.Filtro filtroDemo = new Consulta02.Filtro("", "", "Todos", "Todas", "Todos", "Todos", "", "Contiene", "", false, 0, true);
        Consulta02.Resultado res02 = Consulta02DAO.ejecutar(filtroDemo);
        JasperPrint print02 = Consulta02Service.generarReporte(filtroDemo);
        Consulta02Service.pdf(filtroDemo, Path.of("target/evidencias/Consulta02-datos_prueba.pdf"));

        final vista.Consulta02[] holder = new vista.Consulta02[1];
        SwingUtilities.invokeAndWait(() -> {
            try {
                MainCentralFrame frame = new MainCentralFrame();
                frame.setExtendedState(JFrame.NORMAL);
                frame.setSize(1366, 768);
                frame.setVisible(true);
                for (String name : new String[]{"Monitoreo", "Reportes", "Gestion"}) {
                    frame.cambiarPantalla(name);
                    capture(frame, "UI-" + name + ".png");
                }
                for (int i = 0; i < frame.getJMenuBar().getMenuCount(); i++) {
                    JMenu menu = frame.getJMenuBar().getMenu(i);
                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem item = menu.getItem(j);
                        if (item != null) {
                            item.doClick();
                            System.out.println("PASS menu: " + menu.getText() + " -> " + item.getText());
                        }
                    }
                }
                for (Window w : Window.getWindows()) {
                    w.dispose();
                }

                holder[0] = new vista.Consulta02();
                holder[0].setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(350);
        SwingUtilities.invokeAndWait(() -> {
            try {
                capture(holder[0], "UI-Consulta02-Filtros.png");

                vista.Consulta02.Resultados resultados = new vista.Consulta02.Resultados(res02);
                resultados.setVisible(true);
                capture(resultados, "UI-Consulta02-Resultados.png");

                JasperViewer viewer = new JasperViewer(print02, false);
                viewer.setSize(1200, 760);
                viewer.setVisible(true);
                capture(viewer, "UI-Consulta02-JasperViewer.png");

                for (Window w : Window.getWindows()) {
                    w.dispose();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        System.exit(0);
    }
}
