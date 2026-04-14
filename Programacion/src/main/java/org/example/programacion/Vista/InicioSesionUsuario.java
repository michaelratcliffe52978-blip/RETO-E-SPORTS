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

public class InicioSesionUsuario {
    @FXML private TextField nombreUsuario;
    @FXML private PasswordField contrasena;

    private UsuarioController usuarioController = new UsuarioController();

    @FXML
    public void onUsuario(ActionEvent event) {
        String txtNombre = nombreUsuario.getText();
        String txtPass = contrasena.getText();

        if (txtNombre.trim().isEmpty() || txtPass.trim().isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        if (usuarioController.validarUsuario(txtNombre, txtPass)) {
            irAMenuUsuario(event);
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.");
        }
    }

    private void irAMenuUsuario(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
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