package vista;

import controlador.ReporteControlador;
import modelo.Bus;
import modelo.FiltroReporte;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import servicio.ReporteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReportesView extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ReporteControlador controlador = new ReporteControlador();

    private JComboBox<String> cbTipoReporte;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> cbEstado;
    private JComboBox<BusItem> cbBus;
    private JComboBox<String> cbModalidad;
    private JButton btnPrevisualizar;
    private JButton btnGuardarPdf;
    private JButton btnLimpiar;
    private JLabel lblEstadoProceso;

    public ReportesView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initComponentes();
        cargarBuses();
    }

    private void initComponentes() {
        // Cabecera
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTitulos.setOpaque(false);
        JLabel lblTitulo = new JLabel("Generación de Reportes PDF");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));

        JLabel lblSubtitulo = new JLabel("Seleccione el tipo de reporte y los filtros dinámicos antes de previsualizar o exportar.");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(100, 116, 139));

        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSubtitulo);
        pnlHeader.add(pnlTitulos, BorderLayout.WEST);

        JButton btnVolver = new JButton("← Volver a Monitoreo");
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> MainCentralFrame.getInstance().cambiarPantalla("Monitoreo"));
        pnlHeader.add(btnVolver, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // Tarjeta central de filtros
        JPanel pnlCard = new JPanel(new GridBagLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        cbTipoReporte = new JComboBox<>(new String[]{
                ReporteService.REPORTE_INCIDENCIAS,
                ReporteService.REPORTE_ESTADOS,
                ReporteService.REPORTE_MODALIDADES
        });

        txtFechaInicio = new JTextField();
        txtFechaInicio.setToolTipText("Formato dd/MM/yyyy (ej. 01/11/2026). Dejar vacío para sin límite.");
        txtFechaFin = new JTextField();
        txtFechaFin.setToolTipText("Formato dd/MM/yyyy (ej. 12/11/2026). Dejar vacío para sin límite.");

        cbEstado = new JComboBox<>(new String[]{
                "Todos", "Recibida", "En Evaluación", "Auxilio Despachado", "Atendida", "Falsa Alarma"
        });

        cbBus = new JComboBox<>();

        cbModalidad = new JComboBox<>(new String[]{
                "Todas",
                "Botón de pánico oculto",
                "Tres pulsaciones botón",
                "Llamada de emergencia",
                "Parada no autorizada",
                "Desvío de ruta crítico"
        });

        agregarCampo(pnlCard, gbc, 0, 0, "Tipo de reporte:", cbTipoReporte);
        agregarCampo(pnlCard, gbc, 0, 1, "Estado de alerta:", cbEstado);
        agregarCampo(pnlCard, gbc, 1, 0, "Fecha inicial (dd/MM/yyyy):", txtFechaInicio);
        agregarCampo(pnlCard, gbc, 1, 1, "Fecha final (dd/MM/yyyy):", txtFechaFin);
        agregarCampo(pnlCard, gbc, 2, 0, "Bus (Unidad / Placa):", cbBus);
        agregarCampo(pnlCard, gbc, 2, 1, "Modalidad de activación:", cbModalidad);

        // Barra de acciones dentro de la tarjeta
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlAcciones.setOpaque(false);

        btnPrevisualizar = crearBoton("Previsualizar Reporte", new Color(37, 99, 235), Color.WHITE);
        btnGuardarPdf = crearBoton("Guardar PDF...", new Color(225, 29, 72), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar filtros", new Color(241, 245, 249), new Color(15, 23, 42));

        btnPrevisualizar.addActionListener(e -> ejecutarPrevisualizacion());
        btnGuardarPdf.addActionListener(e -> ejecutarGuardadoPdf());
        btnLimpiar.addActionListener(e -> limpiarFiltros());

        pnlAcciones.add(btnPrevisualizar);
        pnlAcciones.add(btnGuardarPdf);
        pnlAcciones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        pnlCard.add(pnlAcciones, gbc);

        lblEstadoProceso = new JLabel("Listo para generar reportes.");
        lblEstadoProceso.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblEstadoProceso.setForeground(new Color(100, 116, 139));
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 10, 0, 10);
        pnlCard.add(lblEstadoProceso, gbc);

        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.add(pnlCard, BorderLayout.NORTH);
        add(pnlWrapper, BorderLayout.CENTER);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, int col, String etiqueta, JComponent comp) {
        gbc.gridx = col;
        gbc.gridy = fila * 2;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        panel.add(lbl, gbc);

        gbc.gridy = fila * 2 + 1;
        comp.setPreferredSize(new Dimension(260, 34));
        comp.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(comp, gbc);
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarBuses() {
        cbBus.removeAllItems();
        cbBus.addItem(new BusItem(null, "Todos"));
        List<Bus> buses = controlador.listarBusesActivos();
        for (Bus b : buses) {
            cbBus.addItem(new BusItem(b.getIdBus(), b.getNumeroUnidad() + " (" + b.getPlaca() + ")"));
        }
    }

    private void limpiarFiltros() {
        cbTipoReporte.setSelectedIndex(0);
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        cbEstado.setSelectedIndex(0);
        cbBus.setSelectedIndex(0);
        cbModalidad.setSelectedIndex(0);
        lblEstadoProceso.setText("Filtros restablecidos.");
    }

    private FiltroReporte construirFiltro() {
        LocalDate fInicio = parsearFecha(txtFechaInicio.getText().trim(), "Fecha inicial");
        LocalDate fFin = parsearFecha(txtFechaFin.getText().trim(), "Fecha final");

        String estadoSel = (String) cbEstado.getSelectedItem();
        String estado = "Todos".equals(estadoSel) ? null : estadoSel;

        BusItem busSel = (BusItem) cbBus.getSelectedItem();
        Integer idBus = (busSel != null) ? busSel.id() : null;

        String modSel = (String) cbModalidad.getSelectedItem();
        String modalidad = "Todas".equals(modSel) ? null : modSel;

        FiltroReporte filtro = new FiltroReporte(fInicio, fFin, estado, idBus, modalidad);
        filtro.validar();
        return filtro;
    }

    private LocalDate parsearFecha(String texto, String nombreCampo) {
        if (texto.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(nombreCampo + " inválida. Usa el formato dd/MM/yyyy (ej. 11/11/2026).");
        }
    }

    private void ejecutarPrevisualizacion() {
        FiltroReporte filtro;
        try {
            filtro = construirFiltro();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Filtro inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = (String) cbTipoReporte.getSelectedItem();
        bloquearControles(true, "Generando vista previa de " + tipo + "...");

        new SwingWorker<JasperPrint, Void>() {
            @Override
            protected JasperPrint doInBackground() throws Exception {
                return controlador.generar(tipo, filtro);
            }

            @Override
            protected void done() {
                bloquearControles(false, "Listo.");
                try {
                    JasperPrint print = get();
                    if (print.getPages().isEmpty()) {
                        JOptionPane.showMessageDialog(ReportesView.this,
                                "El reporte se generó sin registros para los filtros seleccionados.",
                                "Sin resultados",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    JasperViewer viewer = new JasperViewer(print, false);
                    viewer.setTitle("Reporte de " + tipo + " - SAT-Carmen");
                    viewer.setVisible(true);
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        }.execute();
    }

    private void ejecutarGuardadoPdf() {
        FiltroReporte filtro;
        try {
            filtro = construirFiltro();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Filtro inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = (String) cbTipoReporte.getSelectedItem();
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));
        chooser.setSelectedFile(new File("reporte_" + tipo.toLowerCase() + ".pdf"));

        int opcion = chooser.showSaveDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            lblEstadoProceso.setText("Guardado cancelado por el usuario.");
            return;
        }

        File seleccionado = chooser.getSelectedFile();
        File destino = seleccionado.getName().toLowerCase().endsWith(".pdf")
                ? seleccionado
                : new File(seleccionado.getAbsolutePath() + ".pdf");

        bloquearControles(true, "Exportando PDF a " + destino.getName() + "...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                controlador.guardarPdf(tipo, filtro, destino);
                return null;
            }

            @Override
            protected void done() {
                bloquearControles(false, "Listo.");
                try {
                    get();
                    JOptionPane.showMessageDialog(ReportesView.this,
                            "PDF guardado correctamente en:\n" + destino.getAbsolutePath(),
                            "Exportación exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        }.execute();
    }

    private void bloquearControles(boolean ocupado, String mensaje) {
        btnPrevisualizar.setEnabled(!ocupado);
        btnGuardarPdf.setEnabled(!ocupado);
        btnLimpiar.setEnabled(!ocupado);
        lblEstadoProceso.setText(mensaje);
        setCursor(ocupado ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    private void mostrarError(Exception ex) {
        Throwable causa = (ex.getCause() != null) ? ex.getCause() : ex;
        causa.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "No se pudo completar el reporte:\n" + causa.getMessage(),
                "Error al generar reporte",
                JOptionPane.ERROR_MESSAGE);
    }

    private record BusItem(Integer id, String etiqueta) {
        @Override
        public String toString() {
            return etiqueta;
        }
    }
}