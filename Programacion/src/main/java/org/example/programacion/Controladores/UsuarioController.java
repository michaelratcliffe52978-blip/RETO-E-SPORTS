package org.example.programacion.Controladores;

import org.example.programacion.DAO.UsuarioDAO;

/**
 * Este controlador es el que maneja la entrada de la peña al programa.
 * Sirve para registrar usuarios nuevos y para comprobar si el nombre y la contra
 * que ponen al entrar son correctos (ya sean usuarios normales o el jefazo admin).
 * * @author equipo4
 * @version 1.0
 */
public class UsuarioController {

    /** Objeto para hablar con la base de datos y manejar los usuarios */
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Mete a un usuario nuevo en el sistema.
     * Tienes que pasarle el nombre, la contraseña y qué rol va a tener (user o admin).
     * * @param nombre El nombre que va a usar para loguearse.
     * @param contra La clave secreta del usuario.
     * @param rol Si va a ser un usuario normal o un administrador.
     */
    public void insertarUsuario(String nombre, String contra, String rol) {
        usuarioDAO.insertUsuario(nombre, contra, rol);
    }

    /**
     * Mira si el nombre y la contraseña coinciden con alguno de la base de datos.
     * Esto se usa para el login de toda la vida.
     * * @param nombre El nombre que ha escrito el usuario.
     * @param contra La contraseña que ha escrito.
     * @return true si los datos están bien, false si se ha colado en algo.
     */
    public boolean validarUsuario(String nombre, String contra) {
        return usuarioDAO.validarUsuario(nombre, contra);
    }

    /**
     * Esto es igual que lo de validar usuario, pero solo para ver si el que entra es el admin.
     * Si no es admin, no le deja pasar a las pantallas de gestión chungas.
     * * @param user El nombre del administrador.
     * @param pass La contraseña del administrador.
     * @return true si es el admin de verdad, false si no lo es.
     */
    public boolean validarAdmin(String user, String pass) {
        return usuarioDAO.validarAdmin(user, pass);
    }
}