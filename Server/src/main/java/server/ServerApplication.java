package server;

import concurrency.manager.ConcurrencyManager;
import controllers.OnlineControllerImpl;
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
import model.entities.User;
import network.manager.NetworkManagerSingleton;
import service.UserService;
import userstable.UsersTableStateSingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServerApplication extends Application {
    public static Map<Scenes, Parent> scenes = new HashMap<>();
    public static Map<Scenes, Initializable> controllers = new HashMap<>();
    @Override
    public void init() throws Exception {
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
        stage.setTitle("Ra8ayen Server");
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/speak.png"))));
        ((ServerController)controllers.get(Scenes.SERVER)).setSubSceneInitialNode();
        stage.setResizable(true);
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

        disconnectUsers();

        NetworkManagerSingleton.getInstance().unexportRegistry();

        ConcurrencyManager.getInstance().shutdown();

        Platform.exit();

    }

    public static  void disconnectUsers(){
        OnlineControllerImpl.clients.forEach((k,v)->{
            try {
                v.logout();
                OnlineControllerImpl.getInstance().disconnect(k, v);
                User user = new UserService().getUserByPhoneNumber(k);
                user.setUserStatus(User.UserStatus.Offline);
                user.setUsermode(User.UserMode.Away);
                new UserService().updateUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}