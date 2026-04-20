package org.example.programacion.DAO;

import org.example.programacion.Modelo.Enfrentamientos;
import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jornadas;
import org.example.programacion.Utilidades.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de extraer la información de los marcadores de la base de datos.
 * Es un DAO especial porque mezcla datos de enfrentamientos, equipos y jornadas
 * mediante subconsultas para reconstruir el resultado final de cada partido.
 * * @author equipo4
 * @version 1.0
 */
public class ResultadosDAO {

    /**
     * Recupera todos los partidos jugados (o por jugar) con sus respectivos goles/puntos.
     * Utiliza subconsultas SQL para cruzar el nombre de los equipos con sus IDs y
     * así extraer el resultado correcto de la tabla intermedia EQUIPOS_ENFRENTAMIENTOS.
     * * @return Una lista de objetos {@link Enfrentamientos} con toda la info del marcador.
     */
    public List<Enfrentamientos> getTodosLosResultados() {
        List<Enfrentamientos> lista = new ArrayList<>();

        // Esta consulta es "canela en rama": saca el partido y busca los goles de cada equipo
        // haciendo un cruce interno por nombre e ID.
        String sql = "SELECT e.id_partido, e.fecha_enfrentamiento, e.equipo1, e.equipo2, e.hora, e.id_jornada, " +
                "(SELECT resultado FROM EQUIPOS_ENFRENTAMIENTOS WHERE id_partido = e.id_partido AND id_equipo = (SELECT id_equipo FROM EQUIPOS WHERE NOMBRE_EQUIPO = e.equipo1)) as goles_l, " +
                "(SELECT resultado FROM EQUIPOS_ENFRENTAMIENTOS WHERE id_partido = e.id_partido AND id_equipo = (SELECT id_equipo FROM EQUIPOS WHERE NOMBRE_EQUIPO = e.equipo2)) as goles_v " +
                "FROM ENFRENTAMIENTOS e";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Creamos los equipos sobre la marcha con los nombres que vienen de la tabla
                Equipos eq1 = new Equipos(0, rs.getString("equipo1"), null);
                Equipos eq2 = new Equipos(0, rs.getString("equipo2"), null);

                // Montamos el objeto Jornada con su ID correspondiente
                Jornadas jor = new Jornadas(null, rs.getInt("id_jornada"), null, null);

                // Lógica para los goles: si no hay nada o hay un guion, ponemos un 0
                String resL = rs.getString("goles_l");
                String resV = rs.getString("goles_v");

                int gL = (resL != null && !resL.equals("-") && !resL.isEmpty()) ? Integer.parseInt(resL) : 0;
                int gV = (resV != null && !resV.equals("-") && !resV.isEmpty()) ? Integer.parseInt(resV) : 0;

                // Añadimos el enfrentamiento completo a la lista
                lista.add(new Enfrentamientos(
                        String.valueOf(rs.getInt("id_partido")),
                        java.time.LocalTime.parse(rs.getString("hora")),
                        rs.getDate("fecha_enfrentamiento").toLocalDate(),
                        eq1, eq2, jor, gL, gV
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fallo de SQL en ResultadosDAO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al procesar resultados: " + e.getMessage());
        }
        return lista;
    }
}