package org.example.programacion.Controladores;

import org.example.programacion.Util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioController {

    /**
     * Inserta un nuevo usuario en la base de datos con el tipo 'user' por defecto.
     */
    public static void insertarUsuario(String nombre, String contra) {
        // CORRECCIÓN: Tabla 'USUARIO' y columnas 'NOMBRE_USUARIO', 'CONTRASENA', 'TIPO'
        String sql = "INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, 'user')";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, contra);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario '" + nombre + "' registrado con éxito.");
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Valida si un usuario existe y su contraseña es correcta.
     */
    public static boolean validarUsuario(String nombre, String contra) {
        String sql = "SELECT * FROM USUARIO WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, contra);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true si encuentra el usuario
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}