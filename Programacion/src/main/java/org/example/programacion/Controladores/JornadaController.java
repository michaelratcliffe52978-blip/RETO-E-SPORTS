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
import org.example.programacion.DAO.EnfrentamientoDAO;
import org.example.programacion.Modelo.Jornadas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JornadaController implements Initializable {

    @FXML private TableView<Jornadas> tablaJornadas;
    @FXML private TableColumn<Jornadas, String> colId;
    @FXML private TableColumn<Jornadas, Integer> colNumero;
    @FXML private TableColumn<Jornadas, Object> colFecha;

    @FXML private TextField txtId, txtNumero;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox<String> comboCompeticion;
    @FXML private ListView<String> listaEnfrentamientos;

    private ObservableList<Jornadas> listaJornadas = FXCollections.observableArrayList();
    private JornadaDAO jornadaDAO = new JornadaDAO();
    private EnfrentamientoDAO enfrentamientoDAO = new EnfrentamientoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idJornada"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroJornada"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaJornada"));

        cargarDatosJornadas();

        // Listener para seleccionar fila
        tablaJornadas.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
                cargarEnfrentamientosDeJornada(nuevo);
            }
        });

        // Cargar competiciones si existe el comboBox
        if (comboCompeticion != null) {
            comboCompeticion.setItems(FXCollections.observableArrayList("Competición 1", "Competición 2"));
        }
    }

    private void cargarDatosJornadas() {
        try {
            listaJornadas.clear();
            listaJornadas.addAll(jornadaDAO.getAllJornadas());
            tablaJornadas.setItems(listaJornadas);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar jornadas: " + e.getMessage());
        }
    }

    private void rellenarFormulario(Jornadas j) {
        txtId.setText(j.getIdJornada());
        txtNumero.setText(String.valueOf(j.getNumeroJornada()));
        dateFecha.setValue(j.getFechaJornada());
    }

    private void cargarEnfrentamientosDeJornada(Jornadas j) {
        if (listaEnfrentamientos != null) {
            try {
                int idJornada = Integer.parseInt(j.getIdJornada());
                java.util.List<String> enfrentamientos = enfrentamientoDAO.obtenerEnfrentamientosDeJornada(idJornada);
                
                if (enfrentamientos.isEmpty()) {
                    listaEnfrentamientos.setItems(FXCollections.observableArrayList("No hay enfrentamientos en esta jornada"));
                } else {
                    listaEnfrentamientos.setItems(FXCollections.observableArrayList(enfrentamientos));
                }
            } catch (Exception e) {
                listaEnfrentamientos.setItems(FXCollections.observableArrayList("Error al cargar enfrentamientos"));
                mostrarAlerta("Error", "Error al cargar enfrentamientos: " + e.getMessage());
            }
        }
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
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El número debe ser un valor entero.");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    public void onEliminar() {
        Jornadas sel = tablaJornadas.getSelectionModel().getSelectedItem();
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
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onLimpiar() {
        txtId.clear();
        txtNumero.clear();
        dateFecha.setValue(null);
        if (comboCompeticion != null) {
            comboCompeticion.setValue(null);
        }
        if (listaEnfrentamientos != null) {
            listaEnfrentamientos.setItems(FXCollections.observableArrayList());
        }
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
