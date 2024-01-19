package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesCreator {

    public static void main(String[] args) {
        Properties prop = new Properties();

        prop.setProperty("url", "jdbc:mysql://localhost:3306/chatdbschema");
        prop.setProperty("user", "root");
        prop.setProperty("password", "1234");

        try (FileOutputStream fos = new FileOutputStream("Server/src/main/resources/db.properties")) {
            prop.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}