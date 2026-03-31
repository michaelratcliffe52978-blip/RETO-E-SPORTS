package org.example.programacion.Controladores;

import org.example.programacion.Controladores.UsuarioController;
import org.example.programacion.DAO.UsuarioDAO;
import org.example.programacion.Modelo.Usuario;

public class UsuarioController {
        private static UsuarioDAO usuarioDAO = new UsuarioDAO();

        public static void insertarUsuario(String nombre, String password) {
            Usuario usuario = new Usuario(nombre, password);
            usuarioDAO.insertarUsuario(usuario);
        }
    }

