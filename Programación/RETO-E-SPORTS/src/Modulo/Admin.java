package Modulo;

import java.util.Scanner;

public class Admin extends Usuario{

    private final static Scanner sc = new Scanner(System.in);

    public Admin(String idUsuario, String nombreUsuario, String contrasena) {
        super(idUsuario, nombreUsuario, contrasena);
    }

    public void generarCalendario() {

    }

    public void cerrarEstado(String estado) {
        estado = "cerrado";
    }

    public void introducirResultados(String resultado, String id_partido) {

    }

    public void verInformes(String resultado, String id_partido) {
        System.out.println("El informe del partido " + id_partido + " es " + resultado);
    }

    public void CRUD() {
        System.out.println("Que desea: \n1. Crear \n2. Leer \n3. Actualizar \n4. Borrar");
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                System.out.println("Crear");
                break;
            case 2:
                System.out.println("Leer");
                break;
            case 3:
                System.out.println("Actualizar");
                break;
            case 4:
                System.out.println("Borrar");
                break;
            default:
                System.out.println("Opcion no valida");
                break;
        }
    }


}
