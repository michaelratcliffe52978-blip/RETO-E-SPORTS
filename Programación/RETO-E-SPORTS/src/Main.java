
        import javax.swing.*;
        import java.time.LocalDate;
        import java.time.format.DateTimeFormatter;
        import java.util.Scanner;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;


        public class Main {
            final private static Scanner sc = new Scanner(System.in);
            final private static StringBuilder sb = new StringBuilder("Lista nombres equipos");


            public static void main(String[] args) throws Exception {

                try {
                    equipo();
                    jugadores();
                    DatoNoValido();
                } catch (Exception e) {
                    System.out.println("Problemas \n" +
                            e.getClass() + "\n" +
                            e.getMessage() + "\n" +
                            "El programa finaliza");
                }


            }

            public static void equipo() {
                boolean error = false;
                do {
                    try {
                        System.out.println("Nombre del equipo: ");
                        String nombre = sc.nextLine();
                        System.out.println("Teclea la fecha de la fundación del equipo(dd/mm/yy): ");
                        String fecha = sc.nextLine();
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
                        LocalDate fechaFundacion = LocalDate.parse(fecha, formato);
                        System.out.println("Jugadores: ");
                        String jugadores = sc.nextLine();

                    } catch (Exception e) {

                    }


                } while (!error);
                return;
            }

            public static void jugadores() {
                int njugadores = 0;
                boolean error;

                do {
                    error = false;
                    System.out.print("¿Cuántos jugadores hay en la plantilla? (1-6) :");

                    try {
                        //judaroes en string para que el sistema detecte si esta vacio o no
                        String jugadores = sc.nextLine();
                        //No se puede dejar en blanco
                        if (jugadores.isEmpty()) {
                            throw new DatoNoValido("No puedes dejar este espacio vacío");
                        }
                        //Parsear el dato de entrada=jugadores a int,
                        njugadores = Integer.parseInt(jugadores);

                        if (njugadores < 1 || njugadores > 6) {
                            throw new DatoNoValido("El numero de jugadores debe de ser entre 1 y 6");
                        }
                    } catch (DatoNoValido e) {
                        System.out.println("" + e.getMessage());
                        error = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Introduce un numero valido");
                        error = true;
                    }

                } while (error);

                for (int i = 1; i < njugadores; i++) {
                    System.out.println("Jugador: " + i);

                    System.out.println("Nombre del jugador: ");
                    String nombre = sc.nextLine();
                    sb.append(", ").append(nombre);

                    System.out.println("Nacionalidad: ");
                    String nacionalidad = sc.nextLine();

                    System.out.println("Edad: ");
                    int edad = Integer.parseInt(sc.nextLine());
                }
                System.out.println(sb.toString());

            }


            public static String DatoNoValido(String dato, String mensaje, String expresionRegular) {

                String var = "";
                boolean error;
                do {
                    try {
                        var = JOptionPane.showInputDialog(mensaje);

                        if (var.isEmpty()) {
                            throw new DatoNoValido(dato + " no puede estar vacio");
                        }


                        Pattern pat = Pattern.compile(expresionRegular);
                        Matcher mat = pat.matcher(var);

                        if (!mat.matches())
                            throw new DatoNoValido(dato + "no tiene un formato adecuado");

                        error = false;
                    } catch (DatoNoValido e) {
                        error = true;
                        JOptionPane.showMessageDialog(null, e);
                    }

                } while (error);
                return var;

            }
        }

