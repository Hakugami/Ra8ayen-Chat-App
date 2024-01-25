module org.example.client {
    requires Shared;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires com.jfoenix;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.swing;
    requires org.controlsfx.controls;

    exports model;
    opens model to javafx.fxml;
    opens controller to javafx.fxml;
    exports application;
    opens application to javafx.fxml;
    exports controller;
}