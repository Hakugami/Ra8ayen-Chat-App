package prototyping.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactsController implements Initializable {
    public Button newChat_btn;
    public Button filter_btn;
    public TextField search_field;
    public ListView<String> contacts_list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> contacts = FXCollections.observableArrayList(
                "Contact 1",
                "Contact 2",
                "Contact 3",
                "Contact 4",
                "Contact 5"
        );

        contacts_list.setItems(contacts);
        contacts_list.setCellFactory(param -> new ContactListCell());
    }
}