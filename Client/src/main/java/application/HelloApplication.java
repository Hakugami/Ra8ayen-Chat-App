package application;

import dto.Model.UserModel;
import dto.requests.UpdateUserRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

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
        if (makeUserOffline()) {
            NetworkFactory.getInstance().disconnect(CurrentUser.getCurrentUser().getPhoneNumber(), CurrentUser.getInstance().getCallBackController());
            UnicastRemoteObject.unexportObject(CurrentUser.getCurrentUser().getCallBackController(), true);
        }

        Platform.exit();
    }

    private boolean makeUserOffline() {
        try {
            UserModel userModel = getUserModel();
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setUserModel(userModel);
            return NetworkFactory.getInstance().updateUser(updateUserRequest).isUpdated();
        } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
            return false;
        }
    }

    private UserModel getUserModel() {
        UserModel userModel = new UserModel();
        userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
        userModel.setUserStatus(UserModel.UserStatus.Offline);
        userModel.setUsermode(UserModel.UserMode.Away);
        return userModel;
    }
}