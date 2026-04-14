package org.example.programacion.Modelo;

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

    // Getters con nombres correctos para JavaBeans
    public String getIdJornada() { return idJornada; }
    public void setIdJornada(String idJornada) { this.idJornada = idJornada; }
    
    public int getNumeroJornada() { return numeroJornada; }
    public void setNumeroJornada(int numeroJornada) { this.numeroJornada = numeroJornada; }
    
    public LocalDate getFechaJornada() { return fechaJornada; }
    public void setFechaJornada(LocalDate fechaJornada) { this.fechaJornada = fechaJornada; }
    
    public Enfrentamiento getEnfrentamientos() { return enfrentamientos; }
    public void setEnfrentamientos(Enfrentamiento enfrentamientos) { this.enfrentamientos = enfrentamientos; }
}
