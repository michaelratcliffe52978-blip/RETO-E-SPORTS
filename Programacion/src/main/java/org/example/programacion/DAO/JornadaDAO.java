package org.example.programacion.DAO;

import org.example.programacion.Modelo.Jornada;
import org.example.programacion.Util.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JornadaDAO {

    public int insertJornada(int numero, LocalDate fecha) {
        String sql = "INSERT INTO JORNADA (NUMERO_JORNADA, FECHA_JORNADA) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numero);
            pstmt.setDate(2, Date.valueOf(fecha));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar jornada: " + e.getMessage(), e);
        }

        // Get the ID, assuming ID_JORNADA is auto and NUMERO_JORNADA is unique
        String sqlSelect = "SELECT ID_JORNADA FROM JORNADA WHERE NUMERO_JORNADA = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, numero);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_JORNADA");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ID de jornada: " + e.getMessage(), e);
        }
        throw new RuntimeException("No se pudo obtener ID de jornada");
    }

    public List<Jornada> getAllJornadas() {
        List<Jornada> jornadas = new ArrayList<>();
        String sql = "SELECT ID_JORNADA, NUMERO_JORNADA, FECHA_JORNADA FROM JORNADA";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Jornada j = new Jornada(
                        String.valueOf(rs.getInt("ID_JORNADA")),
                        rs.getInt("NUMERO_JORNADA"),
                        rs.getDate("FECHA_JORNADA").toLocalDate(),
                        null
                );
                jornadas.add(j);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener jornadas: " + e.getMessage(), e);
        }
        return jornadas;
    }

    public void updateJornada(int idJornada, int numero, LocalDate fecha) {
        String sql = "UPDATE JORNADA SET NUMERO_JORNADA = ?, FECHA_JORNADA = ? WHERE ID_JORNADA = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numero);
            pstmt.setDate(2, Date.valueOf(fecha));
            pstmt.setInt(3, idJornada);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar jornada: " + e.getMessage(), e);
        }
    }

    public void deleteJornada(int idJornada) {
        String sql = "DELETE FROM JORNADA WHERE ID_JORNADA = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJornada);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar jornada: " + e.getMessage(), e);
        }
    }
}
