package controller;

import dto.requests.AddContactRequest;
import dto.responses.AddContactResponse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import model.CurrentUser;
import network.NetworkFactory;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddContactController implements Initializable {
    @FXML
    public Label serverReply;
    @FXML
    private TextField phoneField;
    private Popup popup;

    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phoneField.setOnAction(event -> {
            AddContactRequest addContactRequest = new AddContactRequest();
            AddContactResponse addContactResponse ;
            String text = phoneField.getText();
            List<String> friendsPhoneNumbers = List.of(Arrays.stream(text.split(",")).map(String::trim).toArray(String[]::new));
            try {
                addContactRequest.setUserId(CurrentUser.getInstance().getUserID());
                addContactRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
                addContactResponse = NetworkFactory.getInstance().addContact(addContactRequest);
                if(addContactResponse.isDone()){
                    serverReply.setText("Contact added successfully");
                }
                else{
                    serverReply.setText("Contact not added");
                }
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println(text);
            if(popup != null) {
                popup.hide();
            }
        });

    }
}