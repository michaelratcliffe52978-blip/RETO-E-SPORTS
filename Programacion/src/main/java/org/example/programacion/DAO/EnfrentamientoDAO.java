package org.example.programacion.DAO;

import org.example.programacion.Modelo.Equipos;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Esta clase es el cerebro de los partidos en la base de datos.
 * Se encarga de buscar IDs de partidos, actualizar los goles/puntos,
 * y lo más importante: generar el calendario de toda la temporada.
 * * @author equipo4
 * @version 1.0
 */
public class EnfrentamientoDAO {

    /**
     * Busca el ID de un partido sabiendo qué dos equipos juegan.
     * * @param equipo1 Nombre del equipo local.
     * @param equipo2 Nombre del equipo visitante.
     * @return El ID numérico del partido.
     * @throws RuntimeException Si no encuentra ese partido en la tabla.
     */
    public int getEnfrentamientoIdByEquipos(String equipo1, String equipo2) {
        String sql = "SELECT ID_PARTIDO FROM ENFRENTAMIENTOS WHERE EQUIPO1 = ? AND EQUIPO2 = ?";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipo1);
            pstmt.setString(2, equipo2);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("ID_PARTIDO");
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el partido: " + e.getMessage(), e);
        }
        throw new RuntimeException("No he encontrado el partido entre " + equipo1 + " y " + equipo2);
    }

    /**
     * Actualiza los resultados de ambos equipos en un partido concreto.
     * Se mete en la tabla intermedia EQUIPOS_ENFRENTAMIENTOS.
     * * @param idPartido ID del enfrentamiento.
     * @param idEq1 ID del primer equipo.
     * @param res1 Resultado (goles/puntos) del primer equipo.
     * @param idEq2 ID del segundo equipo.
     * @param res2 Resultado del segundo equipo.
     */
    public void actualizarResultado(int idPartido, int idEq1, String res1, int idEq2, String res2) {
        String sql = "UPDATE EQUIPOS_ENFRENTAMIENTOS SET resultado = ? WHERE id_equipo = ? AND id_partido = ?";

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

        } catch (SQLException e) {
            throw new RuntimeException("Fallo al actualizar el marcador: " + e.getMessage(), e);
        }
    }

    /**
     * Saca una lista de textos con todos los partidos y resultados de una jornada.
     * Útil para que el usuario vea cómo quedaron los partidos en el historial.
     * * @param idJornada El ID de la jornada que queremos cotillear.
     * @return Lista de Strings formateados con los resultados.
     */
    public List<String> obtenerEnfrentamientosDeJornada(int idJornada) {
        java.util.List<String> resultados = new java.util.ArrayList<>();
        String sql = "SELECT e.id_partido, e.equipo1, e.equipo2, e.hora, " +
                "ee1.resultado as res_eq1, ee2.resultado as res_eq2 " +
                "FROM ENFRENTAMIENTOS e " +
                "LEFT JOIN EQUIPOS_ENFRENTAMIENTOS ee1 ON e.id_partido = ee1.id_partido " +
                "LEFT JOIN EQUIPOS_ENFRENTAMIENTOS ee2 ON e.id_partido = ee2.id_partido " +
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
            throw new RuntimeException("Error al traer los partidos de la jornada: " + e.getMessage(), e);
        }
        return resultados;
    }

    /**
     * Comprueba si todos los equipos tienen al menos X jugadores.
     * Si alguno está "cojo", devuelve false para que no se pueda empezar la liga.
     * * @param minimo Cuántos jugadores debe tener cada equipo como poco.
     * @return true si todos cumplen, false si falta gente.
     */
    public boolean validarMinimoJugadores(int minimo) {
        String sql = "SELECT COUNT(*) FROM JUGADORES WHERE ID_EQUIPO = ?";
        String sqlEquipos = "SELECT ID_EQUIPO FROM EQUIPOS";

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
            throw new RuntimeException("Fallo al validar las plantillas: " + e.getMessage(), e);
        }
    }

    /**
     * Mira si ya hay partidos metidos en la tabla de ENFRENTAMIENTOS.
     * * @return true si ya hay un calendario hecho.
     */
    public boolean existeCalendario() {
        String sql = "SELECT COUNT(*) as total FROM ENFRENTAMIENTOS";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al chequear si ya hay calendario: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Borra todo el calendario: resultados, enfrentamientos y jornadas.
     * Se usa para "limpiar la pizarra" antes de generar uno nuevo.
     */
    public void eliminarCalendarioAnterior() {
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection()) {
            conn.setAutoCommit(false); // Por si falla, que no borre a medias

            // Borramos de abajo a arriba por las claves foráneas
            String sqlDelEquipoEnfr = "DELETE FROM EQUIPOS_ENFRENTAMIENTOS";
            try (Statement stmt = conn.createStatement()) { stmt.executeUpdate(sqlDelEquipoEnfr); }

            String sqlDelEnfr = "DELETE FROM ENFRENTAMIENTOS";
            try (Statement stmt = conn.createStatement()) { stmt.executeUpdate(sqlDelEnfr); }

            String sqlDelJornada = "DELETE FROM JORNADAS";
            try (Statement stmt = conn.createStatement()) { stmt.executeUpdate(sqlDelJornada); }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido borrar el calendario viejo: " + e.getMessage(), e);
        }
    }

    /**
     * El método "tocho". Genera un calendario automático tipo liga.
     * Si son impares, añade un equipo de descanso. Crea las jornadas y los cruces.
     * * @param equipos Lista de equipos apuntados.
     * @return true si se ha generado guay.
     */
    public boolean generarYGuardarCalendario(List<Equipos> equipos) {
        if (equipos.size() < 2) return false;

        if (existeCalendario()) {
            eliminarCalendarioAnterior();
        }

        // Si son impares, metemos uno de descanso para que cuadren los emparejamientos
        if (equipos.size() % 2 != 0) {
            equipos.add(new Equipos(-1, "DESCANSO", null));
        }

        int numEquipos = equipos.size();
        int numJornadas = numEquipos - 1;
        int partidosPorJornada = numEquipos / 2;

        LocalDate fechaJornada = LocalDate.now().plusDays(7);

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection()) {
            conn.setAutoCommit(false);

            for (int i = 0; i < numJornadas; i++) {
                // Crear la jornada
                String sqlInsertJornada = "INSERT INTO JORNADAS (NUMERO_JORNADA, FECHA_JORNADA) VALUES (?, ?)";
                int idJornada = -1;

                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertJornada, new String[]{"ID_JORNADA"})) {
                    pstmt.setInt(1, (i + 1));
                    pstmt.setDate(2, java.sql.Date.valueOf(fechaJornada));
                    pstmt.executeUpdate();
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) idJornada = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    // Si ya existe por algún motivo, pillamos su ID
                    String sqlGetJornada = "SELECT ID_JORNADA FROM JORNADAS WHERE NUMERO_JORNADA = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlGetJornada)) {
                        pstmt.setInt(1, (i + 1));
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) idJornada = rs.getInt("ID_JORNADA");
                        }
                    }
                }

                LocalTime horaPartido = LocalTime.of(10, 0);

                // Algoritmo para cruzar a todos contra todos
                for (int j = 0; j < partidosPorJornada; j++) {
                    int localIdx = (i + j) % (numEquipos - 1);
                    int visitanteIdx = (numEquipos - 1 - j + i) % (numEquipos - 1);
                    if (j == 0) visitanteIdx = numEquipos - 1;

                    Equipos local = equipos.get(localIdx);
                    Equipos visitante = equipos.get(visitanteIdx);

                    // Si no es el equipo de descanso, guardamos el partido
                    if (local.getIdEquipo() != -1 && visitante.getIdEquipo() != -1 && idJornada != -1) {
                        String sql = "INSERT INTO ENFRENTAMIENTOS (FECHA_ENFRENTAMIENTO, EQUIPO1, EQUIPO2, HORA, ID_JORNADA) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setDate(1, java.sql.Date.valueOf(fechaJornada));
                            pstmt.setString(2, local.getNombreEquipo());
                            pstmt.setString(3, visitante.getNombreEquipo());
                            pstmt.setString(4, horaPartido.toString());
                            pstmt.setInt(5, idJornada);
                            pstmt.executeUpdate();
                        }
                        horaPartido = horaPartido.plusHours(2);
                    }
                }
                fechaJornada = fechaJornada.plusWeeks(1);
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error fatal generando el calendario: " + e.getMessage(), e);
        }
    }
}