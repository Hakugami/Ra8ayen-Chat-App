package controller;
import dto.Model.UserModel;
import dto.requests.UpdateUserRequest;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ContactsController implements Initializable {
    public Label displayName;
    public TextField searchField;
    @FXML
    public Circle statusCircle;
    @FXML
    TreeView<Node> treeView;
    @FXML
    public ImageView ImagProfile;

    @FXML
    public Circle imageClip;

    public ObservableList<ContactData> observableContactDataList;

    public final ObjectProperty<ContactData> selectedContact = new SimpleObjectProperty<>(new ContactData());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setContactsController(this);
        observableContactDataList = FXCollections.observableArrayList();

        double parentWidth = statusCircle.getParent().getBoundsInLocal().getWidth();
        double circleWidth = statusCircle.getRadius() * 2;
        statusCircle.setLayoutX((parentWidth - circleWidth) / 2);
        statusCircle.setLayoutY(parentWidth - circleWidth);
        try {
            setImageProfileData();
        } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            setTreeViewData();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        addListenerToTreeView();
        statusCircle.setOnMouseClicked(this::changeStatus);
    }


    public void setTreeViewData() throws RemoteException {
        observableContactDataList.setAll(CurrentUser.getInstance().getContactDataList());

        TreeItem<Node> rootParent = new TreeItem<>();

        Label label = new Label("Contacts");
        label.setId("myLabel");
        rootParent = new TreeItem<>(label);

        TreeItem<Node> rootOnline = new TreeItem<>(loadFXML("Online", Color.GREEN));
        rootOnline.setExpanded(true);

        TreeItem<Node> rootOffline = new TreeItem<>(loadFXML("Offline", Color.GRAY));
        rootOffline.setExpanded(true);

        for (ContactData contact : observableContactDataList) {
            Color color = contact.getColor();
            System.out.println("Contact: " + contact.getName() + " is " + color);
            TreeItem<Node> contactNode = new TreeItem<>(loadFXML(contact));
            if (color.equals(Color.GRAY)) {
                rootOffline.getChildren().add(contactNode);
            } else {
                rootOnline.getChildren().add(contactNode);
            }
        }


        rootParent.setExpanded(true);
        rootParent.getChildren().addAll(rootOnline, rootOffline);
        treeView.setRoot(rootParent);
        treeView.refresh();

    }

    public void changeStatusColor(Color color) throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
        Platform.runLater(() -> statusCircle.setFill(color));
        if(color.equals(Color.GREEN)){
            CurrentUser.getInstance().setUserMode(CurrentUser.UserMode.Available);
        }
        else if(color.equals(Color.RED)){
            CurrentUser.getInstance().setUserMode(CurrentUser.UserMode.Busy);
        }
        else if(color.equals(Color.YELLOW)){
            CurrentUser.getInstance().setUserMode(CurrentUser.UserMode.Away);
        }
        // Get the current user
        CurrentUser currentUser = CurrentUser.getInstance();

// Create a new UserModel instance
        UserModel user = new UserModel();

// Set only the relevant fields
        user.setUserID(currentUser.getUserID());
        user.setPhoneNumber(currentUser.getPhoneNumber());
        user.setUserName(currentUser.getUserName());
        user.setEmailAddress(currentUser.getEmailAddress());
        user.setProfilePicture(currentUser.getProfilePicture());
        user.setGender(currentUser.getGender());
        user.setCountry(currentUser.getCountry());
        user.setDateOfBirth(currentUser.getDateOfBirth());
        user.setBio(currentUser.getBio());
        user.setUserStatus(currentUser.getUserStatus());
        user.setUserMode(currentUser.getUserMode());
        user.setLastLogin(currentUser.getLastLogin());

// Create the UpdateUserRequest with the new UserModel instance
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(user);

// Send the request
        NetworkFactory.getInstance().updateUser(updateUserRequest);


    }

    private void setImageProfileData() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
        double newRadius = 28;
        imageClip.setRadius(newRadius);
        imageClip.setCenterX(newRadius);
        imageClip.setCenterY(newRadius);
        ImagProfile.setFitWidth(imageClip.getRadius() * 2 + 5);
        ImagProfile.setFitHeight(imageClip.getRadius() * 2 + 5);
        displayName.setText(CurrentUser.getInstance().getUserName());
        Color color = Color.GREEN;
        changeStatusColor(color);
        if (CurrentUser.getInstance().getProfilePictureImage() != null) {
            System.out.println("Not Null data found");
            Image newImage = CurrentUser.getInstance().getProfilePictureImage();
            ImagProfile.setImage(newImage);
        } else {
            System.out.println("Null data found");
        }
    }

    private Node loadFXML(String status, Color color) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/StatusElement.fxml"));
            Node node = loader.load();
            StatusElementController controller = loader.getController();
            controller.setStatusName(status);
            controller.setStatusColor(color);
            return new HBox(node); // Wrap the Node in an HBox
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Node loadFXML(ContactData contactData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/ContactElement.fxml"));
            Node node = loader.load();
            ContactElementController controller = loader.getController();
            controller.setName(contactData.getName());
            controller.setStatus(contactData.getColor());
            controller.setImagId(contactData.getImage().getImage());
            return new HBox(node); // Wrap the Node in an HBox
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListenerToTreeView() {
        System.out.println("TreeView initialized with " + treeView.getExpandedItemCount() + " items");

        treeView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("New item selected in TreeView");
            if (newValue != null) {
                System.out.println("New value is not null");
                Node selectedNode = newValue.getValue();

                if (selectedNode instanceof HBox selectedHBox) {
                    Node root = selectedHBox.getChildren().get(0); // Get the root of the loaded FXML file
                    System.out.println("Root of selected node is " + root.getClass().getName());
                    if (root instanceof HBox rootHBox) {
                        System.out.println("Root is an HBox");
                        for (Node child : rootHBox.getChildren()) {
                            System.out.println("Child class: " + child.getClass().getName());
                        }
                        if (rootHBox.getChildren().get(0) instanceof Pane) {
                            System.out.println("First child is a Pane");
                            // It's a contact node
                            Pane pane = (Pane) rootHBox.getChildren().get(0);
                            ImageView imageView = (ImageView) pane.getChildren().getFirst();
                            if (rootHBox.getChildren().get(1) instanceof Label) {
                                System.out.println("Second child is a Label");
                                Label label = (Label) rootHBox.getChildren().get(1);

                                String name = label.getText();
                                ContactData contactData = new ContactData();
                                contactData.setName(name);
                                contactData.setImage(imageView);
                                setSelectedContact(contactData);
                                contactListener(contactData);
                            } else {
                                System.out.println("Second child is not a Label");
                            }
                        }
                    }
                }
            }
        });
    }

    public void contactListener(ContactData contactData) {
        System.out.println("contactListener called with contact: " + contactData.getName());
        Model.getInstance().getViewFactory().getSelectedContact().setValue(contactData);
    }

    public ContactData getSelectedContact() {
        return selectedContact.get();
    }

    public void setSelectedContact(ContactData selectedContact) {
        System.out.println("setSelectedContact called with contact: " + selectedContact.getName());
        this.selectedContact.set(selectedContact);

        // Get the MainWindowController and add the listener

    }

    public ObjectProperty<ContactData> selectedContactProperty() {
        return selectedContact;
    }

    private void changeStatus(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) { // Check if left button was clicked
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/StatusContextMenu.fxml"));
                Pane pane = loader.load();

                StatusContextMenuController statusContextMenuController = loader.getController();
                statusContextMenuController.setContactsController(this);

                Popup popup = new Popup();
                statusContextMenuController.setPopup(popup);
                popup.getContent().add(pane);
                popup.setAutoHide(true);
                popup.show(statusCircle.getScene().getWindow(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}