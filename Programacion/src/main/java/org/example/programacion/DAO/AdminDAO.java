package org.example.programacion.DAO;

import org.example.programacion.Modelo.Usuarios;
import org.example.programacion.Util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la que se encarga de hablar directamente con la base de datos
 * para gestionar la tabla de USUARIOS.
 * Hace todo el trabajo sucio: leer, meter, cambiar y borrar gente.
 * * @author equipo4
 * @version 1.0
 */
public class AdminDAO {

    /**
     * Se va a la base de datos y se trae a todos los usuarios que hay apuntados.
     * Crea un objeto {@link Usuarios} por cada fila que encuentra.
     * * @return Una lista con todos los usuarios encontrados.
     * @throws RuntimeException Si la base de datos se ralla y no deja leer.
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
            throw new RuntimeException("Error al pillar los usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    /**
     * Mete un usuario nuevo en la tabla. El ID se suele generar solo (autoincremento).
     * * @param username El nombre de cuenta que va a usar.
     * @param password La clave secreta.
     * @param tipo Si va a ser 'admin' o 'user'.
     * @throws RuntimeException Si hay algún fallo al insertar los datos.
     */
    public void insertUsuario(String username, String password, String tipo) {
        String sql = "INSERT INTO USUARIOS (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, tipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al meter el usuario nuevo: " + e.getMessage(), e);
        }
    }

    /**
     * Busca a un usuario por su ID y le cambia el nombre, la contra y el rol.
     * * @param idUsuario El número del usuario que queremos editar.
     * @param username El nuevo nombre de cuenta.
     * @param password La nueva clave.
     * @param tipo El nuevo rol.
     * @throws RuntimeException Si falla la actualización en el SQL.
     */
    public void updateUsuario(int idUsuario, String username, String password, String tipo) {
        String sql = "UPDATE USUARIOS SET NOMBRE_USUARIO = ?, CONTRASENA = ?, TIPO = ? WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, tipo);
            pstmt.setInt(4, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar los datos del usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Borra a un usuario de la base de datos para siempre usando su ID.
     * * @param idUsuario El ID del usuario que vamos a fulminar.
     * @throws RuntimeException Si hay un error al intentar borrar la fila.
     */
    public void deleteUsuario(int idUsuario) {
        String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar el usuario: " + e.getMessage(), e);
        }
    }
}