package dto;

import jakarta.validation.constraints.NotBlank;

public class ActualizarEstadoAlertaDTO {

    @NotBlank(message = "El nuevo estado de la alerta es obligatorio")
    private String nuevoEstado;
    private Integer idOperador;

    public ActualizarEstadoAlertaDTO() {
    }

    public ActualizarEstadoAlertaDTO(String nuevoEstado, Integer idOperador) {
        this.nuevoEstado = nuevoEstado;
        this.idOperador = idOperador;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }
}
