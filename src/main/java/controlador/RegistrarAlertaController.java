package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import dao.AlertaDAO;
import dao.BusDAO;
import dao.ConductorDAO;
import dao.OperadorDAO;
import modelo.Alerta;
import modelo.Bus;
import modelo.Conductor;
import modelo.Operador;
import vista.RegistrarAlertaView;

public class RegistrarAlertaController implements ActionListener {

    private final RegistrarAlertaView view;
    private final AlertaDAO alertaDAO;

    public RegistrarAlertaController(RegistrarAlertaView view) {
        this.view = view;
        this.alertaDAO = new AlertaDAO();
        this.view.getBtnRegistrar().addActionListener(this);

        try {
            List<Conductor> conductores = new ConductorDAO().listarActivos();
            List<Bus> buses = new BusDAO().listarActivos();
            List<Operador> operadores = new OperadorDAO().listarActivos();
            view.cargarOpciones(conductores, buses, operadores);
            if (conductores.isEmpty() || buses.isEmpty()) {
                view.deshabilitarRegistro();
                JOptionPane.showMessageDialog(view,
                        "Se necesita al menos un conductor y un bus activos para registrar una alerta.",
                        "Datos insuficientes", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            view.deshabilitarRegistro();
            JOptionPane.showMessageDialog(view,
                    "No se pudieron cargar conductores, buses y operadores desde MySQL:\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Conductor conductor = view.getConductorSeleccionado();
        Bus bus = view.getBusSeleccionado();
        if (conductor == null || bus == null) {
            JOptionPane.showMessageDialog(view,
                    "Seleccione un conductor y un bus activos.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String descripcion = view.getDescripcion();
        if (descripcion.length() > 255) {
            JOptionPane.showMessageDialog(view,
                    "La descripción no puede superar los 255 caracteres.",
                    "Descripción demasiado larga", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Alerta alerta = new Alerta();
        alerta.setIdConductor(conductor.getIdConductor());
        alerta.setIdBus(bus.getIdBus());
        Operador operador = view.getOperadorSeleccionado();
        alerta.setIdOperador(operador == null ? null : operador.getIdOperador());
        alerta.setTipoActivacion(view.getTipoSeleccionado());
        alerta.setEstado(view.getEstadoSeleccionado());
        alerta.setDescripcion(descripcion.isEmpty() ? null : descripcion);

        int idAlerta = alertaDAO.registrarAlerta(alerta);
        if (idAlerta > 0) {
            JOptionPane.showMessageDialog(view,
                    "Alerta registrada correctamente. ID asignado: " + idAlerta,
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(view,
                    "No se pudo registrar la alerta. Verifique los datos seleccionados y la conexión con MySQL.",
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
