import Modulo.Enfrentamiento;
import Modulo.Equipos;
import Modulo.Jornada;
import Modulo.Jugadores;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private final static Scanner sc = new Scanner(System.in);
    private static ArrayList<Jugadores> listajugadores = new ArrayList<>();
    private static ArrayList<Equipos> listaequipos = new ArrayList<>();
    private static ArrayList<Enfrentamiento> listaenfrentamientos = new ArrayList<>();
    private static ArrayList<Jornada> listajornada = new ArrayList<>();
    private static Set<String> listaId = new HashSet<>();

    public static void main(String[] args) {
        introducirDatos();
    }

    public static void introducirDatos() {
        int nEquipos = patterNEquipos();
        sc.nextLine();
        for (int i = 0; i < nEquipos; i++) {
            datosJugadores();
            datosEquipo();
        }
        System.out.println(listaequipos);
        datosEnfrentamientos();
        datosEnfrentamientos();
        datosJornadona();
    }

    public static void datosJugadores() {
        int nJugadores = patterNJugadores();

        for (int i = 0; i < nJugadores; i++) {
            System.out.println("\nJugador " + (i + 1));
            String id = patterId("Id (aa-0000): ");
            String nombre = leerSoloLetras("Nombre: ");
            String apellido = leerSoloLetras("Apellido: ");
            String nacionalidad = leerSoloLetras("Nacionalidad: ");
            LocalDate fechaNacimiento = leerFechaAnterior("Fecha de nacieminto (dd/MM/yyyy): ");
            String nickname = leerTextoNoVacio("Nickname: ");
            String rol = leerRol();
            Double sueldo = leerDoubleMin1221("Sueldo: ");


            Jugadores j = new Jugadores(id, nombre, apellido, nacionalidad, fechaNacimiento, nickname, rol, sueldo);
            listajugadores.add(j);

        }
    }

    public static void datosEquipo() {
        System.out.println("\nEquipo ");
        String id = patterId("Id (aa-0000): ");
        String nombre = leerSoloLetras("Nombre equipo: ");
        LocalDate fechaNacimiento = leerFechaAnterior("Fecha de nacimiento (dd/MM/yyyy): ");

        ArrayList<Jugadores> jugadoresEquipo = new ArrayList<>(listajugadores);

        Equipos e = new Equipos(id, jugadoresEquipo, fechaNacimiento, nombre);
        listaequipos.add(e);
        listajugadores.clear();
    }

    public static void datosEnfrentamientos() {
        for (int i = 0; i < listaequipos.size() / 2; i++) {

            System.out.println("\nEnfrentamiento " + (i + 1));

            String id = patterId("Id (aa-0000): ");
            LocalDate fechaEnfrentamiento = leerFechaPosterior("Fecha de enfrentamiento (dd/MM/yyyy): ");
            LocalTime hora = leerHora("Hora (HH:mm): ");

            Equipos equipo1 = listaequipos.get(i * 2);
            Equipos equipo2 = listaequipos.get(i * 2 + 1);

            System.out.println("Equipo 1: " + equipo1.getnombreEquipo());
            System.out.println("Equipo 2: " + equipo2.getnombreEquipo());

            Enfrentamiento en = new Enfrentamiento(id, hora, fechaEnfrentamiento, equipo1, equipo2);
            listaenfrentamientos.add(en);
        }
    }

    public static void datosJornadona() {
        for (int i = 0; i < (listaequipos.size() - 1) * 2; i++) {
            System.out.println("\nJornada " + (i + 1));
            String id = patterId("Id (aa-0000): ");
            LocalDate fecha = leerFechaPosterior("Fecha de inicio (dd/MM/yyyy): ");
            Enfrentamiento e1 = listaenfrentamientos.get(i % listaenfrentamientos.size());

            Jornada j = new Jornada(id, (i + 1), fecha, e1);
            listajornada.add(j);
        }

    }















    private static String leerTextoNoVacio(String msg) {
        String txt;
        do {
            System.out.print(msg);
            txt = sc.nextLine().trim();
        } while (txt.isEmpty());
        return txt;
    }

    private static LocalTime leerHora(String msg) {
        LocalTime hora = null;
        boolean ok;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        do {
            ok = true;
            try {
                System.out.print(msg);
                hora = LocalTime.parse(sc.nextLine(), formato);
            } catch (Exception e) {
                ok = false;
                System.out.println("Formato inválido. Usa HH:mm.");
            }
        } while (!ok);
        return hora;
    }


    private static double leerDoubleMin1221(String msg) {
        double n;
        do {
            System.out.print(msg);
            while (!sc.hasNextDouble()) {
                sc.next(); // descarta lo que no es double
                System.out.print("Número inválido, repite: ");
            }
            n = sc.nextDouble();
            sc.nextLine();
            if (n < 1221) {
                System.out.print("\nEl número debe ser mayor o igual que 1221. ");
            }
        } while (n < 1221);

        return n;
    }

    private static String leerSoloLetras(String msg) {
        String texto;
        do {
            System.out.print(msg);
            texto = sc.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("No puede estar vacío.");
            } else if (!texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                System.out.println("Solo se permiten letras.");
                texto = "";
            }

        } while (texto.isEmpty());

        return texto;
    }

    private static String patterId(String msg) {
        String texto;
        do {
            System.out.print(msg);
            texto = sc.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("No puede estar vacío.");
            } else if (!texto.matches("^[a-zA-Z]{2}-[0-9]{4}$")) {
                System.out.println("Deve ser en formato aa-0000.");
                texto = "";
            }else if(listaId.contains(texto)){
                System.out.println("El ID ya existe. Introduce uno diferente.");
                texto = "";
            }

        } while (texto.isEmpty());
        listaId.add(texto);
        return texto;
    }


    private static int patterNJugadores() {
        String njug;
        do {
            System.out.print("Cuantos jugadores quiere introducir en este equipo (2-6): ");
            njug = sc.nextLine();
        } while (!njug.matches("^[2-6]{1}$"));
        int nJugadores = Integer.parseInt(njug);
        return nJugadores;
    }



    private static int patterNEquipos() {
        int nequip;
        boolean w=false;
        do {
            System.out.print("Cuantos equipos quiere introducir (par): ");
            nequip = sc.nextInt();
            if (nequip % 2 == 0 && nequip > 0) {
                nequip = nequip;
                w=true;
            } else {
                System.out.println("El número debe ser par y mayor que 0. Inténtalo de nuevo.");
                w=false;
            }
        } while (!w);
        return nequip;
    }


    private static String paterResultado() {
        String tipo;
        do {
            System.out.print("Tipo (Victoria/Empate/Derrota): ");
            tipo = sc.nextLine();
        } while (!tipo.equalsIgnoreCase("Victoria") && !tipo.equalsIgnoreCase("Empate") && !tipo.equalsIgnoreCase("Derrota"));
        return tipo;
    }


    private static String leerRol() {
        String tipo;
        do {
            System.out.print("Tipo (Centinela/Duelista/Iniciador/Controlador): ");
            tipo = sc.nextLine();
        } while (!tipo.equalsIgnoreCase("Centinela") && !tipo.equalsIgnoreCase("Duelista") && !tipo.equalsIgnoreCase("Controlador") && !tipo.equalsIgnoreCase("Iniciador"));
        return tipo;
    }


    private static String leerSexo() {
        String sexo;
        do {
            System.out.print("  Sexo (Macho/Hembra): ");
            sexo = sc.nextLine();
        } while (!sexo.equalsIgnoreCase("Macho") && !sexo.equalsIgnoreCase("Hembra"));
        return sexo;
    }

    private static LocalDate leerFechaAnterior(String msg) {
        LocalDate fecha = null;
        boolean ok;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
        } while (!ok);
        return fecha;
    }

    private static LocalDate leerFechaPosterior(String msg) {
        LocalDate fecha = null;
        boolean ok;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        do {
            ok = true;
            try {
                System.out.print(msg);
                fecha = LocalDate.parse(sc.nextLine(), formato);
                if (!fecha.isAfter(LocalDate.now())) {
                    ok = false;
                    System.out.println("La fecha debe ser anterior a hoy.");
                }
            } catch (Exception e) {
                ok = false;
                System.out.println("Formato inválido. Usa dd/MM/yyyy.");
            }
        } while (!ok);
        return fecha;
    }



}