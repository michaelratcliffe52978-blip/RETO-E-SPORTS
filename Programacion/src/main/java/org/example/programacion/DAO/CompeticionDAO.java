package org.example.programacion.DAO;

import org.example.programacion.Util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompeticionDAO {

    public boolean actualizarEstado(int idCompeticion, String nuevoEstado) {
        String sql = "UPDATE Competiciones SET estado = ? WHERE id_competicion = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado.toLowerCase());
            pstmt.setInt(2, idCompeticion);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public void cerrarInscripciones() {
        // Actualiza el estado de la competición a cerrado
        String sql = "UPDATE Competiciones SET estado = 'cerrado' WHERE ROWNUM = 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cerrar inscripciones: " + e.getMessage());
        }
    }

    public String getEstado() {
        String sql = "SELECT estado FROM Competiciones WHERE ROWNUM = 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             var rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("estado");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estado: " + e.getMessage());
        }
        return null;
    }
}