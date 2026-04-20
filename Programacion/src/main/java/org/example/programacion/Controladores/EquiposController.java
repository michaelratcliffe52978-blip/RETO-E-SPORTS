package org.example.programacion.Controladores;

import oracle.jdbc.OracleTypes;
import org.example.programacion.DAO.CompeticionDAO;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Vista.EstadisticasEquipos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Este controlador es el que maneja todo lo que tiene que ver con los equipos.
 * Se encarga de pedirlos a la base de datos, guardarlos y borrarlos, pero siempre
 * mirando si la competición no está chapada.
 * * @author equipo4
 * @version 1.0
 */
public class EquiposController {

    /** Objeto para hablar con la tabla de equipos en la base de datos */
    private EquiposDAO equiposDAO = new EquiposDAO();
    /** Objeto para sacar los jugadores de cada equipo */
    private JugadoresDAO jugadoresDAO = new JugadoresDAO();
    /** Objeto para ver cómo va la competición */
    private CompeticionDAO competicionDAO = new CompeticionDAO();

    /**
     * Te da una lista con todos los equipos que hay, y de paso le mete a cada
     * uno sus jugadores correspondientes.
     * * @return Una lista de objetos {@link Equipos} con sus jugadores dentro.
     */
    public List<Equipos> getAllEquipos() {
        List<Equipos> equipos = equiposDAO.getAllEquipos();
        // Vamos equipo por equipo cargando a su gente
        for (Equipos equipo : equipos) {
            equipo.getListaJugadores().addAll(jugadoresDAO.getJugadoresByEquipoId(equipo.getIdEquipo()));
        }
        return equipos;
    }

    /**
     * Este metodo es un poco más especial: llama a un procedimiento almacenado
     * que devuelve un cursor con los jugadores de un equipo concreto, junto con su rol y sueldo.
     */
    // En tu clase EquiposController
    public List<String> obtenerInformeJugadores(String nombreEquipo) throws SQLException {
        List<String> informe = new ArrayList<>();
        String sql = "{call informe_jugadores_equipo(?, ?)}";

        try (Connection conn = org.example.programacion.Utilidades.ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombreEquipo);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    informe.add(rs.getString("nombre_completo") + " | " +
                            rs.getString("rol") + " | " +
                            rs.getDouble("sueldo"));
                }
            }
        }
        return informe;
    }


    /**
     * Este método llama a un procedimiento almacenado que devuelve un cursor con las estadísticas de todos los equipos,
     * incluyendo el número de jugadores, sueldo máximo, mínimo y medio.
     */
    public List<EstadisticasEquipos.EstadisticasRow> obtenerEstadisticasParaTabla() throws SQLException {
        List<EstadisticasEquipos.EstadisticasRow> lista = new ArrayList<>();
        String sql = "{call informe_estadisticas_equipos(?)}";

        try (Connection conn = org.example.programacion.Utilidades.ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    lista.add(new EstadisticasEquipos.EstadisticasRow(
                            rs.getString("nombre_equipo"),
                            rs.getString("fecha_fundacion"),
                            rs.getInt("num_jugadores"),
                            rs.getDouble("sueldo_max"),
                            rs.getDouble("sueldo_min"),
                            rs.getDouble("sueldo_medio")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * Sirve para guardar un equipo. Si el ID es 0, lo crea de cero; si ya tiene ID, lo actualiza.
     * Pero ojo, si la competición está en "cerrado", te suelta un error porque ya no se puede tocar.
     * * @param equipo El objeto equipo que quieres salvar.
     * @throws RuntimeException Si intentas guardar algo con las inscripciones cerradas.
     */
    public void saveEquipo(Equipos equipo) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar equipos porque las inscripciones están cerradas.");
        }
        if (equipo.getIdEquipo() == 0) {
            // Es un equipo nuevo que no estaba en la base de datos
            equiposDAO.insertEquipo(equipo);
        } else {
            // El equipo ya existía, así que solo cambiamos sus datos
            equiposDAO.updateEquipo(equipo);
        }
    }

    /**
     * Borra un equipo de la base de datos usando su ID.
     * Primero mira que la competición esté abierta y luego que el equipo no tenga jugadores,
     * porque si tiene gente dentro, no te deja borrarlo para no liar ninguna.
     * * @param idEquipo El número de ID del equipo a borrar.
     * @throws RuntimeException Si la competición está cerrada o si el equipo aún tiene jugadores.
     */
    public void deleteEquipo(int idEquipo) {
        if ("cerrado".equalsIgnoreCase(competicionDAO.getEstado())) {
            throw new RuntimeException("No se pueden modificar equipos porque las inscripciones están cerradas.");
        }

        // Buscamos el equipo para ver si tiene jugadores asociados
        List<Equipos> equipos = getAllEquipos();
        for (Equipos e : equipos) {
            if (e.getIdEquipo() == idEquipo && !e.getListaJugadores().isEmpty()) {
                throw new RuntimeException("No puedes borrar el equipo porque todavía tiene jugadores asociados.");
            }
        }
        equiposDAO.deleteEquipo(idEquipo);
    }
}