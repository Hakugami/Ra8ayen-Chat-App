package controller;

import javafx.fxml.FXML;
import javafx.stage.Popup;
import model.Model;

import java.io.IOException;

public class CustomizeFontStyleController {

    private String currentFont = "Arial";
    private Popup popup;

    private String style;
    @FXML
    void initialize() {

        Model.getInstance().getControllerFactory().setCustomizeFontStyleController(this);
    }
    void setPopup(Popup popup){
        this.popup = popup;
    }
    @FXML
    void setArial(){
        currentFont = "Arial";
        sendStyle();
        popup.hide();
    }
    @FXML
    void setRoman(){
        currentFont = "Times New Roman";
        sendStyle();
        popup.hide();
    }
    @FXML
    void setGeorgia(){
        currentFont ="Georgia";
        sendStyle();
        popup.hide();

    }
    @FXML
    void setCalibri(){
        currentFont = "Calibri";
        sendStyle();
        popup.hide();
    }

    @FXML
    void setGaramond(){
        currentFont = "Garamond";
        sendStyle();
        popup.hide();
    }

    void sendStyle(){
        try {
            Model.getInstance().getControllerFactory().getCustomizeController().setStyle(currentFont);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
