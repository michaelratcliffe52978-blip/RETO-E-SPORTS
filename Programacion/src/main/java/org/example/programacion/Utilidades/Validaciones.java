package org.example.programacion.Utilidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Esta clase contiene métodos estáticos para validar las entradas por consola.
 * Asegura que los datos cumplen con los formatos requeridos (fechas, horas,
 * números pares, roles específicos) antes de que lleguen a los modelos o DAOs.
 * * @author equipo4
 * @version 1.0
 */
public class Validaciones {

    /** Escáner compartido para la lectura de datos */
    final private static Scanner sc = new Scanner(System.in);

    /**
     * Obliga al usuario a introducir una cadena de texto que no sea solo espacios.
     * @param msg Mensaje informativo para el usuario.
     * @return El texto validado.
     */
    private static String leerTextoNoVacio(String msg) {
        String txt;
        do {
            System.out.print(msg);
            txt = sc.nextLine().trim();
        } while(txt.isEmpty());

        return txt;
    }

    /**
     * Valida que la hora introducida cumpla el formato HH:mm.
     * @param msg Mensaje informativo.
     * @return Objeto {@link LocalTime} con la hora.
     */
    private static LocalTime leerHora(String msg) {
        LocalTime hora = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");

        boolean ok;
        do {
            ok = true;
            try {
                System.out.print(msg);
                hora = LocalTime.parse(sc.nextLine(), formato);
            } catch (Exception e) {
                ok = false;
                System.out.println("Formato inválido. Usa HH:mm.");
            }
        } while(!ok);

        return hora;
    }

    /**
     * Valida que el sueldo sea mayor o igual al Salario Mínimo Profesional configurado.
     * @param msg Mensaje informativo.
     * @return El valor double validado.
     */
    private static double leerDoubleMin1221(String msg) {
        double n;
        do {
            System.out.print(msg);

            while(!sc.hasNextDouble()) {
                sc.next();
                System.out.print("Número inválido, repite: ");
            }

            n = sc.nextDouble();
            sc.nextLine();
            if (n < 1221.0) {
                System.out.print("\nEl número debe ser mayor o igual que 1221 (SMI). ");
            }
        } while(n < 1221.0);

        return n;
    }

    /**
     * Filtra el texto para que solo contenga letras y espacios (mínimo 3 caracteres).
     * @param msg Mensaje informativo.
     * @return Texto limpio.
     */
    private static String leerSoloLetras(String msg) {
        String texto;
        do {
            System.out.print(msg);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("No puede estar vacío.");
            } else if (!texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,}$")) {
                System.out.println("Solo se permiten letras.");
                texto = "";
            }
        } while(texto.isEmpty());

        return texto;
    }

    /**
     * Valida que el número de jugadores para un equipo esté entre 2 y 6.
     * @return El número entero validado.
     */
    private static int patterNJugadores() {
        String njug;
        do {
            System.out.print("Cuantos jugadores quiere introducir en este equipo (2-6): ");
            njug = sc.nextLine();
        } while(!njug.matches("^[2-6]{1}$"));

        return Integer.parseInt(njug);
    }

    /**
     * Asegura que el número de equipos introducido sea par y positivo.
     * @return Número de equipos validado.
     */
    private static int patterNEquipos() {
        boolean w = false;
        int nequip;
        do {
            System.out.print("Cuantos equipos quiere introducir (par): ");
            nequip = sc.nextInt();
            if (nequip % 2 == 0 && nequip > 0) {
                w = true;
            } else {
                System.out.println("El número debe ser par y mayor que 0. Inténtalo de nuevo.");
                w = false;
            }
        } while(!w);

        return nequip;
    }

    /**
     * Restringe el resultado de un partido a dos opciones específicas.
     * @return String con el tipo de resultado.
     */
    private static String paterResultado() {
        String tipo;
        do {
            System.out.print("Tipo (Victoria local/Derrota local): ");
            tipo = sc.nextLine();
        } while(!tipo.equalsIgnoreCase("Victoria local") && !tipo.equalsIgnoreCase("Derrota local"));

        return tipo;
    }

    /**
     * Valida que el rol del jugador sea uno de los predefinidos en eSports.
     * @return El rol validado.
     */
    private static String leerRol() {
        String tipo;
        do {
            System.out.print("Tipo (Centinela/Duelista/Iniciador/Controlador): ");
            tipo = sc.nextLine();
        } while(!tipo.equalsIgnoreCase("Centinela") && !tipo.equalsIgnoreCase("Duelista") && !tipo.equalsIgnoreCase("Controlador") && !tipo.equalsIgnoreCase("Iniciador"));

        return tipo;
    }

    /**
     * Controla que el estado de la competición sea válido.
     * @return El estado validado.
     */
    private static String leerEstado() {
        String estado;
        do {
            System.out.print("Estado (Abierto/Cerrado/En curso/Finalizado): ");
            estado = sc.nextLine();
        } while(!estado.equalsIgnoreCase("Abierto") && !estado.equalsIgnoreCase("Cerrado") && !estado.equalsIgnoreCase("En curso") && !estado.equalsIgnoreCase("Finalizado"));

        return estado;
    }

    /**
     * Valida una fecha en formato dd/MM/yyyy y comprueba que sea en el pasado.
     * @param msg Mensaje informativo.
     * @return {@link LocalDate} validado.
     */
    private static LocalDate leerFechaAnterior(String msg) {
        LocalDate fecha = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        boolean ok;
        do {
            ok = true;
            try {
                System.out.print(msg);
                fecha = LocalDate.parse(sc.nextLine(), formato);
                if (!fecha.isBefore(LocalDate.now())) {
                    ok = false;
                    System.out.println("La fecha debe ser anterior a hoy.");
                }
            } catch (Exception e) {
                ok = false;
                System.out.println("Formato inválido. Usa dd/MM/yyyy.");
            }
        } while(!ok);

        return fecha;
    }

    /**
     * Valida una fecha en formato dd/MM/yyyy y comprueba que sea futura.
     * @param msg Mensaje informativo.
     * @return {@link LocalDate} validado.
     */
    private static LocalDate leerFechaPosterior(String msg) {
        LocalDate fecha = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        boolean ok;
        do {
            ok = true;
            try {
                System.out.print(msg);
                fecha = LocalDate.parse(sc.nextLine(), formato);
                if (!fecha.isAfter(LocalDate.now())) {
                    ok = false;
                    System.out.println("La fecha debe ser posterior a hoy.");
                }
            } catch (Exception e) {
                ok = false;
                System.out.println("Formato inválido. Usa dd/MM/yyyy.");
            }
        } while(!ok);

        return fecha;
    }
}