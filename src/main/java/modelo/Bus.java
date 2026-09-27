package modelo;

public class Bus {
    private int idBus;
    private String placa;
    private String numeroUnidad; // Corresponde a codigo_unidad de la BD
    private String modelo;
    private int capacidad;
    private String estado;

    public Bus() {
    }

    public Bus(int idBus, String placa, String numeroUnidad, String modelo, int capacidad, String estado) {
        this.idBus = idBus;
        this.placa = placa;
        this.numeroUnidad = numeroUnidad;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
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