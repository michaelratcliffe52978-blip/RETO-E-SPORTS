package org.example.programacion.DAO;

import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jugadores;
import org.example.programacion.Util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquiposDAO {

    public List<Equipos> getAllEquipos() {
        List<Equipos> equipos = new ArrayList<>();
        String sql = "SELECT * FROM Equipo";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                equipos.add(new Equipos(
                        rs.getInt("id_equipo"),
                        rs.getString("nombre_equipo"),
                        rs.getDate("fecha_fundacion").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipos;
    }
    public List<String> obtenerNombresEquipos() {
        List<String> equipos = new ArrayList<>();
        String sql = "SELECT NOMBRE FROM EQUIPO";
        // ... lógica para conectar y llenar la lista
        return equipos;
    }



    public void insertEquipo(Equipos equipo) throws SQLException {
        String sql = "INSERT INTO Equipo (nombre_equipo, fecha_fundacion) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipo.getNombreEquipo());
            pstmt.setDate(2, Date.valueOf(equipo.getFechaFundacion()));
            pstmt.executeUpdate();
        }
    }

    public void updateEquipo(Equipos equipo) throws SQLException {
        String sql = "UPDATE Equipo SET nombre_equipo = ?, fecha_fundacion = ? WHERE id_equipo = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipo.getNombreEquipo());
            pstmt.setDate(2, Date.valueOf(equipo.getFechaFundacion()));
            pstmt.setInt(3, equipo.getIdEquipo());
            pstmt.executeUpdate();
        }
    }

    public List<Jugadores> getJugadoresPorEquipo(String nombreEquipo) {
        List<Jugadores> lista = new ArrayList<>();
        // Traemos todas las columnas necesarias de la tabla Jugador
        String sql = "SELECT j.* FROM JUGADOR j " +
                "JOIN EQUIPO e ON j.id_equipo = e.id_equipo " +
                "WHERE e.nombre_equipo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreEquipo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Jugadores(
                        rs.getString("id_jugador"),
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
            e.printStackTrace();
        }
        return lista;
    }

    public void deleteEquipo(int idEquipo) throws SQLException {
        String sql = "DELETE FROM Equipo WHERE id_equipo = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);
            pstmt.executeUpdate();
        }
    }

    public List<String> getAllEquipoNames() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre_equipo FROM Equipo";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                nombres.add(rs.getString("nombre_equipo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombres;
    }

    public boolean validarMinimoJugadores(int minimo) {
        String sql = "SELECT e.nombre_equipo FROM Equipo e " +
                "LEFT JOIN Jugador j ON e.id_equipo = j.id_equipo " +
                "GROUP BY e.id_equipo, e.nombre_equipo " +
                "HAVING COUNT(j.id_jugador) < ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, minimo);
            ResultSet rs = pstmt.executeQuery();
            return !rs.next(); // Si no hay equipos con menos jugadores, retorna true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
