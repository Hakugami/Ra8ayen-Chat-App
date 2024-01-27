package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import model.Model;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideNavBarController implements Initializable {
    public Text Nav_lbl;
    public Button contacts_btn;
    public Button calls_btn;
    public Button profile_btn;
    @FXML
    public Button settings_btn;
    public Button logout_btn;
    @FXML
    private MenuItem profileMenuItem;
    @FXML
    private MenuItem themesMenuItem;
    @FXML
    private ContextMenu settingsContextMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        // Add action listeners to the menu items
        profileMenuItem.setOnAction(event -> {
            // Handle profile item click
            System.out.println("Profile menu item clicked");
            // Here you can add the logic for when the profile menu item is clicked
            // For example, you can open the profile window or load the profile data
        });

        themesMenuItem.setOnAction(event -> {
            // Handle themes item click
            System.out.println("Themes menu item clicked");
            // Here you can add the logic for when the themes menu item is clicked
            // For example, you can open a window that allows the user to change the theme
        });

        settings_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/SettingsContextMenu.fxml"));
                Parent root = fxmlLoader.load();
                Popup popup = new Popup();
                popup.getContent().add(root);
                popup.setAutoHide(true);
                //make it appear at the top of the button
                double x = settings_btn.getScene().getWindow().getX() + settings_btn.getScene().getX() + settings_btn.getWidth() / 2;
                double y = settings_btn.getScene().getWindow().getY() + settings_btn.getScene().getY() + settings_btn.getHeight() / 2;
                popup.show(settings_btn.getScene().getWindow(), x, y+500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addListeners (){
        contacts_btn.setOnAction(event -> onContactsClick());
        calls_btn.setOnAction(event -> onCallsClick());
    }

    private void onCallsClick() {
        Model.getInstance().getViewFactory().getSelectedMenuItem().setValue("Calls");
    }

    private void onContactsClick(){
        Model.getInstance().getViewFactory().getSelectedMenuItem().setValue("Contacts");

    }
}