package dto;

public class ConductorLoginResponseDTO {

    private Integer idConductor;
    private String nombres;
    private String apellidos;
    private String dni;
    private String licencia;
    private Integer idBus;
    private String codigoUnidad;
    private String placa;
    private String mensaje;

    public ConductorLoginResponseDTO() {
    }

    public ConductorLoginResponseDTO(Integer idConductor, String nombres, String apellidos, String dni,
                                    String licencia, Integer idBus, String codigoUnidad, String placa, String mensaje) {
        this.idConductor = idConductor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.licencia = licencia;
        this.idBus = idBus;
        this.codigoUnidad = codigoUnidad;
        this.placa = placa;
        this.mensaje = mensaje;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public Integer getIdBus() {
        return idBus;
    }

    public void setIdBus(Integer idBus) {
        this.idBus = idBus;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
