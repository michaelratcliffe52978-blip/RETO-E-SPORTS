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
import org.example.programacion.Modelo.Usuarios;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clase controladora para la vista de gestión de Usuarios del sistema.
 * Permite a los administradores crear, modificar y eliminar cuentas de acceso,
 * asignando roles específicos (admin/user) a cada registro.
 * * @author equipo4
 * @version 1.0
 */
public class CRUDUsuarios implements Initializable {

    // --- ELEMENTOS DE LA TABLA ---
    @FXML private TableView<Usuarios> tablaUsuarios;
    @FXML private TableColumn<Usuarios, String> colId;
    @FXML private TableColumn<Usuarios, String> colUser;
    @FXML private TableColumn<Usuarios, String> colRol;

    // --- ELEMENTOS DEL FORMULARIO ---
    @FXML private TextField txtId, txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRol;

    /** Lista observable que mantiene los usuarios en sincronía con la TableView */
    private ObservableList<Usuarios> listaUsuarios = FXCollections.observableArrayList();

    /** DAO encargado de las operaciones de persistencia de administración */
    private AdminDAO adminDAO = new AdminDAO();

    /**
     * Inicializa el controlador vinculando las columnas con el modelo Usuarios,
     * cargando la lista inicial y definiendo los roles disponibles en el sistema.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mapeo de columnas con atributos de la clase Usuarios
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarDatosUsuarios();

        // Listener: Detecta cambios en la selección de la tabla para editar usuarios
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
            }
        });

        // Definición de roles permitidos en la aplicación
        comboRol.setItems(FXCollections.observableArrayList("admin", "user"));
    }

    /**
     * Recupera todos los usuarios desde la base de datos y refresca la tabla.
     */
    private void cargarDatosUsuarios() {
        try {
            listaUsuarios.clear();
            listaUsuarios.addAll(adminDAO.getAllUsuarios());
            tablaUsuarios.setItems(listaUsuarios);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron obtener los usuarios: " + e.getMessage());
        }
    }

    /**
     * Vuelca la información de un usuario seleccionado en los campos del formulario.
     * @param u Usuario a editar.
     */
    private void rellenarFormulario(Usuarios u) {
        txtId.setText(u.getIdUsuario());
        txtUsername.setText(u.getNombreUsuario());
        txtPassword.setText(u.getContrasena());
        comboRol.setValue(u.getTipo());
    }

    /**
     * Procesa la acción de guardar. Si el ID es inexistente, inserta un nuevo registro;
     * si el ID existe, actualiza el usuario actual.
     * @param event Evento de acción del botón.
     */
    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || comboRol.getValue() == null) {
            mostrarAlerta("Campos Requeridos", "Username, Password y Rol son campos obligatorios.");
            return;
        }

        try {
            String id = txtId.getText();

            if (id == null || id.isEmpty()) {
                // Lógica para inserción de nuevo usuario
                adminDAO.insertUsuario(txtUsername.getText(), txtPassword.getText(), comboRol.getValue());
            } else {
                // Lógica para actualización de usuario existente
                adminDAO.updateUsuario(Integer.parseInt(id), txtUsername.getText(), txtPassword.getText(), comboRol.getValue());
            }

            cargarDatosUsuarios();
            onLimpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "El ID debe ser un valor numérico válido.");
        } catch (Exception e) {
            mostrarAlerta("Error de Persistencia", "No se pudo completar la operación: " + e.getMessage());
        }
    }

    /**
     * Elimina el usuario seleccionado de la base de datos previa confirmación.
     */
    @FXML
    public void onEliminar() {
        Usuarios sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Selección vacía", "Debe seleccionar un usuario de la lista.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar al usuario " + sel.getNombreUsuario() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                adminDAO.deleteUsuario(Integer.parseInt(sel.getIdUsuario()));
                cargarDatosUsuarios();
                onLimpiar();
            } catch (Exception e) {
                mostrarAlerta("Error", "El usuario no pudo ser eliminado: " + e.getMessage());
            }
        }
    }

    /**
     * Limpia el formulario de entrada y deselecciona cualquier fila de la tabla.
     */
    @FXML
    public void onLimpiar() {
        txtId.clear();
        txtUsername.clear();
        txtPassword.clear();
        comboRol.setValue(null);
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    /**
     * Cambia la escena actual para regresar al menú de gestión CRUD.
     * @param event Evento del botón Volver.
     */
    @FXML
    public void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra diálogos de información o error al usuario.
     * @param titulo Título del diálogo.
     * @param msg Mensaje descriptivo.
     */
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}