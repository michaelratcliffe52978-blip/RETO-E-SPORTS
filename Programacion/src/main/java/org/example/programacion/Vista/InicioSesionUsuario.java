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

/**
 * Clase controladora para la vista de Inicio de Sesión de Usuarios estándar.
 * Gestiona la entrada al sistema para el perfil 'user', permitiendo el acceso
 * a las funciones de consulta de informes tras validar las credenciales.
 * * @author equipo4
 * @version 1.0
 */
public class InicioSesionUsuario {

    /** Campo para el nombre de usuario estándar */
    @FXML private TextField nombreUsuario;

    /** Campo para la contraseña con máscara de seguridad */
    @FXML private PasswordField contrasena;

    /** Controlador de lógica de negocio para la gestión de usuarios */
    private UsuarioController usuarioController = new UsuarioController();

    /**
     * Gestiona el intento de inicio de sesión.
     * Verifica que los campos no estén vacíos y utiliza el controlador
     * para confirmar que el usuario existe y posee el rol adecuado.
     * @param event Evento de acción al pulsar el botón de acceso.
     */
    @FXML
    public void onUsuario(ActionEvent event) {
        String txtNombre = nombreUsuario.getText();
        String txtPass = contrasena.getText();

        // Validación de campos obligatorios
        if (txtNombre.trim().isEmpty() || txtPass.trim().isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor, rellena todos los campos para acceder.");
            return;
        }

        // Validación de credenciales delegada al controlador de negocio
        if (usuarioController.validarUsuario(txtNombre, txtPass)) {
            irAMenuUsuario(event);
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos, o no posee el rol de usuario estándar.");
        }
    }

    /**
     * Realiza el cambio de escena hacia el menú específico de usuarios (consultas).
     * @param event Evento necesario para obtener la ventana (Stage) actual.
     */
    private void irAMenuUsuario(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la interfaz de usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera un diálogo de error visual para informar al usuario sobre fallos en el acceso.
     * @param titulo Título de la ventana de alerta.
     * @param mensaje Descripción del error.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}