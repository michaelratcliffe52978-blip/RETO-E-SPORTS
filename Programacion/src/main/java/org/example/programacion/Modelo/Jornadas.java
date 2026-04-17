package org.example.programacion.Modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

/**
 * Esta clase representa una Jornada de la competición.
 * Utiliza propiedades de JavaFX (Properties) para permitir el "data binding",
 * lo que facilita que los cambios en los datos se reflejen automáticamente
 * en la interfaz gráfica.
 * * @author equipo4
 * @version 1.0
 */
public class Jornadas {

    /** ID de la jornada, envuelto en una propiedad de JavaFX para la UI */
    private SimpleStringProperty idJornada;
    /** Número secuencial de la jornada (Jornada 1, 2, etc.) */
    private SimpleIntegerProperty numeroJornada;
    /** Fecha en la que se disputa la jornada */
    private SimpleObjectProperty<LocalDate> fechaJornada;
    /** Relación con los enfrentamientos que ocurren en esta jornada */
    private Enfrentamientos enfrentamientos;

    /**
     * Constructor básico para crear una jornada.
     * * @param idJornada Identificador único.
     * @param numeroJornada El número de la jornada en el calendario.
     * @param fechaJornada La fecha prevista para los partidos.
     */
    public Jornadas(String idJornada, int numeroJornada, LocalDate fechaJornada) {
        this.idJornada = new SimpleStringProperty(idJornada);
        this.numeroJornada = new SimpleIntegerProperty(numeroJornada);
        this.fechaJornada = new SimpleObjectProperty<>(fechaJornada);
    }

    /**
     * Constructor completo que incluye la relación con los enfrentamientos.
     * * @param idJornada Identificador único.
     * @param numeroJornada El número de la jornada.
     * @param fechaJornada La fecha prevista.
     * @param enfrentamientos Los partidos asociados a esta jornada.
     */
    public Jornadas(String idJornada, int numeroJornada, LocalDate fechaJornada, Enfrentamientos enfrentamientos) {
        this.idJornada = new SimpleStringProperty(idJornada);
        this.numeroJornada = new SimpleIntegerProperty(numeroJornada);
        this.fechaJornada = new SimpleObjectProperty<>(fechaJornada);
        this.enfrentamientos = enfrentamientos;
    }

    // --- MÉTODOS PROPERTY (Específicos para JavaFX) ---

    /** @return La propiedad del ID para vinculación con tablas */
    public SimpleStringProperty idJornadaProperty() { return idJornada; }

    /** @return La propiedad del número para vinculación con tablas */
    public SimpleIntegerProperty numeroJornadaProperty() { return numeroJornada; }

    /** @return La propiedad de la fecha para vinculación con tablas */
    public SimpleObjectProperty<LocalDate> fechaJornadaProperty() { return fechaJornada; }

    // --- GETTERS Y SETTERS TRADICIONALES ---

    /** @return El ID de la jornada como String */
    public String getIdJornada() { return idJornada.get(); }

    /** @param idJornada Nuevo ID a establecer */
    public void setIdJornada(String idJornada) { this.idJornada.set(idJornada); }

    /** @return El número de la jornada */
    public int getNumeroJornada() { return numeroJornada.get(); }

    /** @param numeroJornada Nuevo número de jornada */
    public void setNumeroJornada(int numeroJornada) { this.numeroJornada.set(numeroJornada); }

    /** @return La fecha de la jornada */
    public LocalDate getFechaJornada() { return fechaJornada.get(); }

    /** @param fechaJornada Nueva fecha para la jornada */
    public void setFechaJornada(LocalDate fechaJornada) { this.fechaJornada.set(fechaJornada); }

    /** @return Los enfrentamientos de esta jornada */
    public Enfrentamientos getEnfrentamientos() { return enfrentamientos; }

    /** @param enfrentamientos Los nuevos enfrentamientos a asignar */
    public void setEnfrentamientos(Enfrentamientos enfrentamientos) { this.enfrentamientos = enfrentamientos; }
}