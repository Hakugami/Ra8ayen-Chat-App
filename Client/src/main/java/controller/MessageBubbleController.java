package controller;

import concurrency.manager.ConcurrencyManager;
import dto.Model.MessageModel;
import dto.Model.UserModel;
import dto.requests.RetrieveAttachmentRequest;
import dto.responses.RetrieveAttachmentResponse;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MessageBubbleController implements Initializable {
    @FXML
    private HBox completeMessageHBox;
    @FXML
    private Label messageLabel;
    @FXML
    private ImageView senderImage;
    @FXML
    private VBox messageVBox;
    @FXML
    private HBox senderInfoHBox;
    @FXML
    private Label senderNameLabel;
    @FXML
    private Label senderPhoneLabel;
    @FXML
    private HBox messageAttachmentHBox;
    @FXML
    private ImageView fileIconImageView;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label fileSizeLabel;
    @FXML
    private TextFlow messageContentTextFlow;
    @FXML
    private HBox messageTimeHBox;
    @FXML
    private Label messageTimeLabel;
    @FXML
    private Pane messageStatusPane;
    @FXML
    private Label messageStatusLabel;
    @FXML
    private Circle circlePic;
    private MessageModel message;

    private volatile byte[]  uploadedFileBytes;


    FileChooser fileSaver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //displayCurrentUserMessage();
        messageLabel.maxWidthProperty().bind(
                Model.getInstance().getControllerFactory().getChatController().chatListView.widthProperty().multiply(0.7));
        completeMessageHBox.prefWidthProperty().bind(messageLabel.widthProperty());
        completeMessageHBox.prefHeightProperty().bind(messageLabel.heightProperty());
        try {
            if (message.getSender().getUserID()== CurrentUser.getInstance().getUserID()) {
                displayCurrentUserMessage();
            } else {
                displayOtherUserMessage();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        senderImage.setOnMouseClicked(event -> {
            try {
                showProfile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        circlePic.setOnMouseClicked(event -> {
            try {
                showProfile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void showProfile() throws IOException {
        Popup popup = new Popup();
        UserModel user = message.getSender();
        Parent root = (Parent) Model.getInstance().getViewFactory().getOthersProfile();
        popup.getContent().add(root);
        OthersProfileController controller = Model.getInstance().getControllerFactory().getOthersProfileController();
        controller.setPopup(popup, user);
        controller.setData();
        if(controller.checkIfUserFriend()){
            controller.checkIfBlocked();
        }

        popup.show(senderImage.getScene().getWindow());
        //show the left of the chat window
        double x = senderImage.localToScreen(senderImage.getBoundsInLocal()).getMinX() + senderImage.getFitWidth()/2;
        double y = senderImage.localToScreen(senderImage.getBoundsInLocal()).getMinY() - popup.getHeight() + 10;

        popup.show(senderImage.getScene().getWindow(), x, y);

        popup.setAutoHide(true);


    }


    public  void setMessage(MessageModel message) {
        this.message = message;
    }

    private void displayCurrentUserMessage() {
        Platform.runLater(() -> {
            try {
                loadMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                senderImage.setImage(CurrentUser.getInstance().getProfilePictureImage());
                circlePic.setFill(new ImagePattern(CurrentUser.getInstance().getProfilePictureImage()));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            completeMessageHBox.nodeOrientationProperty().set(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            messageVBox.getChildren().remove(senderInfoHBox);
            if(!message.isAttachment()){
                messageVBox.getChildren().remove(messageAttachmentHBox);
            }
        });

    }

    private void displayOtherUserMessage() throws RemoteException {
        Platform.runLater(() -> {
            try {
                if(message.getStyleMessage()!=null ) {
                    if (message.getStyleMessage().getBackgroundColor()!=null&&message.getStyleMessage().getBackgroundColor().equals("#ED7D31")) {
                        message.getStyleMessage().setBackgroundColor("#6C5F5B");
                    }
                }
                loadMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            // Get the sender's profile picture from the image cache
            Image senderProfilePicture = null;
            try {
                senderProfilePicture = CurrentUser.getInstance().getImageFromCache(message.getSender().getUserID());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            // If the image is not in the cache, convert it from bytes to an image and add it to the cache
            if (senderProfilePicture == null) {
                BufferedImage bufferedImage = ImageUtls.convertByteToImage(message.getSender().getProfilePicture());
                senderProfilePicture = SwingFXUtils.toFXImage(bufferedImage, null);
                try {
                    CurrentUser.getInstance().addImageToCache(message.getSender().getUserID(), senderProfilePicture);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            // Set the sender's profile picture
            senderImage.setImage(senderProfilePicture);
            circlePic.setFill(new ImagePattern(senderProfilePicture));
            messageVBox.getStyleClass().add("receiver-bubble");
            completeMessageHBox.nodeOrientationProperty().set(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
            if(!message.isAttachment()){
                messageVBox.getChildren().remove(messageAttachmentHBox);
            }

        });
    }

private void loadMessage() throws RemoteException {
    // Set the message content
    messageLabel.setText(message.getMessageContent());
    // Set the sender's name and phone number

    if (message.getTime() != null) {
//        messageTimeLabel.setText(message.getTime().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String time = message.getTime().format(formatter);
        messageTimeLabel.setText(time);

    } else {
        messageTimeLabel.setText(""); // or set a default value
    }

    // If the message has an attachment, set the file icon and file size
    if (message.isAttachment()) {
        fileIconImageView.setVisible(true);
    }
    setMessageStyle();
}
//    @FXML
//    void openDownloadSelector() throws NotBoundException, RemoteException {
//        if (message.isAttachment()) {
//            RetrieveAttachmentResponse retrieveAttachmentResponse = NetworkFactory.getInstance().retrieveAttachment(new RetrieveAttachmentRequest(message.getMessageId()));
//            message.setAttachmentData(retrieveAttachmentResponse.getAttachmentData());
//            if (message.getAttachmentData() != null) {
//                fileSaver = new FileChooser();
//                fileSaver.setTitle("Save File");
//                fileSaver.setInitialFileName(message.getMessageContent());
//                File fileToSave = fileSaver.showSaveDialog(null);
//                if (fileToSave != null) {
//                    saveFile(fileToSave);
//                }
//
//            }
//        }
//    }
@FXML
void openDownloadSelector() throws NotBoundException, RemoteException {
    if (message.isAttachment()) {
        System.out.println("Open download selector");
        RetrieveAttachmentResponse retrieveAttachmentResponse = NetworkFactory.getInstance().retrieveAttachment(new RetrieveAttachmentRequest(message.getMessageId()));
        byte[] attachmentData = retrieveAttachmentResponse.getAttachmentData();
        if (attachmentData != null) {
            long totalBytes = attachmentData.length;
            InputStream inputStream = new ByteArrayInputStream(attachmentData);
            fileSaver = new FileChooser();
            fileSaver.setTitle("Save File");
            fileSaver.setInitialFileName(message.getMessageContent());
            File fileToSave = fileSaver.showSaveDialog(null);

            if (fileToSave != null) {
                fileNameLabel.setVisible(false);
                Thread downloadThread = new Thread(() -> {
                    try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        long totalBytesRead = 0;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;

                            double progress = (double) totalBytesRead / totalBytes;

                            long finalTotalBytesRead = totalBytesRead;
                            Platform.runLater(() -> {
                                fileSizeLabel.setText(finalTotalBytesRead + " / " + totalBytes + " bytes");
                            });
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                ConcurrencyManager.getInstance().submitRunnable(downloadThread);
            }
        }
    }
}
    void saveFile(File fileToSave){
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Files.write(fileToSave.toPath(),message.getAttachmentData(), StandardOpenOption.CREATE);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            t2.start();
        }

      private void setMessageStyle(){
          Font CustomFont;
          String style;
          FontWeight fw;
          FontPosture fp;
          int size;
          if(message.isAttachment()){
              try {
                  message.setStyleMessage(Model.getInstance().getControllerFactory().getCustomizeController().getDefaultStyle());
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
          }else{
            if(message.getStyleMessage()!=null){
                if(message.getStyleMessage().isBold()){
                    fw= FontWeight.BOLD;
                }else{
                    fw = FontWeight.NORMAL;
                }
                if(message.getStyleMessage().isItalic()){
                    fp= FontPosture.ITALIC;
                }else{
                    fp= FontPosture.REGULAR;
                }
                if(message.getStyleMessage().isUnderline()){
                    System.out.println("Underline");
                    style = "-fx-underline: true;";
                    messageLabel.setStyle(style);
                    messageLabel.setUnderline(true);
                }
                else {
                    System.out.println("Not Underline");
                    style = "-fx-underline: false;";
                    messageLabel.setStyle(style);
                    messageLabel.setUnderline(false);
                }
                size = message.getStyleMessage().getFontSize();
                CustomFont = Font.font(message.getStyleMessage().getFontStyle(),fw,fp,size);
                System.out.println(fw + " " + fp + " " + size + " " + style+ "=------------------------------------------------------" );
                messageLabel.setStyle("-fx-text-fill: " +message.getStyleMessage().getFontColor()+ ";");
                messageLabel.setFont(CustomFont);

                messageVBox.setStyle("-fx-background-color: " +message.getStyleMessage().getBackgroundColor()+ ";");
                }
            }
          /*try {
              Model.getInstance().getControllerFactory().getCustomizeController().setDefaultStyle();
          } catch (IOException e) {
              throw new RuntimeException(e);
          }*/
      }
}