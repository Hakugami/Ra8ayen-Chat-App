package application;

import controller.Logout;
import javafx.application.Application;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    @Override
    public void stop() throws RemoteException, NotBoundException {
        NetworkFactory.getInstance().disconnect(CurrentUser.getCurrentUser().getPhoneNumber(), CurrentUser.getInstance().getCallBackController());
        UnicastRemoteObject.unexportObject(CurrentUser.getCurrentUser().getCallBackController(), true);
    }
}