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
import org.example.programacion.Controladores.JugadoresController;
import org.example.programacion.Modelo.Jugadores;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioJugador {

    @FXML private TextField txtDni, txtNombre, txtApellidos, txtNickname;
    @FXML private ComboBox<String> comboEquipos;

    private JugadoresController jugadoresController = new JugadoresController();

    @FXML
    public void initialize() {
        cargarEquipos();
    }

    private void cargarEquipos() {
        comboEquipos.setItems(FXCollections.observableArrayList(jugadoresController.getAllEquipoNames()));
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtDni.getText().isEmpty() || comboEquipos.getValue() == null) {
            mostrarAlerta("Error", "DNI y Equipo son obligatorios.");
            return;
        }

        Jugadores jugador = new Jugadores(
                txtDni.getText(),
                txtNombre.getText(),
                txtApellidos.getText(),
                "", // nacionalidad, no hay campo
                LocalDate.now(), // fecha nacimiento, no hay campo
                txtNickname.getText(),
                "", // rol, no hay campo
                0.0 // sueldo, no hay campo
        );

        try {
            jugadoresController.saveJugador(jugador, comboEquipos.getValue());

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