package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioEquipo {

    @FXML private TextField txtNombreEquipo;
    @FXML private DatePicker dpFecha; // Coincide con el fx:id del FXML

    @FXML
    public void onGuardar(ActionEvent event) {
        String nombre = txtNombreEquipo.getText();
        LocalDate fechaSeleccionada = dpFecha.getValue();

        // 1. Validación de campos
        if (nombre.isEmpty() || fechaSeleccionada == null) {
            mostrarAlerta("Error", "El nombre y la fecha de fundación son obligatorios.");
            return;
        }

        // 2. Preparar el SQL (Ajusta los nombres de las columnas a tu tabla EQUIPO)
        String sql = "INSERT INTO EQUIPO (NOMBRE_EQUIPO, FECHA_FUNDACION) VALUES (?, ?)";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);

            // Convertimos el LocalDate de JavaFX al Date de SQL
            pstmt.setDate(2, Date.valueOf(fechaSeleccionada));

            pstmt.executeUpdate();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION, "¡Equipo '" + nombre + "' registrado con éxito!");
            alerta.showAndWait();

            // Volver atrás automáticamente
            onAtras(event);

        } catch (SQLException e) {
            mostrarAlerta("Error de Base de Datos", "No se pudo insertar el equipo: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al volver al menú crear: " + e.getMessage());
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