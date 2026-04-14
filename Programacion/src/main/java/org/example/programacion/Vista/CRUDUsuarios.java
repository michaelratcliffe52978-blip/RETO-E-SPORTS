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
import org.example.programacion.DAO.AdminDAO;
import org.example.programacion.Modelo.Usuario;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CRUDUsuarios implements Initializable {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colId;
    @FXML private TableColumn<Usuario, String> colUser;
    @FXML private TableColumn<Usuario, String> colRol;

    @FXML private TextField txtId, txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRol;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private AdminDAO adminDAO = new AdminDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarDatosUsuarios();

        // Listener para seleccionar fila
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
            }
        });

        // Cargar roles disponibles
        comboRol.setItems(FXCollections.observableArrayList("admin", "user"));
    }

    private void cargarDatosUsuarios() {
        listaUsuarios.clear();
        listaUsuarios.addAll(adminDAO.getAllUsuarios());
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void rellenarFormulario(Usuario u) {
        txtId.setText(u.getIdUsuario());
        txtUsername.setText(u.getNombreUsuario());
        txtPassword.setText(u.getContrasena());
        comboRol.setValue(u.getTipo());
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || comboRol.getValue() == null) {
            mostrarAlerta("Error", "Username, Password y Rol son obligatorios.");
            return;
        }

        try {
            String id = txtId.getText();

            if (id == null || id.isEmpty()) {
                // Nuevo usuario
                adminDAO.insertUsuario(txtUsername.getText(), txtPassword.getText(), comboRol.getValue());
            } else {
                // Actualizar
                adminDAO.updateUsuario(Integer.parseInt(id), txtUsername.getText(), txtPassword.getText(), comboRol.getValue());
            }

            cargarDatosUsuarios();
            onLimpiar();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID debe ser un número válido.");
        }
    }

    @FXML
    public void onEliminar() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Atención", "Selecciona un usuario para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar usuario " + sel.getNombreUsuario() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                adminDAO.deleteUsuario(Integer.parseInt(sel.getIdUsuario()));
                cargarDatosUsuarios();
                onLimpiar();
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onLimpiar() {
        txtId.clear();
        txtUsername.clear();
        txtPassword.clear();
        comboRol.setValue(null);
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    public void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
