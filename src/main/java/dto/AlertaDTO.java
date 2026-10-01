package dto;

import jakarta.validation.constraints.Min;
import modelo.Alerta;
import java.sql.Timestamp;

public class AlertaDTO {

    private int idAlerta;

    @Min(value = 1, message = "Debe asociar un conductor válido a la alerta")
    private int idConductor;

    @Min(value = 1, message = "Debe asociar una unidad de bus válida a la alerta")
    private int idBus;

    private Integer idOperador;
    private Timestamp fechaHora;
    private String tipoActivacion;
    private String estado;
    private String descripcion;

    public AlertaDTO() {
    }

    public AlertaDTO(int idAlerta, int idConductor, int idBus, Integer idOperador,
                     Timestamp fechaHora, String tipoActivacion, String estado, String descripcion) {
        this.idAlerta = idAlerta;
        this.idConductor = idConductor;
        this.idBus = idBus;
        this.idOperador = idOperador;
        this.fechaHora = fechaHora;
        this.tipoActivacion = tipoActivacion;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public static AlertaDTO fromEntity(Alerta entity) {
        if (entity == null) return null;
        return new AlertaDTO(
                entity.getIdAlerta(),
                entity.getIdConductor(),
                entity.getIdBus(),
                entity.getIdOperador(),
                entity.getFechaHora(),
                entity.getTipoActivacion(),
                entity.getEstado(),
                entity.getDescripcion()
        );
    }

    public Alerta toEntity() {
        return new Alerta(
                this.idAlerta,
                this.idConductor,
                this.idBus,
                this.idOperador,
                this.fechaHora,
                this.tipoActivacion,
                this.estado,
                this.descripcion
        );
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoActivacion() {
        return tipoActivacion;
    }

    public void setTipoActivacion(String tipoActivacion) {
        this.tipoActivacion = tipoActivacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
