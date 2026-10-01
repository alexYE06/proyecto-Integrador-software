package servicio;

import dao.AsignacionTurnoDAO;
import dao.BusDAO;
import dao.ConductorDAO;
import dto.AsignacionTurnoDTO;
import excepcion.ReglaNegocioException;
import modelo.AsignacionTurno;
import modelo.Bus;
import modelo.Conductor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignacionTurnoService {

    private final AsignacionTurnoDAO asignacionTurnoDAO;
    private final ConductorDAO conductorDAO;
    private final BusDAO busDAO;

    public AsignacionTurnoService(AsignacionTurnoDAO asignacionTurnoDAO, ConductorDAO conductorDAO, BusDAO busDAO) {
        this.asignacionTurnoDAO = asignacionTurnoDAO;
        this.conductorDAO = conductorDAO;
        this.busDAO = busDAO;
    }

    public List<AsignacionTurnoDTO> listarTurnos() {
        return asignacionTurnoDAO.listarTurnos().stream()
                .map(AsignacionTurnoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AsignacionTurnoDTO registrar(AsignacionTurnoDTO dto) {
        if (dto == null) {
            throw new ReglaNegocioException("Los datos de asignación de turno no pueden ser nulos");
        }
        if (dto.getIdConductor() == null || dto.getIdConductor() <= 0) {
            throw new ReglaNegocioException("Debe seleccionar un conductor válido del padrón");
        }
        if (dto.getIdBus() == null || dto.getIdBus() <= 0) {
            throw new ReglaNegocioException("Debe seleccionar una unidad de bus válida de la flota");
        }
        if (dto.getFecha() == null || dto.getFecha().isBlank()) {
            throw new ReglaNegocioException("La fecha del turno es obligatoria");
        }
        if (dto.getHoraInicio() == null || dto.getHoraInicio().isBlank()) {
            throw new ReglaNegocioException("La hora de inicio es obligatoria");
        }

        // Obtener detalles de conductor y bus para enriquecer la respuesta
        String nombreConductor = "Conductor #" + dto.getIdConductor();
        String dni = "";
        for (Conductor c : conductorDAO.listarConductores()) {
            if (c.getIdConductor() == dto.getIdConductor()) {
                nombreConductor = c.getNombres() + " " + c.getApellidos();
                dni = c.getDni();
                break;
            }
        }

        String codigoUnidad = "BUS-" + dto.getIdBus();
        String placa = "PLACA-" + dto.getIdBus();
        Bus b = busDAO.obtenerPorId(dto.getIdBus());
        if (b != null) {
            codigoUnidad = b.getNumeroUnidad();
            placa = b.getPlaca();
        }

        AsignacionTurno entidad = dto.toEntity();
        entidad.setNombreConductor(nombreConductor);
        entidad.setDniConductor(dni);
        entidad.setCodigoUnidad(codigoUnidad);
        entidad.setPlacaBus(placa);

        boolean guardado = asignacionTurnoDAO.registrarTurno(entidad);
        if (!guardado) {
            entidad.setIdAsignacion((int) (System.currentTimeMillis() % 10000));
        }

        return AsignacionTurnoDTO.fromEntity(entidad);
    }
}
