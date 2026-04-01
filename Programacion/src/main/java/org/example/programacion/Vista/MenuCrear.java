package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.programacion.Controladores.EnfrentamientoController;

import java.io.IOException;

public class MenuCrear {

    private final int idCompeticionActiva = 1;

    @FXML
    public void onCrearJugador(ActionEvent event) {
        if (validarEstado()) {
            navegar(event, "/org/example/programacion/FormularioJugador.fxml");
        }
    }

    @FXML
    public void onCrearEquipo(ActionEvent event) {
        if (validarEstado()) {
            navegar(event, "/org/example/programacion/FormularioEquipo.fxml");
        }
    }

    @FXML
    public void onCrearJornada(ActionEvent event) {
        if (validarEstado()) {
            navegar(event, "/org/example/programacion/FormularioJornada.fxml");
        }
    }

    @FXML
    public void onCrearUsuario(ActionEvent event) {
        // Los usuarios solemos permitir crearlos siempre, pero si quieres bloquearlo, pon el 'if'
        navegar(event, "/org/example/programacion/FormularioUsuario.fxml");
    }

    @FXML
    public void onAtras(ActionEvent event) {
        // Al ser un submenú de "Crear", lo lógico es que 'Atrás' te devuelva al MenuCRUD
        navegar(event, "/org/example/programacion/MenuCRUD.fxml");
    }

    /**
     * Lógica de validación: Si la competición está cerrada, no deja entrar a los formularios.
     */
    private boolean validarEstado() {
        if (!EnfrentamientoController.esEstructuraModificable(idCompeticionActiva)) {
            mostrarAlerta("Competición Bloqueada", "No se pueden crear elementos porque la competición está CERRADA.");
            return false;
        }
        return true;
    }

    private void navegar(ActionEvent event, String ruta) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("No se pudo cargar la vista: " + ruta);
            // Si el archivo no existe todavía, imprimirá el error en la consola de abajo
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}