package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import model.CurrentUser;
import model.Model;

import java.rmi.RemoteException;

public class ProfileController {
    public Label phoneNumber;
    public Label gender;
    public Text userText;
    public Text phoneNumberText;
    public Text genderText;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button UpdateProfile;

    @FXML
    public void initialize() throws RemoteException {
        // Set the user's name
        userText.setText(CurrentUser.getInstance().getUserName());
        genderText.setText(CurrentUser.getInstance().getGender().name());
        phoneNumberText.setText(CurrentUser.getInstance().getPhoneNumber());

        // Add an action to the back button
        UpdateProfile.setOnAction(event -> {
            try {
                Model.getInstance().getViewFactory().getSelectedMenuItem().setValue("UpdateProfile");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}