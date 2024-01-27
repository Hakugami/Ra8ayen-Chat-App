package controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private VBox vbRoot;
    @FXML
    private PieChart genderPieChart;
    @FXML
    private PieChart countryPieChart;

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
    }
}
