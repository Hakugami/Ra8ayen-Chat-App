package controller;

import dto.requests.AddContactRequest;
import dto.responses.AddContactResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class AddContactElementController implements Initializable {
    public Circle circlePic;
    public Label nameLabel;
    public Button deleteButton;
    @FXML
    public Label phoneNumber;

    private Node parentNode;
    private AddContactController addContactController;
    private AddGroupGroupController addGroupGroupController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteButton.setOnAction(event -> {
            if(addContactController != null) {
                addContactController.removeFromList(phoneNumber.getText());
                addContactController.contactsToAdd.remove(parentNode);
            }
            else {
                addGroupGroupController.removeFromListGroups(phoneNumber.getText());
                addGroupGroupController.contactsToAdd.remove(parentNode);
            }
//            addContactController.removeFromList(phoneNumber.getText());
//            addContactController.contactsToAdd.remove(parentNode);
        });
    }

    public void setData(String name , byte [] arr , String phone, Node parentNode, AddContactController addContactController){
        nameLabel.setText(name);
        BufferedImage img = ImageUtls.convertByteToImage(arr);
        Image image = SwingFXUtils.toFXImage(img, null);
        circlePic.setFill(new ImagePattern(image));
        phoneNumber.setText(phone);
        this.parentNode = parentNode;
        this.addContactController = addContactController;
    }

    public void setDataGroup(String userName, byte[] profilePicture, String phoneNumber, Parent root, AddGroupGroupController addGroupGroupController) {
        nameLabel.setText(userName);
        BufferedImage img = ImageUtls.convertByteToImage(profilePicture);
        Image image = SwingFXUtils.toFXImage(img, null);
        circlePic.setFill(new ImagePattern(image));
        this.phoneNumber.setText(phoneNumber);
        this.parentNode = root;
        this.addGroupGroupController = addGroupGroupController;
    }
}