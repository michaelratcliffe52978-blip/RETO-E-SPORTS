package org.example.programacion.Controladores;

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
 * Esta es la clase que manda en la pantalla de admin.
 * Sirve para manejar a los usuarios (crear, borrar, editar y verlos en la tabla).
 * * @author equipo4
 * @version 1.0
 */
public class AdminController implements Initializable {

    // Cosas de la interfaz (FXML)
    @FXML private TableView<Usuarios> tablaUsuarios;
    @FXML private TableColumn<Usuarios, String> colId;
    @FXML private TableColumn<Usuarios, String> colUser;
    @FXML private TableColumn<Usuarios, String> colRol;

    @FXML private TextField txtId, txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRol;

    /** La lista donde guardamos a los usuarios para que se vean en la tabla */
    private ObservableList<Usuarios> listaUsuarios = FXCollections.observableArrayList();

    /** El objeto que conecta con la base de datos */
    private AdminDAO adminDAO = new AdminDAO();

    /**
     * Esto se ejecuta nada más abrir la ventana.
     * Prepara las columnas de la tabla y rellena los datos.
     * * @param url La ruta del archivo FXML
     * @param rb El paquete de recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Enlazamos las columnas con los datos de la clase Usuarios
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarDatosUsuarios();

        // Si el usuario hace clic en una fila de la tabla, se rellenan los cuadros de texto
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                rellenarFormulario(nuevo);
            }
        });

        // Ponemos las opciones de "admin" y "user" en el desplegable
        comboRol.setItems(FXCollections.observableArrayList("admin", "user"));
    }

    /**
     * Limpia la lista y vuelve a traer a todos los usuarios de la base de datos.
     */
    private void cargarDatosUsuarios() {
        listaUsuarios.clear();
        listaUsuarios.addAll(adminDAO.getAllUsuarios());
        tablaUsuarios.setItems(listaUsuarios);
    }

    /**
     * Coge los datos de un usuario y los pone en los huecos de escribir.
     * * @param u El usuario que hemos seleccionado
     */
    private void rellenarFormulario(Usuarios u) {
        txtId.setText(u.getIdUsuario());
        txtUsername.setText(u.getNombreUsuario());
        txtPassword.setText(u.getContrasena());
        comboRol.setValue(u.getTipo());
    }

    /**
     * Se activa al darle al botón Guardar.
     * Si no hay ID, crea un usuario nuevo; si lo hay, lo actualiza.
     * * @param event El clic del botón
     */
    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || comboRol.getValue() == null) {
            mostrarAlerta("Error", "Oye, rellena el nombre, la contra y el rol.");
            return;
        }

        try {
            String id = txtId.getText();

            if (id == null || id.isEmpty()) {
                // Si el ID está vacío, es que es nuevo
                adminDAO.insertUsuario(txtUsername.getText(), txtPassword.getText(), comboRol.getValue());
            } else {
                // Si ya tiene ID, es que estamos editando uno viejo
                adminDAO.updateUsuario(Integer.parseInt(id), txtUsername.getText(), txtPassword.getText(), comboRol.getValue());
            }

            cargarDatosUsuarios();
            onLimpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID tiene que ser un número, no metas letras.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Vaya, ha fallado algo al guardar.");
        }
    }

    /**
     * Borra al usuario que hayas marcado en la tabla, pero antes te pregunta.
     */
    @FXML
    public void onEliminar() {
        Usuarios sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Atención", "Primero elige a alguien de la tabla para borrarlo.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quieres cargarme al usuario " + sel.getNombreUsuario() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                adminDAO.deleteUsuario(Integer.parseInt(sel.getIdUsuario()));
                cargarDatosUsuarios();
                onLimpiar();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se ha podido borrar el usuario.");
            }
        }
    }

    /**
     * Borra todo lo que hayas escrito en los cuadros de texto.
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
     * Te manda de vuelta a la pantalla del menú anterior.
     * * @param event El clic del botón de volver
     */
    @FXML
    public void onVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Error al intentar volver atrás.");
        }
    }

    /**
     * Saca una ventanita de aviso para que el usuario se entere de algo.
     * * @param titulo Lo que sale arriba de la ventana
     * @param msg El mensaje que quieres decir
     */
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}