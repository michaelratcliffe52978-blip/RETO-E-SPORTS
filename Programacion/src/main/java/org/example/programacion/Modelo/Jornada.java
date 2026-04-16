package org.example.programacion.Modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Jornada {

    private SimpleStringProperty idJornada;
    private SimpleIntegerProperty numeroJornada;
    private SimpleObjectProperty<LocalDate> fechaJornada;
    private Enfrentamiento enfrentamientos;

    public Jornada(String idJornada, int numeroJornada, LocalDate fechaJornada) {
        this.idJornada = new SimpleStringProperty(idJornada);
        this.numeroJornada = new SimpleIntegerProperty(numeroJornada);
        this.fechaJornada = new SimpleObjectProperty<>(fechaJornada);
    }

    public Jornada(String idJornada, int numeroJornada, LocalDate fechaJornada, Enfrentamiento enfrentamientos) {
        this.idJornada = new SimpleStringProperty(idJornada);
        this.numeroJornada = new SimpleIntegerProperty(numeroJornada);
        this.fechaJornada = new SimpleObjectProperty<>(fechaJornada);
        this.enfrentamientos = enfrentamientos;
    }

    // Getters para JavaFX properties
    public SimpleStringProperty idJornadaProperty() { return idJornada; }
    public SimpleIntegerProperty numeroJornadaProperty() { return numeroJornada; }
    public SimpleObjectProperty<LocalDate> fechaJornadaProperty() { return fechaJornada; }

    // Getters y setters tradicionales
    public String getIdJornada() { return idJornada.get(); }
    public void setIdJornada(String idJornada) { this.idJornada.set(idJornada); }
    
    public int getNumeroJornada() { return numeroJornada.get(); }
    public void setNumeroJornada(int numeroJornada) { this.numeroJornada.set(numeroJornada); }
    
    public LocalDate getFechaJornada() { return fechaJornada.get(); }
    public void setFechaJornada(LocalDate fechaJornada) { this.fechaJornada.set(fechaJornada); }
    
    public Enfrentamiento getEnfrentamientos() { return enfrentamientos; }
    public void setEnfrentamientos(Enfrentamiento enfrentamientos) { this.enfrentamientos = enfrentamientos; }
}
