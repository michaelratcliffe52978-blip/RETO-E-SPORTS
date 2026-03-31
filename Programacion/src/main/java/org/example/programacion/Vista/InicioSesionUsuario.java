package org.example.programacion.Vista;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.programacion.Controladores.UsuarioController;

import java.io.IOException;

public class InicioSesionUsuario {
    @FXML
    private TextField txtNombre;

    @FXML
    private PasswordField txtPassword;
    public void onUsuario(ActionEvent event) {

            String nombre = txtNombre.getText();
            String password = txtPassword.getText();

            if (nombre.isEmpty() || password.isEmpty()) {
                System.out.println("Por favor, rellena todos los campos.");
                return;
            }

            UsuarioController.insertarUsuario(nombre, password);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/2.Vista.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                System.out.println("Fallo al cargar la vista: " + e.getMessage());
                e.printStackTrace();
            }
        }
        }

