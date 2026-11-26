package vista;

import dao.Consulta03DAO;
import modelo.Consulta03.Filtro;
import modelo.Consulta03.Resultado;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import servicio.Consulta03Service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

/**
 * Consulta 03: resumen diario y alertas distribuidas por unidad de bus.
 */
public class Consulta03 extends JFrame {
    private final JTextField desde = new JTextField(12);
    private final JTextField hasta = new JTextField(12);
    private final JLabel mensaje = new JLabel("Indica fechas opcionales y consulta las alertas por bus.");
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID bus", "Unidad", "Placa", "Total", "Atendidas", "Falsas alarmas", "Auxilio despachado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final JButton consultar = boton("Consultar", new Color(219, 234, 254));
    private final JButton previsualizar = boton("Previsualizar", new Color(226, 232, 240));
    private final JButton exportar = boton("Exportar PDF", new Color(254, 226, 226));
    private final JButton limpiar = boton("Limpiar", new Color(241, 245, 249));

    public Consulta03() {
        super("SAT-CARMEN | Consulta 03 | Alertas por bus");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(980, 620));
        setLayout(new BorderLayout(0, 0));

        JPanel encabezado = new JPanel(new BorderLayout(6, 5));
        encabezado.setBackground(new Color(20, 32, 51));
        encabezado.setBorder(new EmptyBorder(18, 24, 18, 24));
        JLabel titulo = new JLabel("SAT-CARMEN · Consulta 03: análisis diario y alertas por bus");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 19));
        JLabel subtitulo = new JLabel("Compara la tendencia diaria y la distribución de estados por unidad.");
        subtitulo.setForeground(new Color(203, 213, 225));
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);

        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setBackground(Color.WHITE);
        filtros.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(226, 232, 240)),
                new EmptyBorder(12, 16, 12, 16)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        filtros.add(new JLabel("Fecha inicial (AAAA-MM-DD)"), c);
        c.gridx = 1;
        filtros.add(new JLabel("Fecha final, inclusive (AAAA-MM-DD)"), c);
        c.gridy = 1; c.gridx = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        filtros.add(desde, c);
        c.gridx = 1;
        filtros.add(hasta, c);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        acciones.setOpaque(false);
        acciones.add(consultar);
        acciones.add(previsualizar);
        acciones.add(exportar);
        acciones.add(limpiar);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        filtros.add(acciones, c);

        tabla.setRowHeight(28);
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getTableHeader().setReorderingAllowed(false);
        int[] anchos = {70, 145, 135, 85, 120, 135, 160};
        for (int i = 0; i < anchos.length; i++) tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBackground(new Color(248, 250, 252));
        centro.setBorder(new EmptyBorder(18, 22, 12, 22));
        centro.add(filtros, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        mensaje.setBorder(new EmptyBorder(0, 24, 16, 24));
        mensaje.setFont(new Font("SansSerif", Font.ITALIC, 12));
        mensaje.setForeground(new Color(71, 85, 105));
        add(mensaje, BorderLayout.SOUTH);

        consultar.addActionListener(e -> consultar());
        previsualizar.addActionListener(e -> abrirJasper());
        exportar.addActionListener(e -> exportarPdf());
        limpiar.addActionListener(e -> {
            desde.setText("");
            hasta.setText("");
            consultar();
        });
        setSize(1120, 700);
        setLocationRelativeTo(null);
    }

    private Filtro filtro() {
        Filtro f = new Filtro(desde.getText().trim(), hasta.getText().trim());
        Consulta03DAO.validar(f);
        return f;
    }

    private void consultar() {
        final Filtro f;
        try {
            f = filtro();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Revisa las fechas. Usa AAAA-MM-DD.\n" + ex.getMessage(), "Filtro inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        consultar.setEnabled(false);
        mensaje.setText("Consultando alertas por bus...");
        new SwingWorker<Resultado, Void>() {
            @Override
            protected Resultado doInBackground() throws Exception {
                return Consulta03DAO.ejecutar(f);
            }

            @Override
            protected void done() {
                consultar.setEnabled(true);
                try {
                    Resultado r = get();
                    modelo.setRowCount(0);
                    for (Object[] fila : r.filas()) modelo.addRow(fila);
                    mensaje.setText("Consulta completada: " + r.filas().size() + " buses con alertas.");
                } catch (Exception ex) {
                    mostrarError("No se pudo consultar MySQL", ex);
                }
            }
        }.execute();
    }

    private void abrirJasper() {
        final Filtro f;
        try {
            f = filtro();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Filtro inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        bloquear(true, "Generando vista Jasper...");
        new SwingWorker<JasperPrint, Void>() {
            @Override
            protected JasperPrint doInBackground() throws Exception {
                return Consulta03Service.generar(f);
            }

            @Override
            protected void done() {
                bloquear(false, "Listo.");
                try {
                    JasperViewer viewer = new JasperViewer(get(), false);
                    viewer.setTitle("Consulta 03 - Alertas por bus");
                    viewer.setVisible(true);
                } catch (Exception ex) {
                    mostrarError("No se pudo generar la vista Jasper", ex);
                }
            }
        }.execute();
    }

    private void exportarPdf() {
        final Filtro f;
        try {
            f = filtro();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Filtro inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar Consulta 03 como PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));
        chooser.setSelectedFile(new File("Consulta03_Alertas_por_bus.pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf")) file = new File(file.getAbsolutePath() + ".pdf");
        final Path destino = file.toPath();
        if (file.exists() && JOptionPane.showConfirmDialog(this, "¿Reemplazar el archivo?", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        bloquear(true, "Exportando PDF...");
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Consulta03Service.pdf(f, destino);
                return null;
            }

            @Override
            protected void done() {
                bloquear(false, "Listo.");
                try {
                    get();
                    JOptionPane.showMessageDialog(Consulta03.this, "PDF guardado en:\n" + destino.toAbsolutePath());
                } catch (Exception ex) {
                    mostrarError("No se pudo guardar el PDF", ex);
                }
            }
        }.execute();
    }

    private void bloquear(boolean activo, String texto) {
        consultar.setEnabled(!activo);
        previsualizar.setEnabled(!activo);
        exportar.setEnabled(!activo);
        limpiar.setEnabled(!activo);
        mensaje.setText(texto);
    }

    private void mostrarError(String titulo, Exception ex) {
        Throwable causa = ex.getCause() == null ? ex : ex.getCause();
        mensaje.setText(titulo + ".");
        JOptionPane.showMessageDialog(this, titulo + ":\n" + causa.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setBackground(fondo);
        b.setForeground(Color.BLACK);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225)), new EmptyBorder(7, 12, 7, 12)));
        b.setPreferredSize(new Dimension(132, 36));
        b.setFocusPainted(false);
        return b;
    }
}
