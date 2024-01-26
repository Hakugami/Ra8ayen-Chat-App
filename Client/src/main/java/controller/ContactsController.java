package controller;

import application.HelloApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Callback;
import model.CurrentUser;
import model.Model;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Currency;
import java.util.ResourceBundle;


public class ContactsController implements Initializable {
    public Label displayName;
    public TextField searchField;
    @FXML
    TreeView<Node> treeView;


    @FXML
    public ImageView ImagProfile;

    @FXML
    Circle imageClip;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setImageProfileData();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        setTreeViewData();
    }

    void setTreeViewData() {

        TreeItem<Node> rootParent = new TreeItem<>();

        Label label = new Label("Contacts");
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #CA3503;");
        label.setFont(Font.font("Arial", 40));
        label.setPrefWidth(300);
        label.setPrefHeight(25);
        rootParent = new TreeItem<>(label);


        TreeItem<Node> rootOnline = new TreeItem<>( loadFXML("Online", Color.GREEN, "Contacts/StatusElement.fxml"));
        // TreeItem<Node> rootOnline = new TreeItem<>(new NodeData("StatusElement.fxml"));
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
        if(CurrentUser.getInstance().getProfilePictureImage()!=null) {
            System.out.println("Not Null data found");
            Image newImage = CurrentUser.getInstance().getProfilePictureImage();
            ImagProfile.setImage(newImage);
        }else{
            System.out.println("Null data found");
        }
    }
    Node loadFXML(String status, Color color, String fxmlFile) {
       // FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
           HBox customItem= (HBox) Model.getInstance().getViewFactory().getStatusElement();

          //  if (fxmlFile.equals("StatusElement.fxml")) {
                StatusElementController controller = Model.getInstance().getViewFactory().getStatusElementController();
                controller.setStatusName(status);
                controller.setStatusColor(color);
           // }
            Node node = new HBox();
            return customItem;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Node loadFXML(ContactData contactData, String fxmlFile) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            HBox customItem = (HBox) Model.getInstance().getViewFactory().getContact() ;
            if (fxmlFile.equals("ContactElement.fxml")) {
                ContactElementController controller = Model.getInstance().getViewFactory().getContactController();
                controller.setName(contactData.getName());
                controller.setStatus(contactData.getColor());
                controller.setUrl(contactData.getUrl());
            }
            return customItem;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void addListenerToTreeView(){
        treeView.setOnMouseClicked(event->{
            TreeItem<Node> NodeData = treeView.getSelectionModel().getSelectedItem();
            Node selectedNode = NodeData.getValue();

            if(selectedNode instanceof HBox ) {
                HBox selectedHBox = (HBox) selectedNode;

                if (selectedHBox.getChildren().get(0) instanceof Label) {
                    // It's a status node
                    Label label = (Label) selectedHBox.getChildren().get(0);
                    String status = label.getText();
                    Color color = (Color) label.getBackground().getFills().get(0).getFill();
                } else if (selectedHBox.getChildren().get(0) instanceof ImageView) {
                    // It's a contact node
                    ImageView imageView = (ImageView) selectedHBox.getChildren().get(0);
                    Label label = (Label) selectedHBox.getChildren().get(1);

                    String name = "";
                    String url = "";

                    name = label.getText();
                    ContactData contactData = new ContactData();
                    contactData.setName(name);
                    passDataToChat(contactData);
                }

            }
        });
    }
    void passDataToChat(ContactData contactData){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    ChatController csc = Model.getInstance().getViewFactory().getChatController();
                    csc.setName(contactData.getName());
                    System.out.println(csc.getName());

                } catch (IOException e) {
                    System.out.println("Exception throw");
                    throw new RuntimeException(e);
                }
            }
        });


    }


}
