package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public HBox customTitleBar;
    public BorderPane mainBorderPane;
    public AnchorPane swappableMenu;
    public Button addContact_btn;
    @FXML
    public AnchorPane swappableWindow;
    @FXML
    public AnchorPane help;
    public HBox HboxHELPME;
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

        Model.getInstance().getViewFactory().getSelectedContact().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat/Chat.fxml"));
                    Parent root = loader.load();
                    ChatController chatController = loader.getController();
                    chatController.setName(newValue.getName());
                    chatController.setImage(newValue.getImage().getImage());
                    setSwappableWindow(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/AddContact.fxml"));
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

    public void setSwappableWindow(Node node) {
        Platform.runLater(() -> {
            // Clear the swappableWindow and add the new node
            swappableWindow.getChildren().setAll(node);

            // Check if the new node is a Region
            if (node instanceof Region) {
                Region region = (Region) node;

                // Bind the minWidth, minHeight, maxWidth, and maxHeight properties of the new content to the width and height properties of the swappableWindow
                region.minWidthProperty().bind(swappableWindow.widthProperty());
                region.minHeightProperty().bind(swappableWindow.heightProperty());
                region.maxWidthProperty().bind(swappableWindow.widthProperty());
                region.maxHeightProperty().bind(swappableWindow.heightProperty());

                // Add listeners to the width and height properties of the swappableWindow to update the minWidth, minHeight, maxWidth, and maxHeight properties of the new content
                swappableWindow.widthProperty().addListener((observable, oldValue, newValue) -> {
                    region.minWidthProperty().unbind();
                    region.maxWidthProperty().unbind();
                    region.setMinWidth(newValue.doubleValue());
                    region.setMaxWidth(newValue.doubleValue());
                });
                swappableWindow.heightProperty().addListener((observable, oldValue, newValue) -> {
                    region.minHeightProperty().unbind();
                    region.maxHeightProperty().unbind();
                    region.setMinHeight(newValue.doubleValue());
                    region.setMaxHeight(newValue.doubleValue());
                });
            }
        });
        swappableWindow.layout();
    }

}