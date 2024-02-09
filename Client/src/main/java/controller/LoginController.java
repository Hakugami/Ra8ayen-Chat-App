package controller;

//import dto.Controller.TrackOnlineUsers;

import concurrency.manager.ConcurrencyManager;
import dto.requests.*;
import dto.responses.GetContactsResponse;
import dto.responses.GetGroupResponse;
import dto.responses.GetNotificationsResponse;
import dto.responses.LoginResponse;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.ContactData;
import model.CurrentUser;
import model.Group;
import model.Model;
import network.NetworkFactory;
import notification.NotificationManager;
import org.controlsfx.control.Notifications;
import token.TokenManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class LoginController {

    @FXML
    public TextField phoneNumberField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public Button registerButton;
    public Button next;
    public Label Password_lbl;
    public Label User_Name_lbl;
    public Button minimizeButton;
    public Button exitButton;

    @FXML
    AnchorPane loginXml;

    //  private TrackOnlineUsers server;
    private int onlineUsersInDashboard;
    private int onlineUsersCount;


    @FXML
    public void initialize() {
        // Initially hide the password label and password field
        Model.getInstance().getControllerFactory().setLoginController(this);
        String[] data = TokenManager.getInstance().loadData();
        if (data != null && data.length > 1) {
            phoneNumberField.setText(data[1]);
        }
        Password_lbl.setVisible(false);
        passwordField.setVisible(false);
        loginButton.setVisible(false);

        // Set initial position for animation
        Password_lbl.setTranslateX(200);
        passwordField.setTranslateX(200);
        loginButton.setTranslateX(200);

        next.setOnAction(this::checkPhoneNumberAnimation);

        phoneNumberField.setOnAction(this::checkPhoneNumberAnimation);

        passwordField.setOnAction(event -> {
            try {
                handleLoginButton();
            } catch (SQLException | ClassNotFoundException | RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnAction(event -> {
            try {
                handleLoginButton();
            } catch (SQLException | ClassNotFoundException | RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
        registerButton.setOnAction(this::handleRegisterButton);

        exitButton.setOnAction(event -> {
            System.exit(0);
        });
        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });


        onlineUsersCount = 0;
        onlineUsersInDashboard = 0;


    }

    private void handleRegisterButton(ActionEvent event) {
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        AnchorPane registerPane = (AnchorPane) Model.getInstance().getViewFactory().getRegister();
        registerPane.setTranslateX(currentStage.getWidth());
        currentStage.getScene().setRoot(registerPane);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), registerPane);
        tt.setToX(0);
        tt.play();
    }


    private void handleLoginButton() throws SQLException, ClassNotFoundException, RemoteException, NotBoundException {
        Platform.runLater(() -> {
            try {
                if (validateFields()) {
                    LoginRequest loginRequest = new LoginRequest(phoneNumberField.getText(), passwordField.getText());
                    LoginResponse loginResponse = NetworkFactory.getInstance().login(loginRequest);
                    if (loginResponse.getSuccess()) {
                        StringBuilder sb = new StringBuilder();
                        String token = loginResponse.getToken();
                        sb.append(token).append("\n").append(phoneNumberField.getText()).append("\n");
                        TokenManager.getInstance().setToken(sb.toString());
                        loginTransition();
                        /*
                         *
                         * tracking number of online users
                         *
                         * */
                        //onlineUsersCount++;
                        //startTrackingOnlineUsers();
                        //new Logout().startHeartbeat();
                        NetworkFactory.getInstance().heartBeat();
                    } else {
                        System.err.println("Invalid fields1");
                        shakeAnimation();
                    }
                } else {
                    System.err.println("Invalid fields2");
                    //just to check result without any data : what happened when login button is clicked
                    //onlineUsersCount++;
                    //startTrackingOnlineUsers();
                    shakeAnimation();
                }
            } catch (SQLException | ClassNotFoundException | RemoteException | NotBoundException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private boolean checkConnection() {
        try {
            if (!NetworkFactory.getInstance().connect(phoneNumberField.getText(), CurrentUser.getInstance().getCallBackController())) {
                // Halt the thread
                Thread.currentThread().interrupt();

                // Display a notification on the JavaFX Application Thread
                try {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Session Alert")
                                .text("There is an existing session.")
                                .owner(loginButton.getScene().getWindow()) // Set the owner of the Notifications object
                                .showWarning();
                    });
                }
                catch (IllegalStateException e) {
                    System.out.println("Session Alert: " + e.getMessage());
                    Notifications.create()
                            .title("Session Alert")
                            .text("There is an existing session.")
                            .showWarning();
                }

                return false;
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public void loginTransition() {
        if (checkConnection()) {
            System.out.println("Login successful");
            retrieveData();
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            BorderPane mainArea = Model.getInstance().getViewFactory().getMainArea();
            currentStage.setScene(new Scene(mainArea));
        }
    }

    public void autoLoginTransition() {
        if (checkConnection()) {
            System.out.println("Auto login successful");
            retrieveData();
            BorderPane mainArea = Model.getInstance().getViewFactory().getMainArea();
            Stage stage = new Stage();
            stage.setScene(new Scene(mainArea));
            stage.setTitle("Chat App");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
    }

    private void shakeAnimation() {
        final int shakeDistance = 20; // Increase the shake distance
        final int shakeCount = 4; // Increase the shake count
        final int shakeDuration = 500;

        Timeline shakeTimeline = new Timeline(new KeyFrame(Duration.millis(shakeDuration / (shakeCount * 2)),
                new KeyValue(loginXml.translateXProperty(), shakeDistance, Interpolator.EASE_BOTH)));

        for (int i = 1; i < shakeCount; i++) {
            shakeTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * shakeDuration / shakeCount),
                    new KeyValue(loginXml.translateXProperty(), shakeDistance * (i % 2 == 0 ? 1 : -1), Interpolator.EASE_BOTH)));
        }

        shakeTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(shakeDuration),
                new KeyValue(loginXml.translateXProperty(), 0, Interpolator.EASE_BOTH)));

        // Create a flash animation (Timeline) for the password field
        Timeline flashTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(passwordField.styleProperty(), "-fx-border-color: white;")),
                new KeyFrame(Duration.millis(250), new KeyValue(passwordField.styleProperty(), "-fx-border-color: red;")),
                new KeyFrame(Duration.millis(500), new KeyValue(passwordField.styleProperty(), "-fx-border-color: white;"))
        );
        flashTimeline.setCycleCount(4);

        // Play the animations
        shakeTimeline.play();
        flashTimeline.play();
    }

    public void retrieveData() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                CurrentUser.getInstance().loadUser(NetworkFactory.getInstance().getUserModel(TokenManager.getInstance().getToken()));
                List<GetContactsResponse> responses = NetworkFactory.getInstance().getContacts(new GetContactsRequest(CurrentUser.getInstance().getUserID()));
                CurrentUser.getInstance().loadContactsList(responses);
                List<GetGroupResponse> groupResponses = NetworkFactory.getInstance().getGroups(new GetGroupRequest(CurrentUser.getInstance().getUserID()));
                CurrentUser.getInstance().loadGroups(groupResponses);
                // Retrieve messages for each contact chat ID
                System.out.println("loading messages for contacts");
                for (ContactData contact : CurrentUser.getInstance().getContactDataList()) {
                    Model.getInstance().getControllerFactory().getChatController().retrieveMessagesByChatId(contact.getChatId());
                    System.out.println("loading messages for contact: " + contact.getName());
                }

                // Retrieve messages for each group ID
                System.out.println("loading messages for groups");
                for (Group group : CurrentUser.getInstance().getGroupList()) {
                    Model.getInstance().getControllerFactory().getChatController().retrieveMessagesByChatId(group.getGroupId());
                    System.out.println("loading messages for group: " + group.getGroupName());
                }
                GetNotificationsRequest request = new GetNotificationsRequest(CurrentUser.getInstance().getUserID());
                GetNotificationsResponse response = NetworkFactory.getInstance().getNotifications(request);
                System.out.println("loading notifications");
                for (int i = 0; i < response.getNotifications().size(); i++) {
                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setReceiverPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
                    friendRequest.setSenderPhoneNumber(response.getUsers().get(i).getPhoneNumber());
                    friendRequest.setUserModel(response.getUsers().get(i));
                    friendRequest.setId(response.getNotifications().get(i).getId());
                    NotificationManager.getInstance().addNotification(friendRequest);
                    Platform.runLater(() -> {
                        Notifications.create().title("New Friend Request").text("You have a new friend request").showInformation();
                    });
                }


                return null;
            }
        };

        // Handle any exceptions that occurred in the task
        task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Throwable ex = newValue;
                System.out.println("Exception occurred in task: " + ex);
            }
        });

        // Update the UI after the task has completed
        task.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                Platform.runLater(() -> {
                    // Update the UI here
                    try {
                        Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                        try {
                            Model.getInstance().getControllerFactory().getContactsController().setImageProfileData();
                        } catch (SQLException | NotBoundException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        // Start the task on a new thread
        ConcurrencyManager.getInstance().submitTask(task);
    }

    private void startTrackingOnlineUsers() {
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(() -> {
//            try {
//                // online users in the dashboard
//                onlineUsersInDashboard = NetworkFactory.getInstance().getOnlineUsersCount();
//                //compare between current users numbers in dashboard and if there is a new user login
//                if (onlineUsersCount > onlineUsersInDashboard) {
//                    // if yes ---> increment online users numbers in dashbpard
//                    onlineUsersInDashboard = onlineUsersCount;
//                    // update dashboard
//                    NetworkFactory.getInstance().updateOnlineUsersCount(onlineUsersInDashboard);
//                    System.out.println("onlineUsersCount : " + onlineUsersCount + " , " + "onlineUsersInDashboard : " + onlineUsersInDashboard);
//
//                }
//
//            } catch (RemoteException | NotBoundException e) {
//                e.printStackTrace();
//            }
//        }, 0, 5, TimeUnit.SECONDS);
    }


    private boolean validateFields() throws SQLException, ClassNotFoundException {
        if (!isValidPhoneNumber(phoneNumberField.getText()) ||
                !isValidPassword(passwordField.getText())
        ) {
            return false;
        }

        return true;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{11}"))
            return true;
        else {
            System.out.println("Please enter a valid number , 11 digits.");
            return false;
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() >= 8)
            return true;
        else {
            System.out.println("Password must be 8 or more.");
            return false;
        }

    }

    private void checkPhoneNumberAnimation(ActionEvent event) {

        try {
            if (NetworkFactory.getInstance().checkPhoneNumber(phoneNumberField.getText())) {
                // Create a TranslateTransition for the password label and password field
                TranslateTransition transition1 = new TranslateTransition(Duration.seconds(1), Password_lbl);
                TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1), passwordField);
                TranslateTransition transition3 = new TranslateTransition(Duration.seconds(1), loginButton);

                // Create a RotateTransition for the password label and password field
                RotateTransition rotateTransition1 = new RotateTransition(Duration.seconds(1), Password_lbl);
                RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(1), passwordField);
                RotateTransition rotateTransition3 = new RotateTransition(Duration.seconds(1), loginButton);

                // Set the end position (original position) for the animation
                transition1.setToX(0);
                transition2.setToX(0);
                transition3.setToX(0);

                // Set the rotation angle for the RotateTransition
                rotateTransition1.setToAngle(360);
                rotateTransition2.setToAngle(360);


                User_Name_lbl.setVisible(false);
                next.setVisible(false);
                phoneNumberField.setVisible(false);
                Password_lbl.setVisible(true);
                passwordField.setVisible(true);
                loginButton.setVisible(true);

                // Start the animations
                transition1.play();
                transition2.play();
                transition3.play();
                rotateTransition1.play();
                rotateTransition2.play();
                rotateTransition3.play();
            } else {
               Platform.exit();
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}