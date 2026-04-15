package org.example.programacion.Modelo;

import java.util.ArrayList;

public class Competicion {

    private String idCompeticion;
    private String nombreCompeticion;
    private String estado;
    private ArrayList<Jornada> jornadas;

    public Competicion(String idCompeticion, String nombreCompeticion, String estado, ArrayList<Jornada> jornadas) {
        this.idCompeticion = idCompeticion;
        this.nombreCompeticion = nombreCompeticion;
        this.estado = estado;
        this.jornadas = jornadas;
    }

    public String getIdCompeticion() {return idCompeticion;}
    public void setIdCompeticion(String idCompeticion) {this.idCompeticion = idCompeticion;}
    public String getNombreCompeticion() {return nombreCompeticion;}
    public void setNombreCompeticion(String nombreCompeticion) {this.nombreCompeticion = nombreCompeticion;}
    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}
    public ArrayList<Jornada> getJornadas() {return jornadas;}
    public void setJornadas(ArrayList<Jornada> jornadas) {this.jornadas = jornadas;}

}
