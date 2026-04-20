package org.example.programacion.DAO;

import org.example.programacion.Modelo.Jornadas;
import org.example.programacion.Utilidades.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la que se encarga de gestionar las jornadas en la base de datos.
 * Sirve para crear jornadas nuevas, ver todas las que hay, actualizarlas o borrarlas.
 * Básicamente, es el puente entre el código y la tabla JORNADAS.
 * * @author equipo4
 * @version 1.0
 */
public class JornadaDAO {

    /**
     * Mete una jornada nueva en la base de datos y te devuelve su ID.
     * Como el ID se genera solo, después de insertar hace una consulta para pillarlo.
     * * @param numero El número de la jornada (ej: Jornada 1, 2...).
     * @param fecha El día que se juega esa jornada.
     * @return El ID numérico que la base de datos le ha dado a esa jornada.
     * @throws RuntimeException Si algo falla al insertar o al buscar el ID.
     */
    public int insertJornada(int numero, LocalDate fecha) {
        String sql = "INSERT INTO JORNADAS (NUMERO_JORNADA, FECHA_JORNADA) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numero);
            pstmt.setDate(2, Date.valueOf(fecha));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar la jornada: " + e.getMessage(), e);
        }

        // Pillamos el ID recién creado usando el número de jornada
        String sqlSelect = "SELECT ID_JORNADA FROM JORNADAS WHERE NUMERO_JORNADA = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, numero);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_JORNADA");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al pillar el ID de la jornada: " + e.getMessage(), e);
        }
        throw new RuntimeException("No he podido encontrar el ID de la jornada que acabo de crear");
    }

    /**
     * Se trae todas las jornadas que hay guardadas para enseñarlas en el programa.
     * * @return Una lista de objetos {@link Jornadas}.
     * @throws RuntimeException Si la base de datos da problemas al leer.
     */
    public List<Jornadas> getAllJornadas() {
        List<Jornadas> jornadas = new ArrayList<>();
        String sql = "SELECT ID_JORNADA, NUMERO_JORNADA, FECHA_JORNADA FROM JORNADAS";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Jornadas j = new Jornadas(
                        String.valueOf(rs.getInt("ID_JORNADA")),
                        rs.getInt("NUMERO_JORNADA"),
                        rs.getDate("FECHA_JORNADA").toLocalDate(),
                        null
                );
                jornadas.add(j);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de jornadas: " + e.getMessage(), e);
        }
        return jornadas;
    }

    /**
     * Actualiza los datos de una jornada que ya existe.
     * * @param idJornada El ID de la jornada que queremos cambiar.
     * @param numero El nuevo número de jornada.
     * @param fecha La nueva fecha.
     * @throws RuntimeException Si falla el UPDATE en el SQL.
     */
    public void updateJornada(int idJornada, int numero, LocalDate fecha) {
        String sql = "UPDATE JORNADAS SET NUMERO_JORNADA = ?, FECHA_JORNADA = ? WHERE ID_JORNADA = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numero);
            pstmt.setDate(2, Date.valueOf(fecha));
            pstmt.setInt(3, idJornada);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la jornada: " + e.getMessage(), e);
        }
    }

    /**
     * Borra una jornada de la base de datos para siempre.
     * ¡Ojo! Que si tiene partidos asociados igual da error por las claves foráneas.
     * * @param idJornada El ID de la jornada a eliminar.
     * @throws RuntimeException Si hay un error al intentar borrar la fila.
     */
    public void deleteJornada(int idJornada) {
        String sql = "DELETE FROM JORNADAS WHERE ID_JORNADA = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJornada);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la jornada: " + e.getMessage(), e);
        }
    }
}