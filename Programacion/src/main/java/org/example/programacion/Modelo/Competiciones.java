package org.example.programacion.Modelo;

import java.util.ArrayList;

/**
 * Esta clase representa una Competición dentro del sistema.
 * Es el objeto principal que engloba el nombre del torneo, su estado actual
 * (abierta/cerrada) y la lista de todas las jornadas que se van a jugar.
 * * @author equipo4
 * @version 1.0
 */
public class Competiciones {

    /** Identificador único de la competición en la base de datos */
    private String idCompeticion;
    /** Nombre del torneo o liga */
    private String nombreCompeticion;
    /** Estado de la competición (ej: 'abierto' para inscripciones o 'cerrado') */
    private String estado;
    /** Lista de jornadas que forman parte de esta competición */
    private ArrayList<Jornadas> jornadas;

    /**
     * Constructor completo para crear una nueva Competición.
     * * @param idCompeticion El ID único.
     * @param nombreCompeticion El nombre descriptivo de la liga.
     * @param estado El estado inicial del torneo.
     * @param jornadas El conjunto de jornadas que la componen.
     */
    public Competiciones(String idCompeticion, String nombreCompeticion, String estado, ArrayList<Jornadas> jornadas) {
        this.idCompeticion = idCompeticion;
        this.nombreCompeticion = nombreCompeticion;
        this.estado = estado;
        this.jornadas = jornadas;
    }

    /** @return El ID de la competición */
    public String getIdCompeticion() { return idCompeticion; }

    /** @param idCompeticion El nuevo ID a asignar */
    public void setIdCompeticion(String idCompeticion) { this.idCompeticion = idCompeticion; }

    /** @return El nombre de la competición */
    public String getNombreCompeticion() { return nombreCompeticion; }

    /** @param nombreCompeticion El nuevo nombre de la liga */
    public void setNombreCompeticion(String nombreCompeticion) { this.nombreCompeticion = nombreCompeticion; }

    /** @return El estado actual (abierto/cerrado) */
    public String getEstado() { return estado; }

    /** @param estado El nuevo estado a establecer */
    public void setEstado(String estado) { this.estado = estado; }

    /** @return La lista de jornadas asociadas */
    public ArrayList<Jornadas> getJornadas() { return jornadas; }

    /** @param jornadas La nueva lista de jornadas para esta competición */
    public void setJornadas(ArrayList<Jornadas> jornadas) { this.jornadas = jornadas; }

}