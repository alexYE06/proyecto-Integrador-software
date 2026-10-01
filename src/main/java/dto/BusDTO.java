package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import modelo.Bus;

public class BusDTO {

    private int idBus;

    @NotBlank(message = "La placa de la unidad es obligatoria")
    @Size(min = 6, max = 10, message = "La placa debe tener entre 6 y 10 caracteres")
    private String placa;

    @NotBlank(message = "El código de unidad es obligatorio")
    private String numeroUnidad;

    private String modelo;

    @Min(value = 1, message = "La capacidad mínima de pasajeros debe ser al menos 1")
    private int capacidad;

    private String estado;

    public BusDTO() {
    }

    public BusDTO(int idBus, String placa, String numeroUnidad, String modelo, int capacidad, String estado) {
        this.idBus = idBus;
        this.placa = placa;
        this.numeroUnidad = numeroUnidad;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public static BusDTO fromEntity(Bus entity) {
        if (entity == null) return null;
        return new BusDTO(
                entity.getIdBus(),
                entity.getPlaca(),
                entity.getNumeroUnidad(),
                entity.getModelo(),
                entity.getCapacidad(),
                entity.getEstado()
        );
    }

    public Bus toEntity() {
        return new Bus(
                this.idBus,
                this.placa,
                this.numeroUnidad,
                this.modelo,
                this.capacidad,
                this.estado
        );
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(String numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
