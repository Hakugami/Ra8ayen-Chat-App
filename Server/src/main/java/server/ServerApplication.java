package server;

import concurrency.manager.ConcurrencyManager;
import controllers.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.manager.NetworkManagerSingleton;
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

    @Override
    public void stop() throws Exception {
        super.stop();

        if(NetworkManagerSingleton.getInstance().isServerRunning()) {
            NetworkManagerSingleton.getInstance().stop();
        }

        ConcurrencyManager.getInstance().shutdown();

        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}