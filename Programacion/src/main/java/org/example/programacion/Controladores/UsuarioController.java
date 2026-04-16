package org.example.programacion.Controladores;

import org.example.programacion.DAO.UsuarioDAO;

public class UsuarioController {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Inserta un nuevo usuario en la base de datos.
     */
    public void insertarUsuario(String nombre, String contra, String rol) {
        usuarioDAO.insertUsuario(nombre, contra, rol);
    }

    /**
     * Valida si un usuario existe y su contraseña es correcta.
     */
    public boolean validarUsuario(String nombre, String contra) {
        return usuarioDAO.validarUsuario(nombre, contra);
    }

    public boolean validarAdmin(String user, String pass) {
        return usuarioDAO.validarAdmin(user, pass);
    }
}