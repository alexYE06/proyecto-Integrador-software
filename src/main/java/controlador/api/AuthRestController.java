package controlador.api;

import dto.ConductorLoginRequestDTO;
import dto.ConductorLoginResponseDTO;
import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.ConductorService;
import servicio.UsuarioService;

@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión de operadores, administradores y conductores en ruta")
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UsuarioService usuarioService;
    private final ConductorService conductorService;

    public AuthRestController(UsuarioService usuarioService, ConductorService conductorService) {
        this.usuarioService = usuarioService;
        this.conductorService = conductorService;
    }

    @Operation(summary = "Iniciar sesión operadores/admin", description = "Valida las credenciales del usuario de la central y retorna los datos de sesión activa")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        LoginResponseDTO response = usuarioService.autenticar(req);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Iniciar sesión conductor (App Móvil)", description = "Valida el DNI del conductor y asigna la unidad de bus para el turno de ruta")
    @PostMapping("/conductor-login")
    public ResponseEntity<ConductorLoginResponseDTO> conductorLogin(@Valid @RequestBody ConductorLoginRequestDTO req) {
        ConductorLoginResponseDTO response = conductorService.autenticarConductor(req);
        return ResponseEntity.ok(response);
    }
}
