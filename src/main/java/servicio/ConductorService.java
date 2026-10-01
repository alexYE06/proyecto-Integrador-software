package servicio;

import dao.BusDAO;
import dao.ConductorDAO;
import dto.ConductorDTO;
import dto.ConductorLoginRequestDTO;
import dto.ConductorLoginResponseDTO;
import excepcion.CredencialesInvalidasException;
import excepcion.ReglaNegocioException;
import modelo.Bus;
import modelo.Conductor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConductorService {

    private final ConductorDAO conductorDAO;
    private final BusDAO busDAO;

    public ConductorService(ConductorDAO conductorDAO, BusDAO busDAO) {
        this.conductorDAO = conductorDAO;
        this.busDAO = busDAO;
    }

    public ConductorLoginResponseDTO autenticarConductor(ConductorLoginRequestDTO req) {
        if (req == null || req.getDni() == null || req.getDni().trim().isEmpty()) {
            throw new ReglaNegocioException("Debe ingresar el DNI del conductor");
        }

        if (req.getContrasena() == null || req.getContrasena().trim().isEmpty()) {
            throw new ReglaNegocioException("Debe ingresar el PIN o contraseña de conductor");
        }

        String dniLimpio = req.getDni().trim();
        Conductor conductor = conductorDAO.obtenerPorDni(dniLimpio);

        if (conductor == null) {
            throw new CredencialesInvalidasException("DNI no registrado en el padrón de conductores de la empresa");
        }

        if (conductor.getEstado() != null && !conductor.getEstado().equalsIgnoreCase("Activo")) {
            throw new ReglaNegocioException("El conductor se encuentra en estado inactivo o suspendido");
        }

        String pinIngresado = req.getContrasena().trim();
        String ultimos4Dni = dniLimpio.length() >= 4 ? dniLimpio.substring(dniLimpio.length() - 4) : "";
        boolean pinValido = "1234".equals(pinIngresado) || (!ultimos4Dni.isEmpty() && ultimos4Dni.equals(pinIngresado));

        if (!pinValido) {
            throw new CredencialesInvalidasException("PIN de seguridad incorrecto (PIN asignado por defecto: 1234)");
        }

        Integer idBus = req.getIdBus();
        String codigoUnidad = req.getCodigoUnidad();
        String placa = "P-71A";

        if (idBus != null && idBus > 0) {
            Bus bus = busDAO.obtenerPorId(idBus);
            if (bus != null) {
                codigoUnidad = bus.getNumeroUnidad();
                placa = bus.getPlaca();
            }
        } else if (codigoUnidad != null && !codigoUnidad.isBlank()) {
            List<Bus> buses = busDAO.listarBuses();
            for (Bus b : buses) {
                if (codigoUnidad.equalsIgnoreCase(b.getNumeroUnidad())) {
                    idBus = b.getIdBus();
                    placa = b.getPlaca();
                    break;
                }
            }
        }

        if (idBus == null || idBus <= 0) {
            List<Bus> buses = busDAO.listarBuses();
            if (!buses.isEmpty()) {
                Bus primerBus = buses.get(0);
                idBus = primerBus.getIdBus();
                codigoUnidad = primerBus.getNumeroUnidad();
                placa = primerBus.getPlaca();
            } else {
                idBus = 1;
                codigoUnidad = "BUS-071";
                placa = "A1A-710";
            }
        }

        return new ConductorLoginResponseDTO(
                conductor.getIdConductor(),
                conductor.getNombres(),
                conductor.getApellidos(),
                conductor.getDni(),
                conductor.getLicencia(),
                idBus,
                codigoUnidad,
                placa,
                "Inicio de turno autorizado exitosamente"
        );
    }

    public List<ConductorDTO> listarTodos() {
        return conductorDAO.listarConductores().stream()
                .map(ConductorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ConductorDTO registrar(ConductorDTO dto) {
        if (dto == null) {
            throw new ReglaNegocioException("Los datos del conductor no pueden ser nulos");
        }

        if (dto.getNombres() == null || dto.getNombres().trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del conductor es obligatorio");
        }

        if (dto.getApellidos() == null || dto.getApellidos().trim().isEmpty()) {
            throw new ReglaNegocioException("Los apellidos del conductor son obligatorios");
        }

        if (dto.getDni() == null || !dto.getDni().trim().matches("\\d{8}")) {
            throw new ReglaNegocioException("El DNI debe tener exactamente 8 dígitos numéricos");
        }

        if (dto.getEstado() == null || dto.getEstado().trim().isEmpty()) {
            dto.setEstado("Activo");
        }

        Conductor entidad = dto.toEntity();
        conductorDAO.registrarConductor(entidad);

        return ConductorDTO.fromEntity(entidad);
    }
}
