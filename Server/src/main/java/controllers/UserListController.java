package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.entities.ChatTable;
import model.entities.User;
import model.entities.UserTable;

import java.net.URL;
import java.util.ResourceBundle;

public class UserListController implements Initializable {
    @FXML
    VBox vbRoot;
    @FXML
    TableView usersTableView;

    public VBox getVBoxRoot()
    {
        return vbRoot;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(UserTable userTable : UserTable.values()) {
            if(userTable == UserTable.PasswordHash || userTable == UserTable.ProfilePicture) {
                continue;
            }
            TableColumn<User, String> column = new TableColumn<>(userTable.name());
            usersTableView.getColumns().add(column);
        }
    }
}
