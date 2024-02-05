package view;

import controller.*;
import javafx.scene.layout.HBox;
import model.Chat;
import token.TokenManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Model;
import network.NetworkFactory;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ViewFactory {
    private final StringProperty selectedMenuItem;
    private final ObjectProperty<Chat> selectedContact;
    private BorderPane mainArea;


    public ViewFactory() {
        this.selectedMenuItem = new SimpleStringProperty("");
        this.selectedContact = new SimpleObjectProperty<>();
    }

    public StringProperty getSelectedMenuItem() {
        return selectedMenuItem;
    }
    public ObjectProperty<Chat> getSelectedContact() {
        return selectedContact;
    }

    public BorderPane getMainArea() {
        if (mainArea == null) {
            try {
                mainArea = new FXMLLoader(getClass().getResource("/fxml/Home/MainWindow.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mainArea;
    }

    public BorderPane autoLogin() throws NotBoundException, RemoteException {
        NetworkFactory.getInstance().getUserModel(TokenManager.getInstance().getToken());
        return getMainArea();
    }

    public void showProfile() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Profile.fxml"));
        createStage(loader);
    }

    public void showProfileContextMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileContextMenu.fxml"));
        createStage(loader);
    }

    public Node getProfile() {
        try {
            return new FXMLLoader(getClass().getResource("/fxml/NavigationBar/Profile.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showSettingsContextMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/SettingsContextMenu.fxml"));
        createStage(loader);
    }


    public void showLoginWindow() throws NotBoundException, RemoteException {

//        LoginController client = new LoginController();
//        client.connectToServer();
//        client.startTrackingOnlineUsers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Authentication/Login.fxml"));
        createStage(loader);
    }

    public void showHomeWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Home/Home.fxml"));
        createStage(loader);
    }

    public void showContacts() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/Contacts.fxml"));
        ContactsController contactsController = Model.getInstance().getControllerFactory().getContactsController();
        loader.setController(contactsController);
        createStage(loader);
    }

    public void showChatWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat/Chat.fxml"));
        createStage(loader);
    }

    public void showThemes() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/Themes.fxml"));
        createStage(loader);
    }

    public Node getThemes() {
        try {
            return new FXMLLoader(getClass().getResource("/fxml/NavigationBar/Themes.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showRegisterWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Authentication/Register.fxml"));
        createStage(loader);
    }


    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Chat App");
        stage.show();
    }

    public Node getCalls() {
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Calls.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getAddContact() {
        try {
            return new FXMLLoader(getClass().getResource("/fxml/Contacts/AddContact.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getContacts() {
        try {
            return new FXMLLoader(getClass().getResource("/Fxml/Contacts/Contacts.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getSettings() {
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Settings.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getRegister() {
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Authentication/Register.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getLogin() {
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Authentication/Login.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getChat() {
        try {
            return new FXMLLoader(getClass().getResource("/Fxml/Chat/Chat.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public Node getContactElement() throws IOException {
        return new FXMLLoader(getClass().getResource("/Fxml/Contacts/ContactElement.fxml")).load();
    }

    public Node getUpdateProfile() {
        try {
            return new FXMLLoader(getClass().getResource("/fxml/NavigationBar/UpdateProfile.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public HBox getCustomizeLabels() throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/Chat/customizeText.fxml")).load();
    }

    public Node getVoiceChatPopUp() {
        try {
            return new FXMLLoader(getClass().getResource("/fxml/Chat/VoiceChatPopUp.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getVoiceChatWait() {
        try {
            return new FXMLLoader(getClass().getResource("/fxml/Chat/VoiceChatWait.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
