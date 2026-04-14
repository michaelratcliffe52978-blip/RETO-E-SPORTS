package org.example.programacion.Controladores;

import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Jugadores;

import java.sql.SQLException;
import java.util.List;

public class JugadoresController {

    private JugadoresDAO jugadoresDAO = new JugadoresDAO();
    private EquiposDAO equiposDAO = new EquiposDAO();

    public List<Jugadores> getAllJugadores() {
        return jugadoresDAO.getAllJugadores();
    }

    public void saveJugador(Jugadores jugador, String nombreEquipo) throws SQLException {
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

    public void deleteJugador(int idJugador) throws SQLException {
        jugadoresDAO.deleteJugador(idJugador);
    }

    public String getEquipoNameByJugadorId(int idJugador) {
        return jugadoresDAO.getEquipoNameByJugadorId(idJugador);
    }

    public List<String> getAllEquipoNames() {
        return equiposDAO.getAllEquipoNames();
    }

    private int getIdEquipoByName(String nombreEquipo) throws SQLException {
        // Necesito un método en EquiposDAO para obtener id por nombre
        // Por ahora, implementar aquí o agregar a DAO
        String sql = "SELECT id_equipo FROM Equipo WHERE nombre_equipo = ?";
        try (var conn = org.example.programacion.Util.ConexionBD.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreEquipo);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipo");
            }
        }
        throw new SQLException("Equipo no encontrado");
    }
}
