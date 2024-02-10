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
            System.out.println("No token found");
            Model.getInstance().getViewFactory().showLoginWindow();
            return;
        }
        int checkBit = Integer.parseInt(data[2]);
        System.out.println("checkBit = " + checkBit);
        if(checkBit==1 ){
            if(!NetworkFactory.getInstance().checkToken(data[0])){
                System.out.println("Token is not valid");
                TokenManager.getInstance().truncateToken();
                Model.getInstance().getViewFactory().showLoginWindow();
            }
            else{
                System.out.println("Token is valid");
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


    public static void disconnectUser() throws RemoteException {
        if (makeUserOffline()) {
            NetworkFactory.getInstance().disconnect(CurrentUser.getCurrentUser().getPhoneNumber(), CurrentUser.getInstance().getCallBackController());
        }else {
            Platform.runLater(()-> Notifications.create().title("Error").text("Failed to disconnect user").showError());
        }

    }

    private static boolean makeUserOffline() {
        try {
            UserModel userModel = getUserModel();
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setUserModel(userModel);
            if(userModel==null){
                return false;
            }
            return NetworkFactory.getInstance().updateUser(updateUserRequest).isUpdated();
        } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
            return false;
        }
    }

    private static UserModel getUserModel() {
        UserModel userModel = new UserModel();
        if(CurrentUser.getCurrentUser()==null){
            return null;
        }
        userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
        userModel.setCountry(CurrentUser.getCurrentUser().getCountry());
        userModel.setPhoneNumber(CurrentUser.getCurrentUser().getPhoneNumber());
        userModel.setUserName(CurrentUser.getCurrentUser().getUserName());
        //userModel.setProfilePicture(CurrentUser.getCurrentUser().getProfilePicture());
        userModel.setUserStatus(UserModel.UserStatus.Offline);
        userModel.setUsermode(UserModel.UserMode.Away);
        userModel.setGender(CurrentUser.getCurrentUser().getGender());
        userModel.setDateOfBirth(CurrentUser.getCurrentUser().getDateOfBirth());
        userModel.setBio(CurrentUser.getCurrentUser().getBio());
        userModel.setEmailAddress(CurrentUser.getCurrentUser().getEmailAddress());
        return userModel;
    }
}