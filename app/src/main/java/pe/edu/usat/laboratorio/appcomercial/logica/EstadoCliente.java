package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class EstadoCliente {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String id;
    private String nombre;
    public static ArrayList<EstadoCliente> listaEstadosCliente = new ArrayList<>();
}
