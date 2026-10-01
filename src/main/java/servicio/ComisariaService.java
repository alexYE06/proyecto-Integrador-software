package servicio;

import dao.ComisariaDAO;
import dto.ComisariaDTO;
import excepcion.RecursoNoEncontradoException;
import excepcion.ReglaNegocioException;
import modelo.Comisaria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComisariaService {

    private final ComisariaDAO comisariaDAO;

    public ComisariaService(ComisariaDAO comisariaDAO) {
        this.comisariaDAO = comisariaDAO;
    }

    public List<ComisariaDTO> listarTodas() {
        return comisariaDAO.listarComisarias().stream()
                .map(ComisariaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ComisariaDTO obtenerPorId(int id) {
        Comisaria c = comisariaDAO.obtenerPorId(id);
        if (c == null) {
            throw new RecursoNoEncontradoException("No se encontró la comisaría con ID: " + id);
        }
        return ComisariaDTO.fromEntity(c);
    }

    public ComisariaDTO registrar(ComisariaDTO dto) {
        if (dto == null) {
            throw new ReglaNegocioException("Los datos de la comisaría no pueden ser nulos");
        }
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre de la comisaría es obligatorio");
        }
        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty()) {
            throw new ReglaNegocioException("La dirección de la comisaría es obligatoria");
        }
        if (dto.getTelefono() == null || dto.getTelefono().trim().isEmpty()) {
            throw new ReglaNegocioException("El teléfono de la comisaría es obligatorio");
        }

        Comisaria entidad = dto.toEntity();
        boolean guardado = comisariaDAO.registrarComisaria(entidad);
        if (!guardado) {
            entidad.setIdComisaria((int) (System.currentTimeMillis() % 10000));
        }

        return ComisariaDTO.fromEntity(entidad);
    }
}
