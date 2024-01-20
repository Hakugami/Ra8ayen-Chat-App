package prototyping.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.javafx2.models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public HBox customTitleBar;
    public BorderPane mainBorderPane;
    public AnchorPane swappableMenu;
    public Button addContact_btn;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    @FXML
    private Button closeButton;

    private double initialX;
    private double initialY;
    private double previousX, previousY, previousWidth, previousHeight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
            switch (newValue) {
                case "Calls":
                    swappableMenu.getChildren().clear();
                    swappableMenu.getChildren().add(Model.getInstance().getViewFactory().getCalls());
                    break;
                case "Contacts":
                    swappableMenu.getChildren().clear();
                    swappableMenu.getChildren().add(Model.getInstance().getViewFactory().getContacts());
                    break;
                default:
                    break;
            }
        });


        minimizeButton.setOnAction(event -> ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true));
        maximizeButton.setOnAction(event -> {
            Stage stage = ((Stage) ((Button) event.getSource()).getScene().getWindow());
            if (stage.isMaximized()) {
                stage.setX(previousX);
                stage.setY(previousY);
                stage.setWidth(previousWidth);
                stage.setHeight(previousHeight);
                stage.setMaximized(false);
            } else {
                previousX = stage.getX();
                previousY = stage.getY();
                previousWidth = stage.getWidth();
                previousHeight = stage.getHeight();

                Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                stage.setX(visualBounds.getMinX());
                stage.setY(visualBounds.getMinY());
                stage.setWidth(visualBounds.getWidth());
                stage.setHeight(visualBounds.getHeight());
                stage.setMaximized(true);
            }
        });
        closeButton.setOnAction(event -> ((Stage) ((Button) event.getSource()).getScene().getWindow()).close());

        customTitleBar.setOnMousePressed(event -> {
            initialX = event.getSceneX();
            initialY = event.getSceneY();
        });

        customTitleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) customTitleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - initialX);
            stage.setY(event.getScreenY() - initialY);
        });


        addContact_btn.setOnAction(event -> {
            try {
                Popup popup = new Popup();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddContact.fxml"));
                Parent root = loader.load();
                popup.getContent().add(root);

                AddContactController addContactController = loader.getController();
                addContactController.setPopup(popup);

                popup.setAutoHide(true);

                // Show the popup first to calculate its height
                popup.show(addContact_btn.getScene().getWindow());

                // Calculate the x and y coordinates
                double x = addContact_btn.localToScreen(addContact_btn.getBoundsInLocal()).getMinX() + addContact_btn.getWidth() / 2;
                double y = addContact_btn.localToScreen(addContact_btn.getBoundsInLocal()).getMinY() - popup.getHeight();

                // Hide the popup
                popup.hide();

                // Show the popup again at the correct position
                popup.show(addContact_btn.getScene().getWindow(), x, y);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
