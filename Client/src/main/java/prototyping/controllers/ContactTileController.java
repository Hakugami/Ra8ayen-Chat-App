package prototyping.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ContactTileController {

    @FXML
    private AnchorPane root;

    @FXML
    private Text contactName;

    @FXML
    private Circle statusCircle;

    public void setContactName(String name) {
        contactName.setText(name);
    }

    public void setStatusColor(String status) {
        switch (status) {
            case "online":
                statusCircle.setFill(Color.GREEN);
                break;
            case "offline":
                statusCircle.setFill(Color.RED);
                break;
            default:
                statusCircle.setFill(Color.GRAY);
                break;
        }
    }

    public String getContactName() {
        return contactName.getText();
    }
    public String getStatusColor() {
        return statusCircle.getFill().toString();
    }
    public AnchorPane getRoot() {
        return root;
    }
}