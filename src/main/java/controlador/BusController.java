package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import dao.BusDAO;
import modelo.Bus;
import vista.RegistrarBusView;

public class BusController implements ActionListener {

    private final RegistrarBusView view;
    private final BusDAO busDAO;

    public BusController(RegistrarBusView view) {
        this.view = view;
        this.busDAO = new BusDAO();
        this.view.getBtnRegistrar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != view.getBtnRegistrar()) {
            return;
        }

        String placa = view.getPlaca();
        String codigoUnidad = view.getCodigoUnidad();
        String modelo = view.getModelo();
        String capacidadStr = view.getCapacidad();

        if (placa.isEmpty() || codigoUnidad.isEmpty() || modelo.isEmpty() || capacidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Complete todos los campos requeridos.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "La capacidad debe ser un número entero válido.",
                    "Error de formato", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Bus bus = new Bus();
        bus.setPlaca(placa);
        bus.setNumeroUnidad(codigoUnidad); // Map to codigo_unidad
        bus.setModelo(modelo);
        bus.setCapacidad(capacidad);
        bus.setEstado("NORMAL"); // Default state for a new bus

        if (busDAO.registrarBus(bus)) {
            JOptionPane.showMessageDialog(view,
                    "Unidad registrada correctamente en la flota.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(view,
                    "No se pudo registrar el bus. Verifique que la placa o unidad no estén repetidas.",
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
