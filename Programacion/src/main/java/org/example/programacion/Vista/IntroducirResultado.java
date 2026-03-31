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
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class IntroducirResultado implements Initializable {

    // 1. Elementos de la Interfaz (Asegúrate de que los IDs coincidan en Scene Builder)
    @FXML private ChoiceBox<String> Equipo1;
    @FXML private ChoiceBox<String> Equipo2;
    @FXML private ChoiceBox<String> Resultado1;
    @FXML private ChoiceBox<String> Resultado2;

    // Lista maestra para guardar los nombres de los equipos de la BD
    private ObservableList<String> todasLasOpciones = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TAREA A: Cargar equipos desde la Base de Datos
        cargarDatosDesdeBD();

        // TAREA B: Rellenar los ChoiceBox de puntuación (1 a 14)
        ObservableList<String> numeros = FXCollections.observableArrayList();
        for (int i = 1; i <= 14; i++) {
            numeros.add(String.valueOf(i));
        }
        Resultado1.setItems(numeros);
        Resultado2.setItems(numeros);

        // TAREA C: Configurar que no se pueda elegir el mismo equipo en ambos
        configurarFiltroEquipos();
    }

    private void cargarDatosDesdeBD() {
        todasLasOpciones.clear();
        String query = "SELECT NOMBREEQUIPO FROM EQUIPO";
        String url = "jdbc:oracle:thin:@//172.20.225.114:1521/ORCLPDB1";
        String user = "eqdaw04";
        String pass = "eqdaw04";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nombre = rs.getString("NOMBREEQUIPO");
                todasLasOpciones.add(nombre);
            }

            // Llenamos ambos ChoiceBox con todos los equipos al principio
            Equipo1.setItems(FXCollections.observableArrayList(todasLasOpciones));
            Equipo2.setItems(FXCollections.observableArrayList(todasLasOpciones));

        } catch (SQLException e) {
            System.err.println("Error de conexión o SQL:");
            e.printStackTrace();
        }
    }

    private void configurarFiltroEquipos() {
        // Al seleccionar en el Equipo 1, lo quitamos de la lista del Equipo 2
        Equipo1.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                // Guardamos lo que el usuario ya hubiera elegido en el 2 para no borrarlo
                String seleccionadoEn2 = Equipo2.getValue();

                ObservableList<String> filtro = FXCollections.observableArrayList(todasLasOpciones);
                filtro.remove(nuevo);
                Equipo2.setItems(filtro);

                // Si lo que estaba en el 2 sigue en la lista, lo mantenemos seleccionado
                if (filtro.contains(seleccionadoEn2)) {
                    Equipo2.setValue(seleccionadoEn2);
                }
            }
        });
    }

    @FXML
    public void onAtras(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Fallo al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onEnviar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Fallo al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

}