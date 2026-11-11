package modelo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FiltroReporte {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private Integer idBus;
    private String tipoActivacion;

    public FiltroReporte(LocalDate fechaInicio, LocalDate fechaFin, String estado, Integer idBus, String tipoActivacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.idBus = idBus;
        this.tipoActivacion = tipoActivacion;
    }

    public void validar() {
        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la fecha final.");
        }
    }

    public Map<String, Object> toParametersMap() {
        validar();
        Map<String, Object> params = new HashMap<>();

        Timestamp tsInicio = (fechaInicio != null)
                ? Timestamp.valueOf(fechaInicio.atStartOfDay())
                : null;

        // Día siguiente a las 00:00:00 para incluir todo el día final hasta la noche
        Timestamp tsFinExclusiva = (fechaFin != null)
                ? Timestamp.valueOf(fechaFin.plusDays(1).atStartOfDay())
                : null;

        params.put("FECHA_INICIO", tsInicio);
        params.put("FECHA_FIN_EXCLUSIVA", tsFinExclusiva);
        params.put("ESTADO", (estado == null || estado.isBlank()) ? null : estado);
        params.put("ID_BUS", idBus);
        params.put("TIPO_ACTIVACION", (tipoActivacion == null || tipoActivacion.isBlank()) ? null : tipoActivacion);

        return params;
    }
}