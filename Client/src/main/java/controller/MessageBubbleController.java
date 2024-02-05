package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Model.MessageModel;
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
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
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
        messageTimeLabel.setText(message.getTime().toString());
    } else {
        messageTimeLabel.setText(""); // or set a default value
    }

    // If the message has an attachment, set the file icon and file size
    if (message.isAttachment() && message.getAttachmentData() != null) {
        fileIconImageView.setVisible(true);
        fileSizeLabel.setText(String.valueOf(message.getAttachmentData().length));
    }
    setMessageStyle();
}
    @FXML
    void openDownloadSelector() {
        if (message.isAttachment()) {
            if (message.getAttachmentData() != null) {
                fileSaver = new FileChooser();
                fileSaver.setTitle("Save File");
                fileSaver.setInitialFileName(message.getMessageContent());
                File fileToSave = fileSaver.showSaveDialog(null);
                if (fileToSave != null) {
                    saveFile(fileToSave);
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
          /*try {
              Model.getInstance().getControllerFactory().getCustomizeController().setDefaultStyle();
          } catch (IOException e) {
              throw new RuntimeException(e);
          }*/
      }
}