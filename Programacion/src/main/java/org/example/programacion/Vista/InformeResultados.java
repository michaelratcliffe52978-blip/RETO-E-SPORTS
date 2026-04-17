package org.example.programacion.Vista;

import javafx.beans.property.SimpleStringProperty;
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
import org.example.programacion.DAO.ResultadosDAO;
import org.example.programacion.Modelo.Enfrentamientos;

import java.io.IOException;
import java.util.List;

/**
 * Clase controladora para la vista de Informe de Resultados.
 * Esta pantalla muestra un histórico de todos los enfrentamientos disputados,
 * detallando la jornada, los equipos participantes y el marcador final.
 * * @author equipo4
 * @version 1.0
 */
public class InformeResultados {

    /** Tabla principal que visualiza la lista de {@link Enfrentamientos} */
    @FXML private TableView<Enfrentamientos> tablaResultados;

    // --- COLUMNAS DE LA TABLA ---
    @FXML private TableColumn<Enfrentamientos, String> colJornada;
    @FXML private TableColumn<Enfrentamientos, String> colLocal;
    @FXML private TableColumn<Enfrentamientos, String> colVisitante;
    @FXML private TableColumn<Enfrentamientos, String> colMarcador;

    /** DAO especializado en la recuperación de marcadores y partidos */
    private ResultadosDAO resultadosDAO = new ResultadosDAO();

    /**
     * Inicializa la vista configurando las celdas de la tabla.
     * Implementa lógica personalizada para extraer nombres de equipos de objetos anidados
     * y utiliza métodos formateados del modelo para el marcador y la jornada.
     */
    @FXML
    public void initialize() {
        // 1. Vinculación de la Jornada mediante el método getNumeroDeJornada() del modelo
        colJornada.setCellValueFactory(new PropertyValueFactory<>("numeroDeJornada"));

        // 2. Lógica para la Columna Local: Extrae el nombre del objeto Equipos
        colLocal.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getEquipo1() != null) {
                return new SimpleStringProperty(cellData.getValue().getEquipo1().getNombreEquipo());
            }
            return new SimpleStringProperty("N/A");
        });

        // 3. Lógica para la Columna Visitante: Extrae el nombre del objeto Equipos
        colVisitante.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getEquipo2() != null) {
                return new SimpleStringProperty(cellData.getValue().getEquipo2().getNombreEquipo());
            }
            return new SimpleStringProperty("N/A");
        });

        // 4. Vinculación del Marcador mediante el método getResultadoFormateado() del modelo
        colMarcador.setCellValueFactory(new PropertyValueFactory<>("resultadoFormateado"));

        // 5. Carga de datos desde el repositorio
        cargarDatos();
    }

    /**
     * Consulta al DAO todos los resultados registrados en la base de datos
     * y los vuelca en la tabla.
     */
    private void cargarDatos() {
        try {
            List<Enfrentamientos> lista = resultadosDAO.getTodosLosResultados();
            if (lista != null) {
                tablaResultados.setItems(FXCollections.observableArrayList(lista));
            }
        } catch (Exception e) {
            mostrarAlerta("Error de Carga", "No se han podido recuperar los resultados: " + e.getMessage());
        }
    }

    /**
     * Utilidad para mostrar alertas de error en caso de fallos en la persistencia.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Regresa a la pantalla del menú principal de usuario.
     * @param event Evento de disparo del botón.
     */
    @FXML
    void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Error al navegar al menú: " + e.getMessage());
        }
    }
}