package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import model.Model;

public class CustomizeController {
    @FXML
    Label italic;
    @FXML
    Label bold;
    @FXML
    Label colorFont;
    @FXML
    Label sizeIncrease;
    @FXML
    Label sizeDecrease;
    @FXML
    Label underline;
    @FXML
    Label backgroundColor;

    Popup colorPickerPopup;

    Popup colorGroundPickerPopup;
    boolean IsItalic;
    boolean IsBold;

    boolean IsUnderline;
    int ColorDegree;
    int BackGroundDegree;
    boolean IsIncrease;

    ColorPicker colorPicker = new ColorPicker();
    private Color selectedColor;

    ColorPicker colorPickerGround = new ColorPicker();


    private Color selectedBackGround;


    int fontSize=16;
    private static final Color[] COLOR_PALETTE = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.PINK
    };
    @FXML
    void initialize() {
        IsItalic = false;
        IsBold = false;
        IsUnderline = false;
        ColorDegree = 0;
        BackGroundDegree = 0;
        fontSize=15;

        italic.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(!IsItalic){
                        Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-style: italic;");
                    }else{
                        Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-weight: normal;");
                    }
                    IsItalic = !IsItalic;
                }
            });


        });

        bold.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(!IsBold){
                        Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-weight: bold;");
                    }else{
                        Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-weight: normal;");
                    }
                    IsBold = !IsBold;
                }
            });

        });
        sizeIncrease.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    fontSize++;
                    Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-size: "+fontSize+";");
                }
            });

        });
        sizeDecrease.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    fontSize--;
                    Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-size: "+fontSize+";");
                }
            });

        });

        colorFont.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    colorPicker.getCustomColors().addAll(COLOR_PALETTE);
                    colorPicker.setValue(COLOR_PALETTE[0]);
                    colorPickerPopup = createColorPickerPopup(colorPicker);
                    colorPickerPopup.show(colorFont.getScene().getWindow());
                }
            });
        });
        backgroundColor.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    colorPickerGround.getCustomColors().addAll(COLOR_PALETTE);
                    colorPickerGround.setValue(COLOR_PALETTE[0]);
                    colorGroundPickerPopup = createColorPickerPopup(colorPickerGround);
                    colorGroundPickerPopup.show(colorFont.getScene().getWindow());
                }
            });

        });
        colorPicker.setOnAction(event -> {
            selectedColor = colorPicker.getValue(); // Save the selected color
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    colorPickerPopup.hide(); // Hide the pop-up
                    Model.getInstance().getControllerFactory().getChatController().setColor(selectedColor);
                }
            });

        });
        colorPickerGround.setOnAction(event -> {
            selectedBackGround = colorPickerGround.getValue(); // Save the selected color
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    colorGroundPickerPopup.hide(); // Hide the pop-up
                    Model.getInstance().getControllerFactory().getChatController().setBackgroundColor(selectedBackGround);
                }
            });

        });
    }
    private Popup createColorPickerPopup(ColorPicker colorPicker) {
        Popup popup = new Popup();
        popup.getContent().add(colorPicker);
        popup.setAutoHide(true);

        // Adjust the size of the popup as needed
        popup.setWidth(200);
        popup.setHeight(200);

        // Add padding for better appearance
        colorPicker.setPadding(new Insets(10));

        return popup;
    }


}

