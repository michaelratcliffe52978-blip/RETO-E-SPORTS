import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

    final private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        equipo();
        jugador();
    }

    public static void equipo() {
        int numeroEquipo;
        do {
            System.out.print("Ingrese número de equipos (par): ");
            Pattern pattern = Pattern.compile("^[1-6]$");
            numeroEquipo = scanner.nextInt();
            scanner.nextLine();
        }
        while(numeroEquipo % 2 != 0);

        for (int i = 0; i < numeroEquipo; i++) {
            Pattern pattern = Pattern.compile("^[A-Za-z ]{1,15}$");
            Matcher matcher;
            String nombreEquipo;
            String ciudadEquipo;
            do {
                System.out.print("Ingrese el nombre del equipo (máx. 15 letras): ");
                nombreEquipo = scanner.nextLine();
                matcher = pattern.matcher(nombreEquipo);

                if (!matcher.matches()) {
                    System.out.println("Nombre inválido. Use solo letras y máximo 15 caracteres.");
                }
            } while (!matcher.matches());


            do {
                System.out.print("Ingrese la ciudad del equipo (máx. 15 letras): ");
                ciudadEquipo = scanner.nextLine();
                matcher = pattern.matcher(ciudadEquipo);

                if (!matcher.matches()) {
                    System.out.println("Ciudad inválida. Use solo letras y máximo 15 caracteres.");
                }
            } while (!matcher.matches());


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
}



    public static void jugador () {
        int numeroJugador;
        do {
            System.out.print("Ingrese número de jugadores (1-6): ");
            Pattern pattern = Pattern.compile("^[1-6]$");
            numeroJugador = scanner.nextInt();
            scanner.nextLine();
        }
        while(numeroJugador < 1 || numeroJugador > 6);

        for (int i = 0; i < numeroJugador; i++) {
            Pattern pattern = Pattern.compile("^[A-Za-z ]{1,15}$");
            Matcher matcher;

            do {
                System.out.print("Ingrese su nombre: ");
                String nombreJugador = scanner.nextLine();
                matcher = pattern.matcher(nombreJugador);

                if (!matcher.matches()) {
                    System.out.println("Nombre inválido. Use solo letras y máximo 15 caracteres.");
                }
            } while (!matcher.matches());

            do {
                System.out.print("Ingrese su apellido: ");
                String apellidoJugador = scanner.nextLine();
                matcher = pattern.matcher(apellidoJugador);

                if (!matcher.matches()) {
                    System.out.println("Apellido inválido. Use solo letras y máximo 15 caracteres.");
                }
            } while (!matcher.matches());

            System.out.print("Ingrese su nickname: ");
            String nicknameJugador = scanner.nextLine();

            do {
                System.out.print("Ingrese su rol: ");
                String rolJugador = scanner.nextLine();
                matcher = pattern.matcher(rolJugador);

                if (!matcher.matches()) {
                    System.out.println("Rol inválido. Use solo letras y máximo 15 caracteres.");
                }
            } while (!matcher.matches());

            double sueldoJugador;
            do {
                System.out.println("Ingrese su sueldo:");
                sueldoJugador = scanner.nextDouble();
                scanner.nextLine();
                if (sueldoJugador < 1184) {
                    System.out.println("Sueldo inválido. Debe ser al menos 1184.");
                }
            } while (sueldoJugador < 1184);

            do {
                System.out.print("Ingrese su nacionalidad: ");
                String nacionalidadJugador = scanner.nextLine();
                matcher = pattern.matcher(nacionalidadJugador);

                if (!matcher.matches()) {
                    System.out.println("Nacionalidad inválida. Use solo letras y máximo 15 caracteres.");
                }
            } while (!matcher.matches());


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

}