package org.example.programacion.Modelo;

public class Usuario {

    private String idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String tipo;

    public Usuario(String idUsuario, String nombreUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String tipo) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.tipo = tipo;
    }

    public Usuario(String nombre, String password) {
        this.nombreUsuario = nombre;
        this.contrasena = password;
        // El idUsuario se queda null porque lo generas en el DAO
    }

    // Getters y Setters con nombres correctos para JavaBeans
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
