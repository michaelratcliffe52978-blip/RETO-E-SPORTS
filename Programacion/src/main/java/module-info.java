module org.example.programacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ojdbc8;

    opens org.example.programacion to javafx.fxml;
    exports org.example.programacion;

    opens org.example.programacion.Vista to javafx.fxml;
    exports org.example.programacion.Vista;

    opens org.example.programacion.Controladores to javafx.fxml;
    exports org.example.programacion.Controladores;

    opens org.example.programacion.Modelo to javafx.base, javafx.fxml;
    exports org.example.programacion.Modelo;

    opens org.example.programacion.DAO to javafx.fxml;
    exports org.example.programacion.DAO;

    opens org.example.programacion.Utilidades to javafx.fxml;
    exports org.example.programacion.Utilidades;
}