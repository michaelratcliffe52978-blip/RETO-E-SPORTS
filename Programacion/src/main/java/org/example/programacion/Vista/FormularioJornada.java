package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.example.programacion.DAO.JornadaDAO;

import java.io.IOException;
import java.sql.SQLException;

public class FormularioJornada {

    @FXML private Spinner<Integer> spNumeroJornada;
    @FXML private DatePicker dpFechaJornada;

    private JornadaDAO jornadaDAO = new JornadaDAO();

    @FXML
    public void initialize() {
        // Configuramos el Spinner para que vaya del 1 al 100
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spNumeroJornada.setValueFactory(valueFactory);
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        int numero = spNumeroJornada.getValue();
        var fecha = dpFechaJornada.getValue();

        if (fecha == null) {
            mostrarAlerta("Error", "Debe seleccionar una fecha para la jornada.");
            return;
        }

        try {
            jornadaDAO.insertJornada(numero, fecha);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("Jornada " + numero + " creada con éxito.");
            alert.showAndWait();

            onAtras(event);

        } catch (SQLException e) {
            mostrarAlerta("Error de BD", "No se pudo guardar la jornada: " + e.getMessage());
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