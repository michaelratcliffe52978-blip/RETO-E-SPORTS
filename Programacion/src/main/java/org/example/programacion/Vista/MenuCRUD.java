package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase controladora para el Menú de Gestión (CRUD).
 * Actúa como un hub de navegación que permite al administrador desplazarse
 * entre las diferentes pantallas de mantenimiento de la aplicación:
 * Jugadores, Equipos, Jornadas y Usuarios.
 * * @author equipo4
 * @version 1.0
 */
public class MenuCRUD {

    /**
     * Regresa a la pantalla principal del Menú de Administración.
     * @param event Evento de acción capturado desde el botón.
     */
    @FXML
    public void onAtras(ActionEvent event) {
        cargarEscena("/org/example/programacion/MenuAdmin.fxml", event);
    }

    /**
     * Navega a la interfaz de gestión de Jugadores.
     * @param event Evento de acción capturado desde el botón.
     */
    @FXML
    public void onJugadores(ActionEvent event) {
        cargarEscena("/org/example/programacion/CRUDJugadores.fxml", event);
    }

    /**
     * Navega a la interfaz de gestión de Equipos.
     * @param event Evento de acción capturado desde el botón.
     */
    @FXML
    public void onEquipos(ActionEvent event) {
        cargarEscena("/org/example/programacion/CRUDEquipos.fxml", event);
    }

    /**
     * Navega a la interfaz de gestión de Jornadas.
     * @param event Evento de acción capturado desde el botón.
     */
    @FXML
    public void onJornadas(ActionEvent event) {
        cargarEscena("/org/example/programacion/CRUDJornadas.fxml", event);
    }

    /**
     * Navega a la interfaz de gestión de Usuarios del sistema.
     * @param event Evento de acción capturado desde el botón.
     */
    @FXML
    public void onUsuarios(ActionEvent event) {
        cargarEscena("/org/example/programacion/CRUDUsuarios.fxml", event);
    }

    /**
     * Método auxiliar para centralizar la lógica de carga de archivos FXML
     * y el intercambio de escenas en el Stage actual.
     * @param fxmlPath Ruta del recurso FXML a cargar.
     * @param event Evento de origen para obtener la ventana actual.
     */
    private void cargarEscena(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error de Navegación", "No se pudo cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Muestra una ventana modal de error en caso de fallos en el sistema de navegación.
     * @param titulo Título de la alerta.
     * @param mensaje Descripción del error.
     */
    @FXML
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}