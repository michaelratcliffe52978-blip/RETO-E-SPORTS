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
import org.example.programacion.Modelo.Enfrentamiento;

import java.io.IOException;
import java.util.List;

public class InformeResultados {

    // CORRECCIÓN: La tabla debe ser de tipo <Enfrentamiento>, no <String>
    @FXML private TableView<Enfrentamiento> tablaResultados;

    @FXML private TableColumn<Enfrentamiento, String> colJornada;
    @FXML private TableColumn<Enfrentamiento, String> colLocal;
    @FXML private TableColumn<Enfrentamiento, String> colVisitante;
    @FXML private TableColumn<Enfrentamiento, String> colMarcador;

    private ResultadosDAO resultadosDAO = new ResultadosDAO();

    @FXML
    public void initialize() {
        // 1. Vincular Jornada (Usamos el nuevo método que creamos en Enfrentamiento)
        colJornada.setCellValueFactory(new PropertyValueFactory<>("numeroDeJornada"));

        // 2. Columna Local
        colLocal.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getEquipo1() != null) {
                return new SimpleStringProperty(cellData.getValue().getEquipo1().getNombreEquipo());
            }
            return new SimpleStringProperty("N/A");
        });

        // 3. Columna Visitante
        colVisitante.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getEquipo2() != null) {
                return new SimpleStringProperty(cellData.getValue().getEquipo2().getNombreEquipo());
            }
            return new SimpleStringProperty("N/A");
        });

        // 4. CORRECCIÓN DEL MARCADOR: Ya no usamos "hora", usamos el método del resultado
        // Importante: El nombre dentro de las comillas debe coincidir con el método getResultadoFormateado()
        colMarcador.setCellValueFactory(new PropertyValueFactory<>("resultadoFormateado"));

        // 5. Cargar datos reales
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            List<Enfrentamiento> lista = resultadosDAO.getTodosLosResultados();
            if (lista != null) {
                tablaResultados.setItems(FXCollections.observableArrayList(lista));
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar resultados: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuUser.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}