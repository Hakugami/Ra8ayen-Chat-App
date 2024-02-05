
package controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StatusElementController {

    @FXML
    private Circle statusColor;

    @FXML
    private Label statusName;

   public void setStatusColor(Color color){
     statusColor.setVisible(false);
    }
    public void setStatusName(String name){
        statusName.setText(name);
    }

}
