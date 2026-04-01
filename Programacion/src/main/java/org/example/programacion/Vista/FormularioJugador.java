package org.example.programacion.Vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class FormularioJugador {

    @FXML private TextField txtDni, txtNombre, txtApellidos, txtNickname;
    @FXML private ComboBox<String> comboEquipos;

    @FXML
    public void initialize() {
        cargarEquipos();
    }

    private void cargarEquipos() {
        ObservableList<String> nombresEquipos = FXCollections.observableArrayList();
        String sql = "SELECT NOMBRE_EQUIPO FROM EQUIPO";
        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                nombresEquipos.add(rs.getString("NOMBRE_EQUIPO"));
            }
            comboEquipos.setItems(nombresEquipos);
        } catch (SQLException e) {
            System.err.println("Error al cargar equipos: " + e.getMessage());
        }
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtDni.getText().isEmpty() || comboEquipos.getValue() == null) {
            mostrarAlerta("Error", "DNI y Equipo son obligatorios.");
            return;
        }

        String sqlInsert = "INSERT INTO JUGADOR (DNI, NOMBRE, APELLIDOS, NICKNAME, ID_EQUIPO) " +
                "VALUES (?, ?, ?, ?, (SELECT ID_EQUIPO FROM EQUIPO WHERE NOMBRE_EQUIPO = ?))";

        try (Connection conn = org.example.programacion.Util.ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setString(1, txtDni.getText());
            pstmt.setString(2, txtNombre.getText());
            pstmt.setString(3, txtApellidos.getText());
            pstmt.setString(4, txtNickname.getText());
            pstmt.setString(5, comboEquipos.getValue());

            pstmt.executeUpdate();

            Alert exito = new Alert(Alert.AlertType.INFORMATION, "¡Jugador guardado con éxito!");
            exito.showAndWait();

            // Volver automáticamente al menú de creación tras guardar
            onAtras(event);

        } catch (SQLException e) {
            mostrarAlerta("Error de BD", "No se pudo guardar: " + e.getMessage());
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
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}