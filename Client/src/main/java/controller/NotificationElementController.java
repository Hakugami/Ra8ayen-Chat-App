package controller;

import dto.Model.UserModel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class NotificationElementController {
    public Button acceptButton;
    public Button refuseButton;
    public Label Name;
    public Circle imageClip;
    public ImageView ImagId;

    public void setData(UserModel userModel) {
        Name.setText(userModel.getUserName());
        // Set other UI elements based on the userModel data
    }
}