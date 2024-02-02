package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddWindowController implements Initializable {
    public AnchorPane addContact;
    public AnchorPane addGroup;
    public Popup popup;
    public AnchorPane tabWindow;
    @FXML
    private TabPane tabPane;

    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



//        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
//            AnchorPane selectedTabContent = (AnchorPane) newTab.getContent();
//            Stage stage = (Stage) tabPane.getScene().getWindow();
//            stage.setWidth(selectedTabContent.getPrefWidth());
//            stage.setHeight(selectedTabContent.getPrefHeight());
//        });
    }

    public AddGroupGroupController getAddGroupGroupController() {
        return (AddGroupGroupController) addGroup.getUserData();
    }
}