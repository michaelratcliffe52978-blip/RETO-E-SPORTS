package org.example.programacion.Modelo;

import java.time.LocalDate;

public class Users extends Usuarios {

    public Users(String idUsuario, String nombreUsuario, String contrasena) {
        super(idUsuario, nombreUsuario, contrasena);
    }

    public void verInformeEquipo(String id_equipo, String nombe_equipo, LocalDate fechaFundacion) {
        System.out.println("El equipo " + nombe_equipo + " con id '" + id_equipo + "' fundado el " + fechaFundacion);
    }

    public void verInformeResultados(String resultado, String id_partido) {
        System.out.println("El resultado del partido " + id_partido + " es " + resultado);
    }

}
