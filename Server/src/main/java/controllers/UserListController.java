package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.entities.User;
import model.entities.UserTable;
import org.controlsfx.control.Notifications;
import service.UserService;
import java.net.URL;
import java.sql.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserListController implements Initializable {
    @FXML
    VBox vbRoot;
    @FXML
    TableView<User> usersTableView;
    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersTableView.setEditable(true);

        userService = new UserService();
        usersTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        for(UserTable userTable : UserTable.values()) {
            if(userTable != UserTable.ProfilePicture && userTable != UserTable.PasswordHash) {
                usersTableView.getColumns().add(getUserStringTableColumn(userTable));
            }
        }

        usersTableView.getColumns().add(getIconColumn());
        loadUsers();
    }

    private TableColumn<User, Void> getIconColumn() {
        TableColumn<User, Void> iconColumn = new TableColumn<>();
        iconColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView iconView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/Images/delete.png")).toString(), 20, 20, true, true));
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
        return iconColumn;
    }

    private TableColumn<User, String> getUserStringTableColumn(UserTable userTable) {
        TableColumn<User, String> column = new TableColumn<>(userTable.name());
        column.setCellValueFactory(cellData -> new SimpleStringProperty(getUserProperty(cellData.getValue(), userTable)));

        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            User user = event.getRowValue();
            setUserProperty(user, userTable, event.getNewValue());
            userService.updateUser(user);
            loadUsers();
        });
        column.setEditable(true);

        return column;
    }

    private String getUserProperty(User user, UserTable userTable) {
        return switch (userTable) {
            case UserID -> Integer.toString(user.getUserID());
            case PhoneNumber -> user.getPhoneNumber();
            case DisplayName -> user.getUserName();
            case EmailAddress -> user.getEmailAddress();
            case Gender -> user.getGender().name();
            case Country -> user.getCountry();
            case DateOfBirth -> user.getDateOfBirth().toString();
            case Bio -> user.getBio();
            case UserStatus -> user.getUserStatus().name();
            case UserMode -> user.getUsermode().name();
            case LastLogin -> user.getLastLogin();
            default -> "";
        };
    }

    private void setUserProperty(User user, UserTable userTable, String newValue) {
        switch (userTable) {
            case UserID -> user.setUserID(Integer.parseInt(newValue));
            case PhoneNumber -> user.setPhoneNumber(newValue);
            case DisplayName -> user.setUserName(newValue);
            case EmailAddress -> user.setEmailAddress(newValue);
            case Gender -> {
                try {
                    user.setGender(User.Gender.valueOf(newValue));
                } catch (IllegalArgumentException e) {
                    Notifications.create().title("Invalid").text("Invalid Input: Write Male OR Female").showError();
                }
            }
            case Country -> user.setCountry(newValue);
            case DateOfBirth -> user.setDateOfBirth(Date.valueOf(newValue));
            case Bio -> user.setBio(newValue);
            case UserStatus -> {
                try {
                    user.setUserStatus(User.UserStatus.valueOf(newValue));
                } catch (IllegalArgumentException e) {
                    Notifications.create().title("Invalid").text("Invalid Input: Write Online OR Offline").showError();
                }
            }
            case UserMode -> {
                try {
                    user.setUsermode(User.UserMode.valueOf(newValue));
                } catch (IllegalArgumentException e) {
                    Notifications.create().title("Invalid").text("Invalid Input: Write Away OR Busy OR Available").showError();
                }
            }
            case LastLogin -> user.setLastLogin(newValue);
        }
    }

    private void loadUsers() {
        try {
            ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
            usersTableView.setItems(users);
        } catch (Exception e) {
            System.out.println("An error occurred while loading the users.");
        }
    }

    public VBox getVBoxRoot() {
        return vbRoot;
    }
}