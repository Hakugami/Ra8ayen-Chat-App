package controllers;

import dao.impl.UserDaoImpl;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.entities.User;
import network.manager.NetworkManagerSingleton;
import service.TrackOnlineUsersService;

//import service.TrackOnlineUsersService;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController implements Initializable {
    @FXML
    public Label onlineUsersLabel;
    public Label offlineUserLabel;
    @FXML
    private VBox vbRoot;
    @FXML
    private PieChart genderPieChart;
    @FXML
    private PieChart countryPieChart;
    private int count = 0;
    private ScheduledExecutorService scheduler;
    private TrackOnlineUsersService trackOnlineUsersService;
    private AuthenticationControllerSingleton authenticationControllerSingleton;
    int offlineUsers ;
    int onlineUsers;
    TrackOnlineUsersService finalTrackOnlineUsersService;

    public DashboardController() throws RemoteException, MalformedURLException {
        trackOnlineUsersService = TrackOnlineUsersService.getInstance();
        authenticationControllerSingleton = AuthenticationControllerSingleton.getInstance();
        finalTrackOnlineUsersService = trackOnlineUsersService;
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private javafx.collections.ObservableList<PieChart.Data> getCountryChartData() {
        return javafx.collections.FXCollections.observableArrayList(
                new PieChart.Data("Egypt", 20),
                new PieChart.Data("USA", 25),
                new PieChart.Data("ENGLAND", 10),
                new PieChart.Data("GERMANY", 15),
                new PieChart.Data("ITALY", 30),
                new PieChart.Data("FRANCE", 40)
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int maleCount = 20;
        int femaleCount = 25;

        PieChart.Data maleData = new PieChart.Data("Male Users", maleCount);
        PieChart.Data femaleData = new PieChart.Data("Female Users", femaleCount);

        maleData.nameProperty().bind(Bindings.concat("Male Users: ", maleCount));
        femaleData.nameProperty().bind(Bindings.concat("Female Users: ", femaleCount));

        genderPieChart.getData().addAll(maleData, femaleData);
        countryPieChart.getData().addAll(getCountryChartData());

        //--------------------------------------------------------------------------------------------


        scheduler.scheduleAtFixedRate(() -> {

            Platform.runLater(() -> {
                try {
                    onlineUsers = finalTrackOnlineUsersService.getOnlineUsersCount();
                    onlineUsersLabel.setText(" " + onlineUsers );
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }, 0, 1, TimeUnit.SECONDS);
        //--------------------------------------------------------------------------------------------
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.scheduleAtFixedRate(() -> {
//            System.out.println(TrackOnlineUsersService.onlineUsersCountStringProberty().get());
//        }, 0, 1, TimeUnit.SECONDS);
        //-------------------------------------not working -------------------------------------------
        //onlineUsersLabel.textProperty().bind(TrackOnlineUsersService.onlineUsersCountStringProberty());
        //-------------------------------------not working -------------------------------------------
//        onlineUsersLabel.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Updated label text: " + newValue);
//        });
//        try {
//            onlineUsersLabel.setText(" " + trackOnlineUsersService.getOnlineUsersCount());
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
        //--------------------------------------------------------------------------------------------
        //offline label
        scheduler.scheduleAtFixedRate(() -> {
            offlineUsers = authenticationControllerSingleton.getOfflineUsers();
            System.out.println("offlineUsers ---> "+offlineUsers);
            Platform.runLater(() -> offlineUserLabel.setText(""+offlineUsers));
        }, 0, 1, TimeUnit.SECONDS);
        //--------------------------------------------------------------------------------------------
    }



}
