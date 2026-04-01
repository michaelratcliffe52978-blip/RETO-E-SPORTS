package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.programacion.Controladores.EnfrentamientoController;

import java.io.IOException;

public class MenuCRUD {
    public void onAtras(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onCrear(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuCrear.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void onLeer(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void onEliminar(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void onModificar(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Creamos una nueva escena con esa vista
            Scene scene = new Scene(root);

            // 3. Sacamos la "ventana" (Stage) actual a partir del botón que hemos pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Le ponemos la nueva escena a la ventana
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Si algo falla (ej. no encuentra el archivo), nos lo dirá por aquí
            System.out.println("Fallo al cargar la vista +" +
                    ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onGuardarEquipo(ActionEvent event) {
        // 1. Definimos la competición actual (por ahora 1)
        int idCompeticionActiva = 1;

        // 2. LLAMAMOS AL ESCUDO
        if (!EnfrentamientoController.esEstructuraModificable(idCompeticionActiva)) {
            // Si el método devuelve FALSE, mostramos error y cortamos con 'return'
            mostrarAlerta("Acceso Denegado", "La competición está CERRADA. No se permiten cambios.");
            return;
        }

        // 3. SI PASA EL IF, ENTONCES EJECUTAMOS EL CÓDIGO REAL
        System.out.println("Guardando equipo...");
        // Aquí iría tu: EquipoController.insertarEquipo(...);
    }

    // RECUERDA TENER EL MÉTODO mostrarAlerta EN ESTA CLASE TAMBIÉN
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
