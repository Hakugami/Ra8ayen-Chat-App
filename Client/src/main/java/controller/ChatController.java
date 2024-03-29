package controller;

import concurrency.manager.ConcurrencyManager;
import dto.Model.MessageModel;
import dto.Model.StyleMessage;
import dto.Model.UserModel;
import dto.requests.ChatBotRequest;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import dto.requests.VoiceCallRequest;
import dto.responses.ChatBotResponse;
import dto.responses.GetMessageResponse;
import dto.responses.SendMessageResponse;
import dto.responses.VoiceCallResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Screen;
import model.ContactData;
import model.CurrentUser;
import model.Group;
import model.Model;
import network.NetworkFactory;

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

        popup.setAutoHide(false);

        // Show the popup first to calculate its height
        popup.show(Model.getInstance().getViewFactory().getStage());

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
        popup.show(Model.getInstance().getViewFactory().getStage(), x, y);
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

        popup.setAutoHide(false);

        // Show the popup first to calculate its height
        popup.show(Model.getInstance().getViewFactory().getStage());

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
        popup.show(Model.getInstance().getViewFactory().getStage(), x, y);
    }

    public void updateChatContent(String name, Image image) {
        Platform.runLater(() -> {
            nameProperty.set(name);
            if (image != null) {
                ImagContact.setImage(image);
            }
        });
    }

    public void botSendMessage(MessageModel model) {
        System.out.println("Bot Message Arrive to Chat Controller");
        ChatBotRequest chatBotRequest = new ChatBotRequest(model.getMessageContent());
        try {
            StyleMessage styleMessage = new StyleMessage();
            styleMessage.setBold(true);
            styleMessage.setItalic(false);
            styleMessage.setUnderline(false);
            styleMessage.setFontSize(16);
            styleMessage.setFontStyle("Arial");
            styleMessage.setFontColor("#000000");
            styleMessage.setBackgroundColor("#ffffff");
            ChatBotResponse chatBotResponse = NetworkFactory.getInstance().chatBot(chatBotRequest);
            MessageModel messageModel = new MessageModel();
            messageModel.setMessageContent(chatBotResponse.getChatBotResponse());
            messageModel.setSender(CurrentUser.getInstance());
            messageModel.setStyleMessage(styleMessage);
            chatListView.getItems().add(messageModel);

            SendMessageRequest request = new SendMessageRequest();
            request.setMessageContent(chatBotResponse.getChatBotResponse());

            UserModel userModel = new UserModel();
            userModel.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
            userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
            userModel.setUserName(CurrentUser.getCurrentUser().getUserName());
            userModel.setCountry(CurrentUser.getCurrentUser().getCountry());
            userModel.setUserStatus(CurrentUser.getCurrentUser().getUserStatus());
            userModel.setBio(CurrentUser.getCurrentUser().getBio());
            userModel.setDateOfBirth(CurrentUser.getInstance().getDateOfBirth());
            userModel.setEmailAddress(CurrentUser.getCurrentUser().getEmailAddress());
            userModel.setGender(CurrentUser.getCurrentUser().getGender());
            request.setSenderId(CurrentUser.getInstance().getUserID());
            request.setSender(userModel);


            if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group) {
                request.setReceiverId(((Group) Model.getInstance().getViewFactory().getSelectedContact().get()).getGroupId());

                request.setGroupMessage(true);
            } else {
                request.setReceiverId(((ContactData) Model.getInstance().getViewFactory().getSelectedContact().get()).getChatId());
                request.setGroupMessage(false);
            }
            request.setAttachmentData(null);
            request.setIsAttachment(false);

            messageModel.setTime(LocalDateTime.now());
            request.setTime(LocalDateTime.now());

            request.setStyleMessage(styleMessage);

            try {
                CurrentUser.getInstance().addMessageToCache(request.getReceiverId(), messageModel);
                SendMessageResponse response = NetworkFactory.getInstance().sendMessage(request);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Failed to send message to chat bot: " + e.getMessage());
        }
    }


    private void sendMessage() throws RemoteException {

        String message = messageBox.getText();
        if (message.isEmpty()) {
            return;
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setSender(CurrentUser.getInstance());
        try {
            messageModel.setStyleMessage(Model.getInstance().getControllerFactory().getCustomizeController().getMessageStyle());
            Model.getInstance().getControllerFactory().getCustomizeController().setNewStyle();
            messageModel.setTime(LocalDateTime.now());

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

        UserModel userModel = getUserModel();
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

            request.setIsAttachment(false);
        } else {
            request.setIsAttachment(true);
            request.setAttachmentData(uploadedFileBytes);
        }
        messageModel.setTime(LocalDateTime.now());
        request.setTime(LocalDateTime.now());

        try {
            CurrentUser.getInstance().addMessageToCache(request.getReceiverId(), messageModel);
            Model.getInstance().getViewFactory().refreshLatestMessages();
            SendMessageResponse response = NetworkFactory.getInstance().sendMessage(request);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    private static UserModel getUserModel() throws RemoteException {
        UserModel userModel = new UserModel();
        userModel.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
        userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
        userModel.setUserName(CurrentUser.getCurrentUser().getUserName());
        userModel.setCountry(CurrentUser.getCurrentUser().getCountry());
        userModel.setUserStatus(CurrentUser.getCurrentUser().getUserStatus());
        userModel.setBio(CurrentUser.getCurrentUser().getBio());
        userModel.setDateOfBirth(CurrentUser.getInstance().getDateOfBirth());
        userModel.setGender(CurrentUser.getCurrentUser().getGender());
        userModel.setEmailAddress(CurrentUser.getCurrentUser().getEmailAddress());
        userModel.setPhoneNumber(CurrentUser.getCurrentUser().getPhoneNumber());
        return userModel;
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
                        if (item.getStyleMessage() != null)
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

                ConcurrencyManager.getInstance().submitTask(loadFXMLTask);
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


    public void retrieveMessagesByChatId(int chatId) {
        GetMessageRequest getMessageRequest = new GetMessageRequest();
        getMessageRequest.setChatId(chatId);
        Task<Void> databaseQueryTask = createDatabaseQueryTask(getMessageRequest);
        ConcurrencyManager.getInstance().submitTask(databaseQueryTask);
    }



    private void retrieveMessages(GetMessageRequest getMessageRequest) throws RemoteException {
        System.out.println("Retrieving Messages of ChatID " + getMessageRequest.getChatId());
        getMessageRequest.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());

        Task<Void> displayTask = createDisplayTask(getMessageRequest);
        displayTask.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                try {
                    Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
//                if (!chatMessages.isEmpty()) {
//                    Model.getInstance().getControllerFactory().getChatController().chatListView.scrollTo(chatMessages.getLast());
                    double cellHeight = 24.0; // This should be the height of your cell. Adjust as necessary.
                    double listViewHeight = chatListView.getHeight();
                    int visibleCells = (int) Math.floor(listViewHeight / cellHeight);

                    int indexToScroll = Math.max(chatMessages.size() - visibleCells, 0);
                    chatListView.scrollTo(indexToScroll);
//                }
            });
        });

        ConcurrencyManager.getInstance().submitTask(displayTask);


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
        final long MAX_FILE_SIZE = (1024 * 1024) * 1000; // 1 MB x 1000

        AtomicReference<File> selectedFile = new AtomicReference<>();
        selectedFile.set(fileChooser.showOpenDialog(null));
        if (selectedFile.get() != null) {
            Thread t1 = new Thread(() -> {
                if (selectedFile.get().length() > MAX_FILE_SIZE) {
                    System.out.println("File Size is Big");
                } else {
                    try {
                        uploadedFileBytes = Files.readAllBytes(selectedFile.get().toPath());
                        FileName = selectedFile.get().getName();
                        Platform.runLater(() -> {
                            messageBox.setText(FileName);
                        });
                        System.out.println("uploaded File Size is : " + uploadedFileBytes.length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            ConcurrencyManager.getInstance().submitRunnable(t1);
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
                        if (!CurrentUser.getInstance().isMessageCached(message)) {
                            System.out.println("Adding message to cache chatId " + getMessageRequest.getChatId());
                            CurrentUser.getInstance().addMessageToCache(getMessageRequest.getChatId(), message);
                        }
                    }
                }
                return null;
            }
        };
    }

    private Task<Void> createDisplayTask(GetMessageRequest getMessageRequest) {
        return new Task<>() {
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
                                chatMessages.add(cachedMessage);
                                addedMessages.add(cachedMessage);
                            }
                        }
                        // Set the last message of each chatId as the lastMessage
                        if (!cachedMessages.isEmpty()) {
                            MessageModel lastMessage = cachedMessages.get(cachedMessages.size() - 1);
                            // Assuming ContactData and Group classes have a setLastMessage method
                            if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof ContactData) {
                                ContactData contactData = (ContactData) Model.getInstance().getViewFactory().getSelectedContact().get();
                                if (contactData.getChatId() == getMessageRequest.getChatId()) {
                                    contactData.setLastMessage(lastMessage.getMessageContent());
                                }
                            } else if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group) {
                                Group group = (Group) Model.getInstance().getViewFactory().getSelectedContact().get();
                                if (group.getGroupId() == getMessageRequest.getChatId()) {
                                    group.setLastMessage(lastMessage.getMessageContent());
                                }
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
        String existingStyle = messageBox.getStyle() + style;
        messageBox.setStyle(existingStyle);
    }

    public void setColor(String textColor) {
        String existingStyle = messageBox.getStyle() + "-fx-text-fill: " + textColor + ";";
        messageBox.setStyle(existingStyle);
    }


    public void setBackgroundColor(String backgroundColor) {
        String existingStyle = messageBox.getStyle() + "-fx-background-color: " + backgroundColor + ";";
        messageBox.setStyle(existingStyle);
    }

    public void setFontFamily(String FontFamily) {
        System.out.println(FontFamily);

        Font font = Font.font(FontFamily);
        messageBox.setFont(font);
    }

}