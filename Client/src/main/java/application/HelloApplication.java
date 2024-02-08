package application;

import concurrency.manager.ConcurrencyManager;
import dto.Model.UserModel;
import dto.requests.UpdateUserRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import org.controlsfx.control.Notifications;
import token.TokenManager;

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
        String[] data = TokenManager.getInstance().loadData();
        if(data==null|| data.length < 3){
            Model.getInstance().getViewFactory().showLoginWindow();
            return;
        }
        int checkBit = Integer.parseInt(data[2]);
        if(checkBit==1 ){
            if(!NetworkFactory.getInstance().checkToken(data[0])){
                TokenManager.getInstance().truncateToken();
                Model.getInstance().getViewFactory().showLoginWindow();
            }
            else{
                Model.getInstance().getViewFactory().autoLogin();
            }
        }
        else{
        Model.getInstance().getViewFactory().showLoginWindow();
        }
    }

    @Override
    public void stop() throws RemoteException, NotBoundException {
        if (makeUserOffline()) {
            String[] data = TokenManager.getInstance().loadData();
            String write = data[0] + "\n" + data[1] + "\n" +1;
            TokenManager.getInstance().setToken(write);
            NetworkFactory.getInstance().disconnect(CurrentUser.getCurrentUser().getPhoneNumber(), CurrentUser.getInstance().getCallBackController());
            UnicastRemoteObject.unexportObject(CurrentUser.getCurrentUser().getCallBackController(), true);
            ConcurrencyManager.getInstance().forceShutdown();
        }
        Platform.exit();
    }


    public static void disconnectUser() throws RemoteException, NotBoundException {
        if (makeUserOffline()) {
            NetworkFactory.getInstance().disconnect(CurrentUser.getCurrentUser().getPhoneNumber(), CurrentUser.getInstance().getCallBackController());
            ConcurrencyManager.getInstance().forceShutdown();
        }

    }

    private static boolean makeUserOffline() {
        try {
            UserModel userModel = getUserModel();
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setUserModel(userModel);
            return NetworkFactory.getInstance().updateUser(updateUserRequest).isUpdated();
        } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
            return false;
        }
    }

    private static UserModel getUserModel() {
        UserModel userModel = new UserModel();
        userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
        userModel.setUserStatus(UserModel.UserStatus.Offline);
        userModel.setUsermode(UserModel.UserMode.Away);
        return userModel;
    }
}