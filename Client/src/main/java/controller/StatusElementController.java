
package controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusElementController implements Initializable {

    @FXML
    private Circle statusColor;

    @FXML
    private Label statusName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double shiftAmount = 50; // Change this to the amount you want to shift to the left
        statusName.setPadding(new Insets(0, 0, 0, -shiftAmount));
    }

   public void setStatusColor(Color color){
     statusColor.setVisible(false);
    }
    public void setStatusName(String name){
        statusName.setText(name);
    }

}
