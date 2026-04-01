package org.example.programacion.Controladores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnfrentamientoController {

    public static boolean esEstructuraModificable(int idCompeticion) {
        String sql = "SELECT estado FROM Competicion WHERE id_competicion = ?";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCompeticion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String estado = rs.getString("estado");
                    // Retorna TRUE si NO está cerrado (es decir, se puede editar)
                    return !estado.equalsIgnoreCase("cerrado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> generarCalendarioAutomatico(int idJornadaDefault) {
        // 1. Sacamos los nombres de la tabla EQUIPO (que es donde están registrados)
        String sqlEquipos = "SELECT NOMBRE_EQUIPO FROM EQUIPO";

        // 2. Insertamos en ENFRENTAMIENTO con tus columnas exactas
        String sqlInsert = "INSERT INTO ENFRENTAMIENTO (FECHA_ENFRENTAMIENTO, EQUIPO1, EQUIPO2, HORA, ID_JORNADA) VALUES (?, ?, ?, ?, ?)";

        List<String> listaEquipos = new ArrayList<>();
        List<String> partidosCreados = new ArrayList<>();

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection()) {
            // PASO A: Leer los equipos que existen en la base de datos
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlEquipos)) {
                while (rs.next()) {
                    listaEquipos.add(rs.getString("NOMBRE_EQUIPO"));
                }
            }

            // PASO B: Si hay menos de 2 equipos, no se puede hacer un calendario
            if (listaEquipos.size() < 2) {
                return partidosCreados; // Devolverá lista vacía
            }

            // PASO C: Generar combinaciones e insertar
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                for (int i = 0; i < listaEquipos.size(); i++) {
                    for (int j = i + 1; j < listaEquipos.size(); j++) {
                        String eq1 = listaEquipos.get(i);
                        String eq2 = listaEquipos.get(j);

                        pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Fecha hoy
                        pstmt.setString(2, eq1); // Columna equipo1
                        pstmt.setString(3, eq2); // Columna equipo2
                        pstmt.setString(4, "12:00"); // Columna hora
                        pstmt.setInt(5, idJornadaDefault); // Columna id_jornada

                        pstmt.executeUpdate();
                        partidosCreados.add(eq1 + " vs " + eq2);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return partidosCreados;
    }
}
