package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

public class DashboardController {
    private @FXML VBox vbRoot;
    @FXML
    private PieChart genderPieChart;
    @FXML
    private PieChart countryPieChart;

    VBox getVBoxRoot()
    {
        return vbRoot;
    }

    public void init() {
        genderPieChart.getData().addAll(getGenderChartData());
        countryPieChart.getData().addAll(getCountryChartData());
    }

    private javafx.collections.ObservableList<PieChart.Data> getGenderChartData() {
        return javafx.collections.FXCollections.observableArrayList(
                new PieChart.Data("Male Users", 20),
                new PieChart.Data("Female Users", 25)
        );
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
}
