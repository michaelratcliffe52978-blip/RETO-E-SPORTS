package Modulo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Enfrentamiento {

    private String idPartido;
    private LocalTime hora;
    private LocalDate fechaEnfrentamiento;
    private String resultado;
    private Equipos equipo1;
    private Equipos equipo2;

    public Enfrentamiento(String idPartido, LocalTime hora, LocalDate fechaEnfrentamiento, String resultado, Equipos equipo1, Equipos equipo2) {
        this.idPartido = idPartido;
        this.hora = hora;
        this.fechaEnfrentamiento = fechaEnfrentamiento;
        this.resultado = resultado;
        this.equipo1 = equipo1;
        this.equipo2 =  equipo2;
    }

    public Enfrentamiento(String idPartido, LocalTime hora, LocalDate fechaEnfrentamiento, Equipos equipo1, Equipos equipo2) {
        this.idPartido = idPartido;
        this.hora = hora;
        this.fechaEnfrentamiento = fechaEnfrentamiento;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
    }

    public String getidPartido() {return idPartido;}
    public void setidPartido(String idPartido) {this.idPartido = idPartido;}
    public LocalTime getHora() {return hora;}
    public void setHora(LocalTime hora) {this.hora = hora;}
    public LocalDate getfechaEnfrentamiento() {return fechaEnfrentamiento;}
    public void setfechaEnfrentamiento(LocalDate fechaEnfrentamiento) {this.fechaEnfrentamiento = fechaEnfrentamiento;}
    public String getResultado() {return resultado;}
    public void setResultado(String resultado) {this.resultado = resultado;}
    public Equipos getEquipo1() {return equipo1;}
    public void setEquipo1(Equipos equipo1) {this.equipo1 = equipo1;}
    public Equipos getEquipo2() {return equipo2;}
    public void setEquipo2(Equipos equipo2) {this.equipo2 = equipo2;}

}
