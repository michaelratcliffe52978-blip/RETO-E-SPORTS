package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.programacion.DAO.EnfrentamientoDAO;
import org.example.programacion.DAO.EquiposDAO;
import org.example.programacion.DAO.CompeticionDAO;
import org.example.programacion.Modelo.Equipos;

import java.io.IOException;
import java.util.List;

public class MenuAdmin {

    private CompeticionDAO competicionDAO = new CompeticionDAO();
    private EquiposDAO equiposDAO = new EquiposDAO();
    private EnfrentamientoDAO enfrentamientoDAO = new EnfrentamientoDAO();

    @FXML
    public void onCRUD(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/MenuCRUD.fxml");
    }

    @FXML
    public void onIntroducir(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/IntroducirResultado.fxml");
    }

    @FXML
    public void CerrarSesion(ActionEvent event) {
        cambiarEscena(event, "/org/example/programacion/2.Vista.fxml");
    }

    @FXML
    public void onGenerarCalendario(ActionEvent event) {
        // 1. REGLA: Mínimo 1 jugador por equipo
        if (!equiposDAO.validarMinimoJugadores(1)) {
            mostrarAlerta("Error", "No se puede generar: Hay equipos sin jugadores.");
            return;
        }

        // 2. Verificar si el calendario ya existe
        if (enfrentamientoDAO.existeCalendario()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Calendario Existente");
            confirm.setHeaderText("Ya existe un calendario generado");
            confirm.setContentText("¿Deseas eliminar el calendario anterior y generar uno nuevo?");
            
            if (confirm.showAndWait().get() != ButtonType.OK) {
                return; // El usuario canceló
            }
        }

        // 3. Obtener equipos
        List<Equipos> listaEquipos = equiposDAO.getAllEquipos();

        // 4. Generar Round Robin y guardar en BD
        boolean exito = enfrentamientoDAO.generarYGuardarCalendario(listaEquipos);

        if (exito) {
            // 5. REGLA: Cerrar inscripciones
            competicionDAO.cerrarInscripciones();
            mostrarAlerta("Éxito", "Calendario generado (una jornada por semana). Inscripciones cerradas.");
        } else {
            mostrarAlerta("Error", "Error al guardar el calendario. Revisa la conexión.");
        }
    }

    @FXML
    public void onCerrarEstado(ActionEvent event) {
        int idCompeticion = 1; // Este ID debería venir de la selección del usuario

        if (competicionDAO.actualizarEstado(idCompeticion, "cerrado")) {
            mostrarAlerta("Estado Actualizado", "La competición ha sido cerrada.");
        }
    }

    // Método auxiliar para no repetir código de cambio de ventana
    private void cambiarEscena(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}