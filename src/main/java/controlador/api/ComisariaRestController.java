package controlador.api;

import dto.ComisariaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.ComisariaService;

import java.util.List;

@Tag(name = "Comisarías y Auxilio Policial", description = "Endpoints para gestión y registro de comisarías y centros de apoyo en ruta")
@RestController
@RequestMapping("/api/comisarias")
public class ComisariaRestController {

    private final ComisariaService comisariaService;

    public ComisariaRestController(ComisariaService comisariaService) {
        this.comisariaService = comisariaService;
    }

    @Operation(summary = "Listar todas las comisarías", description = "Retorna el directorio completo de dependencias policiales registradas")
    @GetMapping
    public ResponseEntity<List<ComisariaDTO>> listarTodas() {
        return ResponseEntity.ok(comisariaService.listarTodas());
    }

    @Operation(summary = "Obtener comisaría por ID", description = "Retorna los datos de una comisaría específica")
    @GetMapping("/{id}")
    public ResponseEntity<ComisariaDTO> obtenerPorId(@PathVariable("id") int id) {
        return ResponseEntity.ok(comisariaService.obtenerPorId(id));
    }

    @Operation(summary = "Registrar nueva comisaría", description = "Registra una dependencia policial con nombre, dirección, teléfono y coordenadas GPS")
    @PostMapping
    public ResponseEntity<ComisariaDTO> registrar(@Valid @RequestBody ComisariaDTO dto) {
        ComisariaDTO guardada = comisariaService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
}
