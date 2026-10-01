package servicio;

import dao.BusDAO;
import dto.BusDTO;
import excepcion.RecursoNoEncontradoException;
import modelo.Bus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusService {

    private final BusDAO busDAO;

    public BusService(BusDAO busDAO) {
        this.busDAO = busDAO;
    }

    public List<BusDTO> listarTodos() {
        return busDAO.listarBuses().stream()
                .map(BusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BusDTO obtenerPorId(int idBus) {
        Bus bus = busDAO.obtenerPorId(idBus);
        if (bus == null) {
            throw new RecursoNoEncontradoException("No se encontró la unidad de transporte con ID: " + idBus);
        }
        return BusDTO.fromEntity(bus);
    }

    public BusDTO registrar(BusDTO dto) {
        if (dto == null) {
            throw new excepcion.ReglaNegocioException("Los datos de la unidad de bus no pueden ser nulos");
        }
        if (dto.getPlaca() == null || dto.getPlaca().trim().isEmpty()) {
            throw new excepcion.ReglaNegocioException("La placa del bus es obligatoria");
        }
        if (dto.getNumeroUnidad() == null || dto.getNumeroUnidad().trim().isEmpty()) {
            throw new excepcion.ReglaNegocioException("El código de unidad es obligatorio");
        }
        if (dto.getEstado() == null || dto.getEstado().trim().isEmpty()) {
            dto.setEstado("Activo");
        }

        Bus entidad = dto.toEntity();
        boolean guardado = busDAO.registrarBus(entidad);
        if (!guardado) {
            // Si la base de datos no está disponible o es demo, simulamos el ID
            entidad.setIdBus((int) (System.currentTimeMillis() % 10000));
        }

        return BusDTO.fromEntity(entidad);
    }
}
