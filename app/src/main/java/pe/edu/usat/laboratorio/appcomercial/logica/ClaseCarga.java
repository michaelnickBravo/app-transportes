package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class ClaseCarga {

    private int id;
    private String  nombre;

    public static ArrayList<ClaseCarga> listaClaseCarga = new ArrayList<>();

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

    public static String buscarClaseCargaId (String nombre) {

        for (ClaseCarga claseCarga: listaClaseCarga) {
            if (claseCarga.getNombre().equals(nombre)){
                return String.valueOf(claseCarga.getId());
            }
        }
        return "";
    }

}
