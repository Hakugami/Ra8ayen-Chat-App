package controllers;

import Mapper.UserMapperImpl;
import com.google.gson.reflect.TypeToken;
import concurrency.manager.ConcurrencyManager;
import exceptions.DuplicateEntryException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.google.gson.Gson;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;
import model.entities.User;
import model.entities.UserTable;
import org.controlsfx.control.Notifications;
import service.UserService;
import userstable.UsersTableStateSingleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class UserListController implements Initializable {
    @FXML
    VBox vbRoot;
    @FXML
    TableView<User> usersTableView;
    private UserService userService;
    private UserMapperImpl userMapper;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userMapper = new UserMapperImpl();
        usersTableView.setEditable(true);
        userService = new UserService();
        usersTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        usersTableView.setFixedCellSize(60);
        ConcurrencyManager.getInstance().submitTask(this::setupTableColumns);
        ConcurrencyManager.getInstance().submitTask(this::loadUsers);
    }

    private TableColumn<User, String> getUserStringTableColumn(UserTable userTable) {
        TableColumn<User, String> column = new TableColumn<>(userTable.name());
        column.setCellValueFactory(cellData -> new SimpleStringProperty(getUserProperty(cellData.getValue(), userTable)));

        if (userTable == UserTable.Country) {
            column.setCellFactory(tc -> new TableCell<>() {
                private final ComboBox<String> comboBox = new ComboBox<>();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        List<Map<String, String>> countries = loadCountries();
                        comboBox.getItems().setAll(countries.stream().map(country -> country.get("name")).collect(Collectors.toList()));
                        comboBox.setValue(item);
                        setGraphic(comboBox);
                        setAlignment(Pos.CENTER);
                        comboBox.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            user.setCountry(comboBox.getValue());
                            setUserProperty(user, userTable, comboBox.getValue());
                            try {
                                userService.updateUser(user);
                            } catch (DuplicateEntryException e) {
                                Notifications.create().title("Failed to update country").text(e.getMessage()).showError();
                            }
                            UsersTableStateSingleton.getInstance().updateUser(user);
                        });
                    }
                }
            });
        } else if (userTable == UserTable.DateOfBirth) {
            column.setCellFactory(tc -> new TableCell<>() {
                private final DatePicker datePicker = new DatePicker();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        if (item != null) {
                            datePicker.setValue(LocalDate.parse(item));
                        }
                        setGraphic(datePicker);
                        setAlignment(Pos.CENTER_RIGHT);
                        datePicker.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            user.setDateOfBirth(java.sql.Date.valueOf(datePicker.getValue()));
                            setUserProperty(user, userTable, datePicker.getValue().toString());
                            try {
                                userService.updateUser(user);
                            } catch (DuplicateEntryException e) {
                                Notifications.create().title("Failed to update date").text(e.getMessage()).showError();
                            }
                            UsersTableStateSingleton.getInstance().updateUser(user);
                        });
                    }
                }
            });
        } else {
            column.setCellFactory(tc -> {
                TextFieldTableCell<User, String> cell = new TextFieldTableCell<>(new DefaultStringConverter());
                cell.setStyle("-fx-alignment: CENTER;");
                return cell;
            });

            column.setOnEditCommit(event -> {
                User user = event.getRowValue();
                User originalUser = getOriginalUser(user);
                setUserProperty(user, userTable, event.getNewValue());
                try {
                    userService.updateUser(user);
                    ConcurrencyManager.getInstance().submitTask(() -> {
                        try {
                            OnlineControllerImpl.clients.get(user.getPhoneNumber()).updateUserModel(userMapper.entityToModel(user));
                        } catch (RemoteException e) {
                            Notifications.create().title("Error").text("An error occurred while updating the user").showError();
                        }
                    });

                } catch (DuplicateEntryException e) {
                    Notifications.create().title("Duplicate Entry").text(e.getMessage()).showError();
                    user.setUserID(originalUser.getUserID());
                    user.setPhoneNumber(originalUser.getPhoneNumber());
                    user.setUserName(originalUser.getUserName());
                    user.setEmailAddress(originalUser.getEmailAddress());
                    user.setGender(originalUser.getGender());
                    user.setCountry(originalUser.getCountry());
                    user.setDateOfBirth(originalUser.getDateOfBirth());
                    user.setBio(originalUser.getBio());
                    user.setUserStatus(originalUser.getUserStatus());
                    user.setUsermode(originalUser.getUsermode());
                    user.setLastLogin(originalUser.getLastLogin());
                }
                UsersTableStateSingleton.getInstance().updateUser(user);
            });
        }

        column.setEditable(true);
        column.setMinWidth(200);

        return column;
    }

    private User getOriginalUser(User user) {
        User originalUser = new User();
        originalUser.setUserID(user.getUserID());
        originalUser.setPhoneNumber(user.getPhoneNumber());
        originalUser.setUserName(user.getUserName());
        originalUser.setEmailAddress(user.getEmailAddress());
        originalUser.setGender(user.getGender());
        originalUser.setCountry(user.getCountry());
        originalUser.setDateOfBirth(user.getDateOfBirth());
        originalUser.setBio(user.getBio());
        originalUser.setUserStatus(user.getUserStatus());
        originalUser.setUsermode(user.getUsermode());
        originalUser.setLastLogin(user.getLastLogin());
        return originalUser;
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
            case UserID -> {
                Notifications.create().title("Invalid").text("Invalid Input: Cannot change User ID").showError();
                loadUsers();
            }
            case PhoneNumber -> {
                if (!newValue.matches("^(\\+2)?01[0125][0-9]{8}$")) {
                    Notifications.create().title("Invalid").text("Invalid Input: Phone number should match Egyptian phone numbers format").showError();
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
            case DateOfBirth -> {
                try {
                    user.setDateOfBirth(java.sql.Date.valueOf(newValue));
                } catch (IllegalArgumentException e) {
                    Notifications.create().title("Invalid").text("Invalid Input: Should be valid date format").showError();
                }
            }
            case Bio -> user.setBio(newValue);
        }
    }

    public void loadUsers() {
        try {
            usersTableView.setItems(UsersTableStateSingleton.getInstance().getUsers());
        } catch (Exception e) {
            System.out.println("An error occurred while loading the users.");
        }
    }
/*
    private TableColumn<User, Image> getProfilePictureColumn() {
        TableColumn<User, Image> profilePictureColumn = new TableColumn<>("Profile Picture");
        setupImageCellValueFactory(profilePictureColumn);
        setupImageCellFactory(profilePictureColumn);
        return profilePictureColumn;
    }

    private void setupImageCellValueFactory(TableColumn<User, Image> column) {
        column.setCellValueFactory(cellData -> {
            byte[] profilePicture = cellData.getValue().getProfilePicture();
            if (profilePicture != null) {
                ByteArrayInputStream input = new ByteArrayInputStream(profilePicture);
                return new SimpleObjectProperty<>(new Image(input));
            }
            return new SimpleObjectProperty<>();
        });
    }

    private void setupImageCellFactory(TableColumn<User, Image> column) {
        column.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private final HBox hbox = new HBox(imageView);
            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setMouseTransparent(false);
                hbox.setAlignment(Pos.CENTER_RIGHT);
            }

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    setGraphic(hbox);
                }
                setOnMouseClicked(event -> setupImageMouseClickEvent());
            }
        });
    }

    private void setupImageMouseClickEvent() {
        User user = usersTableView.getItems().get(usersTableView.getSelectionModel().getSelectedIndex());
        openImageStage(user);
    }

    private void openImageStage(User user) {
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        ImageView imageView = new ImageView();
        Button updateButton = new Button("Update");
        Button confirmButton = new Button("Confirm");
        HBox hbox = new HBox(updateButton, confirmButton);
        hbox.setSpacing(30);
        hbox.setAlignment(Pos.CENTER);

        if (user.getProfilePicture() != null) {
            ByteArrayInputStream input = new ByteArrayInputStream(user.getProfilePicture());
            imageView.setImage(new Image(input));
        }

        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        setupUpdateButton(updateButton, imageView);
        setupConfirmButton(confirmButton, imageView, user, stage);

        borderPane.setCenter(imageView);
        borderPane.setBottom(hbox);

        stage.setScene(new Scene(borderPane, 300, 300));
        stage.show();
    }

    private void setupUpdateButton(Button updateButton, ImageView imageView) {
        updateButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                try {
                    byte[] profilePicture = Files.readAllBytes(selectedFile.toPath());
                    ByteArrayInputStream input = new ByteArrayInputStream(profilePicture);
                    imageView.setImage(new Image(input));
                } catch (IOException e) {
                    Notifications.create().title("Error").text("An error occurred while reading the image make sure it has right extension").showError();
                }
            }
        });
    }

    private void setupConfirmButton(Button confirmButton, ImageView imageView, User user, Stage stage) {
        confirmButton.setOnAction(event -> {
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(), null), "png", output);
                user.setProfilePicture(output.toByteArray());
                userService.updateUser(user);
                int index = usersTableView.getItems().indexOf(user);
                usersTableView.getItems().set(index, user);
                OnlineControllerImpl.clients.get(user.getPhoneNumber()).updateUserModel(userMapper.entityToModel(user));
                stage.close();
            } catch (IOException | DuplicateEntryException e) {
                loadUsers();
            }
        });
    }*/
    public void setupTableColumns() {
        for (UserTable userTable : UserTable.values()) {
            //if (userTable == UserTable.ProfilePicture) {
                //Platform.runLater(() -> usersTableView.getColumns().add(getProfilePictureColumn()));
            //}
            if (userTable != UserTable.PasswordHash
                    && userTable != UserTable.LastLogin
                    && userTable != UserTable.UserStatus
                    && userTable != UserTable.UserMode
                    && userTable != UserTable.ProfilePicture) {
                Platform.runLater(() -> usersTableView.getColumns().add(getUserStringTableColumn(userTable)));
            }
        }
    }
    private List<Map<String, String>> loadCountries() {
        Gson gson = new Gson();
        Type countryListType = new TypeToken<List<Map<String, String>>>(){}.getType();

        try (InputStream inputStream = getClass().getResourceAsStream("/countries/CountryCodes.json")) {
            return gson.fromJson(new InputStreamReader(Objects.requireNonNull(inputStream)), countryListType);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }
}