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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EstadisticasEquipos implements Initializable {

    @FXML private TableView<EstadisticasRow> tablaEstadisticas;
    @FXML private TableColumn<EstadisticasRow, String> colNombre;
    @FXML private TableColumn<EstadisticasRow, String> colFundacion;
    @FXML private TableColumn<EstadisticasRow, Integer> colNumJugadores;
    @FXML private TableColumn<EstadisticasRow, Double> colSueldoMax;
    @FXML private TableColumn<EstadisticasRow, Double> colSueldoMin;
    @FXML private TableColumn<EstadisticasRow, Double> colSueldoMedio;

    private EquiposController equiposController = new EquiposController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vinculamos columnas con los nombres de los métodos "get" de EstadisticasRow
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFundacion.setCellValueFactory(new PropertyValueFactory<>("fundacion"));
        colNumJugadores.setCellValueFactory(new PropertyValueFactory<>("numJugadores"));
        colSueldoMax.setCellValueFactory(new PropertyValueFactory<>("sueldoMax"));
        colSueldoMin.setCellValueFactory(new PropertyValueFactory<>("sueldoMin"));
        colSueldoMedio.setCellValueFactory(new PropertyValueFactory<>("sueldoMedio"));

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            ObservableList<EstadisticasRow> lista = FXCollections.observableArrayList(
                    equiposController.obtenerEstadisticasParaTabla()
            );
            tablaEstadisticas.setItems(lista);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar las estadísticas: " + e.getMessage());
        }
    }

    @FXML
    private void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // CLASE INTERNA: Sirve para representar cada fila de la tabla
    public static class EstadisticasRow {
        private String nombre, fundacion;
        private int numJugadores;
        private double sueldoMax, sueldoMin, sueldoMedio;

        public EstadisticasRow(String nombre, String fundacion, int numJugadores, double sueldoMax, double sueldoMin, double sueldoMedio) {
            this.nombre = nombre;
            this.fundacion = fundacion;
            this.numJugadores = numJugadores;
            this.sueldoMax = sueldoMax;
            this.sueldoMin = sueldoMin;
            this.sueldoMedio = sueldoMedio;
        }

        public String getNombre() { return nombre; }
        public String getFundacion() { return fundacion; }
        public int getNumJugadores() { return numJugadores; }
        public double getSueldoMax() { return sueldoMax; }
        public double getSueldoMin() { return sueldoMin; }
        public double getSueldoMedio() { return sueldoMedio; }
    }
}