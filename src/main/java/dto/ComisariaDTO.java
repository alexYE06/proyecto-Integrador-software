package dto;

import jakarta.validation.constraints.NotBlank;
import modelo.Comisaria;

import java.math.BigDecimal;

public class ComisariaDTO {

    private int idComisaria;

    @NotBlank(message = "El nombre de la comisaría o base policial es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección de la comisaría es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono de auxilio rápido es obligatorio")
    private String telefono;

    private BigDecimal latitud;
    private BigDecimal longitud;

    public ComisariaDTO() {
    }

    public ComisariaDTO(int idComisaria, String nombre, String direccion, String telefono, BigDecimal latitud, BigDecimal longitud) {
        this.idComisaria = idComisaria;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public static ComisariaDTO fromEntity(Comisaria entity) {
        if (entity == null) return null;
        return new ComisariaDTO(
                entity.getIdComisaria(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getLatitud(),
                entity.getLongitud()
        );
    }

    public Comisaria toEntity() {
        return new Comisaria(
                this.idComisaria,
                this.nombre,
                this.direccion,
                this.telefono,
                this.latitud,
                this.longitud
        );
    }

    public int getIdComisaria() {
        return idComisaria;
    }

    public void setIdComisaria(int idComisaria) {
        this.idComisaria = idComisaria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }
}
