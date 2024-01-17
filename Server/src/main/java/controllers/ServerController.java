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

    @FXML
    private StackPane spSubScene;
    @FXML
    private Button settingsButton;

    public void setSubSceneInitialNode()
    {
        settingsButton.fire();
    }
    public void handleBtnOnActionAnnouncement()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Announcement.fxml"));
            loader.load();
            AnnouncementController announcementController = loader.getController();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ServiceStart.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml"));
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

}