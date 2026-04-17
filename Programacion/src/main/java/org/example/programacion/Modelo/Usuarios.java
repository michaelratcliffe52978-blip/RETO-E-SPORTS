package org.example.programacion.Modelo;

/**
 * Esta clase es la superclase que representa a cualquier usuario del sistema.
 * Contiene los atributos básicos de identificación y credenciales que comparten
 * tanto los administradores como los usuarios estándar.
 * * @author equipo4
 * @version 1.0
 */
public class Usuarios {

    /** Identificador único del usuario en la base de datos */
    private String idUsuario;
    /** Nombre de acceso o login */
    private String nombreUsuario;
    /** Clave de acceso (en texto plano o hash según implementación) */
    private String contrasena;
    /** Rol del usuario (habitualmente 'ADMIN' o 'USER') */
    private String tipo;

    /**
     * Constructor básico con ID, nombre y contraseña.
     * * @param idUsuario ID único del usuario.
     * @param nombreUsuario Nombre de login.
     * @param contrasena Clave de acceso.
     */
    public Usuarios(String idUsuario, String nombreUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    /**
     * Constructor completo que incluye el tipo/rol de usuario.
     * * @param idUsuario ID único del usuario.
     * @param nombreUsuario Nombre de login.
     * @param contrasena Clave de acceso.
     * @param tipo Rol asignado al usuario.
     */
    public Usuarios(String idUsuario, String nombreUsuario, String contrasena, String tipo) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.tipo = tipo;
    }

    /**
     * Constructor simplificado para procesos de autenticación inicial.
     * El ID se deja nulo ya que suele ser gestionado por el DAO tras la validación.
     * * @param nombre Nombre de usuario introducido.
     * @param password Contraseña introducida.
     */
    public Usuarios(String nombre, String password) {
        this.nombreUsuario = nombre;
        this.contrasena = password;
    }

    // --- GETTERS Y SETTERS (Estilo JavaBeans) ---

    /** @return El ID del usuario */
    public String getIdUsuario() { return idUsuario; }

    /** @param idUsuario El nuevo ID a asignar */
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    /** @return El nombre de usuario */
    public String getNombreUsuario() { return nombreUsuario; }

    /** @param nombreUsuario El nuevo nombre de usuario */
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    /** @return La contraseña del usuario */
    public String getContrasena() { return contrasena; }

    /** @param contrasena La nueva contraseña */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    /** @return El tipo o rol del usuario */
    public String getTipo() { return tipo; }

    /** @param tipo El nuevo tipo de usuario ('ADMIN'/'USER') */
    public void setTipo(String tipo) { this.tipo = tipo; }
}