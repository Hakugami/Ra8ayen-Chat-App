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
    private NotificationContextMenuController notificationContextMenuController;
    private VoiceChatPopUpController voiceChatPopUpController;
    private SideNavBarController sideNavBarController;
    private LoginController loginController;


    private CustomizeController customizeController;


    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
    public LoginController getLoginController() {
        if (loginController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login/Login.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                loginController = fxmlLoader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return loginController;
    }
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }
    public void setVoiceChatPopUpController(VoiceChatPopUpController voiceChatPopUpController) {
        this.voiceChatPopUpController = voiceChatPopUpController;
    }
    public void setSideNavBarController(SideNavBarController sideNavBarController) {
        this.sideNavBarController = sideNavBarController;
    }
    public boolean getSideNavBarControllerBot() {
        return sideNavBarController.isChatBotEnabled();
    }

    public VoiceChatPopUpController getVoiceChatPopUpController() throws IOException {
        if(voiceChatPopUpController == null){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/VoiceChatPopUp.fxml"));
            Parent parent = fxmlLoader.load();
            voiceChatPopUpController = fxmlLoader.getController();
        }
        return voiceChatPopUpController;
    }

    public ChatController getChatController() {
        if (chatController == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Chat/Chat.fxml"));
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

    public void setContactsController(ContactsController contactsController) {
        this.contactsController = contactsController;
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

    public void setNotificationContextMenuController(NotificationContextMenuController notificationContextMenuController) {
        this.notificationContextMenuController = notificationContextMenuController;
    }

    public NotificationContextMenuController getNotificationContextMenuController() {
        if (notificationContextMenuController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/NotificationContextMenu.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                notificationContextMenuController = fxmlLoader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return notificationContextMenuController;
    }
    public NotificationElementController getNotificationElementController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/NotificationElement.fxml"));
        Parent parent = fxmlLoader.load();
        return fxmlLoader.getController();
    }
    public CustomizeController getCustomizeController() throws IOException {
        if(customizeController==null){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Chat/customizeText.fxml"));
            Parent parent= fxmlLoader.load();
            customizeController = fxmlLoader.getController();
        }
        return customizeController;
    }

    public void setCustomizeController(CustomizeController customizeController) {
        this.customizeController = customizeController;
    }
}
