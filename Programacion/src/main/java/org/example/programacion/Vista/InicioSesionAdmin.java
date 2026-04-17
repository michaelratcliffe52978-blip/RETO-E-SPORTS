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
 * Clase controladora para la vista de Inicio de Sesión de Administradores.
 * Actúa como la barrera de seguridad inicial, capturando las credenciales
 * del usuario y validándolas contra la base de datos para permitir el acceso
 * a las funciones de gestión del sistema.
 * * @author equipo4
 * @version 1.0
 */
public class InicioSesionAdmin {

    /** Campo de texto para introducir el nombre de usuario administrador */
    @FXML private TextField nombreAdmin;

    /** Campo de máscara para la introducción segura de la contraseña */
    @FXML private PasswordField contrasenaAdmin;

    /** Controlador de lógica de negocio que gestiona la autenticación y roles */
    private UsuarioController usuarioController = new UsuarioController();

    /**
     * Procesa el intento de acceso. Verifica que los campos no estén vacíos y
     * solicita la validación del rol de administrador al controlador.
     * @param event Evento de disparo al pulsar el botón "Acceder".
     */
    @FXML
    public void onAcceder(ActionEvent event) {
        String user = nombreAdmin.getText();
        String pass = contrasenaAdmin.getText();

        // Validación preventiva en cliente
        if (user.trim().isEmpty() || pass.trim().isEmpty()) {
            mostrarAlerta("Campos Requeridos", "Por favor, rellena todos los campos para continuar.");
            return;
        }

        // Validación de credenciales y privilegios en el servidor/BD
        if (usuarioController.validarAdmin(user, pass)) {
            irAMenuAdmin(event);
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos, o no posee privilegios de administrador.");
        }
    }

    /**
     * Realiza el cambio de escena hacia el menú principal de administración
     * tras una autenticación exitosa.
     * @param event Evento de acción para recuperar la ventana actual (Stage).
     */
    private void irAMenuAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error crítico al cargar el menú administrativo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Utilidad para mostrar mensajes de error mediante diálogos modales de JavaFX.
     * @param titulo Título de la alerta.
     * @param mensaje Cuerpo del mensaje de error.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}