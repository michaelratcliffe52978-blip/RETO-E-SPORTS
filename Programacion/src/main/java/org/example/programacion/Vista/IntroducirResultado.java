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
import java.util.ResourceBundle;
import java.sql.SQLException;

import org.example.programacion.Controladores.EnfrentamientoController;
import org.example.programacion.DAO.EquiposDAO;

public class IntroducirResultado implements Initializable {

    @FXML private ChoiceBox<String> Equipo1;
    @FXML private ChoiceBox<String> Equipo2;
    @FXML private ChoiceBox<String> Resultado1;
    @FXML private ChoiceBox<String> Resultado2;

    private ObservableList<String> todasLasOpciones = FXCollections.observableArrayList();

    private EquiposDAO equiposDAO = new EquiposDAO();
    private EnfrentamientoController enfrentamientoController = new EnfrentamientoController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Cargar datos de la tabla EQUIPOS
        cargarDatosDesdeBD();

        // 2. Rellenar puntuaciones del 0 al 14
        ObservableList<String> numeros = FXCollections.observableArrayList();
        for (int i = 0; i <= 14; i++) {
            numeros.add(String.valueOf(i));
        }
        Resultado1.setItems(numeros);
        Resultado2.setItems(numeros);

        // 3. Lógica para que no se pueda elegir el mismo equipo en ambos lados
        configurarFiltroEquipos();
    }

    private void cargarDatosDesdeBD() {
        todasLasOpciones.clear();
        todasLasOpciones.addAll(equiposDAO.getAllEquipoNames());

        Equipo1.setItems(FXCollections.observableArrayList(todasLasOpciones));
        Equipo2.setItems(FXCollections.observableArrayList(todasLasOpciones));
    }

    private void configurarFiltroEquipos() {
        Equipo1.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                String seleccionadoEn2 = Equipo2.getValue();
                ObservableList<String> filtro = FXCollections.observableArrayList(todasLasOpciones);
                filtro.remove(nuevo);
                Equipo2.setItems(filtro);

                if (filtro.contains(seleccionadoEn2)) {
                    Equipo2.setValue(seleccionadoEn2);
                }
            }
        });
    }

    @FXML
    public void onEnviar(ActionEvent event) {
        String nombreE1 = Equipo1.getValue();
        String nombreE2 = Equipo2.getValue();
        String marcador1 = Resultado1.getValue();
        String marcador2 = Resultado2.getValue();

        if (nombreE1 == null || nombreE2 == null || marcador1 == null || marcador2 == null) {
            mostrarAlerta("Campos incompletos", "Por favor, selecciona todos los campos antes de enviar.");
            return;
        }

        try {
            // Llamamos al controlador para procesar en la BD
            enfrentamientoController.actualizarResultado(nombreE1, nombreE2, marcador1, marcador2);

            // Si llega aquí es que no hubo error
            mostrarAlertaInformativa("Éxito", "Resultado actualizado correctamente.");
            navegarAMenu(event);

        } catch (Exception e) {
            // Aquí capturamos el error del Trigger y lo limpiamos
            String mensajeLimpio = limpiarMensajeOracle(e.getMessage());
            mostrarAlerta("Error de Validación", mensajeLimpio);
        }
    }

    /**
     * Método para extraer solo el mensaje personalizado del error de Oracle
     */
    private String limpiarMensajeOracle(String mensajeOriginal) {
        if (mensajeOriginal == null) return "Error desconocido en la base de datos.";

        // Buscamos el patrón ORA-20XXX que define nuestros errores personalizados
        if (mensajeOriginal.contains("ORA-20")) {
            try {
                // El mensaje útil está después de los dos puntos
                int inicio = mensajeOriginal.indexOf(":") + 1;
                // Cortamos antes de que empiece la siguiente traza de error de Oracle
                int fin = mensajeOriginal.indexOf("ORA-", inicio);

                if (fin != -1) {
                    return mensajeOriginal.substring(inicio, fin).trim();
                } else {
                    return mensajeOriginal.substring(inicio).trim();
                }
            } catch (Exception e) {
                return mensajeOriginal; // Fallback por si el parseo falla
            }
        }
        return mensajeOriginal;
    }

    private void navegarAMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar el menú principal.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onAtras(ActionEvent event) {
        navegarAMenu(event);
    }

    // Alerta de ERROR (Roja)
    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Alerta de INFORMACIÓN (Azul)
    private void mostrarAlertaInformativa(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}