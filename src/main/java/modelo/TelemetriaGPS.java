package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TelemetriaGPS {
    
    private Integer idTelemetria;
    private Integer idBus;
    private Integer idAlerta;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private BigDecimal velocidad;
    private LocalDateTime fechaHora;

    public TelemetriaGPS() {
    }
    
    public TelemetriaGPS(Integer idTelemetria, Integer idBus, Integer idAlerta, BigDecimal latitud, BigDecimal longitud,
            BigDecimal velocidad, LocalDateTime fechaHora) {
        this.idTelemetria = idTelemetria;
        this.idBus = idBus;
        this.idAlerta = idAlerta;
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
        this.fechaHora = fechaHora;
    }

    public Integer getIdTelemetria() {
        return idTelemetria;
    }

    public void setIdTelemetria(Integer idTelemetria) {
        this.idTelemetria = idTelemetria;
    }

    public Integer getIdBus() {
        return idBus;
    }

    public void setIdBus(Integer idBus) {
        this.idBus = idBus;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
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

    public BigDecimal getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(BigDecimal velocidad) {
        this.velocidad = velocidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    
}
