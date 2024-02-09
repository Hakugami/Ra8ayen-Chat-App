package controller;

import concurrency.manager.ConcurrencyManager;
import controller.soundUtils.AudioChat;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.ContactData;
import model.CurrentUser;
import network.NetworkFactory;
import org.controlsfx.control.Notifications;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class VoiceChatWaitController implements Initializable {
    public Circle profilePic;
    public Label nameLabel;
    public Button cancelCall;

    public Popup popup;
    public AudioChat audioChat;
    public ComboBox<Mixer.Info> audioDevices;
    public Button exitButton;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        audioChat = AudioChat.getInstance();
        cancelCall.setOnAction(this::handleCancelCall);

        // Set the audio format
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        audioChat.setFormat(format);

        // Get all available mixers
        // Get all available mixers
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        // Load mixers into ComboBox
        audioDevices.setItems(FXCollections.observableArrayList(mixerInfos));

        // Set a default mixer
        if (mixerInfos.length > 0) {
            Mixer.Info defaultMixerInfo = mixerInfos[mixerInfos.length - 1];
            audioChat.setMixer(AudioSystem.getMixer(defaultMixerInfo));
            audioDevices.getSelectionModel().select(defaultMixerInfo);
        }

        // Add action listener to ComboBox
        audioDevices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                audioChat.setMixer(AudioSystem.getMixer(newValue));
            }
        });

        Platform.runLater(() -> {
            Node root = profilePic.getScene().getRoot();

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                popup.setX(event.getScreenX() - xOffset);
                popup.setY(event.getScreenY() - yOffset);
            });
        });

        exitButton.setOnAction(actionEvent -> {
            try {
                handleExitButton();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void establishVoiceCall(String receiverPhoneNumber, String senderPhoneNumber) {
        Notifications.create().title("Voice Call").text("Voice call established").showInformation();
        System.out.println("Voice call established");

        System.out.println("Receiver phone number: " + receiverPhoneNumber);
        System.out.println("Sender phone number: " + senderPhoneNumber);

        audioChat.setReceiverPhoneNumber(receiverPhoneNumber);
        audioChat.setSenderPhoneNumber(senderPhoneNumber);

        Thread callThread = new Thread(() -> {
            try {
                System.out.println("Starting audio chat Sender");
                audioChat.start();
            } catch (LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        ConcurrencyManager.getInstance().submitRunnable(callThread);
    }

    private void handleExitButton() throws RemoteException {
        audioChat.stop();
        popup.hide();
        try {
            NetworkFactory.getInstance().disconnectVoiceChat(CurrentUser.getInstance().getPhoneNumber());
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPopup(Popup popup, String receiverPhoneNumber, String senderPhoneNumber) throws RemoteException {
        Notifications.create().title("Incoming Call").text("You have an incoming call").showInformation();
        establishVoiceCall(receiverPhoneNumber, senderPhoneNumber);
        this.popup = popup;
        for (ContactData contactData : CurrentUser.getInstance().getContactDataList()) {
            if (contactData.getPhoneNumber().equals(receiverPhoneNumber)) {
                nameLabel.setText(contactData.getName());
                profilePic.setFill(new ImagePattern(contactData.getImage().getImage()));
                break;
            }
        }
    }

    private void handleCancelCall(ActionEvent actionEvent) {
        audioChat.stop();
        popup.hide();
        try {
            NetworkFactory.getInstance().disconnectVoiceChat(CurrentUser.getInstance().getPhoneNumber());
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
