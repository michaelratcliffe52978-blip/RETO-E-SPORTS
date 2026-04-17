package org.example.programacion.Modelo;

import java.util.Scanner;

/**
 * Esta clase representa al Administrador del sistema.
 * Hereda de {@link Usuarios} y tiene permisos especiales para gestionar
 * el calendario, cerrar inscripciones, meter resultados y realizar el CRUD.
 * * @author equipo4
 * @version 1.0
 */
public class Admins extends Usuarios {

    /** Escáner para leer la entrada del administrador por consola (para pruebas) */
    private final static Scanner sc = new Scanner(System.in);

    /**
     * Constructor para crear un administrador.
     * * @param idUsuario El ID único del administrador.
     * @param nombreUsuario El nombre de login.
     * @param contrasena La clave de acceso.
     */
    public Admins(String idUsuario, String nombreUsuario, String contrasena) {
        super(idUsuario, nombreUsuario, contrasena);
    }

    /**
     * Lógica para generar el calendario de enfrentamientos de la liga.
     */
    public void generarCalendario() {
        // Por implementar: Llamada al DAO correspondiente
    }

    /**
     * Cambia el estado de la competición para impedir más cambios.
     * * @param estado El estado actual que se desea cambiar.
     */
    public void cerrarEstado(String estado) {
        // Nota: En la implementación final, esto debería actualizar un campo en la BD
        estado = "cerrado";
    }

    /**
     * Permite al administrador anotar los goles o puntos de un encuentro.
     * * @param resultado El marcador final.
     * @param id_partido El identificador del partido a actualizar.
     */
    public void introducirResultados(String resultado, String id_partido) {
        // Por implementar: Lógica de actualización
    }

    /**
     * Muestra por pantalla la información detallada de un partido concreto.
     * * @param resultado Texto con el resultado.
     * @param id_partido ID del partido consultado.
     */
    public void verInformes(String resultado, String id_partido) {
        System.out.println("El informe del partido " + id_partido + " es " + resultado);
    }

    /**
     * Menú interactivo por consola para gestionar las operaciones básicas (CRUD).
     * Permite elegir entre Crear, Leer, Actualizar o Borrar registros.
     */
    public void CRUD() {
        System.out.println("Que desea: \n1. Crear \n2. Leer \n3. Actualizar \n4. Borrar");
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                System.out.println("Accediendo a Crear...");
                break;
            case 2:
                System.out.println("Accediendo a Leer...");
                break;
            case 3:
                System.out.println("Accediendo a Actualizar...");
                break;
            case 4:
                System.out.println("Accediendo a Borrar...");
                break;
            default:
                System.out.println("Opcion no valida");
                break;
        }
    }
}