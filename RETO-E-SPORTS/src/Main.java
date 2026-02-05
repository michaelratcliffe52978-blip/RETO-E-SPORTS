import Modulo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private final static Scanner sc = new Scanner(System.in);
    private static ArrayList<Jugadores> listajugadores = new ArrayList<>();
    private static ArrayList<Equipos> listaequipos = new ArrayList<>();
    private static ArrayList<Enfrentamiento> listaenfrentamientos = new ArrayList<>();
    private static ArrayList<Jornada> listajornada = new ArrayList<>();
    private static ArrayList<Usuario> listausuario = new ArrayList<>();
    private static ArrayList<User> listauser = new ArrayList<>();
    private static ArrayList<Admin> listaadmin = new ArrayList<>();
    private static Competicion comp;
    private static int contador = 1;

    public static void main(String[] args) {
        introducirDatos();
        menu();
    }

    public static void introducirDatos() {
        int nEquipos = patterNEquipos();
        sc.nextLine();
        for (int i = 0; i < nEquipos; i++) {
            datosJugadores();
            datosEquipo();
        }
        System.out.println(listaequipos);
        datosUsuarios();
    }

    public static void datosJugadores() {
        int nJugadores = patterNJugadores();

        for (int i = 0; i < nJugadores; i++) {
            System.out.println("\nJugador " + (i + 1));
            String prefijo = "JU";
            String id = String.format("%s-%02d", prefijo, contador++);
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
        String prefijo = "EQ";
        String id = String.format("%s-%02d", prefijo, contador++);
        String nombre = leerSoloLetras("Nombre equipo: ");
        LocalDate fechaNacimiento = leerFechaAnterior("Fecha de nacimiento (dd/MM/yyyy): ");

        ArrayList<Jugadores> jugadoresEquipo = new ArrayList<>(listajugadores);

        Equipos e = new Equipos(id, jugadoresEquipo, fechaNacimiento, nombre);
        listaequipos.add(e);
        listajugadores.clear();
    }

    public static void datosEnfrentamientos() {

        LocalDate inicio = LocalDate.now().plusWeeks(1);
        LocalDate fin = LocalDate.now().plusWeeks(2);
        long startEpoch = inicio.toEpochDay();
        long endEpoch = fin.toEpochDay();
        long aleatorioEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);
        LocalDate fechaResultado = LocalDate.ofEpochDay(aleatorioEpoch);

        for (int i = 0; i < listaequipos.size() / 2; i++) {
            System.out.println("\nEnfrentamiento " + (i + 1));
            String prefijo = "EN";
            String id = String.format("%s-%02d", prefijo, contador++);

            LocalTime inicioHora = LocalTime.of(9, 0);
            LocalTime finHora = LocalTime.of(21, 0);
            int inicioSegundos = inicioHora.toSecondOfDay();
            int finSegundos = finHora.toSecondOfDay();
            int aleatorioSegundos = ThreadLocalRandom.current().nextInt(inicioSegundos, finSegundos);
            LocalTime horaResultado = LocalTime.ofSecondOfDay(aleatorioSegundos);

            Equipos equipo1 = listaequipos.get(i * 2);
            Equipos equipo2 = listaequipos.get(i * 2 + 1);

            System.out.println("Equipo 1: " + equipo1.getnombreEquipo());
            System.out.println("Equipo 2: " + equipo2.getnombreEquipo());

            Enfrentamiento en = new Enfrentamiento(id, horaResultado, fechaResultado, equipo1, equipo2);
            listaenfrentamientos.add(en);
        }
    }

    public static void datosJornadona() {
        for (int i = 0; i < (listaequipos.size() - 1) * 2; i++) {
            System.out.println("\nGenerando Jornada " + (i + 1));
            String prefijo = "JO";
            String id = String.format("%s-%02d", prefijo, contador++);
            Enfrentamiento e1 = listaenfrentamientos.get(i % listaenfrentamientos.size());

            Jornada j = new Jornada(id, (i + 1), e1.getfechaEnfrentamiento(), e1);
            listajornada.add(j);
        }

    }

    public static void datosUsuarios() {
        System.out.println("¿Quiere crear algún admin o user? (admin/user/no): ");
        String respuesta = sc.nextLine().trim().toLowerCase();
        switch (respuesta) {
            case "admin": {
                System.out.println("Cuantos admin quiere introducir: ");
                int nAdmin = sc.nextInt();
                sc.nextLine();
                for (int i = 0; i < nAdmin; i++) {
                    System.out.println("\nAdmin " + (i + 1));
                    String prefijo = "AD";
                    String id = String.format("%s-%02d", prefijo, contador++);
                    String nombre = leerSoloLetras("Nombre de admin: ");
                    String password = leerTextoNoVacio("Contraseña: ");

                    Admin adm = new Admin(id, nombre, password);
                    listausuario.add(adm);
                    listaadmin.add(adm);
                }
                datosUsuarios();
                break;
            }
            case "user": {
                System.out.println("Cuantos usuarios quiere introducir: ");
                int nUsuarios = sc.nextInt();
                sc.nextLine();
                for (int i = 0; i < nUsuarios; i++) {
                    System.out.println("\nUser " + (i + 1));
                    String prefijo = "US";
                    String id = String.format("%s-%02d", prefijo, contador++);
                    String nombre = leerSoloLetras("Nombre de user: ");
                    String password = leerTextoNoVacio("Contraseña: ");

                    User u = new User(id, nombre, password);
                    listausuario.add(u);
                    listauser.add(u);
                }
                datosUsuarios();
                break;
            }
            case "no":
                break;
            default:
                System.out.println("Respuesta no valida. Intentalo de nuevo.");
                datosUsuarios();
                break;
        }

    }

    public static void datosCompeticion() {
        System.out.println("\nGenerando competicion ");
        String prefijo = "CO";
        String id = String.format("%s-%02d", prefijo, contador++);
        sc.nextLine();
        String nombre = leerSoloLetras("Nombre de la competicion: ");
        String estado = leerEstado();

        comp = new Competicion(id, nombre, estado, listajornada);
    }

    public static void menu() {
        if (listausuario.size() == 0) {
            System.out.println("No hay usuarios disponibles. Por favor, cree al menos un admin o user.");
            datosUsuarios();
        } else {
            System.out.println("¿Qué tipo de usuario eres? (admin/user/cerrar): ");
            String tipoUsuario = sc.nextLine().trim().toLowerCase();
            switch (tipoUsuario) {
                case "admin":
                    System.out.println("Bienvenido Admin. ¿Qué desea hacer?");
                    menuAdmin();
                    break;
                case "user":
                    System.out.println("Bienvenido User. ¿Qué desea hacer?");
                    menuUser();
                    break;
                case "cerrar":
                    System.out.println("Cerrando el programa. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Tipo de usuario no válido. Inténtalo de nuevo.");
                    menu();
                    break;
            }
        }
    }

    public static void menuUser() {
        System.out.println("1. Ver informe de equipo");
        System.out.println("2. Ver informe de resultados");
        System.out.println("3. Cerrar sesión");
        int opcionUser = sc.nextInt();
        switch (opcionUser) {
            case 1:
                System.out.println("Ver informe de equipo");
                for (int i = 0; i < listaequipos.size(); i++) {
                    Equipos e = listaequipos.get(i);
                    System.out.println("  Equipo " + (i+1) + ": " + e.getnombreEquipo() + " fundado el " + e.getfechaFundacion() + ", con jugadores: " + e.getjugadores() + ", ID: " + e.getidEquipo());
                }
                menuUser();
                break;
            case 2:
                System.out.println("Ver informe de resultados");
                if (listaenfrentamientos.isEmpty()) {
                    System.out.println("No hay enfrentamientos disponibles.");
                } else {
                    for (int i = 0; i < listaenfrentamientos.size(); i++) {
                        Enfrentamiento en = listaenfrentamientos.get(i);
                        System.out.println("  Enfrentamiento " + (i + 1) + ": ID: " + en.getidPartido() + ", Fecha: " + en.getfechaEnfrentamiento() + ", Hora: " + en.getHora() + ", Equipo 1: " + en.getEquipo1().getnombreEquipo() + ", Equipo 2: " + en.getEquipo2().getnombreEquipo());
                    }
                }
                menuUser();

                break;
            case 3:
                System.out.println("Cerrando sesión.");
                menu();
                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
                menuUser();
                break;
        }
    }

    public static void menuAdmin() {
        System.out.println("1. Generar calendario");
        System.out.println("2. Cerrar estado de competición");
        System.out.println("3. Introducir resultados");
        System.out.println("4. Ver informes");
        System.out.println("5. CRUD");
        System.out.println("6. Salir");
        int opcionAdmin = sc.nextInt();
        switch (opcionAdmin) {
            case 1:
                System.out.println("Generando calendario...\n");
                datosEnfrentamientos();
                datosJornadona();
                datosCompeticion();
                System.out.println("Calendario generado con éxito.");
                menuAdmin();
                break;
            case 2:
                System.out.println("Cerrando estado de competición");
                Competicion c = comp;
                c.setEstado("Cerrado");
                System.out.println("El estado de la competición es: " + c.getEstado());
                menuAdmin();
                break;
            case 3:
                System.out.println("Introducir resultados");
                for (int i = 0; i < listaenfrentamientos.size(); i++) {
                    Enfrentamiento en = listaenfrentamientos.get(i);
                    System.out.println("Enfrentamiento " + (i + 1) + ": ID: " + en.getidPartido() + ", Fecha: " + en.getfechaEnfrentamiento() + ", Hora: " + en.getHora() + ", Equipo 1: " + en.getEquipo1().getnombreEquipo() + ", Equipo 2: " + en.getEquipo2().getnombreEquipo());
                    sc.nextLine();
                    String resultado = paterResultado();
                    en.setResultado(resultado);
                    System.out.println("Resultado introducido: " + en.getResultado());
                }
                menuAdmin();
                break;
            case 4:
                System.out.println("Ver informes\n");
                System.out.println("1. Ver informe de jugadores");
                System.out.println("2. Ver informe de equipos");
                System.out.println("3. Ver informe de enfrentamientos");
                System.out.println("4. Ver informe de jornadas");
                int informeOpcion = sc.nextInt();
                switch (informeOpcion) {
                    case 1:
                        System.out.println("Informe de jugadores");
                        for (int i = 0; i < listaequipos.size(); i++) {
                            Equipos e = listaequipos.get(i);
                            for (int j = 0; j < e.getjugadores().size(); j++) {
                                Jugadores jugador = e.getjugadores().get(j);
                                System.out.println("  Jugador " + (j + 1) + ": " + jugador.getnombreJugador() + " " + jugador.getApellido() + ", Nickname: " + jugador.getNickname() + ", Rol: " + jugador.getRol() + ", Sueldo: " + jugador.getSueldo());
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Ver informe de equipo");
                        for (int i = 0; i < listaequipos.size(); i++) {
                            Equipos e = listaequipos.get(i);
                            System.out.println("  Equipo " + (i + 1) + ": " + e.getnombreEquipo() + " fundado el " + e.getfechaFundacion() + ", con jugadores: " + e.getjugadores() + ", ID: " + e.getidEquipo());
                        }
                        break;
                    case 3:
                        System.out.println("Ver informe de enfrentamientos");
                        for (int i = 0; i < listaenfrentamientos.size(); i++) {
                            Enfrentamiento en = listaenfrentamientos.get(i);
                            System.out.println("  Enfrentamiento " + (i + 1) + ": ID: " + en.getidPartido() + ", Fecha: " + en.getfechaEnfrentamiento() + ", Hora: " + en.getHora() + ", Equipo 1: " + en.getEquipo1().getnombreEquipo() + ", Equipo 2: " + en.getEquipo2().getnombreEquipo());
                        }
                        break;
                    case 4:
                        System.out.println("Ver informe de jornadas");
                        for (int i = 0; i < listajornada.size(); i++) {
                            Jornada j = listajornada.get(i);
                            System.out.println("  Jornada " + (i + 1) + ": ID: " + j.getidJornada() + ", Número: " + j.getnumeroJornada() + ", Fecha inicio: " + j.getfechaJornada() + ", Enfrentamiento: " + j.getEnfrentamientos().getidPartido());
                        }
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        break;
                }
                menuAdmin();
                break;
            case 5:
                System.out.println("CRUD");
                System.out.println("¿Qué desea: \n1. Crear \n2. Leer \n3. Actualizar \n4. Borrar");
                int opcion = sc.nextInt();
                switch (opcion) {
                    case 1:
                        System.out.println("Crear");
                        System.out.println("1. Crear jugador");
                        System.out.println("2. Crear equipo");
                        System.out.println("3. Crear enfrentamiento");
                        System.out.println("4. Crear jornada");
                        int crearOpcion = sc.nextInt();
                        switch (crearOpcion) {
                            case 1:
                                datosJugadores();
                                break;
                            case 2:
                                datosEquipo();
                                break;
                            case 3:
                                datosEnfrentamientos();
                                break;
                            case 4:
                                datosJornadona();
                                break;
                            default:System.out.println("Opción no válida. Inténtalo de nuevo.");
                                break;
                        }
                        break;
                    case 2:
                        System.out.println("Ver\n");
                        System.out.println("1. Ver jugadores");
                        System.out.println("2. Ver equipos");
                        System.out.println("3. Ver enfrentamientos");
                        System.out.println("4. Ver jornadas");
                        int verOpcion = sc.nextInt();
                        switch (verOpcion) {
                            case 1:
                                System.out.println("Informe de jugadores");
                                for (int i = 0; i < listaequipos.size(); i++) {
                                    Equipos e = listaequipos.get(i);
                                    for (int j = 0; j < e.getjugadores().size(); j++) {
                                        Jugadores jugador = e.getjugadores().get(j);
                                        System.out.println("  Jugador " + (j + 1) + ": " + jugador.getnombreJugador() + " " + jugador.getApellido() + ", Nickname: " + jugador.getNickname() + ", Rol: " + jugador.getRol() + ", Sueldo: " + jugador.getSueldo());
                                    }
                                }
                                break;
                            case 2:
                                System.out.println("Ver informe de equipo");
                                for (int i = 0; i < listaequipos.size(); i++) {
                                    Equipos e = listaequipos.get(i);
                                    System.out.println("  Equipo " + (i + 1) + ": " + e.getnombreEquipo() + " fundado el " + e.getfechaFundacion() + ", con jugadores: " + e.getjugadores() + ", ID: " + e.getidEquipo());
                                }
                                break;
                            case 3:
                                System.out.println("Ver informe de enfrentamientos");
                                for (int i = 0; i < listaenfrentamientos.size(); i++) {
                                    Enfrentamiento en = listaenfrentamientos.get(i);
                                    System.out.println("  Enfrentamiento " + (i + 1) + ": ID: " + en.getidPartido() + ", Fecha: " + en.getfechaEnfrentamiento() + ", Hora: " + en.getHora() + ", Equipo 1: " + en.getEquipo1().getnombreEquipo() + ", Equipo 2: " + en.getEquipo2().getnombreEquipo());
                                }
                                break;
                            case 4:
                                System.out.println("Ver informe de jornadas");
                                for (int i = 0; i < listajornada.size(); i++) {
                                    Jornada j = listajornada.get(i);
                                    System.out.println("  Jornada " + (i + 1) + ": ID: " + j.getidJornada() + ", Número: " + j.getnumeroJornada() + ", Fecha inicio: " + j.getfechaJornada() + ", Enfrentamiento: " + j.getEnfrentamientos().getidPartido());
                                }
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtalo de nuevo.");
                                break;
                        }
                        break;
                    case 3:
                        System.out.println("1. Actualizar jugador");
                        System.out.println("2. Actualizar equipo");
                        System.out.println("3. Actualizar enfrentamiento");
                        System.out.println("4. Actualizar jornada");
                        int actualizarOpcion = sc.nextInt();
                        switch (actualizarOpcion) {
                            case 1:
                                System.out.println("Qué jugador quiere actualizar");
                                int indexGlobal = 1;
                                Map<Integer, Jugadores> mapJugadores = new HashMap<>();
                                for (Equipos e : listaequipos) {
                                    System.out.println("\nEquipo: " + e.getnombreEquipo());
                                    for (Jugadores j : e.getjugadores()) {
                                        System.out.println("  " + indexGlobal + ". " + j.getnombreJugador() + " " + j.getApellido() + ", Nickname: " + j.getNickname() + ", Rol: " + j.getRol() + ", Sueldo: " + j.getSueldo());
                                        mapJugadores.put(indexGlobal, j);
                                        indexGlobal++;
                                    }
                                }
                                int jugadorActualizar = sc.nextInt();
                                sc.nextLine();
                                Jugadores jugador = mapJugadores.get(jugadorActualizar);
                                if (jugador == null) {
                                    System.out.println("Jugador no válido.");
                                    return;
                                }
                                System.out.println("Qué campo quiere actualizar (nombre/apellido/nacionalidad/fechanacimiento/nickname/rol/sueldo): ");
                                String campoActualizar = sc.nextLine().trim().toLowerCase();
                                switch (campoActualizar) {
                                    case "nombre":
                                        String nuevoNombre = leerSoloLetras("Nuevo nombre: ");
                                        jugador.setnombreJugador(nuevoNombre);
                                        System.out.println("Nombre actualizado a: " + nuevoNombre);
                                        break;
                                    case "apellido":
                                        String nuevoApellido = leerSoloLetras("Nuevo apellido: ");
                                        jugador.setApellido(nuevoApellido);
                                        System.out.println("Apellido actualizado a: " + nuevoApellido);
                                        break;
                                    case "nacionalidad":
                                        String nuevaNacionalidad = leerSoloLetras("Nueva nacionalidad: ");
                                        jugador.setNacionalidad(nuevaNacionalidad);
                                        System.out.println("Nacionalidad actualizada a: " + nuevaNacionalidad);
                                        break;
                                    case "fechanacimiento":
                                        LocalDate nuevaFecha = leerFechaAnterior("Nueva fecha de nacimiento (dd/MM/yyyy): ");
                                        jugador.setfechaNacimiento(nuevaFecha);
                                        System.out.println("Fecha de nacimiento actualizada a: " + nuevaFecha);
                                        break;
                                    case "nickname":
                                        String nuevoNickname = leerTextoNoVacio("Nuevo nickname: ");
                                        jugador.setNickname(nuevoNickname);
                                        System.out.println("Nickname actualizado a: " + nuevoNickname);
                                        break;
                                    case "rol":
                                        String nuevoRol = leerRol();
                                        jugador.setRol(nuevoRol);
                                        System.out.println("Rol actualizado a: " + nuevoRol);
                                        break;
                                    case "sueldo":
                                        double nuevoSueldo = leerDoubleMin1221("Nuevo sueldo: ");
                                        jugador.setSueldo(nuevoSueldo);
                                        System.out.println("Sueldo actualizado a: " + nuevoSueldo);
                                        break;
                                    default:
                                        System.out.println("Campo no válido. Inténtalo de nuevo.");
                                }
                                break;
                            case 2:
                                System.out.println("Que equipo quiere actualizar");
                                for (int i = 0; i < listaequipos.size(); i++) {
                                    Equipos e = listaequipos.get(i);
                                    System.out.println("  " + (i + 1) + ". " + e.getnombreEquipo() + " fundado el " + e.getfechaFundacion() + ", con jugadores: " + e.getjugadores() + ", ID: " + e.getidEquipo());
                                }
                                int equipoActualizar = sc.nextInt();
                                sc.nextLine();
                                if (equipoActualizar < 1 || equipoActualizar > listaequipos.size()) {
                                    System.out.println("Equipo no válido.");
                                    return;
                                }
                                Equipos equipo = listaequipos.get(equipoActualizar - 1);
                                System.out.println("Qué campo quiere actualizar (nombre/fechafundacion): ");
                                String campoEquipo = sc.nextLine().trim().toLowerCase();
                                switch (campoEquipo) {
                                    case "nombre":
                                        String nuevoNombreEquipo = leerSoloLetras("Nuevo nombre del equipo: ");
                                        equipo.setnombreEquipo(nuevoNombreEquipo);
                                        System.out.println("Nombre del equipo actualizado a: " + nuevoNombreEquipo);
                                    case  "fechafundacion":
                                        LocalDate nuevaFechaFundacion = leerFechaAnterior("Nueva fecha de fundación (dd/MM/yyyy): ");
                                        equipo.setfechaFundacion(nuevaFechaFundacion);
                                        System.out.println("Fecha de fundación actualizada a: " + nuevaFechaFundacion);
                                        break;
                                    default:
                                        System.out.println("Campo no válido. Inténtalo de nuevo.");
                                }
                                break;
                            case 3:
                                System.out.println("Que enfrentamiento quiere actualizar");
                                for (int i = 0; i < listaenfrentamientos.size(); i++) {
                                    Enfrentamiento en = listaenfrentamientos.get(i);
                                    System.out.println("  " + (i + 1) + ". ID: " + en.getidPartido() + ", Fecha: " + en.getfechaEnfrentamiento() + ", Hora: " + en.getHora() + ", Equipo 1: " + en.getEquipo1().getnombreEquipo() + ", Equipo 2: " + en.getEquipo2().getnombreEquipo());
                                }
                                System.out.println("Opción no válida. Inténtalo de nuevo.");
                                int enfrentamientoActualizar = sc.nextInt();
                                sc.nextLine();
                                if (enfrentamientoActualizar < 1 || enfrentamientoActualizar > listaenfrentamientos.size()) {
                                    System.out.println("Enfrentamiento no válido.");
                                    return;
                                }
                                Enfrentamiento enfrentamiento = listaenfrentamientos.get(enfrentamientoActualizar - 1);
                                System.out.println("Qué campo quiere actualizar (fecha/hora/resultado): ");
                                String campoEnfrentamiento = sc.nextLine().trim().toLowerCase();
                                switch (campoEnfrentamiento) {
                                    case "fecha":
                                        LocalDate nuevaFechaEnfrentamiento = leerFechaPosterior("Nueva fecha de enfrentamiento (dd/MM/yyyy): ");
                                        enfrentamiento.setfechaEnfrentamiento(nuevaFechaEnfrentamiento);
                                        System.out.println("Fecha de enfrentamiento actualizada a: " + nuevaFechaEnfrentamiento);
                                        break;
                                    case "hora":
                                        LocalTime nuevaHora = leerHora("Nueva hora (HH:mm): ");
                                        enfrentamiento.setHora(nuevaHora);
                                        System.out.println("Hora actualizada a: " + nuevaHora);
                                        break;
                                    case "resultado":
                                        String nuevoResultado = paterResultado();
                                        enfrentamiento.setResultado(nuevoResultado);
                                        System.out.println("Resultado actualizado a: " + nuevoResultado);
                                        break;
                                    default:
                                        System.out.println("Campo no válido. Inténtalo de nuevo.");
                                }
                                break;
                            case 4:
                                System.out.println("Que jornada quiere actualizar");
                                for (int i = 0; i < listajornada.size(); i++) {
                                    Jornada j = listajornada.get(i);
                                    System.out.println("  " + (i + 1) + ". ID: " + j.getidJornada() + ", Número: " + j.getnumeroJornada() + ", Fecha inicio: " + j.getfechaJornada() + ", Enfrentamiento: " + j.getEnfrentamientos().getidPartido());
                                }
                                int jornadaActualizar = sc.nextInt();
                                sc.nextLine();
                                if (jornadaActualizar < 1 || jornadaActualizar > listajornada.size()) {
                                    System.out.println("Jornada no válida.");
                                    return;
                                }
                                Jornada jornada = listajornada.get(jornadaActualizar - 1);
                                System.out.println("Qué campo quiere actualizar (numero/fechainicio): ");
                                String campoJornada = sc.nextLine().trim().toLowerCase();
                                switch (campoJornada) {
                                    case "numero":
                                        System.out.print("Nuevo número de jornada: ");
                                        int nuevoNumero = sc.nextInt();
                                        sc.nextLine();
                                        jornada.setnumeroJornada(nuevoNumero);
                                        System.out.println("Número de jornada actualizado a: " + nuevoNumero);
                                        break;
                                    case "fechainicio":
                                        LocalDate nuevaFechaInicio = leerFechaPosterior("Nueva fecha de inicio (dd/MM/yyyy): ");
                                        jornada.setfechaJornada(nuevaFechaInicio);
                                        System.out.println("Fecha de inicio actualizada a: " + nuevaFechaInicio);
                                        break;
                                    default:
                                        System.out.println("Campo no válido. Inténtalo de nuevo.");
                                }
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtalo de nuevo.");
                        }
                        break;
                    case 4:
                        System.out.println("1. Borrar jugador");
                        System.out.println("2. Borrar equipo");
                        System.out.println("3. Borrar enfrentamiento");
                        System.out.println("4. Borrar jornada");
                        int borrarOpcion = sc.nextInt();
                        switch (borrarOpcion) {
                            case 1:
                                System.out.println("Que jugador quiere borrar");
                                for(int i=0; i<listaequipos.size(); i++){
                                    Equipos e = listaequipos.get(i);
                                    System.out.println("\nEquipo: " + e.getnombreEquipo());
                                    for(int j=0; j<e.getjugadores().size(); j++){
                                        Jugadores jugador = e.getjugadores().get(j);
                                        System.out.println("  " + (j+1) + ". " + jugador.getnombreJugador() + " " + jugador.getApellido() + ", Nickname: " + jugador.getNickname() + ", Rol: " + jugador.getRol() + ", Sueldo: " + jugador.getSueldo());
                                    }
                                }
                                int jugadorborrar = sc.nextInt();
                                listaequipos.remove(jugadorborrar);
                                break;
                            case 2:
                                System.out.println("Que equipo quiere borrar");
                                for(int i=0; i<listaequipos.size(); i++){
                                    Equipos e = listaequipos.get(i);
                                    System.out.println("\nEquipo: " + e.getnombreEquipo());
                                }
                                int equipoborrar = sc.nextInt();
                                listaequipos.remove(equipoborrar);
                                break;
                            case 3:
                                System.out.println("Que enfrentamiento quiere borrar");
                                for(int i=0; i<listaenfrentamientos.size(); i++){
                                    Enfrentamiento e = listaenfrentamientos.get(i);
                                    System.out.println("\nEnfrentamiento: " + e.getidPartido());
                                }
                                int enfrentamientoborrar = sc.nextInt();
                                listaenfrentamientos.remove(enfrentamientoborrar);
                                break;
                            case 4:
                                System.out.println("Que jornada quiere borrar");
                                for(int i=0; i<listajornada.size(); i++){
                                    Jornada j = listajornada.get(i);
                                    System.out.println("\nJornada: " + j.getidJornada());
                                }
                                int jornadaborrar = sc.nextInt();
                                listajornada.remove(jornadaborrar);
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtalo de nuevo.");
                                break;
                        }
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        break;
                }
                menuAdmin();
                break;
            case 6:
                System.out.println("Cerrando sesión.");
                menu();
                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
                menuAdmin();
                break;

        }
    }


        private static String leerTextoNoVacio (String msg){
            String txt;
            do {
                System.out.print(msg);
                txt = sc.nextLine().trim();
            } while (txt.isEmpty());
            return txt;
        }

        private static LocalTime leerHora (String msg){
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

        private static double leerDoubleMin1221 (String msg){
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

        private static String leerSoloLetras (String msg){
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

            } while (texto.isEmpty());

            return texto;
        }

        private static int patterNJugadores () {
            String njug;
            do {
                System.out.print("Cuantos jugadores quiere introducir en este equipo (2-6): ");
                njug = sc.nextLine();
            } while (!njug.matches("^[2-6]{1}$"));
            int nJugadores = Integer.parseInt(njug);
            return nJugadores;
        }

        private static int patterNEquipos () {
            int nequip;
            boolean w = false;
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
            } while (!w);
            return nequip;
        }

        private static String paterResultado () {
            String tipo;
            do {
                System.out.print("Tipo (Victoria local/Derrota local): ");
                tipo = sc.nextLine();
            } while (!tipo.equalsIgnoreCase("Victoria local") && !tipo.equalsIgnoreCase("Derrota local"));
            return tipo;
        }

        private static String leerRol () {
            String tipo;
            do {
                System.out.print("Tipo (Centinela/Duelista/Iniciador/Controlador): ");
                tipo = sc.nextLine();
            } while (!tipo.equalsIgnoreCase("Centinela") && !tipo.equalsIgnoreCase("Duelista") && !tipo.equalsIgnoreCase("Controlador") && !tipo.equalsIgnoreCase("Iniciador"));
            return tipo;
        }

        private static String leerEstado () {
            String estado;
            do {
                System.out.print("Estado (Abierto/Cerrado/En curso/Finalizado): ");
                estado = sc.nextLine();
            } while (!estado.equalsIgnoreCase("Abierto") && !estado.equalsIgnoreCase("Cerrado") && !estado.equalsIgnoreCase("En curso") && !estado.equalsIgnoreCase("Finalizado"));
            return estado;
        }

        private static LocalDate leerFechaAnterior (String msg){
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

        private static LocalDate leerFechaPosterior (String msg){
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
                        System.out.println("La fecha debe ser posterior a hoy.");
                    }
                } catch (Exception e) {
                    ok = false;
                    System.out.println("Formato inválido. Usa dd/MM/yyyy.");
                }
            } while (!ok);
            return fecha;
        }


    }