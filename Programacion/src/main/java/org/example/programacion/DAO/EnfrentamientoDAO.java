package org.example.programacion.DAO;

import org.example.programacion.Util.ConexionBD;

import java.sql.*;

public class EnfrentamientoDAO {

    public int getEnfrentamientoIdByEquipos(String equipo1, String equipo2) throws SQLException {
        String sql = "SELECT ID_PARTIDO FROM ENFRENTAMIENTO WHERE EQUIPO1 = ? AND EQUIPO2 = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipo1);
            pstmt.setString(2, equipo2);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_PARTIDO");
            }
        }
        throw new SQLException("Enfrentamiento no encontrado");
    }

    public void actualizarResultado(int idPartido, int idEq1, String res1, int idEq2, String res2) throws SQLException {
        String sql = "MERGE INTO Equipo_Enfrentamiento t " +
                "USING (SELECT ? as id_e, ? as id_p, ? as res FROM dual) s " +
                "ON (t.id_equipo = s.id_e AND t.id_partido = s.id_p) " +
                "WHEN MATCHED THEN UPDATE SET t.resultado = s.res " +
                "WHEN NOT MATCHED THEN INSERT (id_equipo, id_partido, resultado) VALUES (s.id_e, s.id_p, s.res)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Para equipo 1
            pstmt.setInt(1, idEq1);
            pstmt.setInt(2, idPartido);
            pstmt.setString(3, res1);
            pstmt.executeUpdate();

            // Para equipo 2
            pstmt.setInt(1, idEq2);
            pstmt.setInt(2, idPartido);
            pstmt.setString(3, res2);
            pstmt.executeUpdate();
        }
    }
}
