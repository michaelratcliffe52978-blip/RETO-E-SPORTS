package org.example.programacion.Controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.programacion.DAO.JornadaDAO;
import org.example.programacion.Modelo.Jornada;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JornadaController implements Initializable {

    @FXML private TableView<Jornada> tablaJornadas;
    @FXML private TableColumn<Jornada, String> colId;
    @FXML private TableColumn<Jornada, Integer> colNumero;
    @FXML private TableColumn<Jornada, Object> colFecha;
    @FXML private TableColumn<Jornada, String> colCompeticion;

    @FXML private TextField txtId, txtNumero;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox<String> comboCompeticion;

    private ObservableList<Jornada> listaJornadas = FXCollections.observableArrayList();
    private JornadaDAO jornadaDAO = new JornadaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idJornada"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroJornada"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaJornada"));
        colCompeticion.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(""));

        cargarDatosJornadas();

        // Listener para seleccionar fila
        tablaJornadas.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
            }
        });

        // Cargar competiciones si es necesario
        comboCompeticion.setItems(FXCollections.observableArrayList("Competición 1", "Competición 2"));
    }

    private void cargarDatosJornadas() {
        listaJornadas.clear();
        listaJornadas.addAll(jornadaDAO.getAllJornadas());
        tablaJornadas.setItems(listaJornadas);
    }

    private void rellenarFormulario(Jornada j) {
        txtId.setText(j.getIdJornada());
        txtNumero.setText(String.valueOf(j.getNumeroJornada()));
        dateFecha.setValue(j.getFechaJornada());
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtNumero.getText().isEmpty() || dateFecha.getValue() == null) {
            mostrarAlerta("Error", "Número y Fecha son obligatorios.");
            return;
        }

        try {
            int numero = Integer.parseInt(txtNumero.getText());
            String id = txtId.getText();

            if (id == null || id.isEmpty()) {
                // Nueva jornada
                jornadaDAO.insertJornada(numero, dateFecha.getValue());
            } else {
                // Actualizar
                jornadaDAO.updateJornada(Integer.parseInt(id), numero, dateFecha.getValue());
            }

            cargarDatosJornadas();
            onLimpiar();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El número debe ser un valor entero.");
        }
    }

    @FXML
    public void onEliminar() {
        Jornada sel = tablaJornadas.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Atención", "Selecciona una jornada para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar jornada " + sel.getNumeroJornada() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                jornadaDAO.deleteJornada(Integer.parseInt(sel.getIdJornada()));
                cargarDatosJornadas();
                onLimpiar();
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onLimpiar() {
        txtId.clear();
        txtNumero.clear();
        dateFecha.setValue(null);
        comboCompeticion.setValue(null);
        tablaJornadas.getSelectionModel().clearSelection();
    }

    @FXML
    public void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
