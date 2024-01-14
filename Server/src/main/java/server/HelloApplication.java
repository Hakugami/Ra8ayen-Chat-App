package server;

import controllers.DataModel;
import controllers.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    public static boolean isServerRunning = false;

    @Override
    public void start(Stage stage) throws IOException {
        DataModel dataModel = new DataModel();
        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/Fxml/Server.fxml"));
        rootLoader.load();
        ServerController serverController = rootLoader.getController();
        serverController.initModel(dataModel);
        serverController.setSubSceneInitialNode();

        Scene scene = new Scene(rootLoader.getRoot());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}