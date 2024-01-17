package server;

import controllers.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/Fxml/Server.fxml"));
        rootLoader.load();
        ServerController serverController = rootLoader.getController();
        serverController.setSubSceneInitialNode();

        Scene scene = new Scene(rootLoader.getRoot());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}