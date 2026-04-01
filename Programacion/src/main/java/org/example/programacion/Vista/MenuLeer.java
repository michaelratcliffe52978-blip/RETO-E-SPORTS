package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuLeer {

    @FXML
    public void onLeerJugador(ActionEvent event) {
        navegar(event, "/org/example/programacion/ListaJugadores.fxml");
    }

    @FXML
    public void onLeerEquipo(ActionEvent event) {
        navegar(event, "/org/example/programacion/ListaEquipos.fxml");
    }

    @FXML
    public void onLeerJornada(ActionEvent event) {
        navegar(event, "/org/example/programacion/ListaJornadas.fxml");
    }

    @FXML
    public void onLeerUsuario(ActionEvent event) {
        navegar(event, "/org/example/programacion/ListaUsuarios.fxml");
    }

    @FXML
    public void onAtras(ActionEvent event) {
        navegar(event, "/org/example/programacion/MenuCRUD.fxml");
    }

    /**
     * Método auxiliar para cambiar de pantalla.
     */
    private void navegar(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error: No se pudo cargar " + rutaFXML);
            // Si el archivo aún no existe, esto evitará que el programa se cierre.
        }
    }
}