package view;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

public class ControllerFactory {
    private ChatController chatController;
    private ContactsController contactsController;
    private StatusElementController statusElementController;
    private ContactElementController contactElementController;
    private MainWindowController mainWindowController;

    public ChatController getChatController() {
        if (chatController == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Chat/Chat.fxml"));
                Parent parent = fxmlLoader.load();
                chatController = fxmlLoader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return chatController;
    }

    public Node getStatusElement() throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/Contacts/StatusElement.fxml")).load();
    }

    public StatusElementController getStatusElementController() throws IOException {
        if(statusElementController == null){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Contacts/StatusElement.fxml"));
            Parent parent = fxmlLoader.load();
            statusElementController = fxmlLoader.getController();
        }
        return statusElementController;
    }

    public ContactsController getContactsController() {
        if (contactsController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Contacts/Contacts.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                contactsController = fxmlLoader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return contactsController;
    }
    public ContactElementController getContactElementController() throws IOException {
        if(contactElementController == null){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Contacts/ContactElement.fxml"));
            Parent parent = fxmlLoader.load();
            contactElementController = fxmlLoader.getController();
        }
        return contactElementController;
    }

    public MainWindowController getMainWindowController() throws IOException {
        if(mainWindowController == null){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Home/MainWindow.fxml"));
            Parent parent = fxmlLoader.load();
            mainWindowController = fxmlLoader.getController();
        }
        return mainWindowController;
    }

}
