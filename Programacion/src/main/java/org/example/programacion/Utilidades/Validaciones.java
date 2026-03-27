package org.example.programacion.Utilidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Validaciones {

    final private static Scanner sc = new Scanner(System.in);

    private static String leerTextoNoVacio(String msg) {
        String txt;
        do {
            System.out.print(msg);
            txt = sc.nextLine().trim();
        } while(txt.isEmpty());

        return txt;
    }

    private static LocalTime leerHora(String msg) {
        LocalTime hora = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");

        boolean ok;
        do {
            ok = true;

            try {
                System.out.print(msg);
                hora = LocalTime.parse(sc.nextLine(), formato);
            } catch (Exception var5) {
                ok = false;
                System.out.println("Formato inválido. Usa HH:mm.");
            }
        } while(!ok);

        return hora;
    }

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
            if (n < (double)1221.0F) {
                System.out.print("\nEl número debe ser mayor o igual que 1221. ");
            }
        } while(n < (double)1221.0F);

        return n;
    }

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

    private static int patterNJugadores() {
        String njug;
        do {
            System.out.print("Cuantos jugadores quiere introducir en este equipo (2-6): ");
            njug = sc.nextLine();
        } while(!njug.matches("^[2-6]{1}$"));

        int nJugadores = Integer.parseInt(njug);
        return nJugadores;
    }

    private static int patterNEquipos() {
        boolean w = false;

        int nequip;
        do {
            System.out.print("Cuantos equipos quiere introducir (par): ");
            nequip = sc.nextInt();
            if (nequip % 2 == 0 && nequip > 0) {
                nequip = nequip;
                w = true;
            } else {
                System.out.println("El número debe ser par y mayor que 0. Inténtalo de nuevo.");
                w = false;
            }
        } while(!w);

        return nequip;
    }

    private static String paterResultado() {
        String tipo;
        do {
            System.out.print("Tipo (Victoria local/Derrota local): ");
            tipo = sc.nextLine();
        } while(!tipo.equalsIgnoreCase("Victoria local") && !tipo.equalsIgnoreCase("Derrota local"));

        return tipo;
    }

    private static String leerRol() {
        String tipo;
        do {
            System.out.print("Tipo (Centinela/Duelista/Iniciador/Controlador): ");
            tipo = sc.nextLine();
        } while(!tipo.equalsIgnoreCase("Centinela") && !tipo.equalsIgnoreCase("Duelista") && !tipo.equalsIgnoreCase("Controlador") && !tipo.equalsIgnoreCase("Iniciador"));

        return tipo;
    }

    private static String leerEstado() {
        String estado;
        do {
            System.out.print("Estado (Abierto/Cerrado/En curso/Finalizado): ");
            estado = sc.nextLine();
        } while(!estado.equalsIgnoreCase("Abierto") && !estado.equalsIgnoreCase("Cerrado") && !estado.equalsIgnoreCase("En curso") && !estado.equalsIgnoreCase("Finalizado"));

        return estado;
    }

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
            } catch (Exception var5) {
                ok = false;
                System.out.println("Formato inválido. Usa dd/MM/yyyy.");
            }
        } while(!ok);

        return fecha;
    }

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
            } catch (Exception var5) {
                ok = false;
                System.out.println("Formato inválido. Usa dd/MM/yyyy.");
            }
        } while(!ok);

        return fecha;
    }

}
