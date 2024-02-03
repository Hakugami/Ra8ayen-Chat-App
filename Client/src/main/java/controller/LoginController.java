package controller;

//import dto.Controller.TrackOnlineUsers;
import dto.requests.GetContactChatRequest;
import dto.requests.GetContactsRequest;
import dto.requests.GetGroupRequest;
import dto.responses.GetContactChatResponse;
import dto.responses.GetContactsResponse;
import dto.responses.GetGroupResponse;
import token.TokenManager;
import dto.requests.LoginRequest;
import dto.responses.LoginResponse;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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

    @FXML
    AnchorPane loginXml;

  //  private TrackOnlineUsers server;
    private int onlineUsersInDashboard;
    private int onlineUsersCount;


    @FXML
    public void initialize() {
        phoneNumberField.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (SQLException | ClassNotFoundException | RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
        passwordField.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (SQLException | ClassNotFoundException | RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (SQLException | ClassNotFoundException | RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
        registerButton.setOnAction(this::handleRegisterButton);



        onlineUsersCount = 0;
        onlineUsersInDashboard=0;


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


    private void handleLoginButton(ActionEvent event) throws SQLException, ClassNotFoundException, RemoteException, NotBoundException {
        Platform.runLater(() -> {
            try {
                if (validateFields()) {
                    LoginRequest loginRequest = new LoginRequest(phoneNumberField.getText(), passwordField.getText());
                    LoginResponse loginResponse = NetworkFactory.getInstance().login(loginRequest);
                    String token = loginResponse.getToken();
                    TokenManager.getInstance().setToken(token);
                    System.out.println(loginResponse);
                    if (loginResponse.getSuccess()) {
//                        NetworkFactory.getInstance().connect(phoneNumberField.getText(), CurrentUser.getInstance().getCallBackController());
//                        CurrentUser.getInstance().loadUser(NetworkFactory.getInstance().getUserModel(token));
//                        List<GetContactsResponse> responses = NetworkFactory.getInstance().getContacts(new GetContactsRequest(CurrentUser.getInstance().getUserID()));
//                        CurrentUser.getInstance().loadContactsList(responses);
//                        System.out.println(CurrentUser.getInstance().getContactDataList().size());
//                        System.out.println(responses);
//                        List<GetGroupResponse> groupResponses = NetworkFactory.getInstance().getGroups(new GetGroupRequest(CurrentUser.getInstance().getUserID()));
//                        CurrentUser.getInstance().loadGroups(groupResponses);
//                        System.out.println("Groups size " + CurrentUser.getInstance().getGroupList().size());
                        try {
                            retrieveData();
                        } catch (RemoteException | NotBoundException e) {
                            throw new RuntimeException(e);
                        }
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
                    } else {
                        System.err.println("Invalid fields1");
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

    private void retrieveData() throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {

        Runnable runnable = () -> {
            Platform.runLater(() -> {
                try {
                    NetworkFactory.getInstance().connect(phoneNumberField.getText(), CurrentUser.getInstance().getCallBackController());
                    CurrentUser.getInstance().loadUser(NetworkFactory.getInstance().getUserModel(TokenManager.getInstance().getToken()));
                    List<GetContactsResponse> responses = NetworkFactory.getInstance().getContacts(new GetContactsRequest(CurrentUser.getInstance().getUserID()));
                    CurrentUser.getInstance().loadContactsList(responses);
                    System.out.println(CurrentUser.getInstance().getContactDataList().size());
                    System.out.println(responses);
                    List<GetGroupResponse> groupResponses = NetworkFactory.getInstance().getGroups(new GetGroupRequest(CurrentUser.getInstance().getUserID()));
                    CurrentUser.getInstance().loadGroups(groupResponses);
                    System.out.println("Groups size " + CurrentUser.getInstance().getGroupList().size());
                } catch (RemoteException | NotBoundException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        };
            ConcurrencyManager.getInstance().execute(runnable);
    }


   private void startTrackingOnlineUsers() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                // online users in the dashboard
                onlineUsersInDashboard = NetworkFactory.getInstance().getOnlineUsersCount();
                //compare between current users numbers in dashboard and if there is a new user login
                if(onlineUsersCount > onlineUsersInDashboard){
                    // if yes ---> increment online users numbers in dashbpard
                    onlineUsersInDashboard=onlineUsersCount;
                    // update dashboard
                    NetworkFactory.getInstance().updateOnlineUsersCount(onlineUsersInDashboard);
                    System.out.println("onlineUsersCount : "+onlineUsersCount+" , "+"onlineUsersInDashboard : "+onlineUsersInDashboard);

                }

            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


    private void LoadDataToChatList() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
      //  List<ChatData> listOfChats = new ArrayList<>();
        List<GetContactChatRequest> listOfContactId = new ArrayList<>();
        for(ContactData getContactsResponse: CurrentUser.getInstance().getContactDataList()){
            listOfContactId.add(new GetContactChatRequest(CurrentUser.getInstance().getUserID(), getContactsResponse.getId()));
        }
     List<GetContactChatResponse> getContactChatResponses = NetworkFactory.getInstance().getPrivateChats(listOfContactId);
        for(GetContactChatResponse getContactsResponse: getContactChatResponses){
           for(int i = 0 ; i<CurrentUser.getInstance().getContactDataList().size();i++){

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
}