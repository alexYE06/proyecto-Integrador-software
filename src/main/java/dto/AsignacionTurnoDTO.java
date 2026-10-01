package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import modelo.AsignacionTurno;

import java.sql.Date;
import java.sql.Time;

public class AsignacionTurnoDTO {

    private int idAsignacion;

    @NotNull(message = "El conductor es obligatorio")
    private Integer idConductor;

    @NotNull(message = "La unidad de bus es obligatoria")
    private Integer idBus;

    @NotBlank(message = "La fecha del turno es obligatoria")
    private String fecha;

    @NotBlank(message = "La hora de inicio de turno es obligatoria")
    private String horaInicio;

    private String horaFin;
    private String estado;

    // Metadatos para representación visual en tabla
    private String nombreConductor;
    private String dniConductor;
    private String codigoUnidad;
    private String placaBus;

    public AsignacionTurnoDTO() {
    }

    public static AsignacionTurnoDTO fromEntity(AsignacionTurno entity) {
        if (entity == null) return null;
        AsignacionTurnoDTO dto = new AsignacionTurnoDTO();
        dto.setIdAsignacion(entity.getIdAsignacion());
        dto.setIdConductor(entity.getIdConductor());
        dto.setIdBus(entity.getIdBus());
        dto.setFecha(entity.getFecha() != null ? entity.getFecha().toString() : "");
        dto.setHoraInicio(entity.getHoraInicio() != null ? entity.getHoraInicio().toString() : "");
        dto.setHoraFin(entity.getHoraFin() != null ? entity.getHoraFin().toString() : "");
        dto.setEstado(entity.getEstado());
        dto.setNombreConductor(entity.getNombreConductor());
        dto.setDniConductor(entity.getDniConductor());
        dto.setCodigoUnidad(entity.getCodigoUnidad());
        dto.setPlacaBus(entity.getPlacaBus());
        return dto;
    }

    public AsignacionTurno toEntity() {
        AsignacionTurno entity = new AsignacionTurno();
        entity.setIdAsignacion(this.idAsignacion);
        entity.setIdConductor(this.idConductor != null ? this.idConductor : 0);
        entity.setIdBus(this.idBus != null ? this.idBus : 0);
        if (this.fecha != null && !this.fecha.isBlank()) {
            try {
                entity.setFecha(Date.valueOf(this.fecha));
            } catch (Exception ignored) {
                entity.setFecha(new Date(System.currentTimeMillis()));
            }
        }
        if (this.horaInicio != null && !this.horaInicio.isBlank()) {
            try {
                String hi = this.horaInicio.length() == 5 ? this.horaInicio + ":00" : this.horaInicio;
                entity.setHoraInicio(Time.valueOf(hi));
            } catch (Exception ignored) {
                entity.setHoraInicio(Time.valueOf("08:00:00"));
            }
        }
        if (this.horaFin != null && !this.horaFin.isBlank()) {
            try {
                String hf = this.horaFin.length() == 5 ? this.horaFin + ":00" : this.horaFin;
                entity.setHoraFin(Time.valueOf(hf));
            } catch (Exception ignored) {
                entity.setHoraFin(Time.valueOf("16:00:00"));
            }
        }
        entity.setEstado(this.estado != null ? this.estado : "Activo");
        return entity;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getDniConductor() {
        return dniConductor;
    }

    public void setDniConductor(String dniConductor) {
        this.dniConductor = dniConductor;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getPlacaBus() {
        return placaBus;
    }

    public void setPlacaBus(String placaBus) {
        this.placaBus = placaBus;
    }
}
