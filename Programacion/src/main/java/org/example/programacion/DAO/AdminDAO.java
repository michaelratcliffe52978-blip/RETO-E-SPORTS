package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuario;
import org.example.programacion.Util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT ID_USUARIO, NOMBRE_USUARIO, CONTRASENA, TIPO FROM USUARIO";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        String.valueOf(rs.getInt("ID_USUARIO")),
                        rs.getString("NOMBRE_USUARIO"),
                        rs.getString("CONTRASENA"),
                        rs.getString("TIPO")
                );
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void insertUsuario(String username, String password, String tipo) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, tipo);
            pstmt.executeUpdate();
        }
    }

    public void updateUsuario(int idUsuario, String username, String password, String tipo) throws SQLException {
        String sql = "UPDATE USUARIO SET NOMBRE_USUARIO = ?, CONTRASENA = ?, TIPO = ? WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, tipo);
            pstmt.setInt(4, idUsuario);
            pstmt.executeUpdate();
        }
    }

    public void deleteUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        }
    }
}
