package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.programacion.Controladores.UsuarioController;

import java.io.IOException;

public class InicioSesionAdmin {

    @FXML private TextField nombreAdmin;
    @FXML private PasswordField contrasenaAdmin;

    // Usamos el controlador de usuario para la validación
    private UsuarioController usuarioController = new UsuarioController();

    @FXML
    public void onAcceder(ActionEvent event) {
        String user = nombreAdmin.getText();
        String pass = contrasenaAdmin.getText();

        if (user.trim().isEmpty() || pass.trim().isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        // Validamos las credenciales del admin
        if (usuarioController.validarAdmin(user, pass)) {
            irAMenuAdmin(event);
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.");
        }
    }

    private void irAMenuAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}