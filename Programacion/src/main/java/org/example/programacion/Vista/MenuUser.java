package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.programacion.Controladores.EnfrentamientoController;
import org.example.programacion.DAO.CompeticionDAO;

import java.io.IOException;
import java.util.List;

public class MenuUser {

    private CompeticionDAO competicionDAO = new CompeticionDAO();

    @FXML
    public void VerInformeEquipos(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/InformeEquipos.fxml");
    }

    @FXML
    public void VerInformeResultados(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/InformeResultados.fxml");
    }

    @FXML
    public void CerrarSesion(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/2.Vista.fxml");
    }


    // Método auxiliar para no repetir código de cambio de ventana
    private void cambiarEscena(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}