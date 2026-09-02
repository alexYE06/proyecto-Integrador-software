package modelo;

public class Bus {

    private Integer idBus;
    private String placa;
    private String codigoUnidad;
    private String modelo;
    private Integer capacidad;
    private String estado;

    public Bus() {
    }

    public Bus(Integer idBus, String placa, String codigoUnidad, String modelo, Integer capacidad, String estado) {
        this.idBus = idBus;
        this.placa = placa;
        this.codigoUnidad = codigoUnidad;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public Integer getIdBus() {
        return idBus;
    }

    public void setIdBus(Integer idBus) {
        this.idBus = idBus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
