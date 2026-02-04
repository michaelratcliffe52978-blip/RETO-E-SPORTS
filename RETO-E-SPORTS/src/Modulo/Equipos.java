package Modulo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Equipos {

    private String idEquipo;
    private String nombreEquipo;
    private LocalDate fechaFundacion;
    private ArrayList<Jugadores> jugadores;

    public Equipos(String idEquipo, String nombreEquipo, LocalDate fechaFundacion) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.fechaFundacion = fechaFundacion;
    }

    public Equipos(String idEquipo, ArrayList<Jugadores> jugadores, LocalDate fechaFundacion, String nombreEquipo) {
        this.idEquipo = idEquipo;
        this.jugadores = jugadores;
        this.fechaFundacion = fechaFundacion;
        this.nombreEquipo = nombreEquipo;
    }

    public String getidEquipo() {return idEquipo;}
    public void setidEquipo(String idEquipo) {}

    public String getnombreEquipo() {return nombreEquipo;}
    public void setnombreEquipo(String nombreEquipo) {}

    public LocalDate getfechaFundacion() {return fechaFundacion;}
    public void setfechaFundacion(LocalDate fechaFundacion) {}

    public ArrayList<Jugadores> getjugadores() {return jugadores;}
    public void setjugadores(ArrayList<Jugadores> jugadores) {}

}