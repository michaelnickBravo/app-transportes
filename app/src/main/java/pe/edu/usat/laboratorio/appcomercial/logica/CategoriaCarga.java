package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class CategoriaCarga {

    private int id;
    private String  nombre;

    public static ArrayList<CategoriaCarga> listaCategoriaCarga = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static String buscarCategoriaCargaId (String nombre) {

        for (CategoriaCarga categoriaCarga: listaCategoriaCarga) {
            if (categoriaCarga.getNombre().equals(nombre)){
                return String.valueOf(categoriaCarga.getId());
            }
        }
        return "";
    }
}
