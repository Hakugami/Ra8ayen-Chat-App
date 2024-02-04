package controller;

import controller.soundUtils.AudioChat;
import dto.responses.AcceptVoiceCallResponse;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class VoiceChatWaitController implements Initializable {
    public Circle profilePic;
    public Label nameLabel;
    public Button cancelCall;

    public Popup popup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelCall.setOnAction(actionEvent -> {
            System.out.println("Cancel call button clicked");
        });

    }
    public void  establishVoiceCall(String receiverPhoneNumber,String senderPhoneNumber) {
        AudioChat audioChat ;
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

        System.out.println("Receiver phone number: "+receiverPhoneNumber);
        System.out.println("Sender phone number: "+senderPhoneNumber);
        audioChat = AudioChat.getInstance();
        audioChat.setFormat(format);
        audioChat.setMixer(mixer);
        audioChat.setReceiverPhoneNumber(receiverPhoneNumber);
        audioChat.setSenderPhoneNumber(senderPhoneNumber);

        new Thread(() -> {
            try {
                audioChat.start();
            } catch (LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }
    public void setPopup(Popup popup,String receiverPhoneNumber,String senderPhoneNumber) throws RemoteException {
        establishVoiceCall(receiverPhoneNumber,senderPhoneNumber);
        this.popup = popup;
    }
}
