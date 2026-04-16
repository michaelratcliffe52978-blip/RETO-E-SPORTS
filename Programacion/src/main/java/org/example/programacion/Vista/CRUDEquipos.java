package org.example.programacion.Vista;

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
import org.example.programacion.Controladores.EquiposController;
import org.example.programacion.Modelo.Equipos;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CRUDEquipos implements Initializable {

    @FXML private TableView<Equipos> tablaEquipos;
    @FXML private TableColumn<Equipos, Integer> colId;
    @FXML private TableColumn<Equipos, String> colNombre;
    @FXML private TableColumn<Equipos, Object> colFecha;

    @FXML private TextField txtId, txtNombre;
    @FXML private DatePicker dateFecha;

    // Etiqueta opcional para mostrar cuántos jugadores tiene el equipo seleccionado
    @FXML private Label lblInfoJugadores;

    private ObservableList<Equipos> listaEquipos = FXCollections.observableArrayList();

    private EquiposController equiposController = new EquiposController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración de columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFundacion"));

        cargarDatosEquipos();

        // LISTENER: Cuando el usuario hace clic en un equipo de la tabla
        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
                // Mostrar info de jugadores
                if(lblInfoJugadores != null) {
                    lblInfoJugadores.setText("Jugadores en plantilla: " + nuevo.getListaJugadores().size());
                }
            }
        });
    }

    private void cargarDatosEquipos() {
        try {
            listaEquipos.clear();
            listaEquipos.addAll(equiposController.getAllEquipos());
            tablaEquipos.setItems(listaEquipos);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar equipos: " + e.getMessage());
        }
    }

    private void rellenarFormulario(Equipos e) {
        if (e.getIdEquipo() == 0) {
            txtId.clear();
        } else {
            txtId.setText(String.valueOf(e.getIdEquipo()));
        }
        txtNombre.setText(e.getNombreEquipo());
        dateFecha.setValue(e.getFechaFundacion());
    }

    @FXML
    private void onGuardarClick() {
        if (txtNombre.getText().isEmpty() || dateFecha.getValue() == null) {
            mostrarAlerta("Campos vacíos", "Nombre y Fecha son obligatorios.");
            return;
        }

        String id = txtId.getText();
        Equipos equipo;
        if (id == null || id.isEmpty()) {
            equipo = new Equipos(0, txtNombre.getText(), dateFecha.getValue());
        } else {
            equipo = new Equipos(Integer.parseInt(id), txtNombre.getText(), dateFecha.getValue());
        }

        try {
            equiposController.saveEquipo(equipo);
            cargarDatosEquipos();
            onLimpiarClick();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarClick() {
        Equipos sel = tablaEquipos.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            equiposController.deleteEquipo(sel.getIdEquipo());
            cargarDatosEquipos();
            onLimpiarClick();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se puede eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void onLimpiarClick() {
        txtId.clear();
        txtNombre.clear();
        dateFecha.setValue(null);
        if(lblInfoJugadores != null) lblInfoJugadores.setText("");
        tablaEquipos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onAtras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}