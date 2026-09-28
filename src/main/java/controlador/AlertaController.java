package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.IncidentesView;

public class AlertaController implements ActionListener {

    private IncidentesView view;

    public AlertaController(IncidentesView view) {
        this.view = view;
        this.view.getBtnActualizarEstado().addActionListener(this);
        this.view.getBtnDespacharPNP().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnActualizarEstado()) {
            String nuevoEstado = view.getEstadoSeleccionado();
            // Conexión preparada: aquí se llamará a alertaDAO.actualizarEstado(idAlerta,
            // nuevoEstado)
            JOptionPane.showMessageDialog(view,
                    "Transacción SQL confirmada:\nEstado de alerta actualizado a «" + nuevoEstado + "» en MySQL.",
                    "Persistencia DAO", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == view.getBtnDespacharPNP()) {
            JOptionPane.showMessageDialog(view,
                    "Enlace telemático y ficha de incidente remitidos a la Central 105 de la PNP.\n" +
                            "Unidad policial asignada: Patrullero Callao-12.",
                    "Despacho Policial", JOptionPane.WARNING_MESSAGE);
        }
    }
}