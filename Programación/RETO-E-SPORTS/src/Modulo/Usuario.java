package Modulo;

public class Usuario {

    private String idUsuario;
    private String nombreUsuario;
    private String contrasena;

    public Usuario(String idUsuario, String nombreUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public String getidUsuario() {return idUsuario;}
    public void setidUsuario(String idUsuario) {this.idUsuario = idUsuario;}
    public String getnombreUsuario() {return nombreUsuario;}
    public void setnombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario;}
    public String getContrasena() {return contrasena;}
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

}
