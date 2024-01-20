package prototyping.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.javafx2.models.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Label User_Name_lbl;
    public TextField User_Name_field;
    public Label Password_lbl;
    public TextField Password_field;
    public Button Login_btn;
    public Label Error_lbl;
    public Button Register_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Login_btn.setOnAction(actionEvent -> {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), Login_btn);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> {
                BorderPane mainArea = Model.getInstance().getViewFactory().getMainArea();
                Stage currentStage = (Stage) Login_btn.getScene().getWindow();
                currentStage.setScene(new Scene(mainArea));
            });
            ft.play();
        });
        Register_btn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Register.fxml"));
            Stage currentStage = (Stage) Register_btn.getScene().getWindow();
            try {
                AnchorPane registerPane = loader.load();
                registerPane.setTranslateX(currentStage.getWidth());
                currentStage.getScene().setRoot(registerPane);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), registerPane);
                tt.setToX(0);
                tt.play();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
