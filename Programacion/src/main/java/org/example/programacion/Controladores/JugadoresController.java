package org.example.programacion.Controladores;

import oracle.jdbc.OracleTypes;
import org.example.programacion.DAO.CompeticionDAO;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Jugadores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static oracle.jdbc.replay.OracleDataSource.URL;
import static oracle.jdbc.replay.OracleDataSource.USER;
import static org.example.programacion.Utilidades.ConexionBD.PASS;

/**
 * Este controlador es el que maneja a todos los jugadores del torneo.
 * Sirve para ver quién juega, fichar gente nueva, actualizar sus datos
 * o echarlos, pero siempre mirando si la competición sigue abierta.
 * * @author equipo4
 * @version 1.0
 */
public class JugadoresController {

    /** Objeto para hablar con la base de datos sobre los jugadores */
    private JugadoresDAO jugadoresDAO = new JugadoresDAO();
    /** Objeto para sacar info de los equipos */
    private EquiposDAO equiposDAO = new EquiposDAO();
    /** Objeto para ver si la competición está cerrada o no */
    private CompeticionDAO competicionDAO = new CompeticionDAO();

    /**
     * Te devuelve una lista con todos los jugadores que hay registrados en el sistema.
     * * @return Una lista de objetos {@link Jugadores}.
     */
    public List<Jugadores> getAllJugadores() {
        return jugadoresDAO.getAllJugadores();
    }

    /**
     * Sirve para guardar un jugador. Si no tiene ID, lo mete como nuevo; si ya tiene,
     * actualiza sus datos. También busca el ID del equipo usando su nombre.
     * * @param jugador El objeto con los datos del crack en cuestión.
     * @param nombreEquipo El nombre del equipo donde va a jugar.
     * @throws RuntimeException Si las inscripciones ya están cerradas o no se encuentra el equipo.
     */
    public void saveJugador(Jugadores jugador, String nombreEquipo) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar jugadores porque las inscripciones están cerradas.");
        }

        // Buscamos el ID numérico del equipo porque la base de datos no entiende de nombres
        int idEquipo = getIdEquipoByName(nombreEquipo);

        if (jugador.getIdJugador() == null || jugador.getIdJugador().isEmpty()) {
            // Si el ID está vacío, es un fichaje nuevo
            jugadoresDAO.insertJugador(jugador, idEquipo);
        } else {
            // Si ya tiene ID, es que estamos cambiando algo de un jugador que ya estaba
            jugadoresDAO.updateJugador(jugador, idEquipo);
        }
    }

    /**
     * Borra a un jugador de la base de datos usando su ID.
     * Solo te deja si la competición no está en modo "cerrado".
     * * @param idJugador El número de identificación del jugador a borrar.
     * @throws RuntimeException Si intentas borrar a alguien con la competición cerrada.
     */
    public void deleteJugador(int idJugador) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar jugadores porque las inscripciones están cerradas.");
        }
        jugadoresDAO.deleteJugador(idJugador);
    }

    /**
     * Te dice el nombre del equipo en el que juega un jugador concreto.
     * * @param idJugador El ID del jugador que queremos cotillear.
     * @return El nombre del equipo en texto.
     */
    public String getEquipoNameByJugadorId(int idJugador) {
        return jugadoresDAO.getEquipoNameByJugadorId(idJugador);
    }

    /**
     * Saca una lista con los nombres de todos los equipos que hay apuntados.
     * * @return Una lista de Strings con los nombres de los equipos.
     */
    public List<String> getAllEquipoNames() {
        return equiposDAO.getAllEquipoNames();
    }

    /**
     * Un método privado para buscar el ID de un equipo sabiendo solo el nombre.
     * Se conecta a la base de datos y hace la consulta.
     * * @param nombreEquipo El nombre del equipo a buscar.
     * @return El ID del equipo.
     * @throws RuntimeException Si el equipo no aparece o hay algún fallo con el SQL.
     */
    private int getIdEquipoByName(String nombreEquipo) {
        String sql = "SELECT id_equipo FROM EQUIPOS WHERE nombre_equipo = ?";
        try (var conn = org.example.programacion.Utilidades.ConexionBD.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreEquipo);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipo");
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Fallo al pillar el ID del equipo: " + e.getMessage(), e);
        }
        throw new RuntimeException("No he encontrado ningún equipo que se llame: " + nombreEquipo);
    }
}