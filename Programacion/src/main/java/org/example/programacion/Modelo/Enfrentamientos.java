package org.example.programacion.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Enfrentamientos {
    private String idPartido;
    private LocalTime hora;
    private LocalDate fechaEnfrentamiento;
    private Equipos equipo1;
    private Equipos equipo2;

    // Relación con Jornada para obtener el número y campos para el resultado
    private Jornadas jornadas;
    private int golesLocal;
    private int golesVisitante;

    // Constructor completo para que el DAO pueda pasarle todos los datos
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

    // --- GETTERS PARA LA TABLA (IMPORTANTE) ---

    // Este lo usa colJornada (PropertyValueFactory("numeroDeJornada"))
    public int getNumeroDeJornada() {
        return (jornadas != null) ? jornadas.getNumeroJornada() : 0;
    }

    // Este lo usa colMarcador (PropertyValueFactory("resultadoFormateado"))
    public String getResultadoFormateado() {
        return golesLocal + " - " + golesVisitante;
    }

    public String getIdPartido() {
        return idPartido;
    }

    public LocalTime getHora() {
        return hora;
    }

    public LocalDate getFechaEnfrentamiento() {
        return fechaEnfrentamiento;
    }

    public Equipos getEquipo1() {
        return equipo1;
    }

    public Equipos getEquipo2() {
        return equipo2;
    }

    public Jornadas getJornada() {
        return jornadas;
    }

    // --- SETTERS ---
    public void setIdPartido(String idPartido) { this.idPartido = idPartido; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public void setFechaEnfrentamiento(LocalDate fechaEnfrentamiento) { this.fechaEnfrentamiento = fechaEnfrentamiento; }
    public void setEquipo1(Equipos equipo1) { this.equipo1 = equipo1; }
    public void setEquipo2(Equipos equipo2) { this.equipo2 = equipo2; }
    public void setJornada(Jornadas jornadas) { this.jornadas = jornadas; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }
    public int getGolesLocal() { return golesLocal; }
    public int getGolesVisitante() { return golesVisitante; }
}