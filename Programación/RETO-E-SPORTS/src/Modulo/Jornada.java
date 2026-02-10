package Modulo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Jornada {

    private String idJornada;
    private int numeroJornada;
    private LocalDate fechaJornada;
    private Enfrentamiento enfrentamientos;

    public Jornada(String idJornada, int numeroJornada, LocalDate fechaJornada, Enfrentamiento enfrentamientos) {
        this.idJornada = idJornada;
        this.numeroJornada = numeroJornada;
        this.fechaJornada = fechaJornada;
        this.enfrentamientos = enfrentamientos;
    }

    public String getidJornada() {return idJornada;}
    public void setidJornada(String idJornada) {this.idJornada = idJornada;}
    public int getnumeroJornada() {return numeroJornada;}
    public void setnumeroJornada(int numeroJornada) {this.numeroJornada = numeroJornada;}
    public LocalDate getfechaJornada() {return fechaJornada;}
    public void setfechaJornada(LocalDate fechaJornada) {this.fechaJornada = fechaJornada;}
    public Enfrentamiento getEnfrentamientos() {return enfrentamientos;}
    public void setEnfrentamientos(Enfrentamiento enfrentamientos) {this.enfrentamientos = enfrentamientos;}
}
