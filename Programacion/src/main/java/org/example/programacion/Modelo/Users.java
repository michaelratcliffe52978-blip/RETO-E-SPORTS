package org.example.programacion.Modelo;

import java.time.LocalDate;

/**
 * Esta clase representa a un Usuario estándar del sistema.
 * Hereda de {@link Usuarios} y, a diferencia del administrador, este perfil
 * tiene permisos limitados, principalmente enfocados en la consulta de
 * informes de equipos y resultados de los partidos.
 * * @author equipo4
 * @version 1.0
 */
public class Users extends Usuarios {

    /**
     * Constructor para crear un usuario estándar.
     * * @param idUsuario El ID único del usuario.
     * @param nombreUsuario El nombre de acceso (login).
     * @param contrasena La clave de acceso.
     */
    public Users(String idUsuario, String nombreUsuario, String contrasena) {
        super(idUsuario, nombreUsuario, contrasena);
    }

    /**
     * Muestra por consola la información básica de un equipo.
     * * @param id_equipo El identificador del equipo.
     * @param nombe_equipo El nombre oficial del club.
     * @param fechaFundacion La fecha en la que se fundó.
     */
    public void verInformeEquipo(String id_equipo, String nombe_equipo, LocalDate fechaFundacion) {
        System.out.println("El equipo " + nombe_equipo + " con id '" + id_equipo + "' fundado el " + fechaFundacion);
    }

    /**
     * Muestra por consola el resultado final de un partido específico.
     * * @param resultado El marcador o informe del encuentro.
     * @param id_partido El identificador del enfrentamiento.
     */
    public void verInformeResultados(String resultado, String id_partido) {
        System.out.println("El resultado del partido " + id_partido + " es " + resultado);
    }

}