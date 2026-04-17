package org.example.programacion.Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa a un Equipo dentro de la competición.
 * Guarda la información básica como su nombre y fecha de fundación,
 * además de mantener una lista con todos los jugadores que forman la plantilla.
 * * @author equipo4
 * @version 1.0
 */
public class Equipos {
    /** ID único que identifica al equipo en la base de datos */
    private int idEquipo;
    /** Nombre oficial del equipo */
    private String nombreEquipo;
    /** Fecha en la que se creó el equipo */
    private LocalDate fechaFundacion;
    /** Lista de objetos {@link Jugadores} que pertenecen a este equipo */
    private List<Jugadores> listaJugadores;

    /**
     * Constructor para inicializar un equipo.
     * Al crearlo, se genera automáticamente una lista vacía para empezar a meter jugadores.
     * * @param idEquipo ID numérico del equipo.
     * @param nombreEquipo Nombre del club.
     * @param fechaFundacion Fecha de fundación del equipo.
     */
    public Equipos(int idEquipo, String nombreEquipo, LocalDate fechaFundacion) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.fechaFundacion = fechaFundacion;
        this.listaJugadores = new ArrayList<>();
    }

    // --- GETTERS ---

    /** @return El ID del equipo */
    public int getIdEquipo() { return idEquipo; }

    /** @return El nombre del equipo */
    public String getNombreEquipo() { return nombreEquipo; }

    /** @return La fecha de fundación */
    public LocalDate getFechaFundacion() { return fechaFundacion; }

    /** @return La lista de jugadores de la plantilla */
    public List<Jugadores> getListaJugadores() { return listaJugadores; }

    // --- SETTERS ---

    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }

    // --- MÉTODOS DE LÓGICA ---

    /**
     * Añade un nuevo jugador a la lista de este equipo.
     * * @param j El objeto {@link Jugadores} que se une a la plantilla.
     */
    public void añadirJugador(Jugadores j) {
        this.listaJugadores.add(j);
    }

    /**
     * Sobrescribimos el método toString para que JavaFX (en ComboBox o ChoiceBox)
     * muestre directamente el nombre del equipo.
     * * @return El nombre del equipo.
     */
    @Override
    public String toString() {
        return nombreEquipo;
    }


    //ESTA PARTE DEL CÓDIGO ES PARA GENERAR EL CALENDARIOTEST
    private String nombre;
    private List<String> jugadores = new ArrayList<>();


    public void anadirJugador(String nombre) {
        this.jugadores.add(nombre);
    }

    public int getCantidadJugadores() {
        return this.jugadores.size();
    }
}