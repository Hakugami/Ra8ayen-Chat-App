package controller;

import dto.Model.MessageModel;
import dto.Model.UserModel;
import dto.requests.SendMessageRequest;
import dto.responses.SendMessageResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.shape.Circle;
import model.CurrentUser;
import model.Group;
import model.Model;
import network.NetworkFactory;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChatController implements Initializable {
    @FXML
    public ListView<MessageModel> chatListView;
    @FXML
    public TextField messageBox;
    public Button sendMessage;
    @FXML
    public ImageView ImagContact;
    @FXML
    public Circle imageClip;

    @FXML
    public Label NameContact;
    public Button emojiButton;
    @FXML
    private ObservableList<MessageModel> chatMessages;
    private final StringProperty nameProperty = new SimpleStringProperty();

   // private int ChatID;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setChatController(this);
        System.out.println("ChatController: Initializing");
        NameContact.textProperty().bind(nameProperty);

        chatListView.setStyle("-fx-background-image: url('/images/defuatback.jpg')");
        chatMessages = FXCollections.observableArrayList();
        chatListView.setItems(chatMessages);
        chatListView.setCellFactory(param -> new CustomListCell());
        sendMessage.setOnAction(event -> {
            try {
                sendMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        messageBox.setOnAction(event -> {
            try {
                sendMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

         ImagContact.setFitWidth(imageClip.getRadius() * 2);
         ImagContact.setFitHeight(imageClip.getRadius() * 2);
         ImagContact.setClip(imageClip);

         nameProperty.set("Contact Name");


    }

 public void updateChatContent(String name, Image image) {
    Platform.runLater(() -> {
        nameProperty.set(name);
        if (image != null) {
            ImagContact.setImage(image);
        }
    });
}

    private void sendMessage() throws RemoteException {
    //    System.out.println(Model.getInstance().getViewFactory().getSelectedContact().getName().);
        //System.out.println();
        String message = messageBox.getText();
        if (message.isEmpty()) {
            return;
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageContent(message);
        messageModel.setSender(CurrentUser.getInstance());
        chatListView.getItems().add(messageModel);
        messageBox.clear();

        // Send the message to the server

        SendMessageRequest request = new SendMessageRequest();
        request.setMessageContent(message);

        UserModel userModel = new UserModel();
        userModel.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
        userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
        userModel.setUserName(CurrentUser.getCurrentUser().getUserName());
        userModel.setCountry(CurrentUser.getCurrentUser().getCountry());
        userModel.setUserStatus(CurrentUser.getCurrentUser().getUserStatus());
        userModel.setBio(CurrentUser.getCurrentUser().getBio());
        userModel.setDateOfBirth(CurrentUser.getInstance().getDateOfBirth());
        userModel.setEmailAddress(CurrentUser.getCurrentUser().getEmailAddress());

        request.setSender(userModel);

     //   request.setSender(CurrentUser.getInstance());  // CurrentUser is child of UserModel

      //  request.setSenderId(CurrentUser.getInstance().getUserID());
      //send message to receiver Id of selected contact

   //     System.out.println(Model.getInstance().getViewFactory().getSelectedContact().get().getId());

      //  request.setReceiverId(Model.getInstance().getViewFactory().getSelectedContact().get().getId());

//        request.setReceiverId(Model.getInstance().getViewFactory().getSelectedContact().get().getChatId());
        if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group) {
            request.setReceiverId(((Group) Model.getInstance().getViewFactory().getSelectedContact().get()).getGroupId());
        } else {
            request.setReceiverId(((ContactData)Model.getInstance().getViewFactory().getSelectedContact().get()).getChatId());
        }

        request.setAttachment(false);
        request.setTime(LocalDateTime.now());
        try {
          //  System.out.println(Model.getInstance().getViewFactory().getSelectedContact().get().getId());
            System.out.println(request);
            SendMessageResponse response = NetworkFactory.getInstance().sendMessage(request);
            System.out.println(response);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat/MessageBubble.fxml"));
                MessageBubbleController controller = new MessageBubbleController();
                controller.setMessage(item);
                loader.setController(controller);
                try {
                    Node node = loader.load();
                    setGraphic(node);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                setGraphic(null);
            }
            // Set the background color of the list cell to transparent
            this.setStyle("-fx-background-color: transparent;");
        }
    }

    public void setName(String name) {
        nameProperty.set(name);
        System.out.println("ChatController: Name set to " + name);
    }

    public void setImage( Image image){
        System.out.println("ChatController: Image set to " + image);
        ImagContact.setImage(image);
    }
    public void setNewMessage(MessageModel messageModel){
        System.out.println("New Message Arrive to chatList");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("Message Added to List in Run Platform");
                chatListView.getItems().add(messageModel);
            }
        });

    }

}