package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.programacion.Controladores.EnfrentamientoController;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class MenuAdmin {
    public void onCRUD(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void onGenerarCalendario(ActionEvent event) {
        int idJornada = 1;

        // Llamamos al método y obtenemos la lista de partidos
        List<String> partidos = EnfrentamientoController.generarCalendarioAutomatico(idJornada);

        if (partidos.isEmpty()) {
            mostrarAlerta("Atención", "No hay suficientes equipos para generar un calendario.");
            return;
        }

        // Creamos el contenido visual para la alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Calendario Generado");
        alert.setHeaderText("Se han creado " + partidos.size() + " enfrentamientos nuevos:");

        // Usamos un TextArea para que el usuario pueda hacer scroll si hay muchos partidos
        TextArea textArea = new TextArea();
        StringBuilder sb = new StringBuilder();
        for (String p : partidos) {
            sb.append("• ").append(p).append("\n");
        }
        textArea.setText(sb.toString());
        textArea.setEditable(false);
        textArea.setPrefHeight(200); // Altura ajustable

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
    public void onIntroducir(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/IntroducirResultado.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cambiarEstadoCompeticion(int idCompeticion, String nuevoEstado) {
        String sql = "UPDATE Competicion SET estado = ? WHERE id_competicion = ?";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado.toLowerCase());
            pstmt.setInt(2, idCompeticion);

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Competición actualizada a: " + nuevoEstado);
            }
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
        }
    }

    @FXML
    public void onCerrarEstado(ActionEvent event) {
        // Aquí podrías mostrar un diálogo de confirmación
        int idCompeticion = 1;

        cambiarEstadoCompeticion(idCompeticion, "cerrado");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Estado Actualizado");
        alert.setHeaderText(null);
        alert.setContentText("La competición ha sido cerrada. La estructura de equipos está bloqueada.");
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
