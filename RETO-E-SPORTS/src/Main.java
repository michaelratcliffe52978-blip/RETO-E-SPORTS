import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

    final private static Scanner scanner = new Scanner(System.in);
    final private static StringBuilder sb = new StringBuilder();
    private static int opcion, numeroEquipo, numeroJugador;;
    private static String nombreEquipo, ciudadEquipo;
    private static double sueldoJugador;
    private static Pattern pattern = Pattern.compile("^[A-Za-z ]{1,15}$");
    private static Matcher matcher;

    public static void main(String[] args) {
        do {
            menu();
            switch (opcion) {
                case 1:
                    equipo();
                    break;
                case 2:
                    jugador();
                    break;
                case 3:
                    System.out.println("Saliendo del programa.");
                    return;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
        while (opcion != 3);
    }


    public static void menu() {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Registrar equipo");
        System.out.println("2. Registrar jugador");
        System.out.println("3. Salir");
        opcion = scanner.nextInt();
        scanner.nextLine();
    }

    public static void equipo() {
        fnequipo();
        for (int i = 0; i < numeroEquipo; i++) {
            fnombreequipo();

            fciudadequipo();

            finaguracionequipo();
        }
        System.out.println("Los equipos registrados son el: " + sb);

}



    public static void jugador () {
        fnjugadores();

        for (int i = 0; i < numeroJugador; i++) {

            fnombrejugador();

            fapellidojugador();

            fnicknamejugador();

            froljugador();

            fsueldojugador();

            fnacionalidadjugador();

            ffechajugador();

        }
        System.out.println("Los jugadores registrados son: " + sb);

    }



    public static void fnequipo () {
        do {
            System.out.print("Ingrese número de equipos (par): ");
            Pattern pattern = Pattern.compile("^[2-6]$");
            numeroEquipo = scanner.nextInt();
            scanner.nextLine();
        }
        while(numeroEquipo % 2 != 0);
    }

    public static void fnombreequipo () {
        do {
            System.out.print("Ingrese el nombre del equipo (máx. 15 letras): ");
            nombreEquipo = scanner.nextLine();
            sb.append(" ,").append(nombreEquipo);
            matcher = pattern.matcher(nombreEquipo);

            if (!matcher.matches()) {
                System.out.println("Nombre inválido. Use solo letras y máximo 15 caracteres.");
            }
        } while (!matcher.matches());
    }

    public static void fciudadequipo () {
        do {
            System.out.print("Ingrese la ciudad del equipo (máx. 15 letras): ");
            ciudadEquipo = scanner.nextLine();
            matcher = pattern.matcher(ciudadEquipo);

            if (!matcher.matches()) {
                System.out.println("Ciudad inválida. Use solo letras y máximo 15 caracteres.");
            }
        } while (!matcher.matches());
    }

    public static void finaguracionequipo () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaEquipoLocal = null;
        boolean fechaValida;
        do {
            System.out.print("Ingrese la fecha de fundación (dd/MM/yyyy): ");
            String fechaEquipo = scanner.nextLine();

            try {
                fechaEquipoLocal = LocalDate.parse(fechaEquipo, formatter);
                fechaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use el formato correcto: dd/MM/yyyy");
                fechaValida = false;
            }
        } while (!fechaValida);
    }

    public static void fnjugadores () {
        do {
            System.out.print("Ingrese número de jugadores (1-6): ");
            Pattern pattern = Pattern.compile("^[1-6]$");
            numeroJugador = scanner.nextInt();
            scanner.nextLine();
        }
        while(numeroJugador < 1 || numeroJugador > 6);
    }

    public static void fnombrejugador () {
        do {
            System.out.print("Ingrese su nombre: ");
            String nombreJugador = scanner.nextLine();
            sb.append(" ,").append(nombreJugador);
            matcher = pattern.matcher(nombreJugador);

            if (!matcher.matches()) {
                System.out.println("Nombre inválido. Use solo letras y máximo 15 caracteres.");
            }
        } while (!matcher.matches());
    }

    public static void fapellidojugador () {
        do {
            System.out.print("Ingrese su apellido: ");
            String apellidoJugador = scanner.nextLine();
            matcher = pattern.matcher(apellidoJugador);

            if (!matcher.matches()) {
                System.out.println("Apellido inválido. Use solo letras y máximo 15 caracteres.");
            }
        } while (!matcher.matches());
    }

    public static void fnicknamejugador () {
        System.out.print("Ingrese su nickname: ");
        String nicknameJugador = scanner.nextLine();
    }

    public static void froljugador () {
        do {
            System.out.print("Ingrese su rol: ");
            String rolJugador = scanner.nextLine();
            matcher = pattern.matcher(rolJugador);

            if (!matcher.matches()) {
                System.out.println("Rol inválido. Use solo letras y máximo 15 caracteres.");
            }
        } while (!matcher.matches());
    }

    public static void fsueldojugador () {
        do {
            System.out.println("Ingrese su sueldo:");
            sueldoJugador = scanner.nextDouble();
            scanner.nextLine();
            if (sueldoJugador < 1184) {
                System.out.println("Sueldo inválido. Debe ser al menos 1184.");
            }
        } while (sueldoJugador < 1184);
    }

    public static void fnacionalidadjugador () {
        do {
            System.out.print("Ingrese su nacionalidad: ");
            String nacionalidadJugador = scanner.nextLine();
            matcher = pattern.matcher(nacionalidadJugador);

            if (!matcher.matches()) {
                System.out.println("Nacionalidad inválida. Use solo letras y máximo 15 caracteres");
            }
        } while (!matcher.matches());
    }

    public static void ffechajugador () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaJugadorLocal = null;
        boolean fechaValida;
        do {
            System.out.print("Ingrese su fecha de nacimiento: ");
            String fechaJugador = scanner.nextLine();

            try {
                fechaJugadorLocal = LocalDate.parse(fechaJugador, formatter);
                fechaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use el formato correcto: dd/MM/yyyy");
                fechaValida = false;
            }
        } while (!fechaValida);
    }

}