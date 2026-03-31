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

        public void insertarUsuario(Usuario  usuario) {
            String sql = "INSERT INTO usuarios (nombre, password) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement preparedStatement= conn.prepareStatement(sql)) {

                preparedStatement.setString(1, usuario.getnombreUsuario());
                preparedStatement.setString(2, usuario.getContrasena());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


}

