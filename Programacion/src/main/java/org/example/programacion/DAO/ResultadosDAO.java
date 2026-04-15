package org.example.programacion.DAO;

import org.example.programacion.Modelo.Enfrentamiento;
import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jornada;
import org.example.programacion.Util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadosDAO {

    public List<Enfrentamiento> getTodosLosResultados() {
        List<Enfrentamiento> lista = new ArrayList<>();

        // 1. Añadimos las columnas de goles a la consulta SQL
        String sql = "SELECT id_partido, fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada, goles_local, goles_visitante FROM Enfrentamiento";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Equipos eq1 = new Equipos(0, rs.getString("equipo1"), null);
                Equipos eq2 = new Equipos(0, rs.getString("equipo2"), null);

                // Número de jornada real de la BD
                Jornada jornadaTemp = new Jornada(null, rs.getInt("id_jornada"), null, null);

                // 2. Leemos los goles.
                // Usamos getObject y verificamos nulos por si el partido no se ha jugado
                Integer gL = (rs.getObject("goles_local") != null) ? rs.getInt("goles_local") : -1;
                Integer gV = (rs.getObject("goles_visitante") != null) ? rs.getInt("goles_visitante") : -1;

                lista.add(new Enfrentamiento(
                        String.valueOf(rs.getInt("id_partido")),
                        java.time.LocalTime.parse(rs.getString("hora")),
                        rs.getDate("fecha_enfrentamiento").toLocalDate(),
                        eq1,
                        eq2,
                        jornadaTemp,
                        gL, // Pasamos el valor real (o -1 si es nulo)
                        gV  // Pasamos el valor real (o -1 si es nulo)
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage() + ". Revisa si existen las columnas goles_local y goles_visitante.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}