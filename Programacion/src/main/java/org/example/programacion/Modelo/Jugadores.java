package org.example.programacion.Modelo;

import java.time.LocalDate;

public class Jugadores {
    private String idJugador;
    private String nombreJugador;
    private String apellido;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String nickname;
    private String rol;
    private double sueldo;

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

    public String getIdJugador() { return idJugador; }
    public String getNombreJugador() { return nombreJugador; }
    public String getApellido() { return apellido; }
    public String getNacionalidad() { return nacionalidad; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getNickname() { return nickname; }
    public String getRol() { return rol; }
    public double getSueldo() { return sueldo; }

    // Setters
    public void setIdJugador(String idJugador) { this.idJugador = idJugador; }
    public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setRol(String rol) { this.rol = rol; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }
}