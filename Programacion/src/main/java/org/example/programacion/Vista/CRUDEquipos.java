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
import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Modelo.Jugadores;
import org.example.programacion.Util.ConexionBD;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CRUDEquipos implements Initializable {

    @FXML private TableView<Equipos> tablaEquipos;
    @FXML private TableColumn<Equipos, Integer> colId;
    @FXML private TableColumn<Equipos, String> colNombre;
    @FXML private TableColumn<Equipos, Object> colFecha;

    @FXML private TextField txtId, txtNombre;
    @FXML private DatePicker dateFecha;

    // Etiqueta opcional para mostrar cuántos jugadores tiene el equipo seleccionado
    @FXML private Label lblInfoJugadores;

    private ObservableList<Equipos> listaEquipos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración de columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFundacion"));

        cargarDatosEquipos();

        // LISTENER: Cuando el usuario hace clic en un equipo de la tabla
        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
                cargarJugadoresDelEquipo(nuevo); // <--- Aquí se gestiona la relación
            }
        });
    }

    private void cargarDatosEquipos() {
        listaEquipos.clear();
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Equipo")) {

            while (rs.next()) {
                listaEquipos.add(new Equipos(
                        rs.getInt("id_equipo"),
                        rs.getString("nombre_equipo"),
                        rs.getDate("fecha_fundacion").toLocalDate()
                ));
            }
            tablaEquipos.setItems(listaEquipos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método carga los jugadores de la base de datos y los mete
     * en la ArrayList interna del objeto Equipo seleccionado.
     */
    private void cargarJugadoresDelEquipo(Equipos equipo) {
        equipo.getListaJugadores().clear(); // Limpiamos la lista interna del modelo

        String sql = "SELECT * FROM Jugador WHERE id_equipo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipo.getIdEquipo());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Jugadores j = new Jugadores(
                        String.valueOf(rs.getInt("id_jugador")),
                        rs.getString("nombre_jugador"),
                        rs.getString("apellido"),
                        rs.getString("nacionalidad"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nickname"),
                        rs.getString("rol"),
                        rs.getDouble("sueldo")
                );
                equipo.añadirJugador(j); // Los guardamos en el ArrayList del modelo
            }

            // Ejemplo de uso: mostrar cuántos hay en un Label
            if(lblInfoJugadores != null) {
                lblInfoJugadores.setText("Jugadores en plantilla: " + equipo.getListaJugadores().size());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rellenarFormulario(Equipos e) {
        txtId.setText(String.valueOf(e.getIdEquipo()));
        txtNombre.setText(e.getNombreEquipo());
        dateFecha.setValue(e.getFechaFundacion());
    }

    @FXML
    private void onGuardarClick() {
        if (txtNombre.getText().isEmpty() || dateFecha.getValue() == null) {
            mostrarAlerta("Campos vacíos", "Nombre y Fecha son obligatorios.");
            return;
        }

        String id = txtId.getText();
        String sql = (id == null || id.isEmpty())
                ? "INSERT INTO Equipo (nombre_equipo, fecha_fundacion) VALUES (?, ?)"
                : "UPDATE Equipo SET nombre_equipo = ?, fecha_fundacion = ? WHERE id_equipo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setDate(2, Date.valueOf(dateFecha.getValue()));

            if (id != null && !id.isEmpty()) {
                pstmt.setInt(3, Integer.parseInt(id));
            }

            pstmt.executeUpdate();
            cargarDatosEquipos();
            onLimpiarClick();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarClick() {
        Equipos sel = tablaEquipos.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        // Si el equipo tiene jugadores en su lista interna, avisamos
        if (!sel.getListaJugadores().isEmpty()) {
            mostrarAlerta("No se puede eliminar", "Este equipo tiene " + sel.getListaJugadores().size() + " jugadores asociados. Debes eliminarlos o cambiarlos de equipo primero.");
            return;
        }

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Equipo WHERE id_equipo = ?")) {
            pstmt.setInt(1, sel.getIdEquipo());
            pstmt.executeUpdate();
            cargarDatosEquipos();
            onLimpiarClick();
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se puede eliminar por restricción de clave foránea.");
        }
    }

    @FXML
    private void onLimpiarClick() {
        txtId.clear();
        txtNombre.clear();
        dateFecha.setValue(null);
        if(lblInfoJugadores != null) lblInfoJugadores.setText("");
        tablaEquipos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onAtras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}