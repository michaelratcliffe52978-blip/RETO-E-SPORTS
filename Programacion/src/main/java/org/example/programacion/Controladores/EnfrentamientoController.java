package org.example.programacion.Controladores;

import org.example.programacion.DAO.EnfrentamientoDAO;
import org.example.programacion.DAO.JornadaDAO;

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
            throw new RuntimeException("Error al verificar estructura modificable: " + e.getMessage(), e);
        }
        return false;
    }

    public static List<String> generarCalendarioAutomatico(int numeroJornada) {
        // 1. Insertar la jornada si no existe y obtener ID
        JornadaDAO jornadaDAO = new JornadaDAO();
        int idJornada;
        List<String> partidosCreados = new ArrayList<>();
        try {
            idJornada = jornadaDAO.insertJornada(numeroJornada, java.time.LocalDate.now());
        } catch (Exception e) {
            // Si ya existe, obtener el ID existente
            System.out.println("Jornada ya existe, obteniendo ID: " + e.getMessage());
            try {
                String sqlSelect = "SELECT ID_JORNADA FROM JORNADA WHERE NUMERO_JORNADA = ?";
                try (Connection conn2 = org.example.programacion.Util.ConexionBD.getConnection();
                     PreparedStatement pstmt2 = conn2.prepareStatement(sqlSelect)) {
                    pstmt2.setInt(1, numeroJornada);
                    ResultSet rs = pstmt2.executeQuery();
                    if (rs.next()) {
                        idJornada = rs.getInt("ID_JORNADA");
                    } else {
                        throw new RuntimeException("Jornada no encontrada");
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("Error obteniendo ID de jornada: " + e2.getMessage(), e2);
            }
        }

        // 2. Sacamos los nombres de la tabla EQUIPO (que es donde están registrados)
        String sqlEquipos = "SELECT NOMBRE_EQUIPO FROM EQUIPO";

        // 3. Insertamos en ENFRENTAMIENTO con tus columnas exactas
        String sqlInsert = "INSERT INTO ENFRENTAMIENTO (FECHA_ENFRENTAMIENTO, EQUIPO1, EQUIPO2, HORA, ID_JORNADA) VALUES (?, ?, ?, ?, ?)";

        List<String> listaEquipos = new ArrayList<>();

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
                        pstmt.setInt(5, idJornada); // Columna id_jornada

                        pstmt.executeUpdate();
                        partidosCreados.add(eq1 + " vs " + eq2);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL: " + e.getMessage(), e);
        }
        return partidosCreados;
    }

    public void actualizarResultado(String equipo1, String equipo2, String res1, String res2) {
        EnfrentamientoDAO dao = new EnfrentamientoDAO();
        try {
            int idPartido = dao.getEnfrentamientoIdByEquipos(equipo1, equipo2);
            int idEq1 = getIdEquipoByName(equipo1);
            int idEq2 = getIdEquipoByName(equipo2);
            dao.actualizarResultado(idPartido, idEq1, res1, idEq2, res2);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar resultado: " + e.getMessage(), e);
        }
    }

    private static int getIdEquipoByName(String nombre) {
        String sql = "SELECT id_equipo FROM Equipo WHERE nombre_equipo = ?";
        try (var conn = org.example.programacion.Util.ConexionBD.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ID de equipo: " + e.getMessage(), e);
        }
        throw new RuntimeException("Equipo no encontrado");
    }
}
