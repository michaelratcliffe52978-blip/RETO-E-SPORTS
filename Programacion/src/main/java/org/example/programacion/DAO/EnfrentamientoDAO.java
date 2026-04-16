package org.example.programacion.DAO;

import org.example.programacion.Modelo.Equipos;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EnfrentamientoDAO {
    // --- MÉTODOS EXISTENTES ---

    public int getEnfrentamientoIdByEquipos(String equipo1, String equipo2) {
        String sql = "SELECT ID_PARTIDO FROM ENFRENTAMIENTO WHERE EQUIPO1 = ? AND EQUIPO2 = ?";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipo1);
            pstmt.setString(2, equipo2);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("ID_PARTIDO");
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener enfrentamiento: " + e.getMessage(), e);
        }
        throw new RuntimeException("Enfrentamiento no encontrado");
    }

    public void actualizarResultado(int idPartido, int idEq1, String res1, int idEq2, String res2) {
        String sql = "UPDATE Equipo_Enfrentamiento SET resultado = ? WHERE id_equipo = ? AND id_partido = ?";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Actualizar resultado para equipo 1
            pstmt.setString(1, res1);
            pstmt.setInt(2, idEq1);
            pstmt.setInt(3, idPartido);
            pstmt.executeUpdate();

            // Actualizar resultado para equipo 2
            pstmt.setString(1, res2);
            pstmt.setInt(2, idEq2);
            pstmt.setInt(3, idPartido);
            pstmt.executeUpdate();

            System.out.println("Resultados actualizados - Partido: " + idPartido + ", Eq1: " + res1 + ", Eq2: " + res2);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar resultado: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los enfrentamientos de una jornada específica con sus resultados
     */
    public List<String> obtenerEnfrentamientosDeJornada(int idJornada) {
        java.util.List<String> resultados = new java.util.ArrayList<>();
        String sql = "SELECT e.id_partido, e.equipo1, e.equipo2, e.hora, " +
                "ee1.resultado as res_eq1, ee2.resultado as res_eq2 " +
                "FROM ENFRENTAMIENTO e " +
                "LEFT JOIN Equipo_Enfrentamiento ee1 ON e.id_partido = ee1.id_partido " +
                "LEFT JOIN Equipo_Enfrentamiento ee2 ON e.id_partido = ee2.id_partido " +
                "WHERE e.id_jornada = ? " +
                "ORDER BY e.hora";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJornada);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String res1 = rs.getString("res_eq1");
                    String res2 = rs.getString("res_eq2");
                    String resultado = rs.getString("equipo1") + " vs " + rs.getString("equipo2") +
                            " (" + (res1 != null && !res1.equals("-") ? res1 : "-") +
                            " - " + (res2 != null && !res2.equals("-") ? res2 : "-") +
                            ") [" + rs.getString("hora") + "]";
                    resultados.add(resultado);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener enfrentamientos de jornada: " + e.getMessage(), e);
        }
        return resultados;
    }



    /**
     * Valida que todos los equipos tengan al menos el número de jugadores indicado.
     */
    public boolean validarMinimoJugadores(int minimo) {
        String sql = "SELECT COUNT(*) FROM JUGADOR WHERE ID_EQUIPO = ?";
        String sqlEquipos = "SELECT ID_EQUIPO FROM EQUIPO";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rsEquipos = stmt.executeQuery(sqlEquipos)) {

            while (rsEquipos.next()) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, rsEquipos.getInt(1));
                    ResultSet rsCount = pstmt.executeQuery();
                    if (rsCount.next() && rsCount.getInt(1) < minimo) return false;
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar mínimo de jugadores: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si ya existe un calendario creado
     */
    public boolean existeCalendario() {
        String sql = "SELECT COUNT(*) as total FROM ENFRENTAMIENTO";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar calendario: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Elimina el calendario anterior (jornadas y enfrentamientos)
     */
    public void eliminarCalendarioAnterior() {
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection()) {
            conn.setAutoCommit(false);

            // Primero eliminar Equipo_Enfrentamiento
            String sqlDelEquipoEnfr = "DELETE FROM Equipo_Enfrentamiento";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sqlDelEquipoEnfr);
            }

            // Eliminar enfrentamientos
            String sqlDelEnfr = "DELETE FROM ENFRENTAMIENTO";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sqlDelEnfr);
            }

            // Eliminar jornadas
            String sqlDelJornada = "DELETE FROM JORNADA";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sqlDelJornada);
            }

            conn.commit();
            System.out.println("Calendario anterior eliminado correctamente");
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar calendario anterior: " + e.getMessage(), e);
        }
    }

    public boolean generarYGuardarCalendario(List<Equipos> equipos) {
        if (equipos.size() < 2) return false;

        // Verificar si existe calendario anterior
        if (existeCalendario()) {
            eliminarCalendarioAnterior();
            System.out.println("Calendario anterior eliminado. Generando nuevo calendario...");
        }

        // Si el número de equipos es impar, añadimos un equipo "fantasma" para el descanso
        if (equipos.size() % 2 != 0) {
            equipos.add(new Equipos(-1, "DESCANSO", null));
        }

        int numEquipos = equipos.size();
        int numJornadas = numEquipos - 1;
        int partidosPorJornada = numEquipos / 2;

        LocalDate fechaJornada = LocalDate.now().plusDays(7); // Primera jornada en una semana

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection()) {
            conn.setAutoCommit(false); // Para seguridad, si falla algo no guarda nada

            for (int i = 0; i < numJornadas; i++) {
                // Primero, crear la jornada si no existe
                String sqlInsertJornada = "INSERT INTO JORNADA (NUMERO_JORNADA, FECHA_JORNADA) VALUES (?, ?)";
                int idJornada = -1;

                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertJornada, new String[]{"ID_JORNADA"})) {
                    pstmt.setInt(1, (i + 1));
                    pstmt.setDate(2, java.sql.Date.valueOf(fechaJornada));
                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            idJornada = rs.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    // Si ya existe, intentar obtenerla
                    String sqlGetJornada = "SELECT ID_JORNADA FROM JORNADA WHERE NUMERO_JORNADA = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlGetJornada)) {
                        pstmt.setInt(1, (i + 1));
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                                idJornada = rs.getInt("ID_JORNADA");
                            }
                        }
                    }
                }

                LocalTime horaPartido = LocalTime.of(10, 0); // Empieza a las 10:00

                for (int j = 0; j < partidosPorJornada; j++) {
                    int localIdx = (i + j) % (numEquipos - 1);
                    int visitanteIdx = (numEquipos - 1 - j + i) % (numEquipos - 1);

                    if (j == 0) visitanteIdx = numEquipos - 1;

                    Equipos local = equipos.get(localIdx);
                    Equipos visitante = equipos.get(visitanteIdx);

                    // No guardamos el partido si uno es el equipo de descanso
                    if (local.getIdEquipo() != -1 && visitante.getIdEquipo() != -1 && idJornada != -1) {
                        String sql = "INSERT INTO ENFRENTAMIENTO (FECHA_ENFRENTAMIENTO, EQUIPO1, EQUIPO2, HORA, ID_JORNADA) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"ID_PARTIDO"})) {
                            pstmt.setDate(1, java.sql.Date.valueOf(fechaJornada));
                            pstmt.setString(2, local.getNombreEquipo());
                            pstmt.setString(3, visitante.getNombreEquipo());
                            pstmt.setString(4, horaPartido.toString());
                            pstmt.setInt(5, idJornada);
                            pstmt.executeUpdate();

                            // Obtener el ID del partido generado
                            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                                if (rs.next()) {
                                    int idPartido = rs.getInt(1);

                                    // Guardar en Equipo_Enfrentamiento para equipo1
                                    String sqlEquipoEnfr = "INSERT INTO Equipo_Enfrentamiento (id_equipo, id_partido, resultado) VALUES (?, ?, '-')";
                                    try (PreparedStatement pstmtEE = conn.prepareStatement(sqlEquipoEnfr)) {
                                        pstmtEE.setInt(1, local.getIdEquipo());
                                        pstmtEE.setInt(2, idPartido);
                                        pstmtEE.executeUpdate();
                                    }

                                    // Guardar en Equipo_Enfrentamiento para equipo2
                                    try (PreparedStatement pstmtEE = conn.prepareStatement(sqlEquipoEnfr)) {
                                        pstmtEE.setInt(1, visitante.getIdEquipo());
                                        pstmtEE.setInt(2, idPartido);
                                        pstmtEE.executeUpdate();
                                    }
                                }
                            }
                        }
                        horaPartido = horaPartido.plusHours(2); // Siguiente partido 2 horas después
                    }
                }
                fechaJornada = fechaJornada.plusWeeks(1); // Una jornada por semana
            }
            conn.commit();
            System.out.println("Nuevo calendario generado correctamente");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error al generar calendario: " + e.getMessage(), e);
        }
    }
}
