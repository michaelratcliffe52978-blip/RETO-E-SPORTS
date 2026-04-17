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

/**
 * Controlador para la vista de Informes de Equipos.
 * Esta pantalla permite a los usuarios (perfil 'USER' o 'ADMIN') consultar
 * la plantilla detallada de cada equipo seleccionándolo desde un desplegable.
 * * @author equipo4
 * @version 1.0
 */
public class InformeEquipos {

    /** Selector desplegable con los nombres de los equipos registrados */
    @FXML private ComboBox<String> comboEquipos;

    /** Tabla que muestra los integrantes del equipo seleccionado */
    @FXML private TableView<Jugadores> tablaIntegrantes;

    // --- COLUMNAS DE LA TABLA ---
    @FXML private TableColumn<Jugadores, String> colNickname;
    @FXML private TableColumn<Jugadores, String> colNombre;
    @FXML private TableColumn<Jugadores, String> colApellido;
    @FXML private TableColumn<Jugadores, String> colNacionalidad;
    @FXML private TableColumn<Jugadores, String> colRol;
    @FXML private TableColumn<Jugadores, Double> colSueldo;
    @FXML private TableColumn<Jugadores, LocalDate> colFechaNac;

    /** DAO para acceder a los datos de equipos y sus plantillas */
    private EquiposDAO equipoDAO = new EquiposDAO();

    /**
     * Inicializa la vista configurando el mapeo de las columnas con la clase {@link Jugadores}
     * y cargando la lista de nombres de equipos en el ComboBox.
     */
    @FXML
    public void initialize() {
        // Vinculación de columnas: El nombre debe coincidir con los atributos de la clase Jugadores
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

        // Carga inicial del ComboBox desde la base de datos
        List<String> nombres = equipoDAO.getAllEquipoNames();
        comboEquipos.setItems(FXCollections.observableArrayList(nombres));
    }

    /**
     * Evento que se dispara al seleccionar un equipo.
     * Recupera la lista de jugadores asociados a ese nombre y refresca la tabla.
     * @param event Evento de selección en el ComboBox.
     */
    @FXML
    void onEquipoSeleccionado(ActionEvent event) {
        String nombreEquipo = comboEquipos.getValue();
        if (nombreEquipo != null) {
            // Consulta al DAO para obtener los integrantes del equipo seleccionado
            List<Jugadores> lista = equipoDAO.getJugadoresPorEquipo(nombreEquipo);
            tablaIntegrantes.setItems(FXCollections.observableArrayList(lista));
        }
    }

    /**
     * Gestiona el regreso al menú de usuario.
     * @param event Evento de pulsación del botón Volver.
     */
    @FXML
    void onVolver(ActionEvent event) {
        try {
            // Carga de la vista principal del usuario
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al navegar al menú de usuario: " + e.getMessage());
        }
    }
}