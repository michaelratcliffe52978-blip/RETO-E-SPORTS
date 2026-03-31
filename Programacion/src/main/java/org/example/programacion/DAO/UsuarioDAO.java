package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {

    private String url = "jdbc:oracle:thin:@172.20.225.114:1521/EQDAW04";
    private String user = "eqdaw04";
    private String pass = "eqdaw04";

    public void insertarUsuario(Usuario usuario) {

        String sql = "INSERT INTO Usuario (idUsuario, nombreUsuario, contrasena) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idAleatorio = (int) (Math.random() * 10000);
            ps.setInt(1, idAleatorio);
            ps.setString(2, usuario.getnombreUsuario());
            ps.setString(3, usuario.getContrasena());

            ps.executeUpdate();
            System.out.println("¡Registro guardado con éxito con ID: " + idAleatorio);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        }




