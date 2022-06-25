package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class TipoCarga {

    private int id;
    private String  nombre;

    public static ArrayList<TipoCarga> listaTipoCarga = new ArrayList<>();

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

    public static String buscarTipoCargaId (String nombre) {

        for (TipoCarga tipoCarga: listaTipoCarga) {
            if (tipoCarga.getNombre().equals(nombre)){
                return String.valueOf(tipoCarga.getId());
            }
        }
        return "";
    }

}
