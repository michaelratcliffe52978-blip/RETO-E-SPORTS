package org.example.programacion.Controladores;

import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Equipos;

import java.sql.SQLException;
import java.util.List;

public class EquiposController {

    private EquiposDAO equiposDAO = new EquiposDAO();
    private JugadoresDAO jugadoresDAO = new JugadoresDAO();

    public List<Equipos> getAllEquipos() {
        List<Equipos> equipos = equiposDAO.getAllEquipos();
        // Cargar jugadores para cada equipo
        for (Equipos equipo : equipos) {
            equipo.getListaJugadores().addAll(jugadoresDAO.getJugadoresByEquipoId(equipo.getIdEquipo()));
        }
        return equipos;
    }

    public void saveEquipo(Equipos equipo) throws SQLException {
        if (equipo.getIdEquipo() == 0) {
            // Nuevo equipo
            equiposDAO.insertEquipo(equipo);
        } else {
            // Actualizar
            equiposDAO.updateEquipo(equipo);
        }
    }

    public void deleteEquipo(int idEquipo) throws SQLException {
        // Verificar si tiene jugadores
        List<Equipos> equipos = getAllEquipos();
        for (Equipos e : equipos) {
            if (e.getIdEquipo() == idEquipo && !e.getListaJugadores().isEmpty()) {
                throw new SQLException("Equipo tiene jugadores asociados");
            }
        }
        equiposDAO.deleteEquipo(idEquipo);
    }
}
