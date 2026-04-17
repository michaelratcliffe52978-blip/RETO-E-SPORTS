package org.example.programacion.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Esta clase representa un partido o enfrentamiento entre dos equipos.
 * Contiene la información de la fecha, hora, los equipos que juegan,
 * la jornada a la que pertenece y el marcador final.
 * * @author equipo4
 * @version 1.0
 */
public class Enfrentamientos {
    private String idPartido;
    private LocalTime hora;
    private LocalDate fechaEnfrentamiento;
    private Equipos equipo1;
    private Equipos equipo2;

    /** Relación con la clase Jornadas para saber en qué número de jornada cae */
    private Jornadas jornadas;
    private int golesLocal;
    private int golesVisitante;

    /**
     * Constructor completo para crear un enfrentamiento con todos sus detalles.
     * Ideal para ser usado por el ResultadosDAO al cargar datos de la BD.
     * * @param idPartido Identificador único del encuentro.
     * @param hora Hora de inicio.
     * @param fechaEnfrentamiento Fecha del calendario.
     * @param equipo1 Objeto del equipo local.
     * @param equipo2 Objeto del equipo visitante.
     * @param jornadas Objeto jornada asociado.
     * @param golesLocal Puntos o goles del local.
     * @param golesVisitante Puntos o goles del visitante.
     */
    public Enfrentamientos(String idPartido, LocalTime hora, LocalDate fechaEnfrentamiento,
                           Equipos equipo1, Equipos equipo2, Jornadas jornadas,
                           int golesLocal, int golesVisitante) {
        this.idPartido = idPartido;
        this.hora = hora;
        this.fechaEnfrentamiento = fechaEnfrentamiento;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.jornadas = jornadas;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    // --- MÉTODOS ESPECIALES PARA JavaFX ---

    /**
     * Devuelve el número de la jornada.
     * JavaFX lo usa automáticamente mediante PropertyValueFactory("numeroDeJornada").
     * * @return El número de jornada o 0 si no hay jornada asignada.
     */
    public int getNumeroDeJornada() {
        return (jornadas != null) ? jornadas.getNumeroJornada() : 0;
    }

    /**
     * Devuelve el marcador unido por un guion (ej: "2 - 1").
     * JavaFX lo usa automáticamente mediante PropertyValueFactory("resultadoFormateado").
     * * @return String con el resultado estético.
     */
    public String getResultadoFormateado() {
        return golesLocal + " - " + golesVisitante;
    }

    // --- GETTERS ESTÁNDAR ---

    public String getIdPartido() { return idPartido; }
    public LocalTime getHora() { return hora; }
    public LocalDate getFechaEnfrentamiento() { return fechaEnfrentamiento; }
    public Equipos getEquipo1() { return equipo1; }
    public Equipos getEquipo2() { return equipo2; }
    public Jornadas getJornada() { return jornadas; }
    public int getGolesLocal() { return golesLocal; }
    public int getGolesVisitante() { return golesVisitante; }

    // --- SETTERS ---

    public void setIdPartido(String idPartido) { this.idPartido = idPartido; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public void setFechaEnfrentamiento(LocalDate fechaEnfrentamiento) { this.fechaEnfrentamiento = fechaEnfrentamiento; }
    public void setEquipo1(Equipos equipo1) { this.equipo1 = equipo1; }
    public void setEquipo2(Equipos equipo2) { this.equipo2 = equipo2; }
    public void setJornada(Jornadas jornadas) { this.jornadas = jornadas; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }
}