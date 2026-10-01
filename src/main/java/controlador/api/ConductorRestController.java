package controlador.api;

import dto.ConductorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.ConductorService;

import java.util.List;
import java.util.Map;

@Tag(name = "Conductores", description = "Endpoints para el padrón y registro de choferes")
@RestController
@RequestMapping("/api/conductores")
public class ConductorRestController {

    private final ConductorService conductorService;

    public ConductorRestController(ConductorService conductorService) {
        this.conductorService = conductorService;
    }

    @Operation(summary = "Registrar nuevo conductor", description = "Registra un nuevo chofer validando formato de DNI y nombres")
    @PostMapping
    public ResponseEntity<?> registrarConductor(@Valid @RequestBody ConductorDTO conductor) {
        ConductorDTO creado = conductorService.registrar(conductor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Conductor registrado exitosamente", "conductor", creado));
    }

    @Operation(summary = "Listar conductores", description = "Retorna el listado completo de choferes registrados en el padrón")
    @GetMapping
    public ResponseEntity<List<ConductorDTO>> listarConductores() {
        return ResponseEntity.ok(conductorService.listarTodos());
    }
}
