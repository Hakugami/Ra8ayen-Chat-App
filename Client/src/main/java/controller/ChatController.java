package controller;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;

public class ChatController {
    @FXML
    ImageView ImagContact;

    @FXML
    Circle imageClip;

    @FXML
    Label NameContact;
    @FXML
    public void initialize() {
        ImagContact.setFitWidth(imageClip.getRadius() * 2);
        ImagContact.setFitHeight(imageClip.getRadius() * 2);
        String imagePath = "/images/persontwo.jpg";

        // Get the URL of the image
        URL imageUrl = ContactElementController.class.getResource(imagePath);
        if(imageUrl!=null) {
            System.out.println(imageUrl);
            Image newImage = new Image(imageUrl.toString());
            ImagContact.setImage(newImage);
        }else{
            System.out.println("Null data found");
        }
        NameContact.setText("Reem Osama");
    }
}
