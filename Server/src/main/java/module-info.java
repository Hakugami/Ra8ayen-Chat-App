module server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.dashicons;
    requires java.rmi;
    requires mysql.connector.j;
    requires java.sql;

    opens server to javafx.fxml;
    exports server;
}