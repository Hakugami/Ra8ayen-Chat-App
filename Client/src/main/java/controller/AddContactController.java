package controller;

import dto.requests.AddContactRequest;
import dto.responses.AddContactResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    public Button addButton;
    @FXML
    private TextField phoneField;
    private Popup popup;

    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(this::addFriend);

    }

    private void addFriend(ActionEvent event) {
        AddContactRequest addContactRequest = new AddContactRequest();
        AddContactResponse addContactResponse;
        String text = phoneField.getText();
        List<String> friendsPhoneNumbers = List.of(Arrays.stream(text.split(",")).map(String::trim).toArray(String[]::new));
        try {
            addContactRequest.setUserId(CurrentUser.getInstance().getUserID());
            addContactRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
            addContactResponse = NetworkFactory.getInstance().addContact(addContactRequest);
            if (addContactResponse.isDone()) {
                serverReply.setText("Contact added successfully");
            } else {
                serverReply.setText("Contact not added");
                serverReply.setStyle("-fx-text-fill: red");
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(text);
    }
}