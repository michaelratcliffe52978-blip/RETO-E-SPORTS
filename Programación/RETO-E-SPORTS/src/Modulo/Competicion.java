package Modulo;

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

    public String getidCompeticion() {return idCompeticion;}
    public void setidCompeticion(String idCompeticion) {this.idCompeticion = idCompeticion;}
    public String getnombreCompeticion() {return nombreCompeticion;}
    public void setnombreCompeticion(String nombreCompeticion) {this.nombreCompeticion = nombreCompeticion;}
    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}
    public ArrayList<Jornada> getJornadas() {return jornadas;}
    public void setJornadas(ArrayList<Jornada> jornadas) {this.jornadas = jornadas;}

}
