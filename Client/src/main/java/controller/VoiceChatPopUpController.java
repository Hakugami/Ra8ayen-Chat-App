package controller;

import concurrency.manager.ConcurrencyManager;
import controller.soundUtils.AudioChat;
import dto.requests.AcceptVoiceCallRequest;
import dto.requests.RefuseVoiceCallRequest;
import dto.responses.AcceptVoiceCallResponse;
import dto.responses.RefuseVoiceCallResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.ContactData;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import notification.NotificationManager;
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

public class VoiceChatPopUpController implements Initializable {

    public Circle profilePic;
    public Label nameLabel;
    public Button acceptButton;
    public Button refuseButton;
    public String phoneNumber;
    public Popup popup;
    public ComboBox<Mixer.Info> audioDevices;
    public Button exitButton;

    private double xOffset = 0;
    private double yOffset = 0;
    private AudioChat audioChat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setVoiceChatPopUpController(this);
        NotificationManager.getInstance().getNotificationSounds().playVoiceCallSound();
        acceptButton.setOnAction(actionEvent -> {
            try {
                handleAcceptButton();
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });

        refuseButton.setOnAction(actionEvent -> {
            try {
                handleRefuseButton();
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });

        audioChat = new AudioChat();

        // Set the audio format
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        audioChat.setFormat(format);

// Get all available mixers
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        // Load mixers into ComboBox
        audioDevices.setItems(FXCollections.observableArrayList(mixerInfos));

        // Set a default mixer
        if (mixerInfos.length > 0) {
            Mixer.Info defaultMixerInfo = mixerInfos[0];
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

    private void handleAcceptButton() throws RemoteException, NotBoundException {
        Notifications.create().title("Voice Call").text("Voice call established").showInformation();
//        System.out.println("IS THIS MISSING????-------- " + phoneNumber);
        AcceptVoiceCallRequest acceptVoiceCallRequest = new AcceptVoiceCallRequest(CurrentUser.getInstance().getPhoneNumber(), phoneNumber);
        AcceptVoiceCallResponse acceptVoiceCallResponse = NetworkFactory.getInstance().acceptVoiceCallRequest(acceptVoiceCallRequest);
        acceptButton.setDisable(true);
        refuseButton.setDisable(true);
        acceptButton.setVisible(false);
        refuseButton.setVisible(false);
        if (acceptVoiceCallResponse.isAccepted()) {
            System.out.println("Call accepted");
        } else {
            System.out.println("Call refused");
        }
    }

    private void handleRefuseButton() throws RemoteException, NotBoundException {
        RefuseVoiceCallRequest refuseVoiceCallRequest = new RefuseVoiceCallRequest(CurrentUser.getInstance().getPhoneNumber(), phoneNumber);
        RefuseVoiceCallResponse refuseVoiceCallResponse = NetworkFactory.getInstance().refuseVoiceCallRequest(refuseVoiceCallRequest);
        audioChat.stop();
        popup.hide();
    }

    public void establishVoiceCall(AcceptVoiceCallResponse acceptVoiceCallResponse) throws RemoteException {
        Notifications.create().title("Voice Call").text("Voice call established").showInformation();
        System.out.println("Voice call established");

        System.out.println("Receiver phone number: " + acceptVoiceCallResponse.getReceiverPhoneNumber());
        System.out.println("Sender phone number: " + acceptVoiceCallResponse.getSenderPhoneNumber());

        audioChat.setReceiverPhoneNumber(acceptVoiceCallResponse.getSenderPhoneNumber());
        audioChat.setSenderPhoneNumber(acceptVoiceCallResponse.getReceiverPhoneNumber());

        Thread callThread = new Thread(() -> {
            try {
                System.out.println("Starting audio chat Receiver");
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

    public void setPopup(Popup popup, String phoneNumber) throws RemoteException {
        this.popup = popup;
        for (ContactData contactData : CurrentUser.getInstance().getContactDataList()) {
            if (contactData.getPhoneNumber().equals(phoneNumber)) {
                nameLabel.setText(contactData.getName());
                profilePic.setFill(new ImagePattern(contactData.getImage().getImage()));
                this.phoneNumber = contactData.getPhoneNumber();
            }
        }
    }
}