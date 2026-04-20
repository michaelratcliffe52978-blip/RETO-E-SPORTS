package org.example.programacion.DAO;

import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jugadores;
import org.example.programacion.Utilidades.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la que se encarga de gestionar los equipos en la base de datos.
 * Sirve para leer equipos, meter nuevos, actualizarlos y hasta mirar si tienen
 * suficientes jugadores para poder jugar la liga.
 * * @author equipo4
 * @version 1.0
 */
public class EquiposDAO {

    /**
     * Se trae todos los equipos que hay en la base de datos con toda su info.
     * * @return Una lista de objetos {@link Equipos}.
     * @throws RuntimeException Si la base de datos da un error al leer.
     */
    public List<Equipos> getAllEquipos() {
        List<Equipos> equipos = new ArrayList<>();
        String sql = "SELECT * FROM EQUIPOS";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                equipos.add(new Equipos(
                        rs.getInt("id_equipo"),
                        rs.getString("nombre_equipo"),
                        rs.getDate("fecha_fundacion").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al pillar los equipos: " + e.getMessage(), e);
        }
        return equipos;
    }

    /**
     * Método auxiliar para sacar solo los nombres de los equipos.
     * * @return Lista de Strings con los nombres.
     */
    public List<String> obtenerNombresEquipos() {
        List<String> equipos = new ArrayList<>();
        String sql = "SELECT NOMBRE FROM EQUIPOS";
        // Aquí faltaría la lógica de conexión, pero el método está planteado.
        return equipos;
    }

    /**
     * Mete un equipo nuevo en la base de datos.
     * * @param equipo El objeto con la info del equipo (nombre y fecha).
     * @throws RuntimeException Si falla el insert.
     */
    public void insertEquipo(Equipos equipo) {
        String sql = "INSERT INTO EQUIPOS (nombre_equipo, fecha_fundacion) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipo.getNombreEquipo());
            pstmt.setDate(2, Date.valueOf(equipo.getFechaFundacion()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el equipo: " + e.getMessage(), e);
        }
    }

    /**
     * Cambia los datos de un equipo que ya existe usando su ID.
     * * @param equipo El equipo con los datos ya cambiados.
     * @throws RuntimeException Si no se puede actualizar.
     */
    public void updateEquipo(Equipos equipo) {
        String sql = "UPDATE EQUIPOS SET nombre_equipo = ?, fecha_fundacion = ? WHERE id_equipo = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipo.getNombreEquipo());
            pstmt.setDate(2, Date.valueOf(equipo.getFechaFundacion()));
            pstmt.setInt(3, equipo.getIdEquipo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el equipo: " + e.getMessage(), e);
        }
    }

    /**
     * Busca y devuelve todos los jugadores que pertenecen a un equipo concreto por su nombre.
     * Hace un JOIN entre las tablas para no fallar.
     * * @param nombreEquipo El nombre del equipo que queremos consultar.
     * @return Lista de objetos {@link Jugadores}.
     */
    public List<Jugadores> getJugadoresPorEquipo(String nombreEquipo) {
        List<Jugadores> lista = new ArrayList<>();
        String sql = "SELECT j.* FROM JUGADORES j " +
                "JOIN EQUIPOS e ON j.id_equipo = e.id_equipo " +
                "WHERE e.nombre_equipo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreEquipo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Jugadores(
                        rs.getString("id_jugador"),
                        rs.getString("nombre_jugador"),
                        rs.getString("apellido"),
                        rs.getString("nacionalidad"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nickname"),
                        rs.getString("rol"),
                        rs.getDouble("sueldo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Borra un equipo de la base de datos usando su ID.
     * * @param idEquipo El ID del equipo a eliminar.
     * @throws RuntimeException Si el SQL da guerra al borrar.
     */
    public void deleteEquipo(int idEquipo) {
        String sql = "DELETE FROM EQUIPOS WHERE id_equipo = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el equipo: " + e.getMessage(), e);
        }
    }

    /**
     * Saca una lista rápida con todos los nombres de los equipos.
     * * @return Lista de Strings.
     */
    public List<String> getAllEquipoNames() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre_equipo FROM EQUIPOS";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                nombres.add(rs.getString("nombre_equipo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al pillar los nombres de los equipos: " + e.getMessage(), e);
        }
        return nombres;
    }

    /**
     * Este método es muy útil: mira si todos los equipos tienen el mínimo de jugadores
     * que pide la liga. Usa un HAVING para filtrar los que están "cojos".
     * * @param minimo El número de jugadores que deben tener como poco.
     * @return true si todos están en regla, false si algún equipo tiene menos de los que debe.
     */
    public boolean validarMinimoJugadores(int minimo) {
        String sql = "SELECT e.nombre_equipo FROM EQUIPOS e " +
                "LEFT JOIN JUGADORES j ON e.id_equipo = j.id_equipo " +
                "GROUP BY e.id_equipo, e.nombre_equipo " +
                "HAVING COUNT(j.id_jugador) < ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, minimo);
            ResultSet rs = pstmt.executeQuery();
            // Si el ResultSet está vacío, es que ningún equipo tiene menos del mínimo. ¡Todo OK!
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}