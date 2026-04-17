package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuarios;
import org.example.programacion.Util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es el corazón de la seguridad en la base de datos.
 * Se encarga de gestionar la tabla USUARIOS, permitiendo el registro,
 * la edición y, sobre todo, la validación de credenciales para el login.
 * * @author equipo4
 * @version 1.0
 */
public class UsuarioDAO {

    /**
     * Registra un nuevo usuario en la base de datos.
     * * @param username Nombre de acceso.
     * @param password Contraseña secreta.
     * @param rol El tipo de cuenta ('USER' o 'ADMIN').
     * @throws RuntimeException Si falla la conexión o el INSERT.
     */
    public void insertUsuario(String username, String password, String rol) {
        String sql = "INSERT INTO USUARIOS (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, rol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Trae la lista completa de usuarios registrados.
     * * @return Una lista de objetos {@link Usuarios}.
     */
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
            throw new RuntimeException("Error al obtener la lista de usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    /**
     * Actualiza los datos de un usuario existente buscando por su ID.
     * * @param idUsuario El ID del usuario a modificar.
     * @param username Nuevo nombre.
     * @param password Nueva contraseña.
     * @param rol Nuevo rol.
     */
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
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Borra un usuario de la base de datos definitivamente.
     * * @param idUsuario ID del usuario a eliminar.
     */
    public void deleteUsuario(int idUsuario) {
        String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Comprueba si existe un administrador con esas credenciales.
     * Usa UPPER(TIPO) para que dé igual si en la BD pone 'admin' o 'ADMIN'.
     * * @param user Nombre introducido.
     * @param pass Contraseña introducida.
     * @return true si los datos son correctos y es administrador.
     */
    public boolean validarAdmin(String user, String pass) {
        String sql = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND UPPER(TIPO) = 'ADMIN'";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si hay una fila, es que es válido
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fallo al validar acceso de administrador: " + e.getMessage(), e);
        }
    }

    /**
     * Comprueba si existe un usuario estándar con esas credenciales.
     * * @param user Nombre introducido.
     * @param pass Contraseña introducida.
     * @return true si los datos coinciden y el rol es 'USER'.
     */
    public boolean validarUsuario(String user, String pass) {
        String sql = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND UPPER(TIPO) = 'USER'";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fallo al validar acceso de usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Busca y devuelve el objeto Usuario completo si el nombre y la contraseña coinciden.
     * Es muy útil para saber qué tipo de usuario acaba de entrar y personalizar la interfaz.
     * * @param user Nombre de usuario.
     * @param pass Contraseña.
     * @return El objeto {@link Usuarios} si existe, o null si las credenciales fallan.
     */
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
            throw new RuntimeException("Error al recuperar perfil de usuario: " + e.getMessage(), e);
        }
        return null;
    }
}