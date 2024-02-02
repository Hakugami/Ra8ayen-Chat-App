package controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

//import service.TrackOnlineUsersService;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController implements Initializable {
    @FXML
    public Label onlineUsersLabel;
    @FXML
    private VBox vbRoot;
    @FXML
    private PieChart genderPieChart;
    @FXML
    private PieChart countryPieChart;
    //private int count = 0;
    // Get an instance of TrackOnlineUsersService
    //TrackOnlineUsersService trackOnlineUsersService  = TrackOnlineUsersService.getInstance();
    private ScheduledExecutorService executorService;
    String onlineUsersCountString ;

    public DashboardController() throws RemoteException {
    }


    VBox getVBoxRoot()
    {
        return vbRoot;
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

        Platform.runLater(() -> {
            maleData.getNode().getStyleClass().add("male-pie");
            femaleData.getNode().getStyleClass().add("female-pie");
        });
        //-----------------------------------------------------------------------------------------------------

        /*onlineUsersLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("updated label text: " + newValue);
        });
        onlineUsersLabel.setText(" " + count);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            count++;
            Platform.runLater(() -> onlineUsersLabel.setText(" " + count));
        }, 0, 5, TimeUnit.SECONDS);*/
        //-----------------------------------------------------------------------------------------------------

  /*      TrackOnlineUsersService trackOnlineUsersService = null;
        try {
            trackOnlineUsersService = TrackOnlineUsersService.getInstance();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        System.out.println("proberty ---> "+trackOnlineUsersService.onlineUsersCountStringProberty().get());
        System.out.println("label ---> "+onlineUsersLabel.getText());
        // binding label with stringProperty
        onlineUsersLabel.textProperty().bind(trackOnlineUsersService.onlineUsersCountStringProberty());
        //-----------------------------------------------------------------------------------------------------

*/

    }


}
