package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;

public class GroupElementController {


    @FXML
    public void initialize(){
        FontAwesomeIcon icon = FontAwesomeIcon.TOGGLE_ON;
        FontAwesomeIconView itemIcon = new FontAwesomeIconView(icon);
    }
}
