package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuario;
import org.example.programacion.Util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void insertUsuario(String username, String password, String rol) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, rol);
            pstmt.executeUpdate();
        }
    }

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
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public void updateUsuario(int idUsuario, String username, String password, String rol) throws SQLException {
        String sql = "UPDATE USUARIO SET NOMBRE_USUARIO = ?, CONTRASENA = ?, TIPO = ? WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, rol);
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

    public boolean validarAdmin(String user, String pass) {
        String sql = "SELECT * FROM USUARIO WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND UPPER(TIPO) = 'ADMIN'";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Validando admin: " + user);
                boolean result = rs.next();
                System.out.println("Resultado: " + result);
                return result;
            }
        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarUsuario(String user, String pass) {
        String sql = "SELECT * FROM USUARIO WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND UPPER(TIPO) = 'USER'";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Validando usuario: " + user);
                boolean result = rs.next();
                System.out.println("Resultado: " + result);
                return result;
            }
        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerUsuarioConCredenciales(String user, String pass) {
        String sql = "SELECT * FROM USUARIO WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            String.valueOf(rs.getInt("ID_USUARIO")),
                            rs.getString("NOMBRE_USUARIO"),
                            rs.getString("CONTRASENA"),
                            rs.getString("TIPO")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getMessage());
        }
        return null;
    }
}