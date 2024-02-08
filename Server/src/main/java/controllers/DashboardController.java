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
    List<User> users = AuthenticationControllerSingleton.newUsersList;
    private final StringProperty countryDataProperty = new SimpleStringProperty();
    public DashboardController() throws RemoteException, MalformedURLException {
        trackOnlineUsersService = TrackOnlineUsersService.getInstance();
        authenticationControllerSingleton = AuthenticationControllerSingleton.getInstance();
        finalTrackOnlineUsersService = trackOnlineUsersService;
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("users.size() ---> " + users.size());

        //--------------------------------------------------------------------------------------------
        //online label
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
        //offline label
        scheduler.scheduleAtFixedRate(() -> {
            offlineUsers = authenticationControllerSingleton.getOfflineUsers();
            System.out.println("offlineUsers ---> "+offlineUsers);
            Platform.runLater(() -> offlineUserLabel.setText(""+offlineUsers));
        }, 0, 1, TimeUnit.SECONDS);
        //--------------------------------------------------------------------------------------------
        //pie charts
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("users.size() ---> " + users.size());
            updatePieChart();
            updateCountryPieChart();
        }, 0, 5, TimeUnit.SECONDS);

    }



        //-------------------------------------- Gender -------------------------------------------------------


        private void updatePieChart() {

            int maleCount = 0;

            int femaleCount = 0;

            for (User user : users) {
                if (user.getGender().toString().equalsIgnoreCase("male")) {
                    maleCount++;
                } else if (user.getGender().toString().equalsIgnoreCase("female")) {
                    femaleCount++;
                }
            }
            int finalMaleCount = maleCount;

            int finalFemaleCount = femaleCount;

            Platform.runLater(() -> {
                PieChart.Data maleData = new PieChart.Data("Male Users", finalMaleCount);
                PieChart.Data femaleData = new PieChart.Data("Female Users", finalFemaleCount);
                maleData.nameProperty().bind(Bindings.concat("Male Users: ", finalMaleCount));
                femaleData.nameProperty().bind(Bindings.concat("Female Users: ", finalFemaleCount));

                genderPieChart.getData().clear();
                genderPieChart.getData().addAll(maleData, femaleData);
            });
        }
        //----------------------------------- Country -----------------------------------------------------
        private void updateCountryPieChart() {
            // count users per country
            int totalCount = 0;
            List<PieChart.Data> countryData = FXCollections.observableArrayList();
            for (User user : users) {
                String country = user.getCountry();
                totalCount++;
                boolean found = false;
                for (PieChart.Data data : countryData) {
                    if (data.getName().equals(country)) {
                        data.setPieValue(data.getPieValue() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    countryData.add(new PieChart.Data(country, 1));
                }
            }

            // update the pie chart
            for (PieChart.Data data : countryData) {
                data.nameProperty().bind(Bindings.concat(data.getName(), ": ", (int)data.getPieValue()));
            }

            Platform.runLater(() -> {
                countryPieChart.getData().clear();
                countryPieChart.getData().addAll(countryData);
            });
        }
        //-----------------------------------------------------------------------------------------------------------



}
