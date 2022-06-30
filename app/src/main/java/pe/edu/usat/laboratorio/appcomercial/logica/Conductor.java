package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class Conductor {
    private String num_brevete;
    private String nombre;
    private String email;
    private String img;

    public static ArrayList<Conductor> listaConductor = new ArrayList<>();

    public String getNum_brevete() {
        return num_brevete;
    }

    public void setNum_brevete(String num_brevete) {
        this.num_brevete = num_brevete;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
