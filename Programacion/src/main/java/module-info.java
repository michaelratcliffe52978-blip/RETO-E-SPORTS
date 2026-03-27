module org.example.programacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.programacion to javafx.fxml;
    exports org.example.programacion;
}