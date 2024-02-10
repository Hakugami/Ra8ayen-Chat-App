package controller;

import dto.Model.UserModel;
import dto.requests.AddContactRequest;
import dto.responses.AddContactResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import model.Contact;
import model.ContactData;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddContactController implements Initializable {
    @FXML
    public Label serverReply;
    @FXML
    public Button addButton;
    @FXML
    public ListView contactsToAddList;
    public Button addAllButton;
    @FXML
    private TextField phoneField;
    private Popup popup;
    public ObservableList<Node> contactsToAdd;

    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setAddContactController(this);
        contactsToAdd = FXCollections.observableArrayList();
        contactsToAddList.setItems(contactsToAdd);
        addButton.setOnAction(this::addFriend);
        addAllButton.setOnAction(this::addAll);
    }

    private void addFriend(ActionEvent actionEvent) {

            //check if user in your contacts
            if (!checkIfPhoneNumberInContacts(phoneField.getText())) {
                try{
                UserModel userModel = NetworkFactory.getInstance().getUserModelByPhoneNumber(phoneField.getText());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/AddContactElement.fxml"));
                Parent root = loader.load();
                AddContactElementController addContactElementController = loader.getController();
                addContactElementController.setData(userModel.getUserName(), userModel.getProfilePicture(), userModel.getPhoneNumber(), root, this);
                root.setUserData(addContactElementController); // Set the controller as user data
                contactsToAdd.add(root);}
             catch(NotBoundException | IOException e){
                    throw new RuntimeException(e);
                }
            }

    }

    public void removeFromList(String phoneNumber) {
        contactsToAdd.removeIf(node -> {
            AddContactElementController controller = (AddContactElementController) node.getUserData();
            return controller.phoneNumber.getText().equals(phoneNumber);
        });
    }

    public void addAll(ActionEvent actionEvent) {
        AddContactRequest addContactRequest = new AddContactRequest();
        AddContactResponse addContactResponse;
        List<String> friendsPhoneNumbers = contactsToAdd.stream()
                .map(node -> ((AddContactElementController) node.getUserData()).phoneNumber.getText())
                .filter(this::checkIfPhoneNumberInContacts)
                .collect(Collectors.toList());
        try {
            addContactRequest.setUserId(CurrentUser.getInstance().getUserID());
            addContactRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
            addContactResponse = NetworkFactory.getInstance().addContact(addContactRequest);
            if (addContactResponse.isDone()) {
                serverReply.setText("Contact added successfully");
                serverReply.setStyle("-fx-text-fill: green");
                contactsToAdd.clear();
            } else {
                serverReply.setText("Contact not added");
                serverReply.setStyle("-fx-text-fill: red");
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
    boolean checkIfPhoneNumberInContacts(String PhoneNumber){
        for(ContactData contact:CurrentUser.getCurrentUser().getContactDataList()){
            if(contact.getPhoneNumber().equals(PhoneNumber)){
                Notifications.create().title("Failed").text("Phone Number " + phoneField + "in your Contacts").showInformation();
                return true;
            }
        }
        return false;
    }

}


//    private void addFriend(ActionEvent event) {
//        AddContactRequest addContactRequest = new AddContactRequest();
//        AddContactResponse addContactResponse;
//        String text = phoneField.getText();
//        List<String> friendsPhoneNumbers = List.of(Arrays.stream(text.split(",")).map(String::trim).toArray(String[]::new));
//        try {
//            addContactRequest.setUserId(CurrentUser.getInstance().getUserID());
//            addContactRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
//            addContactResponse = NetworkFactory.getInstance().addContact(addContactRequest);
//            if (addContactResponse.isDone()) {
//                serverReply.setText("Contact added successfully");
//                serverReply.setStyle("-fx-text-fill: green");
//            } else {
//                serverReply.setText("Contact not added");
//                serverReply.setStyle("-fx-text-fill: red");
//            }
//        } catch (RemoteException | NotBoundException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(text);
//    }
