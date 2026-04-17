package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.programacion.DAO.CompeticionDAO;

import java.io.IOException;

/**
 * Clase controladora para el Menú Principal de Usuario.
 * Proporciona una interfaz de navegación simplificada para usuarios con privilegios
 * de consulta, permitiendo el acceso a los informes de plantillas y resultados
 * de la competición.
 * * @author equipo4
 * @version 1.0
 */
public class MenuUser {

    /** Acceso a la persistencia para posibles consultas de estado de la competición */
    private CompeticionDAO competicionDAO = new CompeticionDAO();

    /**
     * Navega hacia la pantalla de visualización detallada de los equipos y sus jugadores.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void VerInformeEquipos(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/InformeEquipos.fxml");
    }

    /**
     * Navega hacia la pantalla de consulta de resultados y clasificación.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void VerInformeResultados(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/InformeResultados.fxml");
    }

    /**
     * Finaliza la sesión del usuario actual y regresa a la pantalla de inicio (selección de rol).
     * @param event Evento de acción del botón.
     */
    @FXML
    public void CerrarSesion(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/2.Vista.fxml");
    }

    /**
     * Método auxiliar centralizado para gestionar las transiciones entre escenas de JavaFX.
     * Reduce la duplicidad de código y facilita el mantenimiento de las rutas FXML.
     * @param event Evento de origen para identificar la ventana (Stage) actual.
     * @param fxmlPath Ruta relativa al archivo FXML de la nueva vista.
     */
    private void cambiarEscena(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la escena: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Utilidad para mostrar notificaciones informativas al usuario.
     * @param titulo Título de la alerta.
     * @param mensaje Cuerpo del mensaje.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}