package controllers;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ServerController {


    @FXML private StackPane spSubScene;
    @FXML private Button btnUsers;
    private DataModel dataModel;

    public void setSubSceneInitialNode()
    {
       // btnUsers.fire();
    }

    public void handleBtnOnActionUsers()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserListView.fxml"));
            loader.load();
            UserListViewController listView = loader.getController();

            listView.initModel(dataModel);

            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(listView.getVBoxRoot());
        }
        catch (IOException ex)
        {
            System.out.println();
        }
    }
    public void handleBtnOnActionAnnouncement()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Announcement.fxml"));
            loader.load();
            AnnouncementController announcementController = loader.getController();
            announcementController.init();

            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(announcementController.getVBoxRoot());
        }
        catch (IOException ex)
        {
            System.out.println();
        }
    }

    public void handleBtnOnActionServiceStart()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ServiceStart.fxml"));
            loader.load();
            ServiceStartController serviceStartController = loader.getController();
            serviceStartController.init();

            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(serviceStartController.getVBoxRoot());
        }
        catch (IOException ex)
        {
            System.out.println();
        }
    }

    public void handleBtnOnActionDashboardStart()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            loader.load();
            DashboardController dashboardController = loader.getController();
            dashboardController.init();

            spSubScene.getChildren().clear();
            spSubScene.getChildren().add(dashboardController.getVBoxRoot());
        }
        catch (IOException ex)
        {
            System.out.println();
        }
    }
    public void initModel(DataModel model)
    {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = model;
    }

}