package controllers;

import server.ServerApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private StackPane spSubScene;
    @FXML
    private Button settingsButton;
    @FXML
    private Button announcementButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button dashboardButton;

    public void setSubSceneInitialNode()
    {
        settingsButton.fire();
    }

    private void handleBtnOnActionAnnouncement()
    {
        spSubScene.getChildren().clear();
        spSubScene.getChildren().add(ServerApplication.scenes.get(Scenes.ANNOUNCEMENT));
    }

    private void handleBtnOnActionServiceStart()
    {
        spSubScene.getChildren().clear();
        spSubScene.getChildren().add(ServerApplication.scenes.get(Scenes.SERVICE_START));
    }

    private void handleBtnOnActionDashboardStart()
    {
        spSubScene.getChildren().clear();
        spSubScene.getChildren().add(ServerApplication.scenes.get(Scenes.DASHBOARD));
    }

    private void handleBtnOnActionUsersTableStart()
    {
        spSubScene.getChildren().clear();
        spSubScene.getChildren().add(ServerApplication.scenes.get(Scenes.USER_LIST_VIEW));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        settingsButton.setOnAction(actionEvent -> handleBtnOnActionServiceStart());
        announcementButton.setOnAction(actionEvent -> handleBtnOnActionAnnouncement());
        dashboardButton.setOnAction(actionEvent -> handleBtnOnActionDashboardStart());
        usersButton.setOnAction(actionEvent -> handleBtnOnActionUsersTableStart());
    }
}