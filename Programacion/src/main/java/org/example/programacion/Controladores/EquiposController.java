package org.example.programacion.Controladores;

import org.example.programacion.DAO.CompeticionDAO;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Equipos;

import java.util.List;

public class EquiposController {

    private EquiposDAO equiposDAO = new EquiposDAO();
    private JugadoresDAO jugadoresDAO = new JugadoresDAO();
    private CompeticionDAO competicionDAO = new CompeticionDAO();

    public List<Equipos> getAllEquipos() {
        List<Equipos> equipos = equiposDAO.getAllEquipos();
        // Cargar jugadores para cada equipo
        for (Equipos equipo : equipos) {
            equipo.getListaJugadores().addAll(jugadoresDAO.getJugadoresByEquipoId(equipo.getIdEquipo()));
        }
        return equipos;
    }

    public void saveEquipo(Equipos equipo) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar equipos porque las inscripciones están cerradas.");
        }
        if (equipo.getIdEquipo() == 0) {
            // Nuevo equipo
            equiposDAO.insertEquipo(equipo);
        } else {
            // Actualizar
            equiposDAO.updateEquipo(equipo);
        }
    }

    public void deleteEquipo(int idEquipo) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar equipos porque las inscripciones están cerradas.");
        }
        // Verificar si tiene jugadores
        List<Equipos> equipos = getAllEquipos();
        for (Equipos e : equipos) {
            if (e.getIdEquipo() == idEquipo && !e.getListaJugadores().isEmpty()) {
                throw new RuntimeException("Equipo tiene jugadores asociados");
            }
        }
        equiposDAO.deleteEquipo(idEquipo);
    }
}
