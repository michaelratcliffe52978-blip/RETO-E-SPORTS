package Modulo;

import java.lang.foreign.GroupLayout;
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

    public Jugadores(String idJugador, String nombreJugador, String apellido, String nacionalidad, LocalDate fechaNacimiento, String nickname, String rol, double sueldo) {
        this.idJugador = idJugador;
        this.nombreJugador = nombreJugador;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.nickname = nickname;
        this.rol = rol;
        this.sueldo = sueldo;
    }

    public String getidJugador() {return idJugador;}
    public void setidJugador(String idJugador) {this.idJugador = idJugador;}
    public String getnombreJugador() {return nombreJugador;}
    public void setnombreJugador(String nombreJugador) {this.nombreJugador = nombreJugador;}
    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public String getNacionalidad() {return nacionalidad;}
    public void setNacionalidad(String nacionalidad) {this.nacionalidad = nacionalidad;}
    public LocalDate getfechaNacimiento() {return fechaNacimiento;}
    public void setfechaNacimiento(LocalDate fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}
    public String getRol() {return rol;}
    public void setRol(String rol) {this.rol = rol;}
    public double getSueldo() {return sueldo;}
    public void setSueldo(double sueldo) {this.sueldo = sueldo;}


}
