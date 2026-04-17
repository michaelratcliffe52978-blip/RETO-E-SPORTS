package org.example.programacion.DAO;

import org.example.programacion.Modelo.Enfrentamientos;
import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jornadas;
import org.example.programacion.Util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadosDAO {

    public List<Enfrentamientos> getTodosLosResultados() {
        List<Enfrentamientos> lista = new ArrayList<>();

        // Corregido: Usamos NOMBRE_EQUIPO que es como se llama en tu tabla Equipo
        String sql = "SELECT e.id_partido, e.fecha_enfrentamiento, e.equipo1, e.equipo2, e.hora, e.id_jornada, " +
                "(SELECT resultado FROM EQUIPOS_ENFRENTAMIENTOS WHERE id_partido = e.id_partido AND id_equipo = (SELECT id_equipo FROM EQUIPOS WHERE NOMBRE_EQUIPO = e.equipo1)) as goles_l, " +
                "(SELECT resultado FROM EQUIPOS_ENFRENTAMIENTOS WHERE id_partido = e.id_partido AND id_equipo = (SELECT id_equipo FROM EQUIPOS WHERE NOMBRE_EQUIPO = e.equipo2)) as goles_v " +
                "FROM ENFRENTAMIENTOS e";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Equipos eq1 = new Equipos(0, rs.getString("equipo1"), null);
                Equipos eq2 = new Equipos(0, rs.getString("equipo2"), null);

                // Creamos el objeto Jornada con el número real de la BD
                Jornadas jor = new Jornadas(null, rs.getInt("id_jornada"), null, null);

                // Recuperamos los goles de las subconsultas
                // Si no hay resultado todavía (es NULL o "-"), ponemos 0 por defecto
                String resL = rs.getString("goles_l");
                String resV = rs.getString("goles_v");
                int gL = (resL != null && !resL.equals("-") && !resL.isEmpty()) ? Integer.parseInt(resL) : 0;
                int gV = (resV != null && !resV.equals("-") && !resV.isEmpty()) ? Integer.parseInt(resV) : 0;

                lista.add(new Enfrentamientos(
                        String.valueOf(rs.getInt("id_partido")),
                        java.time.LocalTime.parse(rs.getString("hora")),
                        rs.getDate("fecha_enfrentamiento").toLocalDate(),
                        eq1, eq2, jor, gL, gV
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error SQL en ResultadosDAO: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}