package prototyping.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CallsController implements Initializable {

    public Button newCall_btn;
    public Button filter_btn;
    public TextField search_field;
    public ListView<String> calls_list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList <String> calls = javafx.collections.FXCollections.observableArrayList(
                "Call 1",
                "Call 2",
                "Call 3",
                "Call 4",
                "Call 5"
        );

        calls_list.setItems(calls);
        
    }
}
