package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import model.CurrentUser;

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/updateProfile.fxml"));
                Parent root = loader.load();
                Popup popup = new Popup();
                popup.getContent().add(root);
                popup.setAutoHide(true);

                // Adjust the x and y coordinates to position the popup correctly
                double x = UpdateProfile.getScene().getWindow().getX() + UpdateProfile.getScene().getX() + UpdateProfile.getWidth() / 2;
                double y = UpdateProfile.getScene().getWindow().getY() + UpdateProfile.getScene().getY() + UpdateProfile.getHeight() / 2;

                popup.show(UpdateProfile.getScene().getWindow(), x, y);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}