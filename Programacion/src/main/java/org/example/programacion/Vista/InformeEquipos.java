package org.example.programacion.Vista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.Modelo.Jugadores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class InformeEquipos {

    @FXML private ComboBox<String> comboEquipos;

    // La tabla ahora usa tu clase del modelo
    @FXML private TableView<Jugadores> tablaIntegrantes;

    // Definimos todas las columnas necesarias según tu clase Jugadores
    @FXML private TableColumn<Jugadores, String> colNickname;
    @FXML private TableColumn<Jugadores, String> colNombre;
    @FXML private TableColumn<Jugadores, String> colApellido;
    @FXML private TableColumn<Jugadores, String> colNacionalidad;
    @FXML private TableColumn<Jugadores, String> colRol;
    @FXML private TableColumn<Jugadores, Double> colSueldo;
    @FXML private TableColumn<Jugadores, LocalDate> colFechaNac;

    private EquiposDAO equipoDAO = new EquiposDAO();

    @FXML
    public void initialize() {
        // Configuración de celdas: El texto debe ser IGUAL al nombre del atributo en la clase Jugadores
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

        // Cargar los nombres de los equipos desde el DAO
        // Usamos getAllEquipoNames() que es el método que tienes implementado y con datos
        List<String> nombres = equipoDAO.getAllEquipoNames();
        comboEquipos.setItems(FXCollections.observableArrayList(nombres));
    }

    @FXML
    void onEquipoSeleccionado(ActionEvent event) {
        String nombreEquipo = comboEquipos.getValue();
        if (nombreEquipo != null) {
            // Llamamos al método del DAO que busca jugadores por el nombre del equipo
            List<Jugadores> lista = equipoDAO.getJugadoresPorEquipo(nombreEquipo);
            tablaIntegrantes.setItems(FXCollections.observableArrayList(lista));
        }
    }

    @FXML
    void onVolver(ActionEvent event) {
        try {
            // Asegúrate de que la ruta al FXML sea correcta
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al volver al menú: " + e.getMessage());
        }
    }
}