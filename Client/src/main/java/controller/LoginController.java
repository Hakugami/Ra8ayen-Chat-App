package controller;

//import dto.Controller.TrackOnlineUsers;

import dto.requests.GetContactChatRequest;
import dto.requests.GetContactsRequest;
import dto.requests.GetGroupRequest;
import dto.requests.LoginRequest;
import dto.responses.GetContactChatResponse;
import dto.responses.GetContactsResponse;
import dto.responses.GetGroupResponse;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.ContactData;
import model.CurrentUser;
import model.Group;
import model.Model;
import network.NetworkFactory;
import token.TokenManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
                    String token = loginResponse.getToken();
                    TokenManager.getInstance().setToken(token);
                    System.out.println(loginResponse);
                    if (loginResponse.getSuccess()) {
                        retrieveData();
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        BorderPane mainArea = Model.getInstance().getViewFactory().getMainArea();
                        currentStage.setScene(new Scene(mainArea));
                        /*
                         *
                         * tracking number of online users
                         *
                         * */
                        onlineUsersCount++;
                        startTrackingOnlineUsers();
                        new Logout().startHeartbeat();
                    } else {
                        System.err.println("Invalid fields1");
                        shakeAnimation();
                    }
                } else {
                    System.err.println("Invalid fields2");
                    //just to check result without any data : what happened when login button is clicked
                    //onlineUsersCount++;
                    //startTrackingOnlineUsers();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });
    }

 private void shakeAnimation(){
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
            new KeyFrame(Duration.ZERO, new KeyValue(passwordField.styleProperty(), "-fx-background-color: white;")),
            new KeyFrame(Duration.millis(250), new KeyValue(passwordField.styleProperty(), "-fx-background-color: red;")),
            new KeyFrame(Duration.millis(500), new KeyValue(passwordField.styleProperty(), "-fx-background-color: white;"))
    );
    flashTimeline.setCycleCount(4);

    // Play the animations
    shakeTimeline.play();
    flashTimeline.play();
}

    private void retrieveData() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                NetworkFactory.getInstance().connect(phoneNumberField.getText(), CurrentUser.getInstance().getCallBackController());
                CurrentUser.getInstance().loadUser(NetworkFactory.getInstance().getUserModel(TokenManager.getInstance().getToken()));
                List<GetContactsResponse> responses = NetworkFactory.getInstance().getContacts(new GetContactsRequest(CurrentUser.getInstance().getUserID()));
                CurrentUser.getInstance().loadContactsList(responses);
                List<GetGroupResponse> groupResponses = NetworkFactory.getInstance().getGroups(new GetGroupRequest(CurrentUser.getInstance().getUserID()));
                CurrentUser.getInstance().loadGroups(groupResponses);
                // Retrieve messages for each contact chat ID
                for (ContactData contact : CurrentUser.getInstance().getContactDataList()) {
                    Model.getInstance().getControllerFactory().getChatController().retrieveMessagesByChatId(contact.getChatId());
                    System.out.println("loading messages for contact: " + contact.getName());
                }

                // Retrieve messages for each group ID
                for (Group group : CurrentUser.getInstance().getGroupList()) {
                    Model.getInstance().getControllerFactory().getChatController().retrieveMessagesByChatId(group.getGroupId());
                    System.out.println("loading messages for group: " + group.getGroupName());
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
        new Thread(task).start();
    }

    private void startTrackingOnlineUsers() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                // online users in the dashboard
                onlineUsersInDashboard = NetworkFactory.getInstance().getOnlineUsersCount();
                //compare between current users numbers in dashboard and if there is a new user login
                if (onlineUsersCount > onlineUsersInDashboard) {
                    // if yes ---> increment online users numbers in dashbpard
                    onlineUsersInDashboard = onlineUsersCount;
                    // update dashboard
                    NetworkFactory.getInstance().updateOnlineUsersCount(onlineUsersInDashboard);
                    System.out.println("onlineUsersCount : " + onlineUsersCount + " , " + "onlineUsersInDashboard : " + onlineUsersInDashboard);

                }

            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


    private void LoadDataToChatList() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
        //  List<ChatData> listOfChats = new ArrayList<>();
        List<GetContactChatRequest> listOfContactId = new ArrayList<>();
        for (ContactData getContactsResponse : CurrentUser.getInstance().getContactDataList()) {
            listOfContactId.add(new GetContactChatRequest(CurrentUser.getInstance().getUserID(), getContactsResponse.getId()));
        }
        List<GetContactChatResponse> getContactChatResponses = NetworkFactory.getInstance().getPrivateChats(listOfContactId);
        for (GetContactChatResponse getContactsResponse : getContactChatResponses) {
            for (int i = 0; i < CurrentUser.getInstance().getContactDataList().size(); i++) {

            }
        }
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
                System.exit(0);
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}