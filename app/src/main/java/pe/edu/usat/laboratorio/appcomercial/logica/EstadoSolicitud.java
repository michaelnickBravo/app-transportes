package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class EstadoSolicitud {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    private String id;
    private String id_estado;

    public String getId_estado() {
        return id_estado;
    }

    public void setId_estado(String id_estado) {
        this.id_estado = id_estado;
    }

    public String getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(String id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    private String id_solicitud;
    private String fecha_hora;
    private String observacion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;
    public static ArrayList<EstadoSolicitud> listaEstadosSolicitud = new ArrayList<>();
}
