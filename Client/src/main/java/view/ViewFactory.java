package view;

import controller.ContactsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class ViewFactory {
    private final StringProperty selectedMenuItem;
    private BorderPane mainArea;

    public ViewFactory() {
        this.selectedMenuItem = new SimpleStringProperty("");
    }

    public StringProperty getSelectedMenuItem() {
        return selectedMenuItem;
    }

    public BorderPane getMainArea() {
        if (mainArea == null) {
            try {
                mainArea = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mainArea;
    }

    public void showProfile() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Profile.fxml"));
        createStage(loader);
    }
    public void showProfileContextMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ProfileContextMenu.fxml"));
        createStage(loader);
    }

    public Node getProfile() {
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Profile.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showSettingsContextMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SettingsContextMenu.fxml"));
        createStage(loader);
    }


    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Authentication/Login.fxml"));
        createStage(loader);
    }

    public void showContacts() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Contacts.fxml"));
        ContactsController contactsController = new ContactsController();
        loader.setController(contactsController);
        createStage(loader);
    }

    public void showChatWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ChatWindow.fxml"));
        createStage(loader);
    }

    public void showThemes() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Themes.fxml"));
        createStage(loader);
    }
    public Node getThemes(){
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Themes.fxml")).load();
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
            return new FXMLLoader(getClass().getResource("/FXML/AddContact.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getContacts() {
        try {
            return new FXMLLoader(getClass().getResource("/FXML/Contacts.fxml")).load();
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
    public Node getRegister(){
        try{
            return new FXMLLoader(getClass().getResource("/FXML/Authentication/Register.fxml")).load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Node getLogin(){
        try{
            return new FXMLLoader(getClass().getResource("/FXML/Authentication/Login.fxml")).load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
