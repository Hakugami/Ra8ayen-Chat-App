package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Model.MessageModel;
import dto.requests.RetrieveAttachmentRequest;
import dto.responses.RetrieveAttachmentResponse;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
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
import java.text.SimpleDateFormat;
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
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            completeMessageHBox.nodeOrientationProperty().set(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
//            messageVBox.getChildren().remove(senderInfoHBox);
        });

    }

    private void displayOtherUserMessage() throws RemoteException {
        Platform.runLater(() -> {
            try {
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
            messageVBox.getStyleClass().add("receiver-bubble");
            completeMessageHBox.nodeOrientationProperty().set(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);

        });
    }

private void loadMessage() throws RemoteException {
    // Set the message content
    messageLabel.setText(message.getMessageContent());



    // Set the sender's name and phone number
    senderNameLabel.setText(message.getSender().getUserName());
    senderPhoneLabel.setText(message.getSender().getPhoneNumber());
    if (message.getTime() != null) {
//        messageTimeLabel.setText(message.getTime().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
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
        long totalBytes = attachmentData.length;
        if (attachmentData != null) {
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

                downloadThread.start();
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
                    style = "-fx-underline: true;";
                    messageLabel.setStyle(style);
                }
                else {
                    style = "-fx-underline: false;";
                    messageLabel.setStyle(style);
                }
                size = message.getStyleMessage().getFontSize();
                CustomFont = Font.font("Arial",fw,fp,size);
                System.out.println(fw + " " + fp + " " + size + " " + style+ "=------------------------------------------------------" );
                messageLabel.setFont(CustomFont);
                messageLabel.setStyle("-fx-text-fill: " +message.getStyleMessage().getFontColor()+ ";");
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