package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.programacion.DAO.EnfrentamientoDAO;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.CompeticionDAO;
import org.example.programacion.Modelo.Equipos;

import java.io.IOException;
import java.util.List;

/**
 * Clase controladora del Menú Principal de Administración.
 * Centraliza las operaciones críticas del sistema: gestión de datos (CRUD),
 * registro de resultados y la lógica de generación automática del calendario
 * de la competición mediante el algoritmo Round Robin.
 * * @author equipo4
 * @version 1.0
 */
public class MenuAdmin {

    /** Acceso a la persistencia de competiciones */
    private CompeticionDAO competicionDAO = new CompeticionDAO();
    /** Acceso a la persistencia de equipos y validaciones de plantilla */
    private EquiposDAO equiposDAO = new EquiposDAO();
    /** Acceso a la persistencia y generación de jornadas/enfrentamientos */
    private EnfrentamientoDAO enfrentamientoDAO = new EnfrentamientoDAO();

    /**
     * Navega hacia el submenú de gestión de datos (Usuarios, Equipos, Jugadores).
     * @param event Evento de acción del botón.
     */
    @FXML
    public void onCRUD(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/MenuCRUD.fxml");
    }

    /**
     * Navega hacia la pantalla de introducción de marcadores de partidos.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void onIntroducir(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/IntroducirResultado.fxml");
    }

    /**
     * Finaliza la sesión actual y regresa a la pantalla de selección de rol.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void CerrarSesion(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/2.Vista.fxml");
    }

    /**
     * Orquestador de la generación del calendario de liga.
     * Aplica las reglas de negocio:
     * 1. Verifica que todos los equipos tengan al menos 2 jugadores.
     * 2. Gestiona la sobreescritura si ya existe un calendario previo.
     * 3. Valida que el número de equipos sea par para el emparejamiento.
     * 4. Ejecuta el algoritmo de generación y cierra las inscripciones de la competición.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void onGenerarCalendario(ActionEvent event) {
        // REGLA 1: Mínimo de jugadores por equipo (Validación de integridad)
        if (!equiposDAO.validarMinimoJugadores(2)) {
            mostrarAlerta("Error de Integridad", "No se puede generar el calendario: Existen equipos con plantillas incompletas (mín. 2 jugadores).");
            return;
        }

        // REGLA 2: Control de duplicidad de calendario
        if (enfrentamientoDAO.existeCalendario()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Calendario Detectado");
            confirm.setHeaderText("Ya existe un calendario en la base de datos.");
            confirm.setContentText("¿Deseas eliminarlo y generar uno nuevo? Esta acción no se puede deshacer.");

            if (confirm.showAndWait().get() != ButtonType.OK) {
                return;
            }
        }

        List<Equipos> listaEquipos = equiposDAO.getAllEquipos();

        // REGLA 3: Validación matemática para emparejamientos
        if (listaEquipos.size() % 2 != 0) {
            mostrarAlerta("Configuración Inválida", "El número de equipos debe ser par para generar enfrentamientos directos.");
            return;
        }

        // Ejecución del algoritmo y persistencia
        boolean exito = enfrentamientoDAO.generarYGuardarCalendario(listaEquipos);

        if (exito) {
            // REGLA 4: Bloqueo de inscripciones tras iniciar la liga
            competicionDAO.cerrarInscripciones();
            mostrarAlerta("Proceso Completado", "Calendario generado con éxito. Se han cerrado las inscripciones de nuevos equipos.");
        } else {
            mostrarAlerta("Error Crítico", "No se pudo guardar el calendario. Verifique la conexión con Oracle.");
        }
    }

    /**
     * Cambia manualmente el estado de la competición a 'cerrado'.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void onInformeGeneral(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/informeEstadisticasEquipos.fxml");
    }

    /**
     * Método genérico para la transición entre escenas FXML.
     * @param event Evento que origina el cambio.
     * @param fxmlPath Ruta relativa al archivo FXML de destino.
     */
    private void cambiarEscena(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Fallo al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Muestra diálogos informativos al administrador.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    //ESTA PARTE DEL CÓDIGO ES PARA GENERAR EL CALENDARIOTEST
    // Regla 1: Paridad de equipos
    public boolean esNumeroDeEquiposValido(List<Equipos> equipos) {
        if (equipos == null || equipos.isEmpty()) {
            return false;
        }
        return equipos.size() % 2 == 0;
    }

    // Regla 2: Rango de jugadores (mínimo 2, máximo 6)
    public boolean tieneJugadoresValidos(Equipos equipo) {
        // Regla 2.6: Seguridad contra nulos
        if (equipo == null) {
            return false;
        }
        int cantidad = equipo.getCantidadJugadores();
        return cantidad >= 2 && cantidad <= 6;
    }

}