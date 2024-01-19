module server {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.naming;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.dashicons;
    requires java.rmi;
    requires mysql.connector.j;
    requires java.sql;
    requires Shared;
    requires org.mapstruct;
    requires java.desktop;

    opens server to javafx.fxml;
    exports server;
    exports controllers to javafx.fxml;
    opens controllers to javafx.fxml;
}