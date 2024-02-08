package server;

import concurrency.manager.ConcurrencyManager;
import controllers.Scenes;
import controllers.ServerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.manager.NetworkManagerSingleton;
import service.UserService;
import userstable.UsersTableStateSingleton;

import java.util.HashMap;
import java.util.Map;

public class ServerApplication extends Application {
    public static Map<Scenes, Parent> scenes = new HashMap<>();
    public static Map<Scenes, Initializable> controllers = new HashMap<>();
    @Override
    public void init() throws Exception {
        UserService userService = new UserService();
        UsersTableStateSingleton.getInstance().setUsers(FXCollections.observableArrayList(userService.getAllUsers()));
        for (Scenes scene : Scenes.values()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scene.getFxmlPath()));
            scenes.put(scene, loader.load());
            controllers.put(scene, loader.getController());
        }
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(scenes.get(Scenes.SERVER));
        stage.setScene(scene);
        ((ServerController)controllers.get(Scenes.SERVER)).setSubSceneInitialNode();
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if(NetworkManagerSingleton.getInstance().isServerRunning() && !NetworkManagerSingleton.getInstance().isFirsTimeStart()) {
            NetworkManagerSingleton.getInstance().stop();
        }
        else if (NetworkManagerSingleton.getInstance().isFirsTimeStart()){
            NetworkManagerSingleton.getInstance().unexportStubsOnExit();
        }

        NetworkManagerSingleton.getInstance().unexportRegistry();

        ConcurrencyManager.getInstance().shutdown();

        Platform.exit();
    }

    public static void main(String[] args) {
        launch();
    }
}