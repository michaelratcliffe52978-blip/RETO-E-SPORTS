package org.example.programacion.Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Equipos {
    private int idEquipo;
    private String nombreEquipo;
    private LocalDate fechaFundacion;
    private List<Jugadores> listaJugadores;

    public Equipos(int idEquipo, String nombreEquipo, LocalDate fechaFundacion) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.fechaFundacion = fechaFundacion;
        this.listaJugadores = new ArrayList<>();
    }


    public int getIdEquipo() { return idEquipo; }
    public String getNombreEquipo() { return nombreEquipo; }
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public List<Jugadores> getListaJugadores() { return listaJugadores; }

    public void añadirJugador(Jugadores j) {
        this.listaJugadores.add(j);
    }

    @Override
    public String toString() {
        return nombreEquipo;
    }
}