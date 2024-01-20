package prototyping.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.javafx2.models.Country;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class RegisterController implements Initializable {
    @FXML
    public Label User_Name_lbl;
    @FXML
    public TextField User_Name_field;
    @FXML
    public Label Email_lbl;
    @FXML
    public TextField Email_field;
    @FXML
    public Label Password_lbl;
    @FXML
    public TextField Password_field;
    @FXML
    public Label Confirm_Password_lbl;
    @FXML
    public TextField Confirm_Password_field;
    @FXML
    public Button Register_btn;
    @FXML
    public Button Back_btn;
    @FXML
    public Label Error_lbl;
    public Label Phone_Number_lbl;
    public TextField Phone_Number_field;
    public Label Gender_lbl;
    public ComboBox<String> Gender_field;
    public Label Country_lbl;
    public ComboBox<String> Country_field;
    public Label DateOfBirth_lbl;
    public DatePicker DateOfBirth_field;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Register_btn.setOnAction(actionEvent -> {
            if (User_Name_field.getText().isEmpty() || Email_field.getText().isEmpty() || Password_field.getText().isEmpty() || Confirm_Password_field.getText().isEmpty()) {
                Error_lbl.setText("Please fill all the fields");
            } else if (!Password_field.getText().equals(Confirm_Password_field.getText())) {
                Error_lbl.setText("Passwords do not match");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
                Stage currentStage = (Stage) Register_btn.getScene().getWindow();
                try {
                    AnchorPane loginPane = loader.load();
                    currentStage.getScene().setRoot(loginPane);
                    loginPane.setTranslateX(-currentStage.getWidth());

                    TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), loginPane);
                    tt.setToX(0);
                    tt.play();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Back_btn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
            Stage currentStage = (Stage) Back_btn.getScene().getWindow();
            try {
                AnchorPane loginPane = loader.load();
                currentStage.getScene().setRoot(loginPane);
                loginPane.setTranslateX(-currentStage.getWidth());

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), loginPane);
                tt.setToX(0);
                tt.play();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loadCountryCodes();
        // Create a FilteredList and initially add all ComboBox items to it
        Country_field.setEditable(true);
        FilteredList<String> filteredItems = new FilteredList<>(FXCollections.observableArrayList(Country_field.getItems()), p -> true);

Country_field.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
    final TextField editor = Country_field.getEditor();

    // If the new value is empty, reset the ComboBox's items to the full list of countries
    if (newValue.isEmpty()) {
        Country_field.setItems(FXCollections.observableArrayList(Country_field.getItems()));
        return;
    }

    // If the new value is the same as the old value, do nothing
    if (oldValue != null && oldValue.equals(newValue)) {
        return;
    }

    // If the new value is already in the ComboBox's items, do nothing
    if (Country_field.getItems().contains(newValue)) {
        return;
    }

    // Create a PauseTransition with a duration of 1 second (1000 milliseconds)
    PauseTransition pause = new PauseTransition(Duration.millis(1000));

    // Set the action to be performed after the PauseTransition is over
    pause.setOnFinished(e -> {
        // Create a new Task for the filtering operation
        Task<FilteredList<String>> task = new Task<>() {
            @Override
            protected FilteredList<String> call() {
                // If the entered text is too short, return all items
                if (editor.getText().length() < 2) {
                    return new FilteredList<>(FXCollections.observableArrayList(Country_field.getItems()), p -> true);
                }
                // Otherwise, filter items to only those that contain the entered text
                else {
                    return filteredItems.filtered(item -> item.toLowerCase().contains(editor.getText().toLowerCase()));
                }
            }
        };

        // Update the ComboBox on the JavaFX Application Thread when the Task is done
        task.setOnSucceeded(event -> {
            Country_field.setItems(task.getValue());
            // Open the ComboBox dropdown
            Country_field.show();
            // Do not select any item in the ComboBox
        });

        // Start the Task on a new Thread
        new Thread(task).start();
    });

    // If the PauseTransition is already running, stop it
    if (pause.getStatus() == Animation.Status.RUNNING) {
        pause.stop();
    }

    // Start the PauseTransition
    pause.play();
});
    }

    private void loadCountryCodes() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Read JSON file and convert to list of Country objects
            List<Country> countries = mapper.readValue(Files.readAllBytes(Paths.get("CountryCodes.json")), new TypeReference<List<Country>>() {
            });

            // Create a TreeMap and add each country to the map
            Map<String, String> countryMap = new TreeMap<>();
            for (Country country : countries) {
                countryMap.put(country.getDial_code(), country.getName());
            }

            // Populate the ComboBox with the country codes
            for (Map.Entry<String, String> entry : countryMap.entrySet()) {
                Country_field.getItems().add(entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}