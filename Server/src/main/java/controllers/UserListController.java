package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.entities.User;
import model.entities.UserTable;
import service.UserService;
import java.net.URL;
import java.util.ResourceBundle;

public class UserListController implements Initializable {
    @FXML
    VBox vbRoot;
    @FXML
    TableView<User> usersTableView;
    private UserService userService;

    public VBox getVBoxRoot()
    {
        return vbRoot;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();
        usersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(UserTable userTable : UserTable.values()) {
            if(userTable == UserTable.PasswordHash || userTable == UserTable.ProfilePicture) {
                continue;
            }
            TableColumn<User, String> column = getUserStringTableColumn(userTable);
            usersTableView.getColumns().add(column);
        }


        TableColumn<User, Void> iconColumn = new TableColumn<>();
        iconColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView iconView = new ImageView(new Image("/Images/delete.png", 20, 20, true, true));

            {
                iconView.getStyleClass().add("image-view");
                iconView.setPickOnBounds(true);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : iconView);
                if (!empty) {
                    setOnMouseClicked(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        userService.deleteUser(user);
                        loadUsers();
                    });
                }
            }
        });
        usersTableView.getColumns().add(iconColumn);


        loadUsers();
    }

    private static TableColumn<User, String> getUserStringTableColumn(UserTable userTable) {
        TableColumn<User, String> column = new TableColumn<>(userTable.name());
        column.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return switch (userTable) {
                case UserID -> new SimpleStringProperty(Integer.toString(user.getUserID()));
                case PhoneNumber -> new SimpleStringProperty(user.getPhoneNumber());
                case DisplayName -> new SimpleStringProperty(user.getUserName());
                case EmailAddress -> new SimpleStringProperty(user.getEmailAddress());
                case Gender -> new SimpleStringProperty(user.getGender().name());
                case Country -> new SimpleStringProperty(user.getCountry());
                case DateOfBirth -> new SimpleStringProperty(user.getDateOfBirth().toString());
                case Bio -> new SimpleStringProperty(user.getBio());
                case UserStatus -> new SimpleStringProperty(user.getUserStatus().name());
                case UserMode -> new SimpleStringProperty(user.getUsermode().name());
                case LastLogin -> new SimpleStringProperty(user.getLastLogin());
                default -> new SimpleStringProperty("");
            };
        });
        return column;
    }

    private void loadUsers() {
        try {
            ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
            usersTableView.setItems(users);
        } catch (Exception e) {
            System.out.println("An error occurred while loading the users.");
        }
    }
}