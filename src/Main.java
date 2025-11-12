import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final private static Scanner sc = new Scanner(System.in);
    private static String nombrejugador,apellidojugador, fechanacimiento, njugador, nombreEquipo,fechaFundacion;
    private static int nmenu;

    public static void main(String[] args) {
       do {
           menu();
           switch (nmenu) {
               case 1:
                   equipos();
                   break;
               case 2:
                   jugadores();
                   break;
               case 3:
                   System.out.println("Saliendo del programa...");
               default:
                   System.out.println("Opcion no valida");
           }
       }while (nmenu != 3);


    }

    public static void menu(){
        System.out.println("------- M E N Ú -------");
        System.out.println("1. Equipos");
        System.out.println("2. Jugadores");
        System.out.println("3. Finalizar");
        System.out.print("Introduce tu opción: ");
        nmenu = sc.nextInt();
        sc.nextLine();
    }

    public static void equipos(){
        int nEquipo;
        do{
            System.out.print("Introduce el número de equipos (PAR): ");
            Pattern pat = Pattern.compile("^[2-6]$");
            nEquipo = sc.nextInt();
            sc.nextLine();
        }while (nEquipo % 2 != 0);

        for(int i=0; i < nEquipo; i++){
            boolean nombreValido = false;
            do {
                System.out.print("\nIntroduce el nombre del equipo (máx 30): ");
                nombreEquipo = sc.nextLine();

                if (nombreEquipo.isEmpty()) {
                    System.out.println("❌ No puede estar vacío.");
                } else if (nombreEquipo.length() < 2) {
                    System.out.println("❌ No puede ser un nombre tan corto.");
                } else if (nombreEquipo.length() > 30) {
                    System.out.println("❌ No puede ser un nombre tan largo.");
                } else {
                    nombreValido = true; //
                }
            } while (!nombreValido);


            boolean fechaValida = false;
            while (!fechaValida) {
                System.out.print("Introduce la fecha de fundacion del equipo (dd/MM/yyyy): ");
                fechaFundacion = sc.nextLine();
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fecha = LocalDate.parse(fechaFundacion, formatter);
                    System.out.println("Fecha introducida correctamente.");
                    fechaValida = true; //salimos del bucle
                }catch (Exception e){
                    System.out.println("❌ Fecha no válida. Inténtelo de nuevo.");
                }
            }
            int numeroJugadores=0;
            boolean njugadoresValido = false;
            do {
                System.out.print("Introduce el numero del jugadores de "+nombreEquipo +": ");
                if(sc.hasNextInt()){
                    numeroJugadores = sc.nextInt();

                    if (numeroJugadores < 0) {
                        System.out.println("❌ Los jugadores no pueden ser negativos.");
                    }else {
                        njugadoresValido = true;
                    }
                }else {
                    System.out.println("❌ Debes introducir un número válido.");
                    sc.next(); // limpia el buffer del teclado
                }
            }while (!njugadoresValido);

            System.out.println("✅ Equipo registrado: " + nombreEquipo + " con " + numeroJugadores + " jugadores.");
        }
    }

    public static void jugadores() {

    boolean njugadorValido = false;
        do {
            System.out.println("\n------ NUMERO DE JUGADORES (1-6) ------");
            njugador = sc.nextLine();

            Pattern pat = Pattern.compile("^[1-6]$");
            Matcher mat = pat.matcher(njugador);

            if (!mat.matches()) {
                System.out.println("Introduce un número válido entre 1 y 6.");
            } else {
                njugadorValido = true;
            }
        }while (!njugadorValido);

        // Convertir el texto a número entero
        int numeroJugafores = Integer.parseInt(njugador);
        for(int i=0;i< numeroJugafores;i++){
            boolean nombreValido = false;
            do {
                System.out.print("\nIntroduce el nombre del jugador (máx 20): ");
                nombrejugador = sc.nextLine();

                if (nombrejugador.isEmpty()) {
                    System.out.println("❌ No puede estar vacío.");
                } else if (nombrejugador.length() < 2) {
                    System.out.println("❌ No puede ser un nombre tan corto.");
                } else if (nombrejugador.length() > 20) {
                    System.out.println("❌ No puede ser un nombre tan largo.");
                } else {
                    nombreValido = true;
                }
            } while (!nombreValido);


            boolean apellidoValido = false;
            do {
                System.out.print("Introduce el apellido del jugador: ");
                apellidojugador = sc.nextLine();

                if (apellidojugador.isEmpty()) {
                    System.out.println("❌ No puede estar vacío.");
                } else if (apellidojugador.length() < 2) {
                    System.out.println("❌ No puede ser un apellido tan corto.");
                } else if (apellidojugador.length() > 60) {
                    System.out.println("❌ No puede ser un apellido tan largo.");
                } else {
                    apellidoValido = true; // ✅ Solo aquí es válido
                }
            } while (!apellidoValido); // Se repite mientras NO sea válido

            System.out.print("Introduce la nacionalidad del jugador: ");
            String nacionalidad = sc.nextLine();

            boolean fechaValida = false;
            while (!fechaValida) {
                System.out.print("Introduce la fecha de nacimiento del jugador (dd/MM/yyyy): ");
                fechanacimiento = sc.nextLine();
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fecha = LocalDate.parse(fechanacimiento, formatter);
                    System.out.println("✅ Fecha introducida correctamente.");
                    fechaValida = true; //salimos del bucle
                }catch (Exception e){
                    System.out.println("❌ Fecha no válida. Inténtelo de nuevo.");
                }
            }

            System.out.print("Introduce el nickname del jugador: ");
            String nickname = sc.nextLine();


            System.out.print("Introduce el rol del jugador: ");
            String rol = sc.nextLine();

            int sueldo=0;
            boolean sueldoValido = false;
            do {
                System.out.print("Introduce el sueldo del jugador: ");
                if(sc.hasNextInt()){
                    sueldo = sc.nextInt();

                    if (sueldo < 0) {
                        System.out.println("❌ El sueldo no puede ser negativo.");
                    }else {
                        sueldoValido = true;
                    }
                }else {
                    System.out.println("❌ Debes introducir un número válido.");
                    sc.next(); // limpia el buffer del teclado
                }
            }while (!sueldoValido);
            sc.nextLine();

            System.out.print("Nombre: "+ nombrejugador +
                    "\nApellido: " + apellidojugador +
                    "\nNacionalidad: "+ nacionalidad +
                    "\nFecha de nacimiento: "+fechanacimiento +
                    "\nNickname: "+nickname +
                    "\nRol: "+ rol +
                    "\nSueldo: "+ sueldo
            );
        }
    }
}
