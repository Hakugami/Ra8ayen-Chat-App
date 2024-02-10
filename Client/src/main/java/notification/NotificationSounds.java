package notification;

import javafx.animation.PauseTransition;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class NotificationSounds {
    private MediaPlayer mediaPlayer;

    public void playWarningSound() {
        try {
            new JFXPanel(); // Initializes the JavaFX environment
            URL resource = getClass().getResource("/sounds/warningReal.mp3");
            if (resource == null) {
                System.out.println("Sound file not found");
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("Error playing sound: " + mediaPlayer.getError()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();

            // Stop the sound after 5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> stopSound());
            pause.play();
        } catch (Exception e) {
            System.out.println("Error initializing JavaFX environment: " + e.getMessage());
        }
    }

    public void playSuccessSound() {
        try {
            new JFXPanel(); // Initializes the JavaFX environment
            URL resource = getClass().getResource("/sounds/success.mp3");
            if (resource == null) {
                System.out.println("Sound file not found");
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("Error playing sound: " + mediaPlayer.getError()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();

            // Stop the sound after 5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> stopSound());
            pause.play();
        } catch (Exception e) {
            System.out.println("Error initializing JavaFX environment: " + e.getMessage());
        }
    }

    public void sentMessageSound() {
        try {
            new JFXPanel(); // Initializes the JavaFX environment
            URL resource = getClass().getResource("/sounds/messageSent.mp3");
            if (resource == null) {
                System.out.println("Sound file not found");
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("Error playing sound: " + mediaPlayer.getError()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();

            // Stop the sound after 5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> stopSound());
            pause.play();
        } catch (Exception e) {
            System.out.println("Error initializing JavaFX environment: " + e.getMessage());
        }
    }

    public void playFriendRequestSound() {
        try {
            new JFXPanel(); // Initializes the JavaFX environment
            URL resource = getClass().getResource("/sounds/friendRequest.mp3");
            if (resource == null) {
                System.out.println("Sound file not found");
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("Error playing sound: " + mediaPlayer.getError()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();

            // Stop the sound after 5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> stopSound());
            pause.play();
        } catch (Exception e) {
            System.out.println("Error initializing JavaFX environment: " + e.getMessage());
        }
    }

    public void playAnnouncementSound() {
        try {
            new JFXPanel(); // Initializes the JavaFX environment
            URL resource = getClass().getResource("/sounds/announcement.mp3");
            if (resource == null) {
                System.out.println("Sound file not found");
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("Error playing sound: " + mediaPlayer.getError()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();

            // Stop the sound after 5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
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
}