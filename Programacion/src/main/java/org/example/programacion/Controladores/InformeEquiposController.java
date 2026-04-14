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

public class InformeEquiposController implements Initializable {

    private EquiposDAO equiposDAO = new EquiposDAO();
    private JugadoresDAO jugadoresDAO = new JugadoresDAO();

    @FXML
    private ComboBox<Equipos> comboEquipos;

    @FXML
    private TableView<Jugadores> tablaIntegrantes;

    @FXML
    private TableColumn<Jugadores, String> colNickname;

    @FXML
    private TableColumn<Jugadores, String> colNombre;

    @FXML
    private TableColumn<Jugadores, String> colApellido;

    @FXML
    private TableColumn<Jugadores, String> colNacionalidad;

    @FXML
    private TableColumn<Jugadores, String> colFechaNac;

    @FXML
    private TableColumn<Jugadores, String> colRol;

    @FXML
    private TableColumn<Jugadores, Double> colSueldo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar las columnas de la tabla
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));

        // Cargar equipos en el ComboBox
        cargarEquipos();
    }

    private void cargarEquipos() {
        try {
            List<Equipos> equipos = equiposDAO.getAllEquipos();
            ObservableList<Equipos> observableEquipos = FXCollections.observableArrayList(equipos);
            comboEquipos.setItems(observableEquipos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEquipoSeleccionado(ActionEvent event) {
        Equipos equipoSeleccionado = comboEquipos.getSelectionModel().getSelectedItem();
        if (equipoSeleccionado != null) {
            try {
                List<Jugadores> jugadores = jugadoresDAO.getJugadoresByEquipoId(equipoSeleccionado.getIdEquipo());
                ObservableList<Jugadores> observableJugadores = FXCollections.observableArrayList(jugadores);
                tablaIntegrantes.setItems(observableJugadores);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onVolver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

