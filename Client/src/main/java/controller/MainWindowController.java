package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.ContactData;
import model.Group;
import model.Model;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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

    private static void minimizeWindow(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }

    private static void closeWindow(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

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
                case "UpdateProfile":
                    swappableWindow.getChildren().clear();
                    setSwappableWindow(Model.getInstance().getViewFactory().getUpdateProfile());
                    Model.getInstance().getViewFactory().getSelectedMenuItem().set("");
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
                    if (newValue instanceof Group) {
                        chatController.setName(((Group) newValue).getGroupName());
                        chatController.setImage(((Group) newValue).getGroupImage().getImage());
                    } else {
                        chatController.setName(((ContactData) newValue).getName());
                        chatController.setImage(((ContactData) newValue).getImage().getImage());
                    }
                    setSwappableWindow(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        minimizeButton.setOnAction(MainWindowController::minimizeWindow);
        maximizeButton.setOnAction(this::maximizeWindow);
        closeButton.setOnAction(MainWindowController::closeWindow);

        customTitleBar.setOnMousePressed(this::captureWindowPosition);

        customTitleBar.setOnMouseDragged(this::dragWindow);


        addContact_btn.setOnAction(this::openAddWindow);
//
//        new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                System.out.println("Setting tree view data");
//                Platform.runLater(() -> {
//                    Model.getInstance().getControllerFactory().getLoginController().retrieveData();
//                });
//                System.out.println("should have retrieved data");
//                Platform.runLater(() -> {
//                    try {
//                        Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
//                    } catch (RemoteException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        Model.getInstance().getControllerFactory().getContactsController().setImageProfileData();
//                    } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).start();

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

    private void maximizeWindow(ActionEvent event) {
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
    }

    private void captureWindowPosition(MouseEvent event) {
        initialX = event.getSceneX();
        initialY = event.getSceneY();
    }

    private void dragWindow(MouseEvent event) {
        Stage stage = (Stage) customTitleBar.getScene().getWindow();
        stage.setX(event.getScreenX() - initialX);
        stage.setY(event.getScreenY() - initialY);
    }


    public void openAddWindow(ActionEvent event) {
        try {
            Popup popup = new Popup();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/AddWindow.fxml"));
            Parent root = loader.load();
            popup.getContent().add(root);

            AddWindowController addWindowController = loader.getController();
            addWindowController.setPopup(popup); // Pass the Popup reference to AddWindowController

            // Get the AddGroupGroupController from the AddWindowController
            AddGroupGroupController addGroupGroupController = addWindowController.getAddGroupGroupController();
            addGroupGroupController.setPopup(popup); // Pass the Popup reference to AddGroupGroupController

            popup.setAutoHide(true);

            // Show the popup first to calculate its height
            popup.show(addContact_btn.getScene().getWindow());

            // Calculate the x and y coordinates
            double x = addContact_btn.localToScreen(addContact_btn.getBoundsInLocal()).getMinX() + addContact_btn.getWidth() / 2;
            double y = addContact_btn.localToScreen(addContact_btn.getBoundsInLocal()).getMinY() - popup.getHeight();

            // Hide the popup
//        popup.hide();

            // Show the popup again at the correct position
            popup.show(addContact_btn.getScene().getWindow(), x, y);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddContactWindow(ActionEvent event) {
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
    }
}