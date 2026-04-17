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

/**
 * Este es el controlador para gestionar las jornadas de la competición.
 * Sirve para crear jornadas, ponerles fecha y ver qué partidos hay en cada una.
 * * @author equipo4
 * @version 1.0
 */
public class JornadaController implements Initializable {

    // --- Cosas de la tabla ---
    @FXML private TableView<Jornadas> tablaJornadas;
    @FXML private TableColumn<Jornadas, String> colId;
    @FXML private TableColumn<Jornadas, Integer> colNumero;
    @FXML private TableColumn<Jornadas, Object> colFecha;

    // --- Cosas del formulario ---
    @FXML private TextField txtId, txtNumero;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox<String> comboCompeticion;
    @FXML private ListView<String> listaEnfrentamientos;

    /** La lista donde se guardan las jornadas para que salgan en la tabla */
    private ObservableList<Jornadas> listaJornadas = FXCollections.observableArrayList();

    /** Conexión con la base de datos para las jornadas */
    private JornadaDAO jornadaDAO = new JornadaDAO();

    /** Conexión con la base de datos para ver los partidos de cada jornada */
    private EnfrentamientoDAO enfrentamientoDAO = new EnfrentamientoDAO();

    /**
     * Prepara la pantalla al abrirse. Configura la tabla, carga las jornadas
     * y se queda escuchando por si haces clic en alguna fila.
     * * @param url La ruta del archivo FXML
     * @param rb Los recursos de la escena
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Enlazamos las columnas con los datos del modelo Jornadas
        colId.setCellValueFactory(new PropertyValueFactory<>("idJornada"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroJornada"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaJornada"));

        cargarDatosJornadas();

        // Si pinchas en una jornada de la tabla, se rellenan los campos y salen sus partidos
        tablaJornadas.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
                cargarEnfrentamientosDeJornada(nuevo);
            }
        });

        // Metemos un par de competiciones de ejemplo si el combo existe
        if (comboCompeticion != null) {
            comboCompeticion.setItems(FXCollections.observableArrayList("Competición 1", "Competición 2"));
        }
    }

    /**
     * Vacía la tabla y la vuelve a rellenar con lo que haya en la base de datos.
     */
    private void cargarDatosJornadas() {
        try {
            listaJornadas.clear();
            listaJornadas.addAll(jornadaDAO.getAllJornadas());
            tablaJornadas.setItems(listaJornadas);
        } catch (Exception e) {
            mostrarAlerta("Error", "Vaya, no se han podido cargar las jornadas: " + e.getMessage());
        }
    }

    /**
     * Pone los datos de la jornada seleccionada en los huecos de escribir y en el calendario.
     * * @param j La jornada que has pinchado en la tabla
     */
    private void rellenarFormulario(Jornadas j) {
        txtId.setText(j.getIdJornada());
        txtNumero.setText(String.valueOf(j.getNumeroJornada()));
        dateFecha.setValue(j.getFechaJornada());
    }

    /**
     * Busca los partidos que pertenecen a una jornada y los enseña en la lista de abajo.
     * * @param j La jornada de la que queremos ver los enfrentamientos
     */
    private void cargarEnfrentamientosDeJornada(Jornadas j) {
        if (listaEnfrentamientos != null) {
            try {
                int idJornada = Integer.parseInt(j.getIdJornada());
                java.util.List<String> enfrentamientos = enfrentamientoDAO.obtenerEnfrentamientosDeJornada(idJornada);

                if (enfrentamientos.isEmpty()) {
                    listaEnfrentamientos.setItems(FXCollections.observableArrayList("No hay partidos en esta jornada todavía"));
                } else {
                    listaEnfrentamientos.setItems(FXCollections.observableArrayList(enfrentamientos));
                }
            } catch (Exception e) {
                listaEnfrentamientos.setItems(FXCollections.observableArrayList("Error al cargar los partidos"));
                mostrarAlerta("Error", "No se han podido traer los enfrentamientos: " + e.getMessage());
            }
        }
    }

    /**
     * Guarda la jornada. Si no tiene ID, la crea; si ya tiene, actualiza la que hay.
     * * @param event El clic en el botón de guardar
     */
    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtNumero.getText().isEmpty() || dateFecha.getValue() == null) {
            mostrarAlerta("Error", "Tienes que poner el número y la fecha sí o sí.");
            return;
        }

        try {
            int numero = Integer.parseInt(txtNumero.getText());
            String id = txtId.getText();

            if (id == null || id.isEmpty()) {
                // Crear jornada desde cero
                jornadaDAO.insertJornada(numero, dateFecha.getValue());
            } else {
                // Modificar la jornada que ya existe
                jornadaDAO.updateJornada(Integer.parseInt(id), numero, dateFecha.getValue());
            }

            cargarDatosJornadas();
            onLimpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El número de la jornada tiene que ser un número de verdad.");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se ha podido guardar la jornada.");
        }
    }

    /**
     * Borra la jornada que tengas seleccionada después de preguntarte si estás seguro.
     */
    @FXML
    public void onEliminar() {
        Jornadas sel = tablaJornadas.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Atención", "Elige una jornada de la tabla para cargarla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quieres borrar la jornada " + sel.getNumeroJornada() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                jornadaDAO.deleteJornada(Integer.parseInt(sel.getIdJornada()));
                cargarDatosJornadas();
                onLimpiar();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se ha podido borrar la jornada.");
            }
        }
    }

    /**
     * Limpia todos los campos de texto, la fecha y la lista de partidos para dejarlo como nuevo.
     */
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

    /**
     * Te saca de esta pantalla y te lleva otra vez al menú del CRUD.
     * * @param event El clic en el botón volver
     */
    @FXML
    public void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("No se ha podido volver al menú.");
        }
    }

    /**
     * Función rápida para sacar un mensaje de aviso.
     * * @param titulo Lo que sale arriba en la ventana
     * @param msg El texto del aviso
     */
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}