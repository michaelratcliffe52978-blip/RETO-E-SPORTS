package org.example.programacion.Vista;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la Vista de Inicio (Vista1).
 * Actúa como la pantalla de bienvenida de la aplicación, gestionando el
 * primer salto de navegación hacia el selector de perfiles.
 * * @author equipo4
 * @version 1.0
 */
public class Vista1 {

    /**
     * Gestiona el evento del botón de inicio. Carga la siguiente escena
     * y reemplaza el contenido de la ventana actual.
     * @param event Evento de acción originado por el botón "Iniciar" o similar.
     */
    @FXML
    public void onIniciar(ActionEvent event) {
        try {
            // 1. Cargamos el archivo FXML de la Vista de selección de rol.
            // La ruta absoluta permite localizar el recurso dentro del JAR final.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/2.Vista.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Definimos la nueva escena con el layout cargado.
            Scene scene = new Scene(root);

            // 3. Obtenemos el contenedor principal (Stage) a través del nodo que disparó el evento.
            // Esto permite una navegación fluida sin duplicar ventanas.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Actualizamos la ventana con la nueva escena.
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Gestión de errores en caso de que el recurso FXML sea inaccesible.
            System.err.println("Fallo crítico al cargar la transición inicial: " + e.getMessage());
            e.printStackTrace();
        }
    }

}