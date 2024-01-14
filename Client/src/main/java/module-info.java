module org.example.client {
    requires Shared;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;


    opens org.example.client to javafx.fxml;
    exports org.example.client;
    exports controller to javafx.fxml;
    opens controller to javafx.fxml;
}