package Modulo;

import java.util.ArrayList;

public class Competicion {

    private String idCompeticion;
    private String nombreCompeticion;
    private String estado;
    private ArrayList<Jornada> jornadas;
    private ArrayList<Usuario> usuarios;

    public Competicion(String idCompeticion, String nombreCompeticion, String estado, ArrayList<Jornada> jornadas, ArrayList<Usuario> usuarios) {
        this.idCompeticion = idCompeticion;
        this.nombreCompeticion = nombreCompeticion;
        this.estado = estado;
        this.jornadas = jornadas;
        this.usuarios = usuarios;
    }

    public String getidCompeticion() {return idCompeticion;}
    public void setidCompeticion(String idCompeticion) {this.idCompeticion = idCompeticion;}
    public String getnombreCompeticion() {return nombreCompeticion;}
    public void setnombreCompeticion(String nombreCompeticion) {this.nombreCompeticion = nombreCompeticion;}
    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}
    public ArrayList<Jornada> getJornadas() {return jornadas;}
    public void setJornadas(ArrayList<Jornada> jornadas) {this.jornadas = jornadas;}
    public ArrayList<Usuario> getUsuarios() {return usuarios;}
    public void setUsuarios(ArrayList<Usuario> usuarios) {this.usuarios = usuarios;}

}
