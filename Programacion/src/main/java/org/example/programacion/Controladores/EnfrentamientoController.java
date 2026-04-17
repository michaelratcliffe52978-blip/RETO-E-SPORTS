package org.example.programacion.Controladores;

import org.example.programacion.DAO.EnfrentamientoDAO;
import org.example.programacion.DAO.JornadaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la que corta el bacalao con los partidos y el calendario.
 * Se encarga de ver si se puede tocar la competición, crear los partidos
 * solos y guardar quién ha ganado.
 * * @author equipo4
 * @version 1.0
 */
public class EnfrentamientoController {

    /**
     * Mira si la competición está abierta para poder cambiar cosas.
     * Si pone "cerrado" en la base de datos, pues no te deja tocar nada.
     * * @param idCompeticion El número de la competición que quieres mirar
     * @return true si se puede editar, false si ya está chapada
     */
    public static boolean esEstructuraModificable(int idCompeticion) {
        String sql = "SELECT estado FROM COMPETICIONES WHERE id_competicion = ?";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCompeticion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String estado = rs.getString("estado");
                    // Si no pone "cerrado", es que todavía podemos meter mano
                    return !estado.equalsIgnoreCase("cerrado");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al ver si se puede cambiar la estructura: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Esta es la joya de la corona. Te monta el calendario ella sola.
     * Busca los equipos, los empareja a todos contra todos y mete los partidos en la base de datos.
     * * @param numeroJornada El número de la jornada que quieres crear
     * @return Una lista de textos con los partidos que se han montado (ej: "EquipoA vs EquipoB")
     */
    public static List<String> generarCalendarioAutomatico(int numeroJornada) {
        JornadaDAO jornadaDAO = new JornadaDAO();
        int idJornada;
        List<String> partidosCreados = new ArrayList<>();

        try {
            // Intentamos crear la jornada nueva
            idJornada = jornadaDAO.insertJornada(numeroJornada, java.time.LocalDate.now());
        } catch (Exception e) {
            // Si salta error es que ya existe, así que buscamos su ID
            System.out.println("Esa jornada ya estaba ahí, pillando el ID...");
            try {
                String sqlSelect = "SELECT ID_JORNADA FROM JORNADAS WHERE NUMERO_JORNADA = ?";
                try (Connection conn2 = org.example.programacion.Util.ConexionBD.getConnection();
                     PreparedStatement pstmt2 = conn2.prepareStatement(sqlSelect)) {
                    pstmt2.setInt(1, numeroJornada);
                    ResultSet rs = pstmt2.executeQuery();
                    if (rs.next()) {
                        idJornada = rs.getInt("ID_JORNADA");
                    } else {
                        throw new RuntimeException("No encuentro la jornada por ningún lado");
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("Fallo al sacar el ID de la jornada: " + e2.getMessage(), e2);
            }
        }

        // Pillamos los nombres de los equipos para hacer los cruces
        String sqlEquipos = "SELECT NOMBRE_EQUIPO FROM EQUIPOS";
        String sqlInsert = "INSERT INTO ENFRENTAMIENTOS (FECHA_ENFRENTAMIENTO, EQUIPO1, EQUIPO2, HORA, ID_JORNADA) VALUES (?, ?, ?, ?, ?)";

        List<String> listaEquipos = new ArrayList<>();

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection()) {
            // Metemos los nombres de los equipos en una lista
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlEquipos)) {
                while (rs.next()) {
                    listaEquipos.add(rs.getString("NOMBRE_EQUIPO"));
                }
            }

            // Si no hay al menos dos equipos, no hay partido que valga
            if (listaEquipos.size() < 2) {
                return partidosCreados;
            }

            // Bucle para emparejar a todos sin repetir
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                for (int i = 0; i < listaEquipos.size(); i++) {
                    for (int j = i + 1; j < listaEquipos.size(); j++) {
                        String eq1 = listaEquipos.get(i);
                        String eq2 = listaEquipos.get(j);

                        pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Fecha de hoy mismo
                        pstmt.setString(2, eq1);
                        pstmt.setString(3, eq2);
                        pstmt.setString(4, "12:00"); // Hora por defecto
                        pstmt.setInt(5, idJornada);

                        pstmt.executeUpdate();
                        partidosCreados.add(eq1 + " vs " + eq2);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de SQL chungo: " + e.getMessage(), e);
        }
        return partidosCreados;
    }

    /**
     * Sirve para poner el resultado final de un partido.
     * * @param equipo1 Nombre del primer equipo
     * @param equipo2 Nombre del segundo equipo
     * @param res1 Goles o puntos del primero
     * @param res2 Goles o puntos del segundo
     */
    public void actualizarResultado(String equipo1, String equipo2, String res1, String res2) {
        EnfrentamientoDAO dao = new EnfrentamientoDAO();
        try {
            // Buscamos los IDs porque la base de datos funciona con números, no con nombres
            int idPartido = dao.getEnfrentamientoIdByEquipos(equipo1, equipo2);
            int idEq1 = getIdEquipoByName(equipo1);
            int idEq2 = getIdEquipoByName(equipo2);
            dao.actualizarResultado(idPartido, idEq1, res1, idEq2, res2);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido actualizar el marcador: " + e.getMessage(), e);
        }
    }

    /**
     * Un método para buscar el ID de un equipo sabiendo solo su nombre.
     * * @param nombre El nombre del equipo
     * @return El ID numérico del equipo
     * @throws RuntimeException si el equipo no aparece en la base de datos
     */
    private static int getIdEquipoByName(String nombre) {
        String sql = "SELECT id_equipo FROM EQUIPOS WHERE nombre_equipo = ?";
        try (var conn = org.example.programacion.Util.ConexionBD.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el ID del equipo: " + e.getMessage(), e);
        }
        throw new RuntimeException("El equipo " + nombre + " no existe");
    }
}