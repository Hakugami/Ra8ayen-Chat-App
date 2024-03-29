package controller;

import application.HelloApplication;
import dto.Model.NotificationModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import notification.NotificationManager;
import notification.NotificationSounds;
import token.TokenManager;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class SideNavBarController implements Initializable {
    public Button contacts_btn;
    @FXML
    public Button settings_btn;
    public Button logout_btn;
    @FXML
    public Button notificationButton;
    @FXML
    private MenuItem profileMenuItem;
    @FXML
    private MenuItem themesMenuItem;
    @FXML
    private ToggleButton chatBotToggleButton;

    @FXML
    private Button blockBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setSideNavBarController(this);
        addListeners();
        profileMenuItem.setOnAction(event -> {
            System.out.println("Profile menu item clicked");
        });

        themesMenuItem.setOnAction(event -> {

            System.out.println("Themes menu item clicked");

        });

        settings_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSettingsButton);
        notificationButton.setOnAction(this::handleNotification);
        logout_btn.setOnAction(event -> {
            try {
                handleLogout();
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }
        });

        blockBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleBlockButton);
        chatBotToggleButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleChatBotToggle);
        }

    public boolean isChatBotEnabled() {
        return chatBotToggleButton.isSelected();
    }
    private void addListeners (){
        contacts_btn.setOnAction(event -> onContactsClick());
    }

    private void onCallsClick() {
        Model.getInstance().getViewFactory().getSelectedMenuItem().setValue("Calls");
    }

    private void onContactsClick(){
        Model.getInstance().getViewFactory().getSelectedMenuItem().setValue("Contacts");

    }
private void handleLogout() throws NotBoundException, RemoteException {
    String[] data = TokenManager.getInstance().loadData();
    String write = data[0] + "\n" + data[1] + "\n" + 0;
    HelloApplication.disconnectUser();

    // Get the current stage and hide it
    Stage currentStage = (Stage) logout_btn.getScene().getWindow();
    currentStage.hide();

    Model.getInstance().getViewFactory().showLoginWindow();
    TokenManager.getInstance().setToken(write);
}

    private void handleNotification(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/NotificationContextMenu.fxml"));
            Parent root = fxmlLoader.load();
            Popup popup = new Popup();
            popup.getContent().add(root);
            popup.setAutoHide(true);
            //make it appear at the top of the button
            double x = notificationButton.getScene().getWindow().getX() + notificationButton.getScene().getX() + notificationButton.getWidth() / 2;
            double y = notificationButton.getScene().getWindow().getY() + notificationButton.getScene().getY() + notificationButton.getHeight() / 2;
            popup.show(notificationButton.getScene().getWindow(), x, y + 500);
        } catch (IOException e) {
            System.out.println("Failed to load notification context menu");
        }
    }

    private void handleSettingsButton(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/SettingsContextMenu.fxml"));
            Parent root = fxmlLoader.load();
            Popup popup = new Popup();
            popup.getContent().add(root);
            popup.setAutoHide(true);
            //make it appear at the top of the button
            double x = settings_btn.getScene().getWindow().getX() + settings_btn.getScene().getX() + settings_btn.getWidth() / 2;
            double y = settings_btn.getScene().getWindow().getY() + settings_btn.getScene().getY() + settings_btn.getHeight() / 2;
            popup.show(settings_btn.getScene().getWindow(), x, y + 500);
        } catch (IOException e) {
            System.out.println("Failed to load settings context menu");
        }
    }
    private void handleBlockButton(MouseEvent event){
        Popup popup = new Popup();
        Model.getInstance().getControllerFactory().getBlockedContactsController().setPopUp(popup);
        Model.getInstance().getControllerFactory().getBlockedContactsController().getData();
        popup.getContent().add(Model.getInstance().getViewFactory().getBlockedScreen());
        popup.setAutoHide(true);
        popup.show(blockBtn.getScene().getWindow(), event.getX()+100, event.getY());

        //  Model.getInstance().getViewFactory().getSelectedMenuItem().set("BlockedContact");
    }
    private void handleChatBotToggle(MouseEvent event){
        if (chatBotToggleButton.isSelected()){
            NotificationManager.getInstance().getNotificationSounds().playRobotSound();
        }else{
            NotificationManager.getInstance().getNotificationSounds().playRobotCloseSound();
        }
    }
}