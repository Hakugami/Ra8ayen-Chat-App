package controllers;

import concurrency.manager.ConcurrencyManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import userstable.UsersTableStateSingleton;
import java.net.URL;
import java.sql.Timestamp;
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
        ConcurrencyManager.getInstance().submitTask(this::setupTableColumns);
        ConcurrencyManager.getInstance().submitTask(this::loadUsers);
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

        column.setCellFactory(tc -> {
            TableCell<User, String> cell = new TextFieldTableCell<>();
            cell.setStyle("-fx-alignment: CENTER;");
            return cell;
        });

        column.setOnEditCommit(event -> {
            User user = event.getRowValue();
            setUserProperty(user, userTable, event.getNewValue());
            userService.updateUser(user);
            UsersTableStateSingleton.getInstance().updateUser(user);
        });
        column.setEditable(true);

        column.setMinWidth(100);

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
            case PhoneNumber -> {
                if (newValue.length() != 11) {
                    Notifications.create().title("Invalid").text("Invalid Input: Phone number should be 11 digits").showError();
                } else {
                    user.setPhoneNumber(newValue);
                }
            }
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
            case DateOfBirth, LastLogin -> {
                try {
                    user.setLastLogin(Timestamp.valueOf(newValue).toString());
                } catch (IllegalArgumentException e) {
                    Notifications.create().title("Invalid").text("Invalid Input: Should be valid date format").showError();
                }
            }
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
        }
    }

    public void loadUsers() {
        try {
            usersTableView.setItems(UsersTableStateSingleton.getInstance().getUsers());
        } catch (Exception e) {
            System.out.println("An error occurred while loading the users.");
        }
    }

    public void setupTableColumns() {
        for(UserTable userTable : UserTable.values()) {
            if(userTable != UserTable.ProfilePicture && userTable != UserTable.PasswordHash) {
                Platform.runLater(() -> usersTableView.getColumns().add(getUserStringTableColumn(userTable)));
            }
        }
        Platform.runLater(() -> usersTableView.getColumns().add(getIconColumn()));
    }
}