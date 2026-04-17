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
import java.util.ResourceBundle;

/**
 * Clase controladora para la vista de gestión integral de Jugadores.
 * Permite realizar operaciones CRUD, filtrado y visualización de plantillas,
 * integrando datos de jugadores con sus respectivos equipos mediante el controlador.
 * * @author equipo4
 * @version 1.0
 */
public class CRUDJugadores implements Initializable {

    // --- ELEMENTOS DE LA INTERFAZ ---
    @FXML private TableView<Jugadores> tablaJugadores;
    @FXML private TableColumn<Jugadores, String> colId, colNickname, colNombre, colNacionalidad, colRol, colEquipo;
    @FXML private TableColumn<Jugadores, Double> colSueldo;
    @FXML private TableColumn<Jugadores, Object> colFecha;

    @FXML private TextField txtId, txtNombre, txtApellido, txtNickname, txtSueldo, txtNacionalidad, txtBusqueda;
    @FXML private DatePicker dateFechaNac;
    /** ComboBox para seleccionar el rol deportivo y el equipo al que pertenece */
    @FXML private ComboBox<String> comboRol, comboEquipo;

    /** Lista maestra que contiene todos los jugadores cargados desde la BD */
    private ObservableList<Jugadores> listaMaster = FXCollections.observableArrayList();

    /** Referencia al controlador de lógica de negocio para jugadores */
    private JugadoresController jugadoresController = new JugadoresController();

    /**
     * Inicializa la vista configurando la tabla, cargando los combos de selección
     * y sincronizando la lista de jugadores.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEquiposCombo();
        cargarDatosTabla();

        // Rellenar Roles predefinidos según los estándares del torneo
        comboRol.setItems(FXCollections.observableArrayList("duelista", "controlador", "iniciador", "centinela"));

        // Listener para sincronizar la selección de la tabla con el formulario
        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) rellenarCampos(nuevo);
        });
    }

    /**
     * Define la vinculación entre las columnas de la tabla y los datos del modelo.
     * Incluye lógica para columnas calculadas (Nombre completo y Nombre de equipo).
     */
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // Propiedad calculada: Une nombre y apellido para la visualización
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreJugador() + " " + cellData.getValue().getApellido()));

        // Propiedad calculada: Obtiene el nombre del equipo mediante una consulta externa
        colEquipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerNombreEquipo(cellData.getValue().getIdJugador())));
    }

    /**
     * Procesa la acción de guardar o actualizar un jugador tras validar los campos.
     */
    @FXML
    private void onGuardarClick() {
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || comboRol.getValue() == null || comboEquipo.getValue() == null) {
            mostrarAlerta("Error", "Faltan campos obligatorios para registrar al jugador.");
            return;
        }

        Jugadores jugador = new Jugadores(
                txtId.getText(),
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
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar la ficha del jugador: " + e.getMessage());
        }
    }

    /**
     * Elimina al jugador seleccionado tras una confirmación de seguridad del usuario.
     */
    @FXML
    private void onEliminarClick() {
        Jugadores seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Debe seleccionar un jugador de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea borrar a " + seleccionado.getNickname() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                jugadoresController.deleteJugador(Integer.parseInt(seleccionado.getIdJugador()));
                cargarDatosTabla();
                onLimpiarClick();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar: " + e.getMessage());
            }
        }
    }

    /**
     * Carga todos los jugadores registrados en la lista observable de la tabla.
     */
    private void cargarDatosTabla() {
        try {
            listaMaster.clear();
            listaMaster.addAll(jugadoresController.getAllJugadores());
            tablaJugadores.setItems(listaMaster);
        } catch (Exception e) {
            mostrarAlerta("Error", "Fallo al refrescar la tabla: " + e.getMessage());
        }
    }

    /**
     * Puebla el ComboBox de equipos con los nombres obtenidos de la base de datos.
     */
    private void cargarEquiposCombo() {
        comboEquipo.setItems(FXCollections.observableArrayList(jugadoresController.getAllEquipoNames()));
    }

    /**
     * Método auxiliar para recuperar el nombre del equipo de un jugador específico.
     * @param idJugador ID del jugador.
     * @return Nombre del equipo asignado.
     */
    private String obtenerNombreEquipo(String idJugador) {
        return jugadoresController.getEquipoNameByJugadorId(Integer.parseInt(idJugador));
    }

    /**
     * Traslada los datos del jugador seleccionado en la tabla a los campos de edición.
     * @param j Objeto jugador seleccionado.
     */
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

    /**
     * Restablece el formulario a su estado original (vacío).
     */
    @FXML private void onLimpiarClick() {
        txtId.clear(); txtNombre.clear(); txtApellido.clear(); txtNickname.clear();
        txtNacionalidad.clear(); txtSueldo.clear(); dateFechaNac.setValue(null);
        comboRol.setValue(null); comboEquipo.setValue(null);
    }

    /**
     * Regresa al menú de navegación principal de la administración.
     * @param event Evento de disparo del botón.
     */
    @FXML private void onAtras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Muestra mensajes de advertencia personalizados al usuario.
     */
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}