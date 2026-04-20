package org.example.programacion.DAO;

import org.example.programacion.Utilidades.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Esta clase es la que controla el estado de la competición en la base de datos.
 * Sirve para saber si todavía se puede apuntar gente o si ya se ha cerrado todo
 * y no se puede tocar nada más.
 * * @author equipo4
 * @version 1.0
 */
public class CompeticionDAO {

    /**
     * Cambia el estado de una competición específica (por ejemplo, a 'abierto' o 'cerrado').
     * * @param idCompeticion El número de ID de la competición que queremos cambiar.
     * @param nuevoEstado El texto con el nuevo estado que le queremos poner.
     * @return true si se ha cambiado bien, false si algo ha fallado por el camino.
     */
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

    /**
     * Chapar las inscripciones. Busca la primera competición que encuentra y le
     * pone el estado en 'cerrado' para que nadie más pueda meter mano.
     */
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

    /**
     * Mira en qué estado está la competición ahora mismo.
     * * @return Un String con el estado (abierto, cerrado, etc.) o null si hay algún lío con el SQL.
     */
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