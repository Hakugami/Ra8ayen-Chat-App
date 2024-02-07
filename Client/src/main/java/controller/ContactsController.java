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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.*;
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
    public Circle myProfilePic;

    @FXML
    public Circle imageClip;

    public ObservableList<ContactData> observableContactDataList;

    public final ObjectProperty<ContactData> selectedContact = new SimpleObjectProperty<>(new ContactData());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setContactsController(this);
        observableContactDataList = FXCollections.observableArrayList();


        double myProfilePicBottomRightX = myProfilePic.getLayoutX() + myProfilePic.getRadius() * 2;
        double myProfilePicBottomRightY = myProfilePic.getLayoutY() + myProfilePic.getRadius() * 2;

        // Set the position of the statusCircle to the bottom right corner of the myProfilePic
        statusCircle.setLayoutX(myProfilePicBottomRightX - statusCircle.getRadius());
        statusCircle.setLayoutY(myProfilePicBottomRightY - statusCircle.getRadius()- 25);




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
        TreeItem<Node> group = new TreeItem<>(loadFXML("Groups", Color.PURPLE));
        group.setExpanded(true);

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
        for(int i = 0 ; i < CurrentUser.getInstance().getGroupList().size(); i++){
            TreeItem<Node> groupNode = new TreeItem<>(loadFXML(CurrentUser.getInstance().getGroupList().get(i)));
            group.getChildren().add(groupNode);
        }


        rootParent.setExpanded(true);
        rootParent.getChildren().addAll(rootOnline, rootOffline, group);
        treeView.setRoot(rootParent);
        treeView.refresh();

    }

    public void changeStatusColor(Color color) throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
        Platform.runLater(() -> {
            statusCircle.getStyleClass().removeAll("online-status", "offline-status", "away-status", "busy-status");
        });

        if(color.equals(Color.GREEN)){
            Platform.runLater(()->statusCircle.getStyleClass().add("online-status"));
            CurrentUser.getInstance().setUserStatus(CurrentUser.UserStatus.Online);
            CurrentUser.getInstance().setUserMode(CurrentUser.UserMode.Available);
        }
        else if(color.equals(Color.RED)){
            Platform.runLater(()->statusCircle.getStyleClass().add("busy-status"));
            CurrentUser.getInstance().setUserStatus(CurrentUser.UserStatus.Online);
            CurrentUser.getInstance().setUserMode(CurrentUser.UserMode.Busy);
        }
        else if(color.equals(Color.YELLOW)){
            Platform.runLater(()->statusCircle.getStyleClass().add("away-status"));
            CurrentUser.getInstance().setUserStatus(CurrentUser.UserStatus.Online);
            CurrentUser.getInstance().setUserMode(CurrentUser.UserMode.Away);
        }
        else if(color.equals(Color.GRAY)){
            Platform.runLater(()->statusCircle.getStyleClass().add("offline-status"));
            CurrentUser.getInstance().setUserStatus(CurrentUser.UserStatus.Offline);
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

    public void setImageProfileData() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
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
            myProfilePic.setFill(new ImagePattern(newImage));
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
            return  new HBox(node);
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
            controller.setChatID(contactData.getChatId());
            controller.setLastMessageProperty(contactData.getLastMessage());
            controller.setLastMessageLabel(contactData.getLastMessage());
            return new HBox(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Node loadFXML(Group group) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/ContactElement.fxml"));
            Node node = loader.load();
            ContactElementController controller = loader.getController();
            controller.setName(group.getGroupName());
            controller.setStatus(Color.PURPLE);
            controller.setImagId(group.getGroupImage().getImage());
            controller.setChatID(group.getGroupId());
            controller.setLastMessageProperty(group.getLastMessage());
            controller.setLastMessageLabel(group.getLastMessage());
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
                        if (rootHBox.getChildren().get(1) instanceof VBox) {
                            System.out.println("Second child is a Vbox");
                            VBox vBox = (VBox) rootHBox.getChildren().get(1);
                            Label label = (Label) vBox.getChildren().get(0);
                            HBox hBox = (HBox) vBox.getChildren().get(1);
                            Label label2 = (Label)hBox.getChildren().get(0);

                            String name = label.getText();
                            int ID = Integer.parseInt(label2.getText());

                            // Check if the selected item is a group or a contact
                            try {
                                if (isGroup(name)) {
                                    // Handle group
                                    try {
                                        Group group = getGroup(name);
                                        contactListener(group);
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                    // Do something with the group...
                                } else {
                                    // Handle contact
                                    ContactData contactData = new ContactData();
                                    contactData.setName(name);
                                    contactData.setImage(imageView);
                                    contactData.setChatId(ID);
                                    for(int i = 0 ; i < CurrentUser.getInstance().getContactDataList().size(); i++){
                                        if(CurrentUser.getInstance().getContactDataList().get(i).getChatId() == ID){
                                            contactData.setPhoneNumber(CurrentUser.getInstance().getContactDataList().get(i).getPhoneNumber());
                                            System.out.println("Phone Number of contact ------------------"+CurrentUser.getInstance().getContactDataList().get(i).getPhoneNumber());
                                        }
                                    }
                                    setSelectedContact(contactData);
                                    System.out.println("Id of contact "+contactData.getId());
                                    contactListener(contactData);
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            System.out.println("Second child is not a vbox");
                        }
                    }
                }
            }
        }
    });
}

    private boolean isGroup(String name) throws RemoteException {
        for(int i = 0 ; i < CurrentUser.getInstance().getGroupList().size(); i++){
            if(CurrentUser.getInstance().getGroupList().get(i).getGroupName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private Group getGroup(String name) throws RemoteException {
        for(int i = 0 ; i < CurrentUser.getInstance().getGroupList().size(); i++){
            if(CurrentUser.getInstance().getGroupList().get(i).getGroupName().equals(name)){
                return CurrentUser.getInstance().getGroupList().get(i);
            }
        }
        return null;
    }

    public void contactListener(Chat contactData) {
        Model.getInstance().getViewFactory().getSelectedContact().setValue(contactData);

//        try {
//            Model.getInstance().getControllerFactory().getChatController().getMessageOfContact();
//
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        } catch (NotBoundException e) {
//            throw new RuntimeException(e);
//        }
       // System.out.println(Model.getInstance().getViewFactory().getSelectedContact().get().getName());
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