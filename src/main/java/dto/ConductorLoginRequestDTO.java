package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ConductorLoginRequestDTO {

    @NotBlank(message = "El DNI del conductor es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos numéricos")
    private String dni;

    @NotBlank(message = "La clave o PIN de acceso del conductor es obligatorio")
    private String contrasena;

    private Integer idBus;
    private String codigoUnidad;

    public ConductorLoginRequestDTO() {
    }

    public ConductorLoginRequestDTO(String dni, String contrasena, Integer idBus, String codigoUnidad) {
        this.dni = dni;
        this.contrasena = contrasena;
        this.idBus = idBus;
        this.codigoUnidad = codigoUnidad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getIdBus() {
        return idBus;
    }

    public void setIdBus(Integer idBus) {
        this.idBus = idBus;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }
}
