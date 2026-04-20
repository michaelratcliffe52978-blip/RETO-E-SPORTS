package org.example.programacion.DAO;

import org.example.programacion.Modelo.Jugadores;
import org.example.programacion.Utilidades.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de toda la comunicación con la tabla JUGADORES.
 * Permite listar a todos los cracks del torneo, filtrarlos por equipo,
 * fichar nuevos jugadores, actualizar sus estadísticas o darlos de baja.
 * * @author equipo4
 * @version 1.0
 */
public class JugadoresDAO {

    /**
     * Recupera absolutamente todos los jugadores registrados en la base de datos.
     * * @return Una lista completa de objetos {@link Jugadores}.
     * @throws RuntimeException Si hay algún error al consultar la tabla.
     */
    public List<Jugadores> getAllJugadores() {
        List<Jugadores> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM JUGADORES";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jugadores.add(new Jugadores(
                        String.valueOf(rs.getInt("id_jugador")),
                        rs.getString("nombre_jugador"),
                        rs.getString("apellido"),
                        rs.getString("nacionalidad"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nickname"),
                        rs.getString("rol"),
                        rs.getDouble("sueldo")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista global de jugadores: " + e.getMessage(), e);
        }
        return jugadores;
    }

    /**
     * Busca a los jugadores que pertenecen a un equipo específico usando su ID.
     * * @param idEquipo El ID numérico del equipo.
     * @return Una lista de jugadores que juegan en ese equipo.
     * @throws RuntimeException Si falla la consulta filtrada.
     */
    public List<Jugadores> getJugadoresByEquipoId(int idEquipo) {
        List<Jugadores> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM JUGADORES WHERE id_equipo = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                jugadores.add(new Jugadores(
                        String.valueOf(rs.getInt("id_jugador")),
                        rs.getString("nombre_jugador"),
                        rs.getString("apellido"),
                        rs.getString("nacionalidad"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nickname"),
                        rs.getString("rol"),
                        rs.getDouble("sueldo")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar jugadores por equipo: " + e.getMessage(), e);
        }
        return jugadores;
    }

    /**
     * Obtiene el nombre del equipo de un jugador haciendo un JOIN con la tabla EQUIPOS.
     * * @param idJugador El ID del jugador que queremos consultar.
     * @return El nombre del equipo como String, o null si no se encuentra.
     * @throws RuntimeException Si hay un error en la unión de tablas.
     */
    public String getEquipoNameByJugadorId(int idJugador) {
        String sql = "SELECT e.nombre_equipo FROM EQUIPOS e JOIN JUGADORES j ON e.id_equipo = j.id_equipo WHERE j.id_jugador = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre_equipo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el nombre del equipo mediante JOIN: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Inserta un nuevo jugador en la base de datos asignándolo a un equipo.
     * * @param jugador Objeto con toda la información personal y profesional del jugador.
     * @param idEquipo ID del equipo en el que va a jugar.
     * @throws RuntimeException Si falla el INSERT.
     */
    public void insertJugador(Jugadores jugador, int idEquipo) {
        String sql = "INSERT INTO JUGADORES (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, jugador.getNombreJugador());
            pstmt.setString(2, jugador.getApellido());
            pstmt.setString(3, jugador.getNacionalidad());
            pstmt.setDate(4, Date.valueOf(jugador.getFechaNacimiento()));
            pstmt.setString(5, jugador.getNickname());
            pstmt.setString(6, jugador.getRol());
            pstmt.setDouble(7, jugador.getSueldo());
            pstmt.setInt(8, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al dar de alta al jugador: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza todos los campos de un jugador existente.
     * * @param jugador Objeto con los datos actualizados (incluyendo su ID).
     * @param idEquipo Nuevo ID de equipo (por si ha sido traspasado).
     * @throws RuntimeException Si falla el UPDATE.
     */
    public void updateJugador(Jugadores jugador, int idEquipo) {
        String sql = "UPDATE JUGADORES SET nombre_jugador = ?, apellido = ?, nacionalidad = ?, fecha_nacimiento = ?, nickname = ?, rol = ?, sueldo = ?, id_equipo = ? WHERE id_jugador = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, jugador.getNombreJugador());
            pstmt.setString(2, jugador.getApellido());
            pstmt.setString(3, jugador.getNacionalidad());
            pstmt.setDate(4, Date.valueOf(jugador.getFechaNacimiento()));
            pstmt.setString(5, jugador.getNickname());
            pstmt.setString(6, jugador.getRol());
            pstmt.setDouble(7, jugador.getSueldo());
            pstmt.setInt(8, idEquipo);
            pstmt.setInt(9, Integer.parseInt(jugador.getIdJugador()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar los datos del jugador: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina a un jugador de la base de datos de forma definitiva.
     * * @param idJugador El ID del jugador que queremos borrar.
     * @throws RuntimeException Si hay un problema al ejecutar el DELETE.
     */
    public void deleteJugador(int idJugador) {
        String sql = "DELETE FROM JUGADORES WHERE id_jugador = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJugador);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar al jugador: " + e.getMessage(), e);
        }
    }
}