import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static int numero;
    private static int cantidad;


    public static void main(String[] args) {
        System.out.println("Que deseas registrar?: \n"
        + "1. Equipos \n"
        + "2. Jugadores");
        int opcion = sc.nextInt();

        switch (opcion){
            case 1:
                equipos();
                break;
            case 2:
                    jugadores();
                    break;
            default:
                        System.out.println("numero incorrecto");
        }}


    public static void jugadores(){
try {
    do {
        System.out.println("Ingresa el numero de jugadores (maximo 6): ");
        numero = sc.nextInt();
    }while (numero > 6);
        System.out.println("El numero de jugadores es de: " + numero);
        for (int i = 0; i <= numero; i++) {
            System.out.println("Nombre: ");
            String nombre = sc.next();
            System.out.println("Apellidos: ");
            String apellido = sc.next();
            System.out.println("Nacionalidad: ");
            String nacionalidad = sc.next();
            System.out.println("Fecha de nacimiento: ");
            String fecha_nacimiento = sc.next();
            System.out.println("Nickname: ");
            String nickname = sc.next();
            System.out.println("rol : ");
            String rol = sc.next();
            System.out.println("Sueldo : ");
            int sueldo = sc.nextInt();
            sc.nextLine();
            System.out.println("Nombre : " + nombre + "\n "
                    + "Apellido : " + apellido + "\n "
                    + "Nacionalidad : " + nacionalidad + "\n "
                    + "Fecha de nacimiento : " + fecha_nacimiento + "\n "
                    + "Nickname : " + nickname + "\n "
                    + "Rol : " + rol);
        }
    }catch(Exception e){
        System.out.println("❌valor incorrecto: " + e.getMessage());
    }}


        public static void equipos() {
        try {
            System.out.println("cuantos equipos van a participar? Tienen que ser pares");
            cantidad = sc.nextInt();
            if (cantidad % 2 == 0) {
                for (int i = 0; i < cantidad; i++) {
                    System.out.println("Nombre del equipo: ");
                    String nombre = sc.next();
                    System.out.println("Pais de origen: ");
                    String origen = sc.next();
                    System.out.println("Cuantos jugadores tienes: ");
                    int cant_jug = sc.nextInt();

                }}else {
                System.out.println("numero impar");

            } } catch (Exception e) {
            System.out.println("❌valor incorrecto: " + e.getMessage());

        }
    }}

