package controller;

import controller.soundUtils.AudioChat;
import dto.requests.AcceptVoiceCallRequest;
import dto.requests.RefuseVoiceCallRequest;
import dto.responses.AcceptVoiceCallResponse;
import dto.responses.RefuseVoiceCallResponse;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.ContactData;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setVoiceChatPopUpController(this);
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
    }

    private void handleAcceptButton() throws RemoteException, NotBoundException {
        AcceptVoiceCallRequest acceptVoiceCallRequest = new AcceptVoiceCallRequest(CurrentUser.getInstance().getPhoneNumber(), phoneNumber);
        AcceptVoiceCallResponse acceptVoiceCallResponse = NetworkFactory.getInstance().acceptVoiceCallRequest(acceptVoiceCallRequest);
        if (acceptVoiceCallResponse.isAccepted()) {
            System.out.println("Call accepted");
        } else {
            System.out.println("Call refused");
        }
    }

    private void handleRefuseButton() throws RemoteException, NotBoundException {
        RefuseVoiceCallRequest refuseVoiceCallRequest = new RefuseVoiceCallRequest(CurrentUser.getInstance().getPhoneNumber(), phoneNumber);
        RefuseVoiceCallResponse refuseVoiceCallResponse = NetworkFactory.getInstance().refuseVoiceCallRequest(refuseVoiceCallRequest);
        if (refuseVoiceCallResponse.isRefused()) {
            System.out.println("Call refused");
        } else {
            System.out.println("Call accepted");
        }
    }

    public void establishVoiceCall(AcceptVoiceCallResponse acceptVoiceCallResponse) throws RemoteException {
        AudioChat audioChat;
        System.out.println("Voice call established");
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);

        // Get all available mixers
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        // Select a specific mixer
        Mixer mixer = null;
        for (Mixer.Info mixerInfo : mixerInfos) {
            System.out.println(mixerInfo.getName());
            if (mixerInfo.getName().equals("Microphone Array (Realtek(R) Audio)")) {
                mixer = AudioSystem.getMixer(mixerInfo);
                break;
            }
        }

        if (mixer == null) {
            System.out.println("Mixer not found");

        }
        acceptVoiceCallResponse.setSenderPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
        System.out.println("Receiver phone number: " + acceptVoiceCallResponse.getReceiverPhoneNumber());
        System.out.println("Sender phone number: " + acceptVoiceCallResponse.getSenderPhoneNumber());

        audioChat = AudioChat.getInstance();
        audioChat.setFormat(format);
        audioChat.setMixer(mixer);
        audioChat.setReceiverPhoneNumber(acceptVoiceCallResponse.getReceiverPhoneNumber());
        audioChat.setSenderPhoneNumber(acceptVoiceCallResponse.getSenderPhoneNumber());

        new Thread(() -> {
            try {
                audioChat.start();
            } catch (LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }

    public void setPopup(Popup popup, String phoneNumber) throws RemoteException {
        this.popup = popup;
        for (ContactData contactData : CurrentUser.getInstance().getContactDataList()) {
            if (contactData.getPhoneNumber().equals(phoneNumber)) {
                nameLabel.setText(contactData.getName());
                profilePic.setFill(new ImagePattern(contactData.getImage().getImage()));
                phoneNumber = contactData.getPhoneNumber();
            }
        }
    }
}
