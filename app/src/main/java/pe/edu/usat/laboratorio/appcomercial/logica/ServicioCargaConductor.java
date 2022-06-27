package pe.edu.usat.laboratorio.appcomercial.logica;

public class ServicioCargaConductor {
    private String claseCarga;
    private String cliente;
    private String descripcionCarga;
    private String direccionPartida;
    private String fechaHoraPartida;
    private String direccionLlegada;
    private String categoriaCarga;
    private String tipoCarga;
    private String numeroDocumento;
    private String placa;
    private double pesoCarga;
    private int id;
    private int estado_id;
    private String nombre_estado;

    public String getNombre_estado() {
        return nombre_estado;
    }

    public void setNombre_estado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public String getClaseCarga() {
        return claseCarga;
    }

    public void setClaseCarga(String claseCarga) {
        this.claseCarga = claseCarga;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcionCarga() {
        return descripcionCarga;
    }

    public void setDescripcionCarga(String descripcionCarga) {
        this.descripcionCarga = descripcionCarga;
    }

    public String getDireccionPartida() {
        return direccionPartida;
    }

    public void setDireccionPartida(String direccionPartida) {
        this.direccionPartida = direccionPartida;
    }

    public String getFechaHoraPartida() {
        return fechaHoraPartida;
    }

    public void setFechaHoraPartida(String fechaHoraPartida) {
        this.fechaHoraPartida = fechaHoraPartida;
    }

    public String getDireccionLlegada() {
        return direccionLlegada;
    }

    public void setDireccionLlegada(String direccionLlegada) {
        this.direccionLlegada = direccionLlegada;
    }

    public String getCategoriaCarga() {
        return categoriaCarga;
    }

    public void setCategoriaCarga(String categoriaCarga) {
        this.categoriaCarga = categoriaCarga;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getPesoCarga() {
        return pesoCarga;
    }

    public void setPesoCarga(double pesoCarga) {
        this.pesoCarga = pesoCarga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(int estado_id) {
        this.estado_id = estado_id;
    }
}
