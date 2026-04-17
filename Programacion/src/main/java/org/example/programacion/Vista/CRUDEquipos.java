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
import java.util.ResourceBundle;

/**
 * Clase controladora para la vista de gestión (CRUD) de Equipos.
 * Se encarga de manejar la tabla de visualización, el formulario de edición
 * y la comunicación con el EquiposController para persistir los datos.
 * * @author equipo4
 * @version 1.0
 */
public class CRUDEquipos implements Initializable {

    // Componentes inyectados desde el FXML
    @FXML private TableView<Equipos> tablaEquipos;
    @FXML private TableColumn<Equipos, Integer> colId;
    @FXML private TableColumn<Equipos, String> colNombre;
    @FXML private TableColumn<Equipos, Object> colFecha;

    @FXML private TextField txtId, txtNombre;
    @FXML private DatePicker dateFecha;

    /** Etiqueta para mostrar información adicional de la plantilla seleccionada */
    @FXML private Label lblInfoJugadores;

    /** Lista observable que sincroniza los datos del controlador con la TableView */
    private ObservableList<Equipos> listaEquipos = FXCollections.observableArrayList();

    /** Referencia al controlador de lógica de negocio */
    private EquiposController equiposController = new EquiposController();

    /**
     * Método que se ejecuta al cargar la ventana. Configura las columnas de la tabla,
     * carga los datos iniciales y establece los escuchadores (listeners).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vinculamos las columnas con los atributos del modelo Equipos
        colId.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFundacion"));

        cargarDatosEquipos();

        // LISTENER: Detecta cuando el usuario selecciona una fila para rellenar el formulario
        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
                if(lblInfoJugadores != null) {
                    lblInfoJugadores.setText("Jugadores en plantilla: " + nuevo.getListaJugadores().size());
                }
            }
        });
    }

    /**
     * Consulta al controlador todos los equipos y actualiza la lista de la tabla.
     */
    private void cargarDatosEquipos() {
        try {
            listaEquipos.clear();
            listaEquipos.addAll(equiposController.getAllEquipos());
            tablaEquipos.setItems(listaEquipos);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar equipos: " + e.getMessage());
        }
    }

    /**
     * Vuelca los datos de un objeto Equipo en los campos de texto del formulario.
     * @param e Equipo seleccionado.
     */
    private void rellenarFormulario(Equipos e) {
        if (e.getIdEquipo() == 0) {
            txtId.clear();
        } else {
            txtId.setText(String.valueOf(e.getIdEquipo()));
        }
        txtNombre.setText(e.getNombreEquipo());
        dateFecha.setValue(e.getFechaFundacion());
    }

    /**
     * Gestiona el evento del botón Guardar. Detecta si es una inserción (ID vacío)
     * o una actualización (ID presente) y delega en el controlador.
     */
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

    /**
     * Elimina el equipo seleccionado de la tabla previa confirmación.
     */
    @FXML
    private void onEliminarClick() {
        Equipos sel = tablaEquipos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Aviso", "Selecciona un equipo de la tabla para eliminarlo.");
            return;
        }

        try {
            equiposController.deleteEquipo(sel.getIdEquipo());
            cargarDatosEquipos();
            onLimpiarClick();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se puede eliminar (compruebe si tiene jugadores asociados): " + e.getMessage());
        }
    }

    /**
     * Limpia todos los campos del formulario y deselecciona la tabla.
     */
    @FXML
    private void onLimpiarClick() {
        txtId.clear();
        txtNombre.clear();
        dateFecha.setValue(null);
        if(lblInfoJugadores != null) lblInfoJugadores.setText("");
        tablaEquipos.getSelectionModel().clearSelection();
    }

    /**
     * Regresa al menú anterior cargando el archivo FXML correspondiente.
     * @param event Evento de acción del botón.
     */
    @FXML
    private void onAtras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilidad para mostrar ventanas emergentes de aviso al usuario.
     * @param titulo Título de la ventana.
     * @param msg Mensaje de cuerpo.
     */
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}