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

import org.example.programacion.Controladores.EnfrentamientoController;
import org.example.programacion.DAO.EquiposDAO;

/**
 * Controlador para la vista de introducción de resultados de partidos.
 * Permite a los administradores registrar los marcadores de los enfrentamientos,
 * gestionando la validación de equipos y el tratamiento de errores personalizados
 * provenientes de la base de datos Oracle.
 * * @author equipo4
 * @version 1.0
 */
public class IntroducirResultado implements Initializable {

    /** Selectores de equipo para el encuentro */
    @FXML private ChoiceBox<String> Equipo1;
    @FXML private ChoiceBox<String> Equipo2;

    /** Selectores de puntuación (marcador) */
    @FXML private ChoiceBox<String> Resultado1;
    @FXML private ChoiceBox<String> Resultado2;

    /** Lista completa de equipos para gestionar los filtros dinámicos */
    private ObservableList<String> todasLasOpciones = FXCollections.observableArrayList();

    /** DAOs y Controladores para la gestión de datos */
    private EquiposDAO equiposDAO = new EquiposDAO();
    private EnfrentamientoController enfrentamientoController = new EnfrentamientoController();

    /**
     * Inicializa la vista cargando los equipos de la base de datos,
     * poblando los selectores de puntuación y configurando el filtro
     * de exclusión mutua para los equipos.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatosDesdeBD();

        // Rellenar puntuaciones del 0 al 14 (estándar de la competición)
        ObservableList<String> numeros = FXCollections.observableArrayList();
        for (int i = 0; i <= 14; i++) {
            numeros.add(String.valueOf(i));
        }
        Resultado1.setItems(numeros);
        Resultado2.setItems(numeros);

        configurarFiltroEquipos();
    }

    /**
     * Recupera los nombres de los equipos desde la BD y rellena los ChoiceBox iniciales.
     */
    private void cargarDatosDesdeBD() {
        todasLasOpciones.clear();
        todasLasOpciones.addAll(equiposDAO.getAllEquipoNames());

        Equipo1.setItems(FXCollections.observableArrayList(todasLasOpciones));
        Equipo2.setItems(FXCollections.observableArrayList(todasLasOpciones));
    }

    /**
     * Configura un Listener que evita que el mismo equipo pueda ser seleccionado
     * simultáneamente como local y visitante.
     */
    private void configurarFiltroEquipos() {
        Equipo1.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                String seleccionadoEn2 = Equipo2.getValue();
                ObservableList<String> filtro = FXCollections.observableArrayList(todasLasOpciones);
                filtro.remove(nuevo); // Quitamos el equipo seleccionado en 1 de las opciones de 2
                Equipo2.setItems(filtro);

                if (filtro.contains(seleccionadoEn2)) {
                    Equipo2.setValue(seleccionadoEn2);
                }
            }
        });
    }

    /**
     * Procesa el envío del resultado. Captura posibles excepciones de SQL
     * (incluyendo las lanzadas por Triggers) y las limpia para mostrarlas al usuario.
     * @param event Evento de clic en el botón enviar.
     */
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
            // Persistencia del resultado a través del controlador
            enfrentamientoController.actualizarResultado(nombreE1, nombreE2, marcador1, marcador2);
            mostrarAlertaInformativa("Éxito", "Resultado actualizado correctamente.");
            navegarAMenu(event);

        } catch (Exception e) {
            // Tratamiento de errores ORA-20XXX de Oracle
            String mensajeLimpio = limpiarMensajeOracle(e.getMessage());
            mostrarAlerta("Error de Validación", mensajeLimpio);
        }
    }

    /**
     * Analiza el String de error de Oracle para extraer únicamente el mensaje
     * de texto definido por el desarrollador en el Trigger (RAISE_APPLICATION_ERROR).
     * @param mensajeOriginal Traza completa del error de base de datos.
     * @return El mensaje simplificado y legible para el usuario.
     */
    private String limpiarMensajeOracle(String mensajeOriginal) {
        if (mensajeOriginal == null) return "Error desconocido en la base de datos.";

        if (mensajeOriginal.contains("ORA-20")) {
            try {
                int inicio = mensajeOriginal.indexOf(":") + 1;
                int fin = mensajeOriginal.indexOf("ORA-", inicio);

                if (fin != -1) {
                    return mensajeOriginal.substring(inicio, fin).trim();
                } else {
                    return mensajeOriginal.substring(inicio).trim();
                }
            } catch (Exception e) {
                return mensajeOriginal;
            }
        }
        return mensajeOriginal;
    }

    /**
     * Navega de vuelta al menú administrativo.
     */
    private void navegarAMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAtras(ActionEvent event) {
        navegarAMenu(event);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInformativa(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}