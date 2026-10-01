package controlador.api;

import dto.ActualizarEstadoAlertaDTO;
import dto.AlertaDTO;
import dto.PaginaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import servicio.AlertaService;
import servicio.AlertaSseService;

import java.util.List;
import java.util.Map;

@Tag(name = "Alertas e Incidentes", description = "Endpoints para registro de pánico silencioso, despacho de auxilio, telemetría y eventos en tiempo real")
@RestController
@RequestMapping("/api/alertas")
public class AlertaRestController {

    private final AlertaService alertaService;
    private final AlertaSseService alertaSseService;

    public AlertaRestController(AlertaService alertaService, AlertaSseService alertaSseService) {
        this.alertaService = alertaService;
        this.alertaSseService = alertaSseService;
    }

    @Operation(summary = "Canal de telemetría y alertas en tiempo real (Server-Sent Events)",
               description = "Conexión persistente SSE que emite notificaciones instantáneas a la central de despacho cuando un bus dispara una alerta o cambia de estado.")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter suscribirStream() {
        return alertaSseService.suscribir();
    }

    @Operation(summary = "Listar alertas (con soporte opcional de paginación)",
               description = "Retorna la lista de incidentes. Si se envían 'page' y 'size', devuelve la respuesta paginada con metadatos.")
    @GetMapping
    public ResponseEntity<?> listarTodas(
            @Parameter(description = "Número de página (0-indexado)", required = false)
            @RequestParam(name = "page", required = false) Integer page,
            @Parameter(description = "Tamaño de página (cantidad de registros)", required = false)
            @RequestParam(name = "size", required = false) Integer size) {

        if (page != null) {
            int tamanio = (size != null && size > 0) ? size : 10;
            return ResponseEntity.ok(alertaService.listarPaginado(page, tamanio));
        }

        return ResponseEntity.ok(alertaService.listarTodas());
    }

    @Operation(summary = "Listar historial de alertas paginado", description = "Retorna un bloque específico de alertas para paginación de tablas")
    @GetMapping("/paginado")
    public ResponseEntity<PaginaDTO<AlertaDTO>> listarPaginado(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(alertaService.listarPaginado(page, size));
    }

    @Operation(summary = "Obtener alerta por ID", description = "Busca los datos de una alerta específica por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlertaDTO> obtenerPorId(@PathVariable("id") int id) {
        return ResponseEntity.ok(alertaService.obtenerPorId(id));
    }

    @Operation(summary = "Registrar nueva alerta de pánico", description = "Registra una alerta silenciosa emitida por el conductor y la difunde en tiempo real vía SSE")
    @PostMapping
    public ResponseEntity<AlertaDTO> registrarAlerta(@Valid @RequestBody AlertaDTO alerta) {
        AlertaDTO creada = alertaService.registrar(alerta);
        // Difusión instantánea en tiempo real a todas las pantallas de central conectadas
        alertaSseService.notificarNuevaAlerta(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Actualizar estado de la alerta", description = "Cambia el estado del incidente y notifica en vivo a la central vía SSE")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable("id") int idAlerta,
            @Valid @RequestBody ActualizarEstadoAlertaDTO req) {
        alertaService.cambiarEstado(idAlerta, req.getNuevoEstado(), req.getIdOperador());
        // Notificación en vivo del cambio de estado vía SSE
        alertaSseService.notificarCambioEstado(idAlerta, req.getNuevoEstado());
        return ResponseEntity.ok(Map.of(
                "mensaje", "Estado actualizado correctamente",
                "idAlerta", idAlerta,
                "nuevoEstado", req.getNuevoEstado()
        ));
    }
}
