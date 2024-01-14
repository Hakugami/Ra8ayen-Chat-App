module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.dashicons;
    requires java.rmi;
    requires mysql.connector.j;
    requires java.sql;

    opens org.example.demo1 to javafx.fxml;
    exports org.example.demo1;
}