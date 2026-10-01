package servicio;

import dao.AlertaDAO;
import dto.AlertaDTO;
import excepcion.RecursoNoEncontradoException;
import excepcion.ReglaNegocioException;
import modelo.Alerta;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertaService {

    private final AlertaDAO alertaDAO;

    public AlertaService(AlertaDAO alertaDAO) {
        this.alertaDAO = alertaDAO;
    }

    public List<AlertaDTO> listarTodas() {
        return alertaDAO.listarAlertas().stream()
                .map(AlertaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public dto.PaginaDTO<AlertaDTO> listarPaginado(int pagina, int tamanio) {
        if (pagina < 0) pagina = 0;
        if (tamanio <= 0) tamanio = 10;

        List<AlertaDTO> todas = listarTodas();
        int totalElementos = todas.size();
        int totalPaginas = (totalElementos == 0) ? 0 : (int) Math.ceil((double) totalElementos / tamanio);

        int desde = pagina * tamanio;
        int hasta = Math.min(desde + tamanio, totalElementos);

        List<AlertaDTO> sublista = (desde >= totalElementos) ? List.of() : todas.subList(desde, hasta);

        return new dto.PaginaDTO<>(sublista, pagina, totalPaginas, totalElementos, tamanio);
    }

    public AlertaDTO obtenerPorId(int idAlerta) {
        Alerta alerta = alertaDAO.obtenerPorId(idAlerta);
        if (alerta == null) {
            throw new RecursoNoEncontradoException("No se encontró el incidente/alerta con ID: " + idAlerta);
        }
        return AlertaDTO.fromEntity(alerta);
    }

    public AlertaDTO registrar(AlertaDTO dto) {
        if (dto == null) {
            throw new ReglaNegocioException("Los datos de la alerta no pueden ser nulos");
        }

        if (dto.getIdConductor() <= 0) {
            throw new ReglaNegocioException("Debe asociar un conductor válido a la alerta");
        }

        if (dto.getIdBus() <= 0) {
            throw new ReglaNegocioException("Debe asociar una unidad de bus válida a la alerta");
        }

        if (dto.getFechaHora() == null) {
            dto.setFechaHora(new Timestamp(System.currentTimeMillis()));
        }

        if (dto.getEstado() == null || dto.getEstado().trim().isEmpty()) {
            dto.setEstado("Pendiente");
        }

        Alerta entidad = dto.toEntity();
        int idGenerado = alertaDAO.registrarAlerta(entidad);
        if (idGenerado > 0) {
            dto.setIdAlerta(idGenerado);
        }

        return dto;
    }

    public void cambiarEstado(int idAlerta, String nuevoEstado, Integer idOperador) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new ReglaNegocioException("El nuevo estado de la alerta es obligatorio");
        }

        Alerta existente = alertaDAO.obtenerPorId(idAlerta);
        if (existente == null) {
            throw new RecursoNoEncontradoException("No se encontró la alerta con ID: " + idAlerta);
        }

        boolean actualizado = alertaDAO.cambiarEstado(idAlerta, nuevoEstado.trim(), idOperador);
        if (!actualizado) {
            throw new ReglaNegocioException("No se pudo actualizar el estado de la alerta " + idAlerta);
        }
    }
}
