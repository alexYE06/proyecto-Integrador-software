package controlador.api;

import dto.AsignacionTurnoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.AsignacionTurnoService;

import java.util.List;

@Tag(name = "Asignación de Turnos y Roles", description = "Endpoints para la programación y asignación operativa de conductores a unidades de transporte")
@RestController
@RequestMapping("/api/turnos")
public class AsignacionTurnoRestController {

    private final AsignacionTurnoService asignacionTurnoService;

    public AsignacionTurnoRestController(AsignacionTurnoService asignacionTurnoService) {
        this.asignacionTurnoService = asignacionTurnoService;
    }

    @Operation(summary = "Listar turnos programados", description = "Retorna el historial y programación de conductores asignados a buses con detalles de unidad y horario")
    @GetMapping
    public ResponseEntity<List<AsignacionTurnoDTO>> listarTurnos() {
        return ResponseEntity.ok(asignacionTurnoService.listarTurnos());
    }

    @Operation(summary = "Asignar nuevo turno", description = "Programa a un conductor en una unidad de bus en una fecha y rango de horas específico")
    @PostMapping
    public ResponseEntity<AsignacionTurnoDTO> registrarTurno(@Valid @RequestBody AsignacionTurnoDTO dto) {
        AsignacionTurnoDTO guardado = asignacionTurnoService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
