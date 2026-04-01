package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormularioUsuario {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRol;

    @FXML
    public void onGuardar(ActionEvent event) {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();
        String rol = comboRol.getValue();

        // Validación
        if (user.isEmpty() || pass.isEmpty() || rol == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        // SQL: Asegúrate de que tu tabla se llame USUARIO
        String sql = "INSERT INTO USUARIO (USERNAME, PASSWORD, ROL) VALUES (?, ?, ?)";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, rol);

            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario '" + user + "' creado con éxito como " + rol);
            alert.showAndWait();

            onAtras(event);

        } catch (SQLException e) {
            mostrarAlerta("Error de BD", "No se pudo crear el usuario: " + e.getMessage());
        }
    }

    @FXML
    public void onAtras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuCrear.fxml"));
            Parent root = loader.load();
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
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}