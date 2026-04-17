package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase controladora para la Vista de Selección de Rol (Vista2).
 * Actúa como punto de bifurcación de la aplicación, permitiendo al usuario
 * elegir el tipo de acceso (Administrador o Usuario) para redirigirlo a
 * su correspondiente pantalla de inicio de sesión.
 * * @author equipo4
 * @version 1.0
 */
public class Vista2 {

    /**
     * Gestiona la transición hacia la pantalla de inicio de sesión para Administradores.
     * @param event Evento de acción disparado por el botón de Administración.
     */
    @FXML
    public void onAdmin (ActionEvent event) {
        try {
            // 1. Cargamos el archivo FXML del login administrativo.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/InicioSesionAdmin.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Preparamos la nueva escena.
            Scene scene = new Scene(root);

            // 3. Obtenemos el Stage actual para realizar el cambio de vista.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Aplicamos el cambio y mostramos.
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login Admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestiona la transición hacia la pantalla de inicio de sesión para Usuarios estándar.
     * @param event Evento de acción disparado por el botón de Usuario.
     */
    @FXML
    public void onUsuario (ActionEvent event) {
        try {
            // 1. Cargamos el archivo FXML del login de usuario.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/InicioSesionUsuario.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Preparamos la nueva escena.
            Scene scene = new Scene(root);

            // 3. Obtenemos el Stage actual.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Aplicamos el cambio y mostramos.
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login Usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
}