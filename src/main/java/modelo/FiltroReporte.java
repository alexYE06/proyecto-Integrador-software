package modelo;

import java.sql.Timestamp;

public class FiltroReporte {
    private Timestamp fechaInicio;
    private Timestamp fechaFinExclusiva;
    private String estado;
    private Integer idBus;
    private String tipoActivacion;

    public FiltroReporte(Timestamp fechaInicio, Timestamp fechaFinExclusiva, String estado, Integer idBus, String tipoActivacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFinExclusiva = fechaFinExclusiva;
        this.estado = estado;
        this.idBus = idBus;
        this.tipoActivacion = tipoActivacion;
    }

    // Aquí abajo generas tus Getters y Setters correspondientes
    public Timestamp getFechaInicio() { return fechaInicio; }
    public Timestamp getFechaFinExclusiva() { return fechaFinExclusiva; }
    public String getEstado() { return estado; }
    public Integer getIdBus() { return idBus; }
    public String getTipoActivacion() { return tipoActivacion; }
}