module org.example.client {
    requires Shared;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires com.jfoenix;
    requires com.fasterxml.jackson.databind;

    opens org.example.client to javafx.fxml;
    exports org.example.client;
    exports controller to javafx.fxml;
    exports model;
    opens controller to javafx.fxml;
}