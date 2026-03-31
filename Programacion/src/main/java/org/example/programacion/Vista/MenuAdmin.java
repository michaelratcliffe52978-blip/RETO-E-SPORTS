package org.example.programacion.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuAdmin {
    public void onCRUD(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuCRUD.fxml"));
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
    public void onGenerar(ActionEvent event) {
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
    public void onIntroducir(ActionEvent event) {
        try {

            // 1. Cargamos el archivo FXML de la Vista 2.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/IntroducirResultado.fxml"));
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
    public void onCerrar(ActionEvent event) {
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
}
