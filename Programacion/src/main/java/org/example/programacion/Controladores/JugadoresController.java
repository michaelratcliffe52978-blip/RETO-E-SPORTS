package org.example.programacion.Controladores;

import org.example.programacion.DAO.CompeticionDAO;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Jugadores;

import java.util.List;

public class JugadoresController {

    private JugadoresDAO jugadoresDAO = new JugadoresDAO();
    private EquiposDAO equiposDAO = new EquiposDAO();
    private CompeticionDAO competicionDAO = new CompeticionDAO();

    public List<Jugadores> getAllJugadores() {
        return jugadoresDAO.getAllJugadores();
    }

    public void saveJugador(Jugadores jugador, String nombreEquipo) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar jugadores porque las inscripciones están cerradas.");
        }
        // Obtener id_equipo por nombre
        // Asumiendo que nombreEquipo es único
        int idEquipo = getIdEquipoByName(nombreEquipo);
        if (jugador.getIdJugador() == null || jugador.getIdJugador().isEmpty()) {
            // Nuevo
            jugadoresDAO.insertJugador(jugador, idEquipo);
        } else {
            // Actualizar
            jugadoresDAO.updateJugador(jugador, idEquipo);
        }
    }

    public void deleteJugador(int idJugador) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar jugadores porque las inscripciones están cerradas.");
        }
        jugadoresDAO.deleteJugador(idJugador);
    }

    public String getEquipoNameByJugadorId(int idJugador) {
        return jugadoresDAO.getEquipoNameByJugadorId(idJugador);
    }

    public List<String> getAllEquipoNames() {
        return equiposDAO.getAllEquipoNames();
    }

    private int getIdEquipoByName(String nombreEquipo) {
        // Necesito un método en EquiposDAO para obtener id por nombre
        // Por ahora, implementar aquí o agregar a DAO
        String sql = "SELECT id_equipo FROM EQUIPOS WHERE nombre_equipo = ?";
        try (var conn = org.example.programacion.Util.ConexionBD.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreEquipo);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipo");
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error al obtener ID de equipo: " + e.getMessage(), e);
        }
        throw new RuntimeException("Equipo no encontrado");
    }
}
