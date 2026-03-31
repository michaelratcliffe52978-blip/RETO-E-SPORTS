package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {

        private String url = "jdbc:mysql://localhost:3306/tu_bd";
        private String user = "eqdaw04";
        private String pass = "eqdaw04";

        public boolean insertarUsuario(String nombre, String password) {
            String sql = "INSERT INTO usuarios (nombre, password) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, nombre);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


}

