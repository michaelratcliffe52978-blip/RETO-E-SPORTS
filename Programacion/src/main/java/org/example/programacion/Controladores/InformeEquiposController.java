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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.JugadoresDAO;
import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jugadores;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Este controlador sirve para ver quién está en cada equipo.
 * Eliges un equipo en el desplegable y, ¡pumba!, te salen todos sus jugadores
 * con sus datos en la tabla.
 * * @author equipo4
 * @version 1.0
 */
public class InformeEquiposController implements Initializable {

    /** Para pillar los equipos de la base de datos */
    private EquiposDAO equiposDAO = new EquiposDAO();
    /** Para pillar los jugadores de la base de datos */
    private JugadoresDAO jugadoresDAO = new JugadoresDAO();

    @FXML private ComboBox<Equipos> comboEquipos;
    @FXML private TableView<Jugadores> tablaIntegrantes;

    // --- Columnas de la tabla ---
    @FXML private TableColumn<Jugadores, String> colNickname;
    @FXML private TableColumn<Jugadores, String> colNombre;
    @FXML private TableColumn<Jugadores, String> colApellido;
    @FXML private TableColumn<Jugadores, String> colNacionalidad;
    @FXML private TableColumn<Jugadores, String> colFechaNac;
    @FXML private TableColumn<Jugadores, String> colRol;
    @FXML private TableColumn<Jugadores, Double> colSueldo;

    /**
     * Prepara todo el tinglado cuando se abre la pantalla.
     * Conecta las columnas con los datos de los jugadores y carga los equipos en el combo.
     * * @param url La ruta del FXML
     * @param resourceBundle Los recursos de idioma y tal
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Enlazamos cada columna con el atributo que toca de la clase Jugadores
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));

        cargarEquipos();
    }

    /**
     * Trae todos los equipos que existen y los mete en el ComboBox para que puedas elegir uno.
     */
    private void cargarEquipos() {
        try {
            List<Equipos> equipos = equiposDAO.getAllEquipos();
            if (equipos != null) {
                ObservableList<Equipos> observableEquipos = FXCollections.observableArrayList(equipos);
                comboEquipos.setItems(observableEquipos);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No he podido cargar los equipos: " + e.getMessage());
        }
    }

    /**
     * Este método salta cuando eliges un equipo del desplegable.
     * Busca qué jugadores hay en ese equipo y los planta en la tabla.
     * * @param event El evento de seleccionar algo en el combo
     */
    @FXML
    public void onEquipoSeleccionado(ActionEvent event) {
        Equipos equipoSeleccionado = comboEquipos.getSelectionModel().getSelectedItem();

        if (equipoSeleccionado != null) {
            try {
                // Buscamos los jugadores que tengan el ID del equipo que hemos marcado
                List<Jugadores> jugadores = jugadoresDAO.getJugadoresByEquipoId(equipoSeleccionado.getIdEquipo());
                ObservableList<Jugadores> observableJugadores = FXCollections.observableArrayList(jugadores);
                tablaIntegrantes.setItems(observableJugadores);
            } catch (Exception e) {
                mostrarAlerta("Error", "Fallo al filtrar los jugadores: " + e.getMessage());
            }
        }
    }

    /**
     * Para salir de aquí y volver a la pantalla del menú de usuario.
     * * @param event El clic en el botón de volver
     */
    @FXML
    public void onVolver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar volver al menú: " + e.getMessage());
        }
    }

    /**
     * Una ayudita para sacar mensajes por pantalla si algo va mal.
     * * @param titulo El título de la ventanita
     * @param msg Lo que le quieres contar al usuario
     */
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}