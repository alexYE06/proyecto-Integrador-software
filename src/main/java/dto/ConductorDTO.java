package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import modelo.Conductor;

public class ConductorDTO {

    private Integer idConductor;

    @NotBlank(message = "Los nombres del conductor son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos del conductor son obligatorios")
    private String apellidos;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private String dni;

    private String telefono;
    private String licencia;
    private String estado;

    public ConductorDTO() {
    }

    public ConductorDTO(Integer idConductor, String nombres, String apellidos, String dni, String telefono,
                        String licencia, String estado) {
        this.idConductor = idConductor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.licencia = licencia;
        this.estado = estado;
    }

    public static ConductorDTO fromEntity(Conductor entity) {
        if (entity == null) return null;
        return new ConductorDTO(
                entity.getIdConductor(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getDni(),
                entity.getTelefono(),
                entity.getLicencia(),
                entity.getEstado()
        );
    }

    public Conductor toEntity() {
        return new Conductor(
                this.idConductor,
                this.nombres,
                this.apellidos,
                this.dni,
                this.telefono,
                this.licencia,
                this.estado
        );
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
