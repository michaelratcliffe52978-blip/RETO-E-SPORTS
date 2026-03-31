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
    @FXML private TextField nombreUsuario;
    @FXML private PasswordField contrasena;

    @FXML
    public void onUsuario(ActionEvent event) {
        String txtNombre = nombreUsuario.getText();
        String txtPass = contrasena.getText();

        if (txtNombre != null && !txtNombre.trim().isEmpty() && !txtPass.trim().isEmpty()) {
            UsuarioController.insertarUsuario(txtNombre, txtPass);
            System.out.println("Insertando: " + txtNombre);
        } else {
            System.out.println("Error: Campos vacíos");
        }
    }
}