package org.example.programacion.DAO;

import org.example.programacion.Modelo.Jugadores;
import org.example.programacion.Util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadoresDAO {

    public List<Jugadores> getAllJugadores() {
        List<Jugadores> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM Jugador";
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
            throw new RuntimeException("Error al obtener jugadores: " + e.getMessage(), e);
        }
        return jugadores;
    }

    public List<Jugadores> getJugadoresByEquipoId(int idEquipo) {
        List<Jugadores> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM Jugador WHERE id_equipo = ?";
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
            throw new RuntimeException("Error al obtener jugadores por equipo: " + e.getMessage(), e);
        }
        return jugadores;
    }

    public String getEquipoNameByJugadorId(int idJugador) {
        String sql = "SELECT e.nombre_equipo FROM Equipo e JOIN Jugador j ON e.id_equipo = j.id_equipo WHERE j.id_jugador = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre_equipo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener nombre de equipo por ID de jugador: " + e.getMessage(), e);
        }
        return null;
    }

    public void insertJugador(Jugadores jugador, int idEquipo) {
        String sql = "INSERT INTO Jugador (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            throw new RuntimeException("Error al insertar jugador: " + e.getMessage(), e);
        }
    }

    public void updateJugador(Jugadores jugador, int idEquipo) {
        String sql = "UPDATE Jugador SET nombre_jugador = ?, apellido = ?, nacionalidad = ?, fecha_nacimiento = ?, nickname = ?, rol = ?, sueldo = ?, id_equipo = ? WHERE id_jugador = ?";

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
            throw new RuntimeException("Error al actualizar jugador: " + e.getMessage(), e);
        }
    }

    public void deleteJugador(int idJugador) {
        String sql = "DELETE FROM Jugador WHERE id_jugador = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJugador);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar jugador: " + e.getMessage(), e);
        }
    }
}
