package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import model.CurrentUser;
import model.Model;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ContactsController implements Initializable {
    public Label displayName;
    public TextField searchField;
    @FXML
    TreeView<Node> treeView;

    @FXML
    public ImageView ImagProfile;

    @FXML
    public Circle imageClip;

    public final ObjectProperty<ContactData> selectedContact = new SimpleObjectProperty<>( new ContactData());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setImageProfileData();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        setTreeViewData();
        addListenerToTreeView();
    }

    void setTreeViewData() {
        TreeItem<Node> rootParent = new TreeItem<>();

        Label label = new Label("Contacts");
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #CA3503;");
        label.setFont(Font.font("Arial", 40));
        label.setPrefWidth(300);
        label.setPrefHeight(25);
        rootParent = new TreeItem<>(label);

        TreeItem<Node> rootOnline = new TreeItem<>(loadFXML("Online", Color.GREEN, "Contacts/StatusElement.fxml"));
        rootOnline.setExpanded(true);
        rootOnline.getChildren().addAll(
                new TreeItem<>(loadFXML(new ContactData("Reem", Color.CYAN, "/images/personone.jpg"), "ContactElement.fxml")),
                new TreeItem<>(loadFXML(new ContactData("Shrouk", Color.BURLYWOOD, "/images/persontwo.jpg"), "ContactElement.fxml")),
                new TreeItem<>(loadFXML(new ContactData("Rawda", Color.YELLOW, "/images/personfour.jpg"), "ContactElement.fxml"))
        );

        TreeItem<Node> rootOffline = new TreeItem<>(loadFXML("Offline", Color.RED, "StatusElement.fxml"));
        rootOffline.setExpanded(true);
        rootOffline.getChildren().addAll(
                new TreeItem<>(loadFXML(new ContactData("Reem", Color.CYAN, "/images/personseven.png"), "ContactElement.fxml")),
                new TreeItem<>(loadFXML(new ContactData("Shrouk", Color.BURLYWOOD, "/images/personthree.jpg"), "ContactElement.fxml")),
                new TreeItem<>(loadFXML(new ContactData("Rawda", Color.YELLOW, "/images/personten.png"), "ContactElement.fxml"))
        );

        rootParent.setExpanded(true);
        rootParent.getChildren().addAll(rootOnline, rootOffline);

        treeView.setRoot(rootParent);
    }

    void setImageProfileData() throws RemoteException {
        ImagProfile.setFitWidth(imageClip.getRadius() * 2);
        ImagProfile.setFitHeight(imageClip.getRadius() * 2);
        displayName.setText(CurrentUser.getInstance().getUserName());
        if (CurrentUser.getInstance().getProfilePictureImage() != null) {
            System.out.println("Not Null data found");
            Image newImage = CurrentUser.getInstance().getProfilePictureImage();
            ImagProfile.setImage(newImage);
        } else {
            System.out.println("Null data found");
        }
    }

Node loadFXML(String status, Color color, String fxmlFile) {
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

Node loadFXML(ContactData contactData, String fxmlFile) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/ContactElement.fxml"));
        Node node = loader.load();
        ContactElementController controller = loader.getController();
        controller.setName(contactData.getName());
        controller.setStatus(contactData.getColor());
        controller.setUrl(contactData.getUrl());
        return new HBox(node); // Wrap the Node in an HBox
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

void addListenerToTreeView() {
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

    public void contactListener(ContactData contactData){
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

}