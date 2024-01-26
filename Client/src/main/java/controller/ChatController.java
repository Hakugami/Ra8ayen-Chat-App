package controller;


import dto.Model.MessageModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ChatController implements Initializable {
    @FXML
    public ListView<MessageModel> chatListView;
    @FXML
    public TextField messageBox;
    public Button sendMessage;
    @FXML
    ImageView ImagContact;

    @FXML
    Circle imageClip;

    @FXML
    Label NameContact;
    @FXML
    private ObservableList<MessageModel> chatMessages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatListView.setStyle("-fx-background-image: url('/images/defuatback.jpg')");
        chatMessages = FXCollections.observableArrayList();
        chatListView.setItems(chatMessages);
        chatListView.setCellFactory(param -> new CustomListCell());
        sendMessage.setOnAction(event -> sendMessage());
        messageBox.setOnAction(event -> sendMessage());

        ImagContact.setFitWidth(imageClip.getRadius() * 2);
        ImagContact.setFitHeight(imageClip.getRadius() * 2);
        String imagePath = "/images/persontwo.jpg";

        // Get the URL of the image
        URL imageUrl = ContactElementController.class.getResource(imagePath);
        if (imageUrl != null) {
            System.out.println(imageUrl);
            Image newImage = new Image(imageUrl.toString());
            ImagContact.setImage(newImage);
        } else {
            System.out.println("Null data found");
        }
        NameContact.setText("Reem Osama");


    }

    private void sendMessage() {
        String message = messageBox.getText();
        if (message.isEmpty()) {
            return;
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageContent(message);
        chatListView.getItems().add(messageModel);
        messageBox.clear();
    }

    class CustomListCell extends ListCell<MessageModel> {

        private final Label label = new Label();
        private final ContextMenu contextMenu = new ContextMenu();
        private final MenuItem copyMenuItem = new MenuItem("Copy");

        {
            label.getStyleClass().add("chat_message_bubble");
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            copyMenuItem.setOnAction(event -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(chatListView.getSelectionModel().getSelectedItems().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));
                clipboard.setContent(content);
            });

            contextMenu.getItems().add(copyMenuItem);
            setContextMenu(contextMenu);
        }

        @Override
        protected void updateItem(MessageModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                label.setText(item.getMessageContent());
                setGraphic(label);
            } else {
                setGraphic(null);
            }
            // Set the background color of the list cell to transparent
            this.setStyle("-fx-background-color: transparent;");
        }
    }
    public void setName(String name){
        NameContact.setText(name);
    }

    public String getName(){
        return NameContact.getText();
    }


}


