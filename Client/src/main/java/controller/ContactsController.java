package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;


public class ContactsController {

    @FXML
    private ListView<ContactData> ListViewId;

    @FXML
    private ImageView ImagProfile;

    @FXML
    Circle imageClip;
    private ObservableList<String> listData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setImageProfileData();
        setListViewData();
    }
    void setImageProfileData(){
        ImagProfile.setFitWidth(imageClip.getRadius() * 2);
        ImagProfile.setFitHeight(imageClip.getRadius() * 2);
        String imagePath = "/images/personProfile.png";

        // Get the URL of the image
        URL imageUrl = ContactElementController.class.getResource(imagePath);
        if(imageUrl!=null) {
            System.out.println(imageUrl);
            Image newImage = new Image(imageUrl.toString());
            ImagProfile.setImage(newImage);
        }else{
            System.out.println("Null data found");
        }
    }
    void setListViewData(){
        // Set up the ListView with a custom cell factory
        ListViewId.setCellFactory(new Callback<ListView<ContactData>, ListCell<ContactData>>() {
            @Override
            public ListCell<ContactData> call(ListView<ContactData> listView) {
                return new ListCell<ContactData>() {
                    @Override
                    protected void updateItem(ContactData item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                // Load the FXML for each item
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Contacts/ContactElement.fxml"));
                                HBox customItem = loader.load();

                                // Access the controller and set the data
                                ContactElementController controller = loader.getController();
                                controller.setName(item.getName());
                                controller.setStatus(item.getColor());
                                controller.setUrl(item.getUrl());
                                // Set the custom item as the graphic for the cell
                                setGraphic(customItem);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });

        // Example: Add some data to the ListView
        ListViewId.getItems().add(new ContactData("Reem",Color.CYAN,"/images/personone.jpg"));
        ListViewId.getItems().add(new ContactData("Shrouk",Color.BURLYWOOD,"/images/persontwo.jpg"));
        ListViewId.getItems().add(new ContactData("Maram",Color.RED,"/images/personthree.jpg"));
        ListViewId.getItems().add(new ContactData("Rawda", Color.YELLOW,"/images/personfour.jpg"));
        ListViewId.getItems().add(new ContactData("Yasmin", Color.BURLYWOOD,"/images/personfive.png"));
        ListViewId.getItems().add(new ContactData("Sando", Color.CYAN,"/images/personsix.png"));
        ListViewId.getItems().add(new ContactData("Habiba", Color.BURLYWOOD,"/images/personseven.png"));
        ListViewId.getItems().add(new ContactData("Lama", Color.RED,"/images/personseight.png"));
        ListViewId.getItems().add(new ContactData("Sahar", Color.YELLOW,"/images/personnine.jpg"));
        ListViewId.getItems().add(new ContactData("Nada", Color.BLUE,"/images/personten.png"));
    }


}
