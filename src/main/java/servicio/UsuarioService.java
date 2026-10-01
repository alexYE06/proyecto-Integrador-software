package servicio;

import dao.UsuarioDAO;
import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import excepcion.CredencialesInvalidasException;
import excepcion.ReglaNegocioException;
import modelo.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        if (request == null || request.getUsuario() == null || request.getUsuario().isBlank()
                || request.getContrasena() == null || request.getContrasena().isBlank()) {
            throw new ReglaNegocioException("Debe ingresar usuario y contraseña");
        }

        Usuario usuario = usuarioDAO.validarAcceso(request.getUsuario().trim(), request.getContrasena());

        if (usuario == null) {
            throw new CredencialesInvalidasException("Credenciales incorrectas o usuario inactivo en el sistema");
        }

        return new LoginResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getRol(),
                usuario.getEstado(),
                "Autenticación exitosa"
        );
    }
}
