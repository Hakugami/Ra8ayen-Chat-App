package notification;

import javafx.animation.PauseTransition;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class NotificationSounds {
    private MediaPlayer mediaPlayer;

    private void playSound(String soundFile) {
        try {
            new JFXPanel();
            URL resource = getClass().getResource(soundFile);
            if (resource == null) {
                System.out.println("Sound file not found");
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("Error playing sound: " + mediaPlayer.getError()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();

            PauseTransition pause = new PauseTransition(Duration.seconds(7));
            pause.setOnFinished(event -> stopSound());
            pause.play();
        } catch (Exception e) {
            System.out.println("Error initializing JavaFX environment: " + e.getMessage());
        }
    }

    private void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void playWarningSound() {
        playSound("/sounds/warningReal.mp3");
    }

    public void playSuccessSound() {
        playSound("/sounds/success.mp3");
    }

    public void sentMessageSound() {
        playSound("/sounds/messageSent.mp3");
    }

    public void playFriendRequestSound() {
        playSound("/sounds/friendRequest.mp3");
    }

    public void playAnnouncementSound() {
        playSound("/sounds/announcement.mp3");
    }

    public void playErrorSound() {
        playSound("/sounds/invalidInput.mp3");
    }

    public void playRobotSound() {
        playSound("/sounds/robot.mp3");
    }

    public void playRobotCloseSound() {
        playSound("/sounds/robotClose.mp3");
    }
}