
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.String;

public class Main {
    final private static Scanner sc = new Scanner(System.in);
    final private static StringBuilder sb = new StringBuilder("Registro");
    final private static DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yy");
    final private static double SMI = 1134.00;


    public static void main(String[] args) throws Exception {

        try {
            equipo();
            jugadores();
            new DatosNoValido();
            new FormatoNoAdecuado();
            new EspacioEmpty();
            new FechaIncorrecta();

        } catch (Exception e) {
            System.out.println("Problemas \n" +
                    e.getClass() + "\n" +
                    e.getMessage() + "\n" +
                    "El programa finaliza");
        }

    }

    public static void equipo() {
        NumeroDeEquipos();
        NombreDeEquipos();
        FechaDeFundacion();
    }
    public static void NumeroDeEquipos() {
        boolean error = false;
        int nEquipos = 0;
        do {
            error = false;
            System.out.print("Ingresa el numero de equipos(número par) :");

            try {
                String equipos = sc.nextLine();
                if (equipos.isEmpty())
                    throw new EspacioEmpty();
                nEquipos = Integer.parseInt(equipos);
                if (nEquipos <= 0 || nEquipos % 2 != 0)
                    throw new DatosNoValido();
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
            } catch (DatosNoValido e) {
                System.out.println("Debe ser un número par mayor que 0.");
            }
        }while (!error);
    }
    public static void NombreDeEquipos() {
        boolean error;
        do {
            error = false;

            System.out.println("Nombre del equipo: ");
            try {
                String nombreEquipo = sc.nextLine();
                if (nombreEquipo.isEmpty())
                    throw new EspacioEmpty();
            }catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
            }
        }while (!error);
    }

    public static void FechaDeFundacion() {
        boolean error;
        do {
            error=false;
            System.out.println("Teclea la fecha de la fundación del equipo(dd/mm/yy): ");
            try{
                String fecha = sc.nextLine();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");

                if(fecha.isEmpty()){
                    throw new EspacioEmpty();}

                LocalDate fechaFundacion = LocalDate.parse(fecha, formato);

                System.out.println("Fecha de fundación registrada: " + fechaFundacion.format(formato));

                if (fechaFundacion.isAfter(LocalDate.now())) {
                    throw new FechaIncorrecta();}
            }catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
            }catch (FechaIncorrecta e){
                System.out.println("La fecha no puede ser futura.");
            }

        }while (!error);

    }

    public static void jugadores() {
        NumeroDeJugadores();
        NombreJugador();
        NacionalidadJugador();
        EdadJugador();
        FechaNacimientoJugador();
        Nickname();
        Rol();
        Sueldo();
    }
    public static void NumeroDeJugadores() {
        boolean error;
        int njugadores = 0;

        do {
            error = false;
            System.out.print("¿Cuántos jugadores hay en la plantilla? (1-6): ");
            try {
                String jugadores = sc.nextLine().trim();
                if (jugadores.isEmpty()) throw new DatosNoValido();

                njugadores = Integer.parseInt(jugadores);
                if (njugadores < 1 || njugadores > 6) throw new DatosNoValido();

            } catch (DatosNoValido e) {
                System.out.println("Debe ser un número entre 1 y 6 y no puede estar vacío.");
                error = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
                error = true;
            }
        } while (error);

        // Mostrar el número confirmado
        System.out.println("Número de jugadores confirmado: " + njugadores);

        // Ahora pedimos los datos de cada jugador
        for (int i = 1; i <= njugadores; i++) {
            System.out.println("\n--- Jugador " + i + " ---");
            NombreJugador();
            NacionalidadJugador();
            EdadJugador();
            FechaNacimientoJugador();
            Nickname();
            Rol();
            Sueldo();
        }
    }
    public static void NombreJugador() {
        boolean error;
        do {
            error = false;
            System.out.print("Nombre del jugador: ");
            try {
                String nombre = sc.nextLine().trim();
                if (nombre.isEmpty()) throw new EspacioEmpty();
                System.out.println("Nombre registrado: " + nombre);
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            }
        } while (error);
    }
    public static void NacionalidadJugador() {
        boolean error;
        do {
            error = false;
            System.out.print("Nacionalidad del jugador: ");
            try {
                String nacionalidad = sc.nextLine().trim();
                if (nacionalidad.isEmpty()) throw new EspacioEmpty();
                System.out.println("Nacionalidad registrada: " + nacionalidad);
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            }
        } while (error);
    }
    public static void EdadJugador() {
        boolean error;
        do {
            error = false;
            System.out.print("Edad del jugador: ");
            try {
                String edad = sc.nextLine().trim();
                if (edad.isEmpty()) throw new EspacioEmpty();
                int num = Integer.parseInt(edad);
                if (num <= 0) throw new DatosNoValido();
                System.out.println("Edad registrada: " + num);
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
                error = true;
            } catch (DatosNoValido e) {
                System.out.println("La edad debe ser mayor que 0.");
                error = true;
            }
        } while (error);
    }
    public static void FechaNacimientoJugador() {
        boolean error;
        do {
            error = false;
            System.out.print("Fecha de nacimiento (dd/MM/yy): ");
            try {
                String fecha = sc.nextLine().trim();
                if (fecha.isEmpty()) throw new EspacioEmpty();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
                LocalDate fnac = LocalDate.parse(fecha, formato);
                System.out.println("Fecha de nacimiento registrada: " + fnac.format(formato));
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Formato inválido. Usa dd/MM/yy (ej: 05/11/25).");
                error = true;
            }
        } while (error);
    }
    public static void Nickname() {
        boolean error;
        do {
            error = false;
            System.out.print("Nickname del jugador: ");
            try {
                String nick = sc.nextLine().trim();
                if (nick.isEmpty()) throw new EspacioEmpty();
                System.out.println("Nickname registrado: " + nick);
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            }
        } while (error);
    }
    public static void Rol() {
        boolean error;
        do {
            error = false;
            System.out.print("Rol del jugador: ");
            try {
                String rol = sc.nextLine().trim();
                if (rol.isEmpty()) throw new EspacioEmpty();
                System.out.println("Rol registrado: " + rol);
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            }
        } while (error);
    }
    public static void Sueldo() {
        boolean error;
        final double SMI = 1134.00;
        do {
            error = false;
            System.out.print("Sueldo (€): ");
            try {
                String s = sc.nextLine().trim().replace(',', '.');
                if (s.isEmpty()) throw new EspacioEmpty();
                double sueldo = Double.parseDouble(s);
                if (sueldo <= SMI) throw new DatosNoValido();
                System.out.println("Sueldo registrado: " + sueldo + " €");
            } catch (EspacioEmpty e) {
                System.out.println("No puedes dejar este espacio vacío.");
                error = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce una cantidad numérica válida (ej: 1500.00).");
                error = true;
            } catch (DatosNoValido e) {
                System.out.println("El sueldo debe ser mayor que el SMI (" + SMI + " €).");
                error = true;
            }
        } while (error);
    }




    public static String DatosNoValido(String dato, String mensaje, String expresionRegular) {

        String var = "";
        boolean error;
        do {
            try {
                var = JOptionPane.showInputDialog(mensaje);

                if (var.isEmpty()) {
                    throw new DatosNoValido();
                }
                Pattern pat = Pattern.compile(expresionRegular);
                Matcher mat = pat.matcher(var);

                if (!mat.matches())
                    throw new FormatoNoAdecuado();


            } catch (DatosNoValido e) {
                error = true;
                System.out.println(dato + " no puede estar vacio");
            } catch (FormatoNoAdecuado ex) {}
            error = true;
            System.out.println(dato + "no tiene un formato adecuado");
        } while (error);
        return var;

    }
}

