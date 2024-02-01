package controller;

import dto.Model.MessageModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.CurrentUser;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class MessageBubbleController implements Initializable {
    @FXML
    private HBox completeMessageHBox;
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
    private Text messageText;
    @FXML
    private HBox messageTimeHBox;
    @FXML
    private Label messageTimeLabel;
    @FXML
    private Pane messageStatusPane;
    @FXML
    private Label messageStatusLabel;
    private MessageModel message;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayCurrentUserMessage();
      /*  try {
            if (message.getSender().getUserID()== CurrentUser.getInstance().getUserID()) {
                displayCurrentUserMessage();
            } else {
                displayOtherUserMessage();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void setMessage(MessageModel message) {
        this.message = message;
    }

    private void displayCurrentUserMessage() {
        loadMessage();
        completeMessageHBox.nodeOrientationProperty().set(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        messageVBox.getChildren().remove(senderInfoHBox);

    }

    private void displayOtherUserMessage() {
        loadMessage();
        messageVBox.getStyleClass().add("receiver-bubble");
        completeMessageHBox.nodeOrientationProperty().set(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
//        messageVBox.getStyleClass().add("rec");
    }

    private void loadMessage() {
        messageText.setText(message.getMessageContent());
   /*     messageTimeLabel.setText(String.valueOf(message.getTime()));
        BufferedImage bufferedImage = ImageUtls.convertByteToImage(message.getSender().getProfilePicture());
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
        senderImage.setImage(fxImage);
        senderNameLabel.setText(message.getSender().getUserName());
        senderPhoneLabel.setText(message.getSender().getPhoneNumber());*/

    }
}