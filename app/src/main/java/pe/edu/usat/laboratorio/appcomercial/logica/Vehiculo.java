package pe.edu.usat.laboratorio.appcomercial.logica;

import java.util.ArrayList;

public class Vehiculo {
    private String placa;
    private int numeroRuedas;
    private String estado;
    private String conductor;

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getNum_brevete() {
        return num_brevete;
    }

    public void setNum_brevete(String num_brevete) {
        this.num_brevete = num_brevete;
    }

    private String num_brevete;


    public static ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getNumeroRuedas() {
        return numeroRuedas;
    }

    public void setNumeroRuedas(int numeroRuedas) {
        this.numeroRuedas = numeroRuedas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
