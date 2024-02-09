package controller.soundUtils;


import dto.requests.SendVoicePacketRequest;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.CurrentUser;
import network.NetworkFactory;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AudioChat {
    private static AudioChat audioChat;
    private AudioFormat format;
    private Mixer mixer;

    private  String receiverPhoneNumber;
    private  String senderPhoneNumber;
    private volatile boolean running = false; // Add this line

    private SendVoicePacketRequest sendVoicePacketRequest;

    private byte[] receivedData;

    private AudioChat() {
    }
    public static AudioChat getInstance() {
        if (audioChat == null) {
            audioChat = new AudioChat();
        }
        return audioChat;
    }

    public void setMixer(Mixer mixer) {
        this.mixer = mixer;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public void setReceivedData(SendVoicePacketRequest sendVoicePacketRequest) {
       this.sendVoicePacketRequest = sendVoicePacketRequest;
    }


public void start() throws LineUnavailableException, IOException {
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

    // Get a DataLine from the selected mixer
    TargetDataLine microphone = (TargetDataLine) mixer.getLine(info);
    microphone.open(format);
    microphone.start();

    byte[] buffer = new byte[16384];
    int numBytesRead;

    // Initialize JavaFX
    new JFXPanel();

    running = true; // Add this line
    // Capture microphone data into a byte array and send it to the server continuously
    while (running) {
        if (mixer == null) {
            throw new IllegalStateException("Mixer has not been initialized");
        }
        if ((numBytesRead = microphone.read(buffer, 0, buffer.length)) > 0) {
            SendVoicePacketRequest sendVoicePacketRequest = new SendVoicePacketRequest(receiverPhoneNumber, senderPhoneNumber ,buffer);
            // Send the audio data to the server
            try {
                NetworkFactory.getInstance().sendVoicePacket(sendVoicePacketRequest);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Sent audio data of length: " + numBytesRead);
            try {
                SendVoicePacketRequest receivedPacket = NetworkFactory.getInstance().receiveVoiceMessage(senderPhoneNumber);
                System.out.println("THIS SHOULD BE DIFFERENT FOR THE CLIENTS------------------- " + receiverPhoneNumber+" MY PHONE IS  "+CurrentUser.getInstance().getPhoneNumber());
                if (receivedPacket != null&&!receivedPacket.getSenderPhoneNumber().equals(CurrentUser.getInstance().getPhoneNumber())) {
                    receivedData = receivedPacket.getData();
                } else {
                    System.out.println("No packet received from the server");
                }
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
            if (receivedData != null) {
                System.out.println("Received audio data of length: " + receivedData.length);

                // Write the received audio data to a temporary file
                Path tempFile = Files.createTempFile("audio", ".wav");
                try (ByteArrayInputStream bais = new ByteArrayInputStream(receivedData);
                     AudioInputStream ais = new AudioInputStream(bais, format, receivedData.length);
                     FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, fos);
                }

                // Play the received audio data using JavaFX
                Media media = new Media(tempFile.toUri().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                System.out.println("Played audio data of length: " + receivedData.length);
            }
        }
    }
}

    public void stop() {
        running = false; // Add this line
        Thread.currentThread().interrupt();
    }
}