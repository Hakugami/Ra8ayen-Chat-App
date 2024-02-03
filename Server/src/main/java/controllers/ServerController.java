package controllers;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import dao.impl.UserDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import model.entities.User;

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

    public ServerController() throws RemoteException {
    }

    public void setSubSceneInitialNode()
    {
        settingsButton.fire();
    }
    private void handleBtnOnActionAnnouncement()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Announcement.fxml"));
            loader.load();
            AnnouncementController announcementController = loader.getController();
            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(announcementController.getVBoxRoot());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void handleBtnOnActionServiceStart()
    {

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ServiceStart.fxml"));
            loader.load();
            ServiceStartController serviceStartController = loader.getController();
            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(serviceStartController.getVBoxRoot());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void handleBtnOnActionDashboardStart()
    {

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml"));
            loader.load();
            DashboardController dashboardController = loader.getController();
            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(dashboardController.getVBoxRoot());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    private void handleBtnOnActionUsersTableStart()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UserListView.fxml"));
            loader.load();
            UserListController userListController = loader.getController();
            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(userListController.getVBoxRoot());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        settingsButton.setOnAction(actionEvent -> handleBtnOnActionServiceStart());
        announcementButton.setOnAction(actionEvent -> handleBtnOnActionAnnouncement());
        dashboardButton.setOnAction(actionEvent -> handleBtnOnActionDashboardStart());
        usersButton.setOnAction(actionEvent -> handleBtnOnActionUsersTableStart());
    }
}