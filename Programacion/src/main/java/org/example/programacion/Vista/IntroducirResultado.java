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
import java.sql.*;
import java.util.ResourceBundle;

import org.example.programacion.Util.ConexionBD;

public class IntroducirResultado implements Initializable {

    @FXML private ChoiceBox<String> Equipo1;
    @FXML private ChoiceBox<String> Equipo2;
    @FXML private ChoiceBox<String> Resultado1;
    @FXML private ChoiceBox<String> Resultado2;

    private ObservableList<String> todasLasOpciones = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Cargar datos de la tabla EQUIPO
        cargarDatosDesdeBD();

        // 2. Rellenar puntuaciones del 0 al 14
        ObservableList<String> numeros = FXCollections.observableArrayList();
        for (int i = 0; i <= 14; i++) {
            numeros.add(String.valueOf(i));
        }
        Resultado1.setItems(numeros);
        Resultado2.setItems(numeros);

        // 3. Lógica para que no se pueda elegir el mismo equipo en ambos lados
        configurarFiltroEquipos();
    }

    private void cargarDatosDesdeBD() {
        todasLasOpciones.clear();
        // CORRECCIÓN: Nombre de tabla 'EQUIPO' y columna 'NOMBRE_EQUIPO'
        String query = "SELECT NOMBRE_EQUIPO FROM EQUIPO";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // CORRECCIÓN: Debe coincidir con el nombre de la columna en SQL
                todasLasOpciones.add(rs.getString("NOMBRE_EQUIPO"));
            }

            Equipo1.setItems(FXCollections.observableArrayList(todasLasOpciones));
            Equipo2.setItems(FXCollections.observableArrayList(todasLasOpciones));

        } catch (SQLException e) {
            System.err.println("Error al obtener equipos de la base de datos:");
            e.printStackTrace();
        }
    }

    private void configurarFiltroEquipos() {
        Equipo1.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                String seleccionadoEn2 = Equipo2.getValue();
                ObservableList<String> filtro = FXCollections.observableArrayList(todasLasOpciones);
                filtro.remove(nuevo);
                Equipo2.setItems(filtro);

                if (filtro.contains(seleccionadoEn2)) {
                    Equipo2.setValue(seleccionadoEn2);
                }
            }
        });
    }

    @FXML
    public void onEnviar(ActionEvent event) {
        String nombreE1 = Equipo1.getValue();
        String nombreE2 = Equipo2.getValue();
        String marcador1 = Resultado1.getValue();
        String marcador2 = Resultado2.getValue();

        // 1. Buscamos los IDs de los equipos y del partido
        // Necesitamos el ID del equipo 1, el ID del equipo 2 y el ID del enfrentamiento
        String sqlBusqueda = "SELECT p.ID_PARTIDO, e1.ID_EQUIPO as ID1, e2.ID_EQUIPO as ID2 " +
                "FROM ENFRENTAMIENTO p " +
                "JOIN EQUIPO e1 ON e1.NOMBRE_EQUIPO = p.EQUIPO1 " +
                "JOIN EQUIPO e2 ON e2.NOMBRE_EQUIPO = p.EQUIPO2 " +
                "WHERE p.EQUIPO1 = ? AND p.EQUIPO2 = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBusqueda)) {

            pstmt.setString(1, nombreE1);
            pstmt.setString(2, nombreE2);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int idP = rs.getInt("ID_PARTIDO");
                int idE1 = rs.getInt("ID1");
                int idE2 = rs.getInt("ID2");

                // 2. Guardamos en la tabla intermedia
                actualizarResultadoIntermedia(idP, idE1, marcador1, idE2, marcador2);

                navegarAMenu(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizarResultadoIntermedia(int idPartido, int idEq1, String res1, int idEq2, String res2) {
        // Usamos MERGE (o UPSERT) para que si ya existe lo actualice, y si no, lo cree
        String sql = "MERGE INTO Equipo_Enfrentamiento t " +
                "USING (SELECT ? as id_e, ? as id_p, ? as res FROM dual) s " +
                "ON (t.id_equipo = s.id_e AND t.id_partido = s.id_p) " +
                "WHEN MATCHED THEN UPDATE SET t.resultado = s.res " +
                "WHEN NOT MATCHED THEN INSERT (id_equipo, id_partido, resultado) VALUES (s.id_e, s.id_p, s.res)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Insertar/Actualizar para el primer equipo
            pstmt.setInt(1, idEq1);
            pstmt.setInt(2, idPartido);
            pstmt.setString(3, res1);
            pstmt.executeUpdate();

            // Insertar/Actualizar para el segundo equipo
            pstmt.setInt(1, idEq2);
            pstmt.setInt(2, idPartido);
            pstmt.setString(3, res2);
            pstmt.executeUpdate();

            System.out.println("Relaciones creadas/actualizadas en Equipo_Enfrentamiento.");

        } catch (SQLException e) {
            System.err.println("Error en tabla intermedia: " + e.getMessage());
        }
    }

    private void navegarAMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/programacion/MenuAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar el menú principal.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onAtras(ActionEvent event) {
        navegarAMenu(event);
    }
}