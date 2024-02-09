package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class BlockedContacElementController {

    @FXML
    Label NameLabel;

    @FXML
    Label phoneNumberLabel;

    @FXML
    Button deleteButton;

    @FXML
    AnchorPane root;
    @FXML
    void initialize(){

    }
    void setNameLabel(String name){
        NameLabel.setText(name);
    }
    void setPhoneNumberLabel(String phoneNumber){
        phoneNumberLabel.setText(phoneNumber);
    }
    Button getDeleteButton(){
        return deleteButton;
    }
    AnchorPane getView(){
        return root;
    }
}
