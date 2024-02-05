package controller;

import dto.Model.MessageModel;
import dto.Model.UserModel;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import dto.requests.VoiceCallRequest;
import dto.responses.GetMessageResponse;
import dto.responses.SendMessageResponse;
import dto.responses.VoiceCallResponse;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;
import model.ContactData;
import model.CurrentUser;
import model.Group;
import model.Model;
import network.NetworkFactory;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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
    public Button emojiButton; //refer to attachment
    public Button voiceChat;
    @FXML
    private ObservableList<MessageModel> chatMessages;
    private final StringProperty nameProperty = new SimpleStringProperty();

    private FileChooser fileChooser;

    private volatile byte[] uploadedFileBytes;

    private String FileName;

    public HBox customize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox root = null;
        try {
            root = Model.getInstance().getViewFactory().getCustomizeLabels();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (root != null) {
            customize.getChildren().add(root);
        }

        chatListView.prefWidthProperty().bind(((Pane) chatListView.getParent()).widthProperty());
        Model.getInstance().getControllerFactory().setChatController(this);
        System.out.println("ChatController: Initializing");
        NameContact.textProperty().bind(nameProperty);
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
        chatListView.getItems().addListener((javafx.collections.ListChangeListener<MessageModel>) c -> {
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(100),
                    ae -> chatListView.scrollTo(chatMessages.size() - 1)));
            timeline.play();
        });

        try {
            getMessageOfContact();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        voiceChat.setOnAction(event -> {
            Platform.runLater(() -> {
                try {
                    handleVoiceChat();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        });
    }


    public void receiveVoiceChatRequest(String phoneNumber) throws IOException {
        Popup popup = new Popup();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat/VoiceChatPopUp.fxml"));
        Parent root = loader.load();
        popup.getContent().add(root);

        VoiceChatPopUpController voiceChatPopUpController = loader.getController();
        voiceChatPopUpController.setPopup(popup, phoneNumber);

        popup.setAutoHide(true);

        // Show the popup first to calculate its height
        popup.show(voiceChat.getScene().getWindow());

        // Calculate the center coordinates of the screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = screenBounds.getWidth() / 2;
        double centerY = screenBounds.getHeight() / 2;

        // Calculate the x and y coordinates
        double x = centerX - popup.getWidth() / 2;
        double y = centerY - popup.getHeight() / 2;

        // Hide the popup
        popup.hide();

        // Show the popup again at the correct position
        popup.show(voiceChat.getScene().getWindow(), x, y);
    }


    private void handleVoiceChat() throws IOException {

        String phoneNumber = "";
        if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof ContactData) {
            phoneNumber = ((ContactData) Model.getInstance().getViewFactory().getSelectedContact().get()).getPhoneNumber();
        }
        System.out.println("Sending Voice Call Request to " + phoneNumber);
        VoiceCallRequest request = new VoiceCallRequest(phoneNumber, CurrentUser.getInstance().getPhoneNumber());
        try {
            VoiceCallResponse response = NetworkFactory.getInstance().connect(request);
            System.out.println("Voice call request sent" + response);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

//        Notifications.create()
//                .title("Voice Chat")
//                .text("Voice Chat Request Sent")
//                .showInformation();
//        FXMLLoader loader = new FXMLLoader();
//        Popup popup = new Popup();
//        Parent root = null;
//        try {
//            InputStream fxmlStream = getClass().getResourceAsStream("/fxml/chat/VoiceChatWait.fxml");
//            root = loader.load(fxmlStream);
//            Notifications.create()
//                    .title("Voice Chat")
//                    .text("Voice Chat Wait Controller is set")
//                    .showInformation();
//        } catch (IOException e) {
//            Notifications.create()
//                    .title("Voice Chat controller")
//                    .text(e.getMessage())
//                    .showInformation();
//            throw new RuntimeException(e);
//        }
        Popup popup = new Popup();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat/VoiceChatWait.fxml"));
        Parent root = loader.load();

        popup.getContent().add(root);

        VoiceChatWaitController voiceChatWaitController = loader.getController();
        voiceChatWaitController.setPopup(popup, phoneNumber, CurrentUser.getInstance().getPhoneNumber());

        popup.setAutoHide(true);

        // Show the popup first to calculate its height
        popup.show(voiceChat.getScene().getWindow());

        // Calculate the center coordinates of the screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = screenBounds.getWidth() / 2;
        double centerY = screenBounds.getHeight() / 2;

        // Calculate the x and y coordinates
        double x = centerX - popup.getWidth() / 2;
        double y = centerY - popup.getHeight() / 2;

        // Hide the popup
        popup.hide();

        // Show the popup again at the correct position
        popup.show(voiceChat.getScene().getWindow(), x, y);
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
        messageModel.setSender(CurrentUser.getInstance());
        try {
            messageModel.setStyleMessage(Model.getInstance().getControllerFactory().getCustomizeController().getMessageStyle());
            Model.getInstance().getControllerFactory().getCustomizeController().setNewStyle();
            System.out.println("Message Style set from chat controller");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (uploadedFileBytes != null) {
            messageModel.setAttachment(true);
            messageModel.setAttachmentData(uploadedFileBytes);
            messageModel.setMessageContent(FileName);
        } else {
            messageModel.setMessageContent(message);
        }

        chatListView.getItems().add(messageModel);
        messageBox.clear();

        // Send the message to the server

        SendMessageRequest request = new SendMessageRequest();
        request.setMessageContent(message);

        request.setStyleMessage(messageModel.getStyleMessage());

        UserModel userModel = new UserModel();
        userModel.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
        userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
        userModel.setUserName(CurrentUser.getCurrentUser().getUserName());
        userModel.setCountry(CurrentUser.getCurrentUser().getCountry());
        userModel.setUserStatus(CurrentUser.getCurrentUser().getUserStatus());
        userModel.setBio(CurrentUser.getCurrentUser().getBio());
        userModel.setDateOfBirth(CurrentUser.getInstance().getDateOfBirth());
        userModel.setEmailAddress(CurrentUser.getCurrentUser().getEmailAddress());
        request.setSenderId(CurrentUser.getInstance().getUserID());
        request.setSender(userModel);


        if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group) {
            request.setReceiverId(((Group) Model.getInstance().getViewFactory().getSelectedContact().get()).getGroupId());

            request.setGroupMessage(true);
        } else {
            request.setReceiverId(((ContactData) Model.getInstance().getViewFactory().getSelectedContact().get()).getChatId());
            request.setGroupMessage(false);
        }
        if (uploadedFileBytes == null) {
            System.out.println("UploadFile NULL");
            request.setIsAttachment(false);
        } else {
            request.setIsAttachment(true);
            request.setAttachmentData(uploadedFileBytes);
        }
        request.setTime(LocalDateTime.now());

        try {
            //  System.out.println(Model.getInstance().getViewFactory().getSelectedContact().get().getId());
            System.out.println(request);
            CurrentUser.getInstance().addMessageToCache(request.getReceiverId(), messageModel);
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
                Task<Node> loadFXMLTask = new Task<Node>() {
                    @Override
                    protected Node call() throws Exception {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat/MessageBubble.fxml"));
                        MessageBubbleController controller = new MessageBubbleController();
                        if(item.getStyleMessage()!=null)
                            System.out.println("Message Style arrive to update item");
                        controller.setMessage(item);

                        loader.setController(controller);
                        return loader.load();
                    }
                };

                loadFXMLTask.setOnSucceeded(event -> {
                    setGraphic(loadFXMLTask.getValue());
                });

                loadFXMLTask.setOnFailed(event -> {
                    Throwable exception = loadFXMLTask.getException();
                    throw new RuntimeException(exception);
                });

                new Thread(loadFXMLTask).start();
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

    public void setImage(Image image) {
        System.out.println("ChatController: Image set to " + image);
        ImagContact.setImage(image);
    }

    public void setNewMessage(MessageModel messageModel) {
        System.out.println("New Message Arrive to chatList");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("Message Added to List in Run Platform");
                //   chatListView.getItems().add(messageModel);
                chatMessages.add(messageModel);
            }
        });

    }

    public void getMessageOfContact() throws RemoteException, NotBoundException {
        GetMessageRequest getMessageRequest = new GetMessageRequest();
        //System.out.println(((ContactData)Model.getInstance().getViewFactory().getSelectedContact().get()).getChatId());

        //getMessageRequest.setChatId();
        if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof ContactData) {
            getMessageRequest.setChatId(((ContactData) Model.getInstance().getViewFactory().getSelectedContact().get()).getChatId());

            retrieveMessages(getMessageRequest);
        } else if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group) {
            System.out.println("Group Chat condition");
            getMessageRequest.setChatId(((Group) Model.getInstance().getViewFactory().getSelectedContact().get()).getGroupId());
            retrieveMessages(getMessageRequest);
        }
    }

    //    private void retrieveMessages(GetMessageRequest getMessageRequest) throws RemoteException, NotBoundException {
//        System.out.println("Retrieving Messages of ChatID " + getMessageRequest.getChatId() );
//        getMessageRequest.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
//        GetMessageResponse getMessageResponse = NetworkFactory.getInstance().getMessageOfChatID(getMessageRequest);
//        if (getMessageResponse == null) {
//            System.out.println("No Message to this Contact Found");
//        } else {
//            System.out.println("Message Size " + getMessageResponse.getMessageList().size());
//
//            for(MessageModel messageModel:getMessageResponse.getMessageList()){
//                if(messageModel.getIsAttachment()) {
//                    System.out.println("Message With Attachment " + messageModel.getIsAttachment() + "Message Attatchment Size "+messageModel.getAttachmentData().length);
//                }
//            }
//            Platform.runLater(() -> {
//                chatMessages.clear();
//                chatMessages.setAll(getMessageResponse.getMessageList());
//            });
//        }
//    }
    public void retrieveMessagesByChatId(int chatId) {
        GetMessageRequest getMessageRequest = new GetMessageRequest();
        getMessageRequest.setChatId(chatId);
        Task<Void> databaseQueryTask = createDatabaseQueryTask(getMessageRequest);
        new Thread(databaseQueryTask).start();
    }

//    private void retrieveMessages(GetMessageRequest getMessageRequest) throws RemoteException {
//        System.out.println("Retrieving Messages of ChatID " + getMessageRequest.getChatId());
//        getMessageRequest.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
//
//
//        Task<Void> serverRetrievalTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                // Check if the cache is empty
//                if (CurrentUser.getInstance().getChatMessageMap().get(getMessageRequest.getChatId()) == null || CurrentUser.getInstance().getChatMessageMap().get(getMessageRequest.getChatId()).isEmpty()) {
//                    // If the cache is empty, retrieve the messages from the server
//                    GetMessageResponse getMessageResponse = NetworkFactory.getInstance().getMessageOfChatID(getMessageRequest);
//                    if (getMessageResponse == null) {
//                        System.out.println("No Message to this Contact Found");
//                    } else {
//                        System.out.println("Message Size " + getMessageResponse.getMessageList().size());
//                        Platform.runLater(() -> {
//                            chatMessages.clear();
//                            chatMessages.addAll(getMessageResponse.getMessageList());
//                        });
//                        // Update the cache with the retrieved messages
//                        for (MessageModel message : getMessageResponse.getMessageList()) {
//                            CurrentUser.getInstance().addMessageToCache(getMessageRequest.getChatId(), message);
//                        }
//                    }
//                } else {
//                    // If the cache is not empty, display the messages from the cache
//                    Platform.runLater(() -> {
//                        try {
//                            List<MessageModel> newMessages = CurrentUser.getInstance().getChatMessageMap().get(getMessageRequest.getChatId());
//                            for (MessageModel newMessage : newMessages) {
//                                if (!chatMessages.contains(newMessage)) {
//                                    chatMessages.add(newMessage);
//                                }
//                            }
//                        } catch (RemoteException e) {
//                            throw new RuntimeException(e);
//                        }
//                    });
//                    // Fetch the latest messages from the server
//                    GetMessageResponse getMessageResponse = NetworkFactory.getInstance().getMessageOfChatID(getMessageRequest);
//                    if (getMessageResponse != null) {
//                        System.out.println("Message Size " + getMessageResponse.getMessageList().size());
//                        Platform.runLater(() -> {
//                            chatMessages.addAll(getMessageResponse.getMessageList());
//                        });
//                        // Update the cache with the retrieved messages
//                        for (MessageModel message : getMessageResponse.getMessageList()) {
//                            // Add the sender's and receiver's profile pictures to the image cache
//                            BufferedImage senderImage = ImageUtls.convertByteToImage(message.getSender().getProfilePicture());
//                            BufferedImage receiverImage = ImageUtls.convertByteToImage(message.getReceiver().getProfilePicture());
//                            Image fxSenderImage = SwingFXUtils.toFXImage(senderImage, null);
//                            Image fxReceiverImage = SwingFXUtils.toFXImage(receiverImage, null);
//                            CurrentUser.getInstance().addImageToCache(message.getSender().getUserID(), fxSenderImage);
//                            CurrentUser.getInstance().addImageToCache(message.getReceiver().getUserID(), fxReceiverImage);
//
//                            // Remove the profile pictures from the MessageModel
//                            message.getSender().setProfilePicture(null);
//                            message.getReceiver().setProfilePicture(null);
//
//                            if (!CurrentUser.getInstance().isMessageCached(message)) {
//                                CurrentUser.getInstance().addMessageToCache(getMessageRequest.getChatId(), message);
//                            }
//                        }
//                    }
//                }
//                return null;
//            }
//        };
//
//        serverRetrievalTask.setOnFailed(event -> {
//            Throwable exception = serverRetrievalTask.getException();
//            // Handle exception here
//            System.out.println("Failed to retrieve messages: " + exception.getMessage());
//        });
//
//        new Thread(serverRetrievalTask).start();
//    }

    // In ChatController.java
//    private void retrieveMessages(GetMessageRequest getMessageRequest) throws RemoteException {
//        System.out.println("Retrieving Messages of ChatID " + getMessageRequest.getChatId());
//        getMessageRequest.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
//
//
//        Task<Void> serverRetrievalTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                // Check if the cache is empty
//                GetMessageResponse getMessageResponse = NetworkFactory.getInstance().getMessageOfChatID(getMessageRequest);
//                if (getMessageResponse == null) {
//                    System.out.println("No Message to this Contact Found");
//                } else {
//                    System.out.println("Message Size " + getMessageResponse.getMessageList().size());
//                    Platform.runLater(() -> {
//                        // Only add messages that are not already in the chatMessages list
//                        for (MessageModel message : getMessageResponse.getMessageList()) {
//                            if (!chatMessages.contains(message)) {
//                                chatMessages.add(message);
//                            }
//                        }
//                    });
//                    // Update the cache with the retrieved messages
//                    for (MessageModel message : getMessageResponse.getMessageList()) {
//                        // Add the sender's and receiver's profile pictures to the image cache
//                        BufferedImage senderImage = ImageUtls.convertByteToImage(message.getSender().getProfilePicture());
//                        BufferedImage receiverImage = ImageUtls.convertByteToImage(message.getReceiver().getProfilePicture());
//                        Image fxSenderImage = SwingFXUtils.toFXImage(senderImage, null);
//                        Image fxReceiverImage = SwingFXUtils.toFXImage(receiverImage, null);
//                        CurrentUser.getInstance().addImageToCache(message.getSender().getUserID(), fxSenderImage);
//                        CurrentUser.getInstance().addImageToCache(message.getReceiver().getUserID(), fxReceiverImage);
//
//                        // Remove the profile pictures from the MessageModel
//                        message.getSender().setProfilePicture(null);
//                        message.getReceiver().setProfilePicture(null);
//                        if (!CurrentUser.getInstance().isMessageCached(message)) {
//                            CurrentUser.getInstance().addMessageToCache(getMessageRequest.getChatId(), message);
//                        }
//                    }
//                }
//                return null;
//            }
//        };
//
//        serverRetrievalTask.setOnFailed(event -> {
//            Throwable exception = serverRetrievalTask.getException();
//            // Handle exception here
//            System.out.println("Failed to retrieve messages: " + exception.getMessage());
//        });
//
//        new Thread(serverRetrievalTask).start();
//    }

//    private void retrieveMessages(GetMessageRequest getMessageRequest) throws RemoteException {
//        System.out.println("Retrieving Messages of ChatID " + getMessageRequest.getChatId());
//        getMessageRequest.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
//
//        // Task for querying the database and updating the cache
//        Task<Void> databaseQueryTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                GetMessageResponse getMessageResponse = NetworkFactory.getInstance().getMessageOfChatID(getMessageRequest);
//                if (getMessageResponse != null) {
//                    System.out.println("Message Size " + getMessageResponse.getMessageList().size());
//                    // Update the cache with the retrieved messages
//                    for (MessageModel message : getMessageResponse.getMessageList()) {
//                        if (!CurrentUser.getInstance().isMessageCached(message)) {
//                            CurrentUser.getInstance().addMessageToCache(getMessageRequest.getChatId(), message);
//                        }
//                    }
//                }
//                return null;
//            }
//        };
//
//        // Task for displaying the messages from the cache
//        Task<Void> displayTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                List<MessageModel> cachedMessages = CurrentUser.getInstance().getChatMessageMap().get(getMessageRequest.getChatId());
//                if (cachedMessages != null) {
//                    Platform.runLater(() -> {
//                        // Use a Set to keep track of the messages that have already been added
//                        Set<MessageModel> addedMessages = new HashSet<>(chatMessages);
//                        for (MessageModel cachedMessage : cachedMessages) {
//                            // Only add the message to the chatMessages list if it's not already in the set
//                            if (!addedMessages.contains(cachedMessage)) {
//                                chatMessages.add(cachedMessage);
//                                addedMessages.add(cachedMessage);
//                            }
//                        }
//                    });
//                }
//                return null;
//            }
//        };
//
//        // Add a listener to the databaseQueryTask to start the displayTask after it's done
//        databaseQueryTask.stateProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == Worker.State.SUCCEEDED) {
//                new Thread(displayTask).start();
//            }
//        });
//
//        // Start the databaseQueryTask
//        new Thread(databaseQueryTask).start();
//    }

private void retrieveMessages(GetMessageRequest getMessageRequest) throws RemoteException {
    System.out.println("Retrieving Messages of ChatID " + getMessageRequest.getChatId());
    getMessageRequest.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());

    Task<Void> displayTask = createDisplayTask(getMessageRequest);

    new Thread(displayTask).start();


}


    @FXML
    void SendAttachment() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fileChooser = new FileChooser();
                ChooseFileToSend();
            }
        });
    }

    public void ChooseFileToSend() {
        final long MAX_FILE_SIZE = 1024 * 1024; // 1 MB

        AtomicReference<File> selectedFile = new AtomicReference<>();
        selectedFile.set(fileChooser.showOpenDialog(null));
        if (selectedFile.get() != null) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (selectedFile.get().length() > MAX_FILE_SIZE) {
                        System.out.println("File Size is Big");
                    } else {
                        try {
                            uploadedFileBytes = Files.readAllBytes(selectedFile.get().toPath());
                            FileName = selectedFile.get().getName();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageBox.setText(FileName);
                                    messageBox.setEditable(false);
                                }
                            });
                            System.out.println("uploaded File Size is : " + uploadedFileBytes.length);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            t1.start();
        }
    }

    private Task<Void> createDatabaseQueryTask(GetMessageRequest getMessageRequest) {
    return new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            GetMessageResponse getMessageResponse = NetworkFactory.getInstance().getMessageOfChatID(getMessageRequest);
            if (getMessageResponse != null) {
                System.out.println("Message Size " + getMessageResponse.getMessageList().size());
                // Update the cache with the retrieved messages
                for (MessageModel message : getMessageResponse.getMessageList()) {
                    // Add the sender's and receiver's profile pictures to the image cache
//                    BufferedImage senderImage = ImageUtls.convertByteToImage(message.getSender().getProfilePicture());
//                    BufferedImage receiverImage = ImageUtls.convertByteToImage(message.getReceiver().getProfilePicture());
//                    Image fxSenderImage = SwingFXUtils.toFXImage(senderImage, null);
//                    Image fxReceiverImage = SwingFXUtils.toFXImage(receiverImage, null);
//                    CurrentUser.getInstance().addImageToCache(message.getSender().getUserID(), fxSenderImage);
//                    CurrentUser.getInstance().addImageToCache(message.getReceiver().getUserID(), fxReceiverImage);
//
//                    // Remove the profile pictures from the MessageModel
//                    message.getSender().setProfilePicture(null);
//                    message.getReceiver().setProfilePicture(null);
//                    if(message.getChatId()==4){
//                        System.out.println("GROUP 4 works paleees"+message.getMessageContent());
//                    }

                    if (!CurrentUser.getInstance().isMessageCached(message)) {
                        CurrentUser.getInstance().addMessageToCache(getMessageRequest.getChatId(), message);
                    }
                }
            }
            return null;
        }
    };
}

private Task<Void> createDisplayTask(GetMessageRequest getMessageRequest) {
    return new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            List<MessageModel> cachedMessages = CurrentUser.getInstance().getChatMessageMap().get(getMessageRequest.getChatId());
            if (cachedMessages != null) {
                Platform.runLater(() -> {
                    // Use a Set to keep track of the messages that have already been added
                    Set<MessageModel> addedMessages = new HashSet<>(chatMessages);
                    for (MessageModel cachedMessage : cachedMessages) {
                        // Only add the message to the chatMessages list if it's not already in the set
                        if (!addedMessages.contains(cachedMessage)) {
                            Platform.runLater(()->chatMessages.add(cachedMessage));
                            addedMessages.add(cachedMessage);
                        }
                    }
                });
            }
            return null;
        }
    };
}




    public void setStyle(String style) {
        System.out.println(style);
        messageBox.setStyle(style);
    }
    public void setColor(String textColor){
        messageBox.setStyle("-fx-text-fill: " +textColor+ ";");
    }


    public void setBackgroundColor(String backgroundColor){
        messageBox.setStyle("-fx-background-color: " +backgroundColor + ";");
    }

}