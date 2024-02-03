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
import service.TrackOnlineUsersService;

//import service.TrackOnlineUsersService;

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
    //private int count = 0;
    private ScheduledExecutorService scheduler;
    String onlineUsersCountString ;
    UserDaoImpl userDaoImp = new UserDaoImpl();
    List<User> users = userDaoImp.getAll();
    private final StringProperty countryDataProperty = new SimpleStringProperty();
    private ObservableList<User> userss = FXCollections.observableArrayList();
    private TrackOnlineUsersService trackOnlineUsersService;

    public DashboardController() throws RemoteException {
        scheduler = Executors.newScheduledThreadPool(1);
    }


    VBox getVBoxRoot()
    {
        return vbRoot;
    }

    private javafx.collections.ObservableList<PieChart.Data> getCountryChartData() {
        return javafx.collections.FXCollections.observableArrayList();
    }

    /*private javafx.collections.ObservableList<PieChart.Data> getCountryChartData() {
        return javafx.collections.FXCollections.observableArrayList(
                new PieChart.Data("Egypt", 20),
                new PieChart.Data("USA", 25),
                new PieChart.Data("ENGLAND", 10),
                new PieChart.Data("GERMANY", 15),
                new PieChart.Data("ITALY", 30),
                new PieChart.Data("FRANCE", 40)
        );
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*int maleCount = 20;
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
        });*/
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

        if(ServiceStartController.isServerOn) {

            //-----------------------------------------------------------------------------------------------------
            //onlineUseLabel
            TrackOnlineUsersService trackOnlineUsersService = null;
            try {
                trackOnlineUsersService = TrackOnlineUsersService.getInstance();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }


            System.out.println("proberty ---> " + trackOnlineUsersService.onlineUsersCountStringProberty().get());
            System.out.println("label ---> " + onlineUsersLabel.getText());
            // binding label with stringProperty
            onlineUsersLabel.textProperty().bind(trackOnlineUsersService.onlineUsersCountStringProberty());
            //-----------------------------------------------------------------------------------------------------
            //offlineUserLabel
            TrackOnlineUsersService finalTrackOnlineUsersService = trackOnlineUsersService;
            StringBinding offlineUserCountBinding = new StringBinding() {
                {
                    super.bind(finalTrackOnlineUsersService.onlineUsersCountStringProberty(), userss);
                }

                @Override
                protected String computeValue() {
                    int totalUserCount = users.size();
                    System.out.println("userss.size(); --->"+users.size());
                    int onlineUserCount = Integer.parseInt(finalTrackOnlineUsersService.onlineUsersCountStringProberty().get());
                    int offlineUserCount = totalUserCount - onlineUserCount;
                    return String.valueOf(offlineUserCount);
                }
            };
            offlineUserLabel.textProperty().bind(offlineUserCountBinding);
            //-----------------------------------------------------------------------------------------------------

            startUpdatingPieChart();
            startUpdatingCountryPieChart();
            bindCountryPieChartData();
        }
        else {

            ObservableList<PieChart.Data> genderChartData = FXCollections.observableArrayList();
            ObservableList<PieChart.Data> countryChartData = FXCollections.observableArrayList();

            genderPieChart.setData(genderChartData);
            countryPieChart.setData(countryChartData);
            Platform.runLater(() -> {
                for (PieChart.Data data : genderChartData) {
                    Node node = data.getNode();
                    node.setStyle("-fx-pie-color: lightgray;");
                }

                for (PieChart.Data data : countryChartData) {
                    Node node = data.getNode();
                    node.setStyle("-fx-pie-color: lightgray;");
                }
            });

            // Indicate No Data
            if (genderChartData.isEmpty()) {
                genderChartData.add(new PieChart.Data("No Data", 100));
            }

            if (countryChartData.isEmpty()) {
                countryChartData.add(new PieChart.Data("No Data", 100));
            }
        }

    }
    //-----------------------------------------------------------------------------------------------------------

    public void startUpdatingPieChart() {

        scheduler.scheduleAtFixedRate(() -> {

            updatePieChart();

        }, 0, 1, TimeUnit.MINUTES);
    }

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
    //-----------------------------------------------------------------------------------------------------------
    private void startUpdatingCountryPieChart() {
        scheduler.scheduleAtFixedRate(this::updateCountryDataFromDatabase, 0, 1, TimeUnit.MINUTES);
    }

    private void updateCountryDataFromDatabase() {

        StringBuilder newData = new StringBuilder();
        for (User user : users) {
            String country = user.getCountry();
            newData.append(country).append(",");
        }

        Platform.runLater(() -> countryDataProperty.set(newData.toString()));
    }

    private void bindCountryPieChartData() {
        countryDataProperty.addListener((observable, oldValue, newValue) -> {
            countryPieChart.getData().clear();
            /*
            //Another Way
            newValue is a string containing ---> country names separated by commas
            String[] countries = newValue.split(",");
            Map<String, Integer> countryCounts = new HashMap<>();

            for (String country : countries) {
                countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
             }
            */

            Map<String, Integer> countryCounts = getStringIntegerMap(newValue);

            //iterates through each entry in the countryCounts map
            for (Map.Entry<String, Integer> entry : countryCounts.entrySet()) {
                String country = entry.getKey();
                int count = entry.getValue();
                //PieChart.Data object represents a slice of data in the pie chart
                PieChart.Data data = new PieChart.Data(country ,count);
                countryPieChart.getData().add(data);
            }

            for (PieChart.Data data : countryPieChart.getData()) {
                // updates the label [name] of each slice.
                data.nameProperty().set(data.getName() + " : " + (int) data.getPieValue());
            }
        });
    }

    private static Map<String, Integer> getStringIntegerMap(String newValue) {
        String[] countries = newValue.split(",");

        Map<String, Integer> countryCounts = new HashMap<>();

        for (String country : countries) {
            //increments the count by 1 ---> if the country already exists in the map
            //mapped each country with the number of users exist in it
            countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
        }
        return countryCounts;
    }
    //-----------------------------------------------------------------------------------------------------------

}
