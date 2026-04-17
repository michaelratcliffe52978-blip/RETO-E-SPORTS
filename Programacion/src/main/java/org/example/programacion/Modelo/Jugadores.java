package org.example.programacion.Modelo;

import java.time.LocalDate;

/**
 * Esta clase representa a un Jugador (pro-player) dentro del sistema.
 * Almacena toda su información personal, profesional (como el nickname y el rol)
 * y financiera (sueldo).
 * * @author equipo4
 * @version 1.0
 */
public class Jugadores {
    /** Identificador único del jugador en la base de datos */
    private String idJugador;
    /** Nombre de pila del jugador */
    private String nombreJugador;
    /** Apellido del jugador */
    private String apellido;
    /** País de origen del jugador */
    private String nacionalidad;
    /** Fecha de nacimiento para control de edad */
    private LocalDate fechaNacimiento;
    /** Nombre de guerra o alias deportivo */
    private String nickname;
    /** Posición o función en el equipo (ej: Top, Mid, Jungla...) */
    private String rol;
    /** Remuneración económica del jugador */
    private double sueldo;

    /**
     * Constructor completo para crear un objeto Jugador con todos sus datos.
     * * @param idJugador ID único.
     * @param nombreJugador Nombre real.
     * @param apellido Apellido real.
     * @param nacionalidad Nacionalidad.
     * @param fechaNacimiento Fecha de nacimiento.
     * @param nickname Alias en el juego.
     * @param rol Rol desempeñado.
     * @param sueldo Sueldo anual/mensual.
     */
    public Jugadores(String idJugador, String nombreJugador, String apellido, String nacionalidad,
                     LocalDate fechaNacimiento, String nickname, String rol, double sueldo) {
        this.idJugador = idJugador;
        this.nombreJugador = nombreJugador;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.nickname = nickname;
        this.rol = rol;
        this.sueldo = sueldo;
    }

    // --- GETTERS ---

    /** @return El ID del jugador */
    public String getIdJugador() { return idJugador; }

    /** @return El nombre del jugador */
    public String getNombreJugador() { return nombreJugador; }

    /** @return El apellido del jugador */
    public String getApellido() { return apellido; }

    /** @return La nacionalidad del jugador */
    public String getNacionalidad() { return nacionalidad; }

    /** @return La fecha de nacimiento */
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    /** @return El apodo (nickname) del jugador */
    public String getNickname() { return nickname; }

    /** @return El rol o posición en el juego */
    public String getRol() { return rol; }

    /** @return El sueldo del jugador */
    public double getSueldo() { return sueldo; }

    // --- SETTERS ---

    public void setIdJugador(String idJugador) { this.idJugador = idJugador; }
    public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setRol(String rol) { this.rol = rol; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }
}