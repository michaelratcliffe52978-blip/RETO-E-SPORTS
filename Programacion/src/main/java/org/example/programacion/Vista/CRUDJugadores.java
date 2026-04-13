package org.example.programacion.Vista;

import javafx.beans.property.SimpleStringProperty;
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
import org.example.programacion.Modelo.Jugadores;
import org.example.programacion.Util.ConexionBD;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CRUDJugadores implements Initializable {

    // --- TABLA Y COLUMNAS ---
    @FXML private TableView<Jugadores> tablaJugadores;
    @FXML private TableColumn<Jugadores, String> colId, colNickname, colNombre, colNacionalidad, colRol, colEquipo;
    @FXML private TableColumn<Jugadores, Double> colSueldo;
    @FXML private TableColumn<Jugadores, Object> colFecha;

    // --- CAMPOS DE FORMULARIO ---
    @FXML private TextField txtId, txtNombre, txtApellido, txtNickname, txtSueldo, txtNacionalidad, txtBusqueda;
    @FXML private DatePicker dateFechaNac;
    @FXML private ComboBox<String> comboRol, comboEquipo;

    private ObservableList<Jugadores> listaMaster = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEquiposCombo();
        cargarDatosTabla();

        // Rellenar Roles según el CONSTRAINT de tu BD
        comboRol.setItems(FXCollections.observableArrayList("duelista", "controlador", "iniciador", "centinela"));

        // Listener para seleccionar filas
        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) rellenarCampos(nuevo);
        });
    }

    private void configurarTabla() {
        // Enlace directo con atributos del modelo (Jugadores.java)
        colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // Nombre Completo (Calculado: Nombre + Apellido)
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreJugador() + " " + cellData.getValue().getApellido()));

        // Columna Equipo (Calculada mediante consulta a la BD)
        colEquipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerNombreEquipo(cellData.getValue().getIdJugador())));
    }

    @FXML
    private void onGuardarClick() {
        if (txtNombre == null || txtApellido == null || comboRol.getValue() == null) {
            mostrarAlerta("Error", "Faltan campos críticos por vincular en el FXML.");
            return;
        }

        String id = txtId.getText();
        String sql;

        if (id == null || id.isEmpty()) {
            sql = "INSERT INTO Jugador (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT id_equipo FROM Equipo WHERE nombre_equipo = ?))";
        } else {
            sql = "UPDATE Jugador SET nombre_jugador=?, apellido=?, nacionalidad=?, fecha_nacimiento=?, nickname=?, rol=?, sueldo=?, " +
                    "id_equipo=(SELECT id_equipo FROM Equipo WHERE nombre_equipo = ?) WHERE id_jugador = ?";
        }

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtApellido.getText());
            pstmt.setString(3, txtNacionalidad.getText());
            pstmt.setDate(4, Date.valueOf(dateFechaNac.getValue()));
            pstmt.setString(5, txtNickname.getText());
            pstmt.setString(6, comboRol.getValue());
            pstmt.setDouble(7, Double.parseDouble(txtSueldo.getText()));
            pstmt.setString(8, comboEquipo.getValue());

            if (id != null && !id.isEmpty()) pstmt.setInt(9, Integer.parseInt(id));

            pstmt.executeUpdate();
            cargarDatosTabla();
            onLimpiarClick();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarClick() {
        Jugadores seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un jugador para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Borrar a " + seleccionado.getNickname() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Jugador WHERE id_jugador = ?")) {
                pstmt.setInt(1, Integer.parseInt(seleccionado.getIdJugador()));
                pstmt.executeUpdate();
                cargarDatosTabla();
                onLimpiarClick();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void cargarDatosTabla() {
        listaMaster.clear();
        try (Connection conn = ConexionBD.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Jugador")) {
            while (rs.next()) {
                listaMaster.add(new Jugadores(
                        String.valueOf(rs.getInt("id_jugador")),
                        rs.getString("nombre_jugador"),
                        rs.getString("apellido"),
                        rs.getString("nacionalidad"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nickname"),
                        rs.getString("rol"),
                        rs.getDouble("sueldo")
                ));
            }
            tablaJugadores.setItems(listaMaster);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void cargarEquiposCombo() {
        try (Connection conn = ConexionBD.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT nombre_equipo FROM Equipo ORDER BY nombre_equipo")) {
            ObservableList<String> equipos = FXCollections.observableArrayList();
            while (rs.next()) equipos.add(rs.getString("nombre_equipo"));
            comboEquipo.setItems(equipos);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private String obtenerNombreEquipo(String idJugador) {
        String sql = "SELECT e.nombre_equipo FROM Equipo e JOIN Jugador j ON e.id_equipo = j.id_equipo WHERE j.id_jugador = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idJugador));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nombre_equipo");
        } catch (Exception e) { }
        return "Sin Equipo";
    }

    private void rellenarCampos(Jugadores j) {
        txtId.setText(j.getIdJugador());
        txtNombre.setText(j.getNombreJugador());
        txtApellido.setText(j.getApellido());
        txtNickname.setText(j.getNickname());
        txtNacionalidad.setText(j.getNacionalidad());
        txtSueldo.setText(String.valueOf(j.getSueldo()));
        dateFechaNac.setValue(j.getFechaNacimiento());
        comboRol.setValue(j.getRol());
        comboEquipo.setValue(obtenerNombreEquipo(j.getIdJugador()));
    }

    @FXML private void onLimpiarClick() {
        txtId.clear(); txtNombre.clear(); txtApellido.clear(); txtNickname.clear();
        txtNacionalidad.clear(); txtSueldo.clear(); dateFechaNac.setValue(null);
        comboRol.setValue(null); comboEquipo.setValue(null);
    }

    @FXML private void onAtras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}