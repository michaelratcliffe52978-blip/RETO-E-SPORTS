package org.example.programacion.Vista;

import javafx.beans.property.SimpleStringProperty;
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
import org.example.programacion.Controladores.JugadoresController;
import org.example.programacion.Modelo.Jugadores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CRUDJugadores implements Initializable {

    // --- TABLA Y COLUMNAS ---
    @FXML private TableView<Jugadores> tablaJugadores;
    @FXML private TableColumn<Jugadores, String> colId, colNickname, colNombre, colNacionalidad, colRol, colEquipo;
    @FXML private TableColumn<Jugadores, Double> colSueldo;
    @FXML private TableColumn<Jugadores, Object> colFecha;

    // --- CAMPOS DE FORMULARIO ---
    @FXML private TextField txtId, txtNombre, txtApellido, txtNickname, txtSueldo, txtNacionalidad, txtBusqueda;
    @FXML private DatePicker dateFechaNac;
    @FXML private ComboBox<String> comboRol, comboEquipo;

    private ObservableList<Jugadores> listaMaster = FXCollections.observableArrayList();

    private JugadoresController jugadoresController = new JugadoresController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEquiposCombo();
        cargarDatosTabla();

        // Rellenar Roles según el CONSTRAINT de tu BD
        comboRol.setItems(FXCollections.observableArrayList("duelista", "controlador", "iniciador", "centinela"));

        // Listener para seleccionar filas
        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) rellenarCampos(nuevo);
        });
    }

    private void configurarTabla() {
        // Enlace directo con atributos del modelo (Jugadores.java)
        colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // Nombre Completo (Calculado: Nombre + Apellido)
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreJugador() + " " + cellData.getValue().getApellido()));

        // Columna Equipo (Calculada mediante consulta a la BD)
        colEquipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerNombreEquipo(cellData.getValue().getIdJugador())));
    }

    @FXML
    private void onGuardarClick() {
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || comboRol.getValue() == null || comboEquipo.getValue() == null) {
            mostrarAlerta("Error", "Faltan campos obligatorios.");
            return;
        }

        String id = txtId.getText();
        Jugadores jugador = new Jugadores(
                id,
                txtNombre.getText(),
                txtApellido.getText(),
                txtNacionalidad.getText(),
                dateFechaNac.getValue(),
                txtNickname.getText(),
                comboRol.getValue(),
                Double.parseDouble(txtSueldo.getText())
        );

        try {
            jugadoresController.saveJugador(jugador, comboEquipo.getValue());
            cargarDatosTabla();
            onLimpiarClick();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarClick() {
        Jugadores seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un jugador para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Borrar a " + seleccionado.getNickname() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                jugadoresController.deleteJugador(Integer.parseInt(seleccionado.getIdJugador()));
                cargarDatosTabla();
                onLimpiarClick();
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
            }
        }
    }

    private void cargarDatosTabla() {
        listaMaster.clear();
        listaMaster.addAll(jugadoresController.getAllJugadores());
        tablaJugadores.setItems(listaMaster);
    }

    private void cargarEquiposCombo() {
        comboEquipo.setItems(FXCollections.observableArrayList(jugadoresController.getAllEquipoNames()));
    }

    private String obtenerNombreEquipo(String idJugador) {
        return jugadoresController.getEquipoNameByJugadorId(Integer.parseInt(idJugador));
    }

    private void rellenarCampos(Jugadores j) {
        txtId.setText(j.getIdJugador());
        txtNombre.setText(j.getNombreJugador());
        txtApellido.setText(j.getApellido());
        txtNickname.setText(j.getNickname());
        txtNacionalidad.setText(j.getNacionalidad());
        txtSueldo.setText(String.valueOf(j.getSueldo()));
        dateFechaNac.setValue(j.getFechaNacimiento());
        comboRol.setValue(j.getRol());
        comboEquipo.setValue(obtenerNombreEquipo(j.getIdJugador()));
    }

    @FXML private void onLimpiarClick() {
        txtId.clear(); txtNombre.clear(); txtApellido.clear(); txtNickname.clear();
        txtNacionalidad.clear(); txtSueldo.clear(); dateFechaNac.setValue(null);
        comboRol.setValue(null); comboEquipo.setValue(null);
    }

    @FXML private void onAtras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}