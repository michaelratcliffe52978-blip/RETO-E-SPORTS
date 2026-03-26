module com.example.esport {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.esport to javafx.fxml;
    exports com.example.esport;
}