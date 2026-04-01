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
import org.example.programacion.Util.ConexionBD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InicioSesionAdmin {

    // Asegúrate de que estos ID coincidan con tu FXML
    @FXML private TextField nombreAdmin;
    @FXML private PasswordField contrasenaAdmin;

    @FXML
    public void onAcceder(ActionEvent event) {
        String user = nombreAdmin.getText();
        String pass = contrasenaAdmin.getText();

        // 1. Validar campos vacíos
        if (user.trim().isEmpty() || pass.trim().isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        // 2. Verificar en la base de datos si existe y es ADMIN
        if (esAdminValido(user, pass)) {
            System.out.println("Acceso concedido al administrador: " + user);
            irAMenuAdmin(event);
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos, o no tienes permisos de administrador.");
        }
    }

    private boolean esAdminValido(String user, String pass) {
        // Consulta exacta para tu tabla USUARIO en singular
        String sql = "SELECT * FROM USUARIO WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND TIPO = 'admin'";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true si encontró coincidencia
            }
        } catch (SQLException e) {
            System.err.println("Error en la validación de admin: " + e.getMessage());
            return false;
        }
    }

    private void irAMenuAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}