package modelo;

import java.time.LocalDateTime;

public class Alerta {

    private Integer idAlerta;
    private Integer idConductor;
    private Integer idBus;
    private Integer idOperador;
    private LocalDateTime fechaHora;
    private String tipoActivacion;
    private String estado;
    private String descripcion;

    public Alerta() {
    }

    public Alerta(Integer idAlerta, Integer idConductor, Integer idBus, Integer idOperador,
            LocalDateTime fechaHora, String tipoActivacion, String estado, String descripcion) {
        this.idAlerta = idAlerta;
        this.idConductor = idConductor;
        this.idBus = idBus;
        this.idOperador = idOperador;
        this.fechaHora = fechaHora;
        this.tipoActivacion = tipoActivacion;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public Integer getIdBus() {
        return idBus;
    }

    public void setIdBus(Integer idBus) {
        this.idBus = idBus;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
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
