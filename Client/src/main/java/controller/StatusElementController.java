
package controller;
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
        statusColor.setFill(color);
    }
    public void setStatusName(String name){
        statusName.setText(name);
    }

}
