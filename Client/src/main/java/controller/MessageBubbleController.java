package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Model.MessageModel;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
//        messageText.setText(message.getMessageContent());
        messageLabel.setText(message.getMessageContent());
   //     messageTimeLabel.setText(String.valueOf(message.getTime()));
        BufferedImage bufferedImage = ImageUtls.convertByteToImage(message.getSender().getProfilePicture());
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
        senderImage.setImage(fxImage);
        senderNameLabel.setText(message.getSender().getUserName());
        senderPhoneLabel.setText(message.getSender().getPhoneNumber());

        if(message.isAttachment()&&message.getAttachmentData()!=null){
            FontAwesomeIcon icon = FontAwesomeIcon.DOWNLOAD;
            FontAwesomeIconView itemIcon = new FontAwesomeIconView(icon);
            itemIcon.setSize("16px");
            fileNameLabel.setGraphic(itemIcon);
            fileSizeLabel.setText(String.valueOf(message.getAttachmentData().length));
        }
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
}