package view;

import controller.ContactsController;
import dto.Model.MessageModel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import model.Chat;
import model.CurrentUser;
import model.Model;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

public class ViewFactory {
    private final StringProperty selectedMenuItem;
    private final ObjectProperty<Chat> selectedContact;
    private BorderPane mainArea;

    private static final Logger LOGGER = Logger.getLogger(ViewFactory.class.getName());

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

            try {
                mainArea = new FXMLLoader(getClass().getResource("/fxml/Home/MainWindow.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return mainArea;
    }

    public void autoLogin(){
        Model.getInstance().getControllerFactory().getLoginController().autoLoginTransition();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Authentication/Register.fxml"));
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
        stage.setTitle("Ra8ayen");
        stage.getIcons().add(Model.getInstance().getIcon());
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
            return new FXMLLoader(getClass().getResource("/fxml/Contacts/Contacts.fxml")).load();
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

    public AnchorPane getCustomizeFontStyle() throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/Chat/CustomizeFontStyle.fxml")).load();
    }

    public AnchorPane getAddContactElement() throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/Contacts/AddContactElement.fxml")).load();
    }


    public void refreshLatestMessages() {
        try {
            // Loop over each group
            CurrentUser.getInstance().getGroupList().stream().forEach(group -> {
                // Get the messages for the group
                List<MessageModel> messages = null;
                try {
                    messages = CurrentUser.getInstance().getChatMessageMap().get(group.getGroupId());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                if (messages != null && !messages.isEmpty()) {
                    // Set the last message of the group
                    group.setLastMessage(messages.getLast().getMessageContent());
                }
            });

            CurrentUser.getInstance().getContactDataList().stream().forEach(contactData -> {
                // Get the messages for the contact data
                List<MessageModel> messages = null;
                try {
                    messages = CurrentUser.getInstance().getChatMessageMap().get(contactData.getChatId());
                    if (messages == null || messages.isEmpty()) {
                        LOGGER.info("No messages found for chatId: " + contactData.getChatId());
                    } else {
                        LOGGER.info("Messages found for chatId: " + contactData.getChatId() + " Number of messages: " + messages.size());
                        // Set the last message of the contact data
                        contactData.setLastMessage(messages.get(messages.size() - 1).getMessageContent());
                    }
                } catch (RemoteException e) {
                    LOGGER.severe("RemoteException occurred while retrieving messages for chatId: " + contactData.getChatId() + " Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });

            // Update the tree view data in the ContactsController on the JavaFX Application Thread
            Platform.runLater(() -> {
                try {
                    Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public AnchorPane getBlockedScreen(){
        try {
            return new FXMLLoader(getClass().getResource("/fxml/NavigationBar/BlockedContacts.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Node getBlockedElementScreen(){
        try {
            return new FXMLLoader(getClass().getResource("/fxml/NavigationBar/BlockedContactElement.fxml")).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Window getStage() {
        return mainArea.getScene().getWindow();
    }
}
