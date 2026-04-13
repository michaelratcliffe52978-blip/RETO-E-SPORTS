module org.example.programacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.programacion to javafx.fxml;
    exports org.example.programacion;


    opens org.example.programacion.Vista to javafx.fxml;
    exports org.example.programacion.Vista;


    opens org.example.programacion.Modelo to javafx.base, javafx.fxml;
    exports org.example.programacion.Modelo;
}