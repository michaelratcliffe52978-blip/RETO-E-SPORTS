package org.example.programacion.Modelo;

public class Usuario {

    private String idUsuario;
    private String nombreUsuario;
    private String contrasena;

    public Usuario(String idUsuario, String nombreUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public Usuario(String nombre, String password) {
        this.nombreUsuario = nombre;
        this.contrasena = password;
        // El idUsuario se queda null porque lo generas en el DAO
    }

    public String getidUsuario() {return idUsuario;}
    public void setidUsuario(String idUsuario) {this.idUsuario = idUsuario;}
    public String getnombreUsuario() {return nombreUsuario;}
    public void setnombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario;}
    public String getContrasena() {return contrasena;}
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

}
