module org.example.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.client to javafx.fxml;
    exports org.example.client;
}