package vista;

import dao.Consulta02DAO;
import modelo.Consulta02.Catalogos;
import modelo.Consulta02.Filtro;
import modelo.Consulta02.Resultado;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import servicio.Consulta02Service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Consulta 02 multitabla (6 tablas con LEFT JOIN desde alerta) integrada con SQL parametrizado y JasperReports.
 * Tablas: alerta + bus + despacho_auxilio + comisaria + operador + cerco_preventivo.
 */
public class Consulta02 extends JFrame {

    public static final String[] COLUMNAS = Consulta02Service.COLUMNAS;

    private final JTextField desde = new JTextField();
    private final JTextField hasta = new JTextField();
    private final JComboBox<String> estadoAlerta = new JComboBox<>(new String[]{"Todos"});
    private final JComboBox<String> comisaria = new JComboBox<>(new String[]{"Todas", "Sin comisaría"});
    private final JComboBox<String> estadoDespacho = new JComboBox<>(new String[]{"Todos", "Sin despacho"});
    private final JComboBox<String> estadoCerco = new JComboBox<>(new String[]{"Todos", "Sin cerco"});
    private final JTextField bus = new JTextField();
    private final JComboBox<String> modoBus = new JComboBox<>(new String[]{"Contiene", "Empieza en", "Termina en"});
    private final JTextField operador = new JTextField();
    private final JComboBox<String> logica = new JComboBox<>(new String[]{"Todas (Y)", "Cualquiera (O)"});
    private final JComboBox<String> orden = new JComboBox<>(Consulta02Service.ORDENES);
    private final JCheckBox descendente = new JCheckBox("Orden descendente", true);

    private final JButton btnConsultar = crearBoton("Consultar tabla SQL", new Color(15, 23, 42), Color.WHITE);
    private final JButton btnPrevisualizar = crearBoton("Previsualizar JasperReport", new Color(37, 99, 235), Color.WHITE);
    private final JButton btnExportarPdf = crearBoton("Exportar PDF", new Color(225, 29, 72), Color.WHITE);
    private final JButton btnLimpiar = crearBoton("Limpiar filtros", new Color(241, 245, 249), new Color(15, 23, 42));
    private final JLabel mensaje = new JLabel("Cargando catálogos desde MySQL...");

    public static Connection conexion() throws SQLException {
        return conexion.ConexionBD.obtenerConexion();
    }

    public Consulta02() {
        super("SAT-CARMEN | Consulta 02 | Despacho de Auxilio y Cercos Preventivos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 250, 252));
        setLayout(new BorderLayout(0, 0));

        JPanel pnlHeader = new JPanel(new BorderLayout(8, 4));
        pnlHeader.setBackground(new Color(20, 32, 51));
        pnlHeader.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel lblTitulo = new JLabel("SAT-CARMEN · Consulta 02: Despacho de Auxilio y Cercos Preventivos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Consulta SQL de 6 tablas (alerta + bus + despacho_auxilio + comisaria + operador + cerco_preventivo) integrada con JasperReports");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(203, 213, 225));

        pnlHeader.add(lblTitulo, BorderLayout.NORTH);
        pnlHeader.add(lblSubtitulo, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        desde.setToolTipText("Formato AAAA-MM-DD (ejemplo: 2026-09-25). Vacío = sin límite inicial.");
        hasta.setToolTipText("Formato AAAA-MM-DD (incluye todo el día indicado). Vacío = sin límite final.");
        bus.setToolTipText("Placa (ej. ABC-123) o código de unidad (ej. BUS-001).");
        operador.setToolTipText("Nombre o apellido del operador asignado o que despachó el auxilio.");

        JPanel pnlCard = new JPanel(new GridLayout(0, 2, 16, 10));
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(18, 22, 18, 22)
        ));

        agregarCampo(pnlCard, "Desde fecha (AAAA-MM-DD, opcional):", desde);
        agregarCampo(pnlCard, "Hasta fecha (AAAA-MM-DD, inclusivo):", hasta);
        agregarCampo(pnlCard, "Estado de la alerta:", estadoAlerta);
        agregarCampo(pnlCard, "Comisaría asignada:", comisaria);
        agregarCampo(pnlCard, "Estado del despacho de auxilio:", estadoDespacho);
        agregarCampo(pnlCard, "Estado del cerco preventivo:", estadoCerco);
        agregarCampo(pnlCard, "Placa o unidad del bus:", bus);
        agregarCampo(pnlCard, "Coincidencia de placa / unidad:", modoBus);
        agregarCampo(pnlCard, "Nombre del operador contiene:", operador);
        agregarCampo(pnlCard, "Combinar filtros activos:", logica);
        agregarCampo(pnlCard, "Ordenar resultados por:", orden);

        descendente.setOpaque(false);
        descendente.setFont(new Font("SansSerif", Font.BOLD, 12));
        descendente.setForeground(new Color(51, 65, 85));
        agregarCampo(pnlCard, "Dirección de ordenamiento:", descendente);

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(16, 24, 10, 24));
        pnlCentro.add(pnlCard, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnConsultar);
        pnlBotones.add(btnPrevisualizar);
        pnlBotones.add(btnExportarPdf);
        pnlBotones.add(btnLimpiar);

        mensaje.setFont(new Font("SansSerif", Font.ITALIC, 12));
        mensaje.setForeground(new Color(71, 85, 105));

        JPanel pnlPie = new JPanel(new BorderLayout(8, 10));
        pnlPie.setOpaque(false);
        pnlPie.setBorder(new EmptyBorder(4, 24, 18, 24));
        pnlPie.add(pnlBotones, BorderLayout.NORTH);
        pnlPie.add(mensaje, BorderLayout.SOUTH);
        add(pnlPie, BorderLayout.SOUTH);

        btnLimpiar.addActionListener(e -> limpiarFiltros());
        btnConsultar.addActionListener(e -> buscarTablaSql());
        btnPrevisualizar.addActionListener(e -> previsualizarJasper(this, leerFiltro()));
        btnExportarPdf.addActionListener(e -> exportarPdfJasper(this, leerFiltro()));

        setSize(900, 680);
        setLocationRelativeTo(null);
        cargarCatalogosIniciales();
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(30, 41, 59));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lbl);
        panel.add(campo);
    }

    private static JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarCatalogosIniciales() {
        new SwingWorker<Catalogos, Void>() {
            @Override
            protected Catalogos doInBackground() throws Exception {
                return Consulta02DAO.cargarCatalogos();
            }

            @Override
            protected void done() {
                try {
                    Catalogos cat = get();
                    for (String ea : cat.estadosAlerta()) {
                        estadoAlerta.addItem(ea);
                    }
                    for (String c : cat.comisarias()) {
                        comisaria.addItem(c);
                    }
                    for (String est : cat.estadosDespacho()) {
                        estadoDespacho.addItem(est);
                    }
                    for (String ec : cat.estadosCerco()) {
                        estadoCerco.addItem(ec);
                    }
                    mensaje.setText("Conexión lista. Incluye todas las alertas (con o sin despacho/cerco). Usa 'Cualquiera (O)' para ampliar resultados.");
                } catch (Exception ex) {
                    mensaje.setText("No se cargaron catálogos iniciales. Verifica la conexión MySQL.");
                }
            }
        }.execute();
    }

    private void limpiarFiltros() {
        desde.setText("");
        hasta.setText("");
        estadoAlerta.setSelectedIndex(0);
        comisaria.setSelectedIndex(0);
        estadoDespacho.setSelectedIndex(0);
        estadoCerco.setSelectedIndex(0);
        bus.setText("");
        modoBus.setSelectedIndex(0);
        operador.setText("");
        logica.setSelectedIndex(0);
        orden.setSelectedIndex(0);
        descendente.setSelected(true);
        mensaje.setText("Filtros restablecidos.");
    }

    private Filtro leerFiltro() {
        return new Filtro(
                desde.getText().trim(),
                hasta.getText().trim(),
                (String) estadoAlerta.getSelectedItem(),
                (String) comisaria.getSelectedItem(),
                (String) estadoDespacho.getSelectedItem(),
                (String) estadoCerco.getSelectedItem(),
                bus.getText().trim(),
                (String) modoBus.getSelectedItem(),
                operador.getText().trim(),
                logica.getSelectedIndex() == 1,
                orden.getSelectedIndex(),
                descendente.isSelected()
        );
    }

    private boolean validarFiltro(Component padre, Filtro f) {
        try {
            Consulta02DAO.construir(f);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    padre,
                    "Revisa el formato de fechas (AAAA-MM-DD) y el intervalo.\n" + e.getMessage(),
                    "Filtro inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
    }

    private void bloquearBotones(boolean ocupado, String estado) {
        btnConsultar.setEnabled(!ocupado);
        btnPrevisualizar.setEnabled(!ocupado);
        btnExportarPdf.setEnabled(!ocupado);
        btnLimpiar.setEnabled(!ocupado);
        mensaje.setText(estado);
        setCursor(ocupado ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    private void buscarTablaSql() {
        Filtro f = leerFiltro();
        if (!validarFiltro(this, f)) {
            return;
        }
        bloquearBotones(true, "Ejecutando consulta multitabla en MySQL...");
        new SwingWorker<Resultado, Void>() {
            @Override
            protected Resultado doInBackground() throws Exception {
                return Consulta02DAO.ejecutar(f);
            }

            @Override
            protected void done() {
                bloquearBotones(false, "Listo.");
                try {
                    Resultado r = get();
                    new Resultados(r).setVisible(true);
                    mensaje.setText("Consulta completada: " + r.filas().size() + " registros encontrados.");
                } catch (Exception ex) {
                    Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                    mensaje.setText("No se pudo ejecutar la consulta.");
                    JOptionPane.showMessageDialog(
                            Consulta02.this,
                            "Revisa la conexión a MySQL, base de datos y permisos.\n" + causa.getMessage(),
                            "Error de consulta SQL",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    private void previsualizarJasper(Component padre, Filtro f) {
        if (!validarFiltro(padre, f)) {
            return;
        }
        bloquearBotones(true, "Compilando Consulta02.jrxml y consultando MySQL...");
        new SwingWorker<JasperPrint, Void>() {
            @Override
            protected JasperPrint doInBackground() throws Exception {
                return Consulta02Service.generarReporte(f);
            }

            @Override
            protected void done() {
                bloquearBotones(false, "Vista previa generada.");
                try {
                    JasperPrint print = get();
                    JasperViewer viewer = new JasperViewer(print, false);
                    viewer.setTitle("SAT-CARMEN | Consulta 02 - JasperReport (Vista previa y PDF)");
                    viewer.setVisible(true);
                } catch (Exception ex) {
                    Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                    mensaje.setText("Error al generar reporte Jasper.");
                    JOptionPane.showMessageDialog(
                            padre,
                            "No se pudo abrir la vista previa de JasperReports:\n" + causa.getMessage(),
                            "Error JasperReports",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    private void exportarPdfJasper(Component padre, Filtro f) {
        if (!validarFiltro(padre, f)) {
            return;
        }
        Path destino = seleccionarDestinoPdf(padre);
        if (destino == null) {
            mensaje.setText("Exportación cancelada.");
            return;
        }
        bloquearBotones(true, "Exportando PDF mediante JasperReports...");
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Consulta02Service.pdf(f, destino);
                return null;
            }

            @Override
            protected void done() {
                bloquearBotones(false, "PDF exportado correctamente.");
                try {
                    get();
                    JOptionPane.showMessageDialog(
                            padre,
                            "PDF generado con JasperReports y guardado en:\n" + destino.toAbsolutePath(),
                            "Exportación PDF exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                    mensaje.setText("Error al exportar PDF.");
                    JOptionPane.showMessageDialog(
                            padre,
                            "No se pudo guardar el PDF:\n" + causa.getMessage(),
                            "Error al exportar PDF",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    private static Path seleccionarDestinoPdf(Component padre) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Exportar Consulta 02 a PDF (JasperReports)");
        fc.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));
        fc.setSelectedFile(new File("Consulta02_Despacho_Cercos.pdf"));
        if (fc.showSaveDialog(padre) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        Path p = fc.getSelectedFile().toPath();
        if (!p.toString().toLowerCase().endsWith(".pdf")) {
            p = Path.of(p + ".pdf");
        }
        if (Files.exists(p) && JOptionPane.showConfirmDialog(
                padre,
                "¿Deseas reemplazar el archivo PDF existente?",
                "Archivo existente",
                JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return null;
        }
        return p;
    }

    public static class Resultados extends JFrame {

        public Resultados(Resultado r) {
            super("SAT-CARMEN | Consulta 02 | Resultados SQL y Exportación JasperReports");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout(0, 0));

            DefaultTableModel model = new DefaultTableModel(COLUMNAS, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            for (Object[] fila : r.filas()) {
                model.addRow(fila);
            }

            JTable tabla = new JTable(model);
            tabla.setRowHeight(28);
            tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
            tabla.setFillsViewportHeight(true);
            tabla.setGridColor(new Color(226, 232, 240));
            tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
            tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));

            int[] anchos = {65, 145, 135, 115, 220, 185, 115, 115};
            for (int i = 0; i < anchos.length && i < tabla.getColumnModel().getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            }

            JPanel pnlCabecera = new JPanel(new BorderLayout(4, 4));
            pnlCabecera.setBackground(new Color(233, 240, 246));
            pnlCabecera.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(203, 213, 225)),
                    new EmptyBorder(12, 18, 12, 18)
            ));
            JLabel lblResumen = new JLabel(Consulta02Service.resumen(r.filtro()));
            lblResumen.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblResumen.setForeground(new Color(30, 41, 59));
            JLabel lblConteo = new JLabel(
                    r.filas().size() + " registros encontrados"
                    + (r.filas().isEmpty() ? " — Sin coincidencias para los filtros seleccionados." : " (6 tablas SQL con LEFT JOIN sincronizadas con JasperReports).")
            );
            lblConteo.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblConteo.setForeground(new Color(20, 32, 51));
            pnlCabecera.add(lblResumen, BorderLayout.NORTH);
            pnlCabecera.add(lblConteo, BorderLayout.SOUTH);

            add(pnlCabecera, BorderLayout.NORTH);
            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(new EmptyBorder(12, 16, 8, 16));
            scroll.getViewport().setBackground(Color.WHITE);
            add(scroll, BorderLayout.CENTER);

            JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
            botones.setBackground(new Color(248, 250, 252));

            JButton btnPreview = crearBoton("Previsualizar JasperReport", new Color(37, 99, 235), Color.WHITE);
            JButton btnPdf = crearBoton("Exportar PDF", new Color(225, 29, 72), Color.WHITE);
            JButton btnSql = crearBoton("Ver SQL y parámetros", new Color(15, 23, 42), Color.WHITE);
            JButton btnCerrar = crearBoton("Volver a filtros", new Color(226, 232, 240), new Color(15, 23, 42));

            botones.add(btnPreview);
            botones.add(btnPdf);
            botones.add(btnSql);
            botones.add(btnCerrar);
            add(botones, BorderLayout.SOUTH);

            btnCerrar.addActionListener(e -> dispose());

            btnSql.addActionListener(e -> {
                JTextArea areaSql = new JTextArea(
                        r.sentencia().sql() + "\n\nParámetros: " + r.sentencia().valores(),
                        14,
                        78
                );
                areaSql.setLineWrap(true);
                areaSql.setWrapStyleWord(true);
                areaSql.setEditable(false);
                JOptionPane.showMessageDialog(
                        this,
                        new JScrollPane(areaSql),
                        "Consulta 02 parametrizada (6 tablas)",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });

            btnPreview.addActionListener(e -> {
                btnPreview.setEnabled(false);
                new SwingWorker<JasperPrint, Void>() {
                    @Override
                    protected JasperPrint doInBackground() throws Exception {
                        return Consulta02Service.generarReporte(r.filtro());
                    }

                    @Override
                    protected void done() {
                        btnPreview.setEnabled(true);
                        try {
                            JasperPrint print = get();
                            JasperViewer viewer = new JasperViewer(print, false);
                            viewer.setTitle("SAT-CARMEN | Consulta 02 - JasperReport");
                            viewer.setVisible(true);
                        } catch (Exception ex) {
                            Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                            JOptionPane.showMessageDialog(
                                    Resultados.this,
                                    "No se pudo abrir JasperViewer:\n" + causa.getMessage(),
                                    "Error JasperReports",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }.execute();
            });

            btnPdf.addActionListener(e -> {
                Path destino = seleccionarDestinoPdf(this);
                if (destino == null) {
                    return;
                }
                btnPdf.setEnabled(false);
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Consulta02Service.pdf(r.filtro(), destino);
                        return null;
                    }

                    @Override
                    protected void done() {
                        btnPdf.setEnabled(true);
                        try {
                            get();
                            JOptionPane.showMessageDialog(
                                    Resultados.this,
                                    "PDF generado con JasperReports y guardado en:\n" + destino.toAbsolutePath(),
                                    "Exportación PDF exitosa",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } catch (Exception ex) {
                            Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                            JOptionPane.showMessageDialog(
                                    Resultados.this,
                                    "No se pudo guardar el PDF:\n" + causa.getMessage(),
                                    "Error al exportar PDF",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }.execute();
            });

            setSize(1260, 650);
            setLocationRelativeTo(null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Consulta02().setVisible(true));
    }
}
