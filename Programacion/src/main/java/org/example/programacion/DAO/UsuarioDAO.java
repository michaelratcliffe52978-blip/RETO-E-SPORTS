package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuarios;
import org.example.programacion.Util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void insertUsuario(String username, String password, String rol) {
        String sql = "INSERT INTO USUARIOS (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, rol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario: " + e.getMessage(), e);
        }
    }

    public List<Usuarios> getAllUsuarios() {
        List<Usuarios> usuarios = new ArrayList<>();
        String sql = "SELECT ID_USUARIO, NOMBRE_USUARIO, CONTRASENA, TIPO FROM USUARIOS";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuarios u = new Usuarios(
                        String.valueOf(rs.getInt("ID_USUARIO")),
                        rs.getString("NOMBRE_USUARIO"),
                        rs.getString("CONTRASENA"),
                        rs.getString("TIPO")
                );
                usuarios.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    public void updateUsuario(int idUsuario, String username, String password, String rol) {
        String sql = "UPDATE USUARIOS SET NOMBRE_USUARIO = ?, CONTRASENA = ?, TIPO = ? WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, rol);
            pstmt.setInt(4, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    public void deleteUsuario(int idUsuario) {
        String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    public boolean validarAdmin(String user, String pass) {
        String sql = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND UPPER(TIPO) = 'ADMIN'";

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
            throw new RuntimeException("Error al validar admin: " + e.getMessage(), e);
        }
    }

    public boolean validarUsuario(String user, String pass) {
        String sql = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND UPPER(TIPO) = 'USER'";

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
            throw new RuntimeException("Error al validar usuario: " + e.getMessage(), e);
        }
    }

    public Usuarios obtenerUsuarioConCredenciales(String user, String pass) {
        String sql = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuarios(
                            String.valueOf(rs.getInt("ID_USUARIO")),
                            rs.getString("NOMBRE_USUARIO"),
                            rs.getString("CONTRASENA"),
                            rs.getString("TIPO")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuario con credenciales: " + e.getMessage(), e);
        }
        return null;
    }
}