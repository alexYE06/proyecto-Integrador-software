package controlador.api;

import dto.BusDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.BusService;

import java.util.List;

@Tag(name = "Flota de Buses", description = "Endpoints para monitoreo en vivo de las unidades de transporte")
@RestController
@RequestMapping("/api/buses")
public class BusRestController {

    private final BusService busService;

    public BusRestController(BusService busService) {
        this.busService = busService;
    }

    @Operation(summary = "Listar todos los buses", description = "Retorna el listado completo de unidades con placa, modelo y estado")
    @GetMapping
    public ResponseEntity<List<BusDTO>> listarBuses() {
        return ResponseEntity.ok(busService.listarTodos());
    }

    @Operation(summary = "Obtener bus por ID", description = "Busca una unidad de transporte por su identificador primario")
    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> obtenerPorId(@PathVariable("id") int id) {
        return ResponseEntity.ok(busService.obtenerPorId(id));
    }

    @Operation(summary = "Registrar nueva unidad de bus", description = "Da de alta un bus con placa, número de unidad, modelo y capacidad en la flota")
    @PostMapping
    public ResponseEntity<BusDTO> registrarBus(@jakarta.validation.Valid @RequestBody BusDTO dto) {
        BusDTO guardado = busService.registrar(dto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(guardado);
    }
}
