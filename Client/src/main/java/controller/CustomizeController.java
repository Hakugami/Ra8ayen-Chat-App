package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Model.StyleMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.util.Callback;
import model.Model;


import java.io.IOException;

public class CustomizeController {
    @FXML
    ToggleButton italic;
    @FXML
    ToggleButton bold;
    @FXML
    ColorPicker colorFont;
    @FXML
    Button sizeIncrease;
    @FXML
    Button sizeDecrease;
    @FXML
    ToggleButton underline;
    @FXML
    ColorPicker backgroundColor;

    @FXML
    Button fontFamily;
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


   private StyleMessage styleMessage;
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
        styleMessage  = new StyleMessage(false,false,false,16,"#000000","#ED7D31");
        event();
        addFontAwesomeIcon();
        createToolTip();
       Model.getInstance().getControllerFactory().setCustomizeController(this);

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

    private void createToolTip(){
        Tooltip tooltip = new Tooltip("Font Color");
        tooltip.setStyle("-fx-font-size: 10");
        colorFont.setTooltip(tooltip);
        Tooltip tooltip1 = new Tooltip("Background Color");
        tooltip1.setStyle("-fx-font-size: 10");
        backgroundColor.setTooltip(tooltip1);
    }
   public  void event(){
       italic.setOnMouseClicked(mouseEvent -> {
           Platform.runLater(new Runnable() {
               @Override
               public void run() {
                   if(!IsItalic){
                       Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-style: italic;");
                       styleMessage.setItalic(true);
                   }else{
                       styleMessage.setItalic(false);
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
                       styleMessage.setBold(true);
                       Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-weight: bold;");
                   }else{
                       styleMessage.setBold(false);
                       Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-weight: normal;");
                   }
                   IsBold = !IsBold;
               }
           });

       });
         underline.setOnMouseClicked(mouseEvent -> {
              Platform.runLater(new Runnable() {
                @Override
                public void run() {
                     if(!IsUnderline){
                          styleMessage.setUnderline(true);
                          Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-underline: true;");
                     }else{
                          styleMessage.setUnderline(false);
                          Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-underline: false;");
                     }
                     IsUnderline = !IsUnderline;
                }
              });

         });
       sizeIncrease.setOnMouseClicked(mouseEvent -> {
           Platform.runLater(new Runnable() {
               @Override
               public void run() {
                   fontSize++;
                   styleMessage.setFontSize(fontSize);
                   Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-size: "+fontSize+";");
               }
           });

       });
       sizeDecrease.setOnMouseClicked(mouseEvent -> {
           Platform.runLater(new Runnable() {
               @Override
               public void run() {
                   fontSize--;
                   styleMessage.setFontSize(fontSize);
                   Model.getInstance().getControllerFactory().getChatController().setStyle("-fx-font-size: "+fontSize+";");
               }
           });

       });

//       colorFont.setOnMouseClicked(mouseEvent -> {
//           Platform.runLater(new Runnable() {
//               @Override
//               public void run() {
//                   colorPicker.getCustomColors().addAll(COLOR_PALETTE);
//                   colorPicker.setValue(COLOR_PALETTE[0]);
//                   colorPickerPopup = createColorPickerPopup(colorPicker);
//                   colorPickerPopup.show(colorFont.getScene().getWindow());
//               }
//           });
//       });
//       backgroundColor.setOnMouseClicked(mouseEvent -> {
//           Platform.runLater(new Runnable() {
//               @Override
//               public void run() {
//                   colorPickerGround.getCustomColors().addAll(COLOR_PALETTE);
//                   colorPickerGround.setValue(COLOR_PALETTE[0]);
//                   colorGroundPickerPopup = createColorPickerPopup(colorPickerGround);
//                   colorGroundPickerPopup.show(colorFont.getScene().getWindow());
//               }
//           });
//
//       });
//       colorPicker.setOnAction(event -> {
//           selectedColor = colorPicker.getValue(); // Save the selected color
//           Platform.runLater(new Runnable() {
//               @Override
//               public void run() {
//                   colorPickerPopup.hide(); // Hide the pop-up
//                   styleMessage.setFontColor(toHexCode(selectedColor));
//                   Model.getInstance().getControllerFactory().getChatController().setColor(toHexCode(selectedColor));
//               }
//           });
//
//       });
//       colorPickerGround.setOnAction(event -> {
//           selectedBackGround = colorPickerGround.getValue(); // Save the selected color
//           Platform.runLater(new Runnable() {
//               @Override
//               public void run() {
//                   colorGroundPickerPopup.hide(); // Hide the pop-up
//                   styleMessage.setBackgroundColor(toHexCode(selectedBackGround));
//                   Model.getInstance().getControllerFactory().getChatController().setBackgroundColor(toHexCode(selectedBackGround));
//               }
//           });
//
//       });

       colorFont.setOnAction(event -> {
           selectedColor = colorFont.getValue(); // Save the selected color
           Platform.runLater(new Runnable() {
               @Override
               public void run() {
                   styleMessage.setFontColor(toHexCode(selectedColor));
                   Model.getInstance().getControllerFactory().getChatController().setColor(toHexCode(selectedColor));
               }
           });

       });
         backgroundColor.setOnAction(event -> {
              selectedBackGround = backgroundColor.getValue(); // Save the selected color
              Platform.runLater(new Runnable() {
                @Override
                public void run() {
                     styleMessage.setBackgroundColor(toHexCode(selectedBackGround));
                     Model.getInstance().getControllerFactory().getChatController().setBackgroundColor(toHexCode(selectedBackGround));
                }
              });

         });
       fontFamily.setOnMouseClicked(mouseEvent -> {
           Platform.runLater(new Runnable() {
               @Override
               public void run() {
                   appearContextFontFamily(mouseEvent);
               }
           });

       });
   }
    private String toHexCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    public StyleMessage getMessageStyle(){
        return styleMessage;
    }
    public void setNewStyle(){
        styleMessage = ResetNewStyle();
    }
    public StyleMessage ResetNewStyle(){
        StyleMessage styleMessage1 = new StyleMessage();
        styleMessage1.setFontColor(styleMessage.getFontColor());
        styleMessage1.setBold(styleMessage.isBold());
        styleMessage1.setItalic(styleMessage.isItalic());
        styleMessage1.setUnderline(styleMessage.isUnderline());
        styleMessage1.setFontSize(styleMessage.getFontSize());
        styleMessage1.setBackgroundColor(styleMessage.getBackgroundColor());
        styleMessage1.setFontStyle(styleMessage.getFontStyle());
        return styleMessage1;
    }

    void addFontAwesomeIcon(){
        FontAwesomeIconView iconBold = new FontAwesomeIconView(FontAwesomeIcon.BOLD);
        FontAwesomeIconView iconItalic = new FontAwesomeIconView(FontAwesomeIcon.ITALIC);
        FontAwesomeIconView iconUnderline = new FontAwesomeIconView(FontAwesomeIcon.UNDERLINE);
        FontAwesomeIconView iconColor = new FontAwesomeIconView(FontAwesomeIcon.PAINT_BRUSH);
        FontAwesomeIconView iconBackground = new FontAwesomeIconView(FontAwesomeIcon.TINT);
        FontAwesomeIconView iconPlus = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
        FontAwesomeIconView iconMinus = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
        FontAwesomeIconView iconFontFamily = new FontAwesomeIconView(FontAwesomeIcon.FONT);

        iconBold.setSize("1em");
        iconItalic.setSize("1em");
        iconUnderline.setSize("1em");
        iconColor.setSize("1em");
        iconBackground.setSize("1em");
        iconPlus.setSize("1em");
        iconMinus.setSize("1em");
        iconFontFamily.setSize("1em");

        Button colorFontButton = new Button();
        colorFontButton.setGraphic(iconColor);

        Button backgroundColorButton = new Button();
        backgroundColorButton.setGraphic(iconBackground);

        italic.setGraphic(iconItalic);
        bold.setGraphic(iconBold);
        sizeIncrease.setGraphic(iconPlus);
        sizeDecrease.setGraphic(iconMinus);
        underline.setGraphic(iconUnderline);
        fontFamily.setGraphic(iconFontFamily);
    }
    public void appearContextFontFamily(MouseEvent mouseEvent){
        try {
            AnchorPane root = Model.getInstance().getViewFactory().getCustomizeFontStyle();
            Popup popup = new Popup();
            Model.getInstance().getControllerFactory().getCustomizeFontStyleController().setPopup(popup);
            popup.getContent().add(root);
            popup.setAutoHide(true);
            popup.show(fontFamily.getScene().getWindow(),mouseEvent.getScreenX(), mouseEvent.getScreenY()-200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void setStyle(String style){
        System.out.println("Style has been Arrived " +style);
        styleMessage.setFontStyle(style);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Model.getInstance().getControllerFactory().getChatController().setFontFamily(style);
            }
        });

    }

    //set a default style via the css
    /*

    .receiver-bubble {

     -fx-background-color: rgb(108, 95, 91);
 -fx-background-radius: 15 0 15 15;
}

.sender-info {

}

.messageContent {
    -fx-text-fill: rgb(79, 74, 69);
}

.timestamp {
    -fx-text-fill: rgb(246, 241, 238);
}

     */
    public StyleMessage getDefaultStyle(){
        styleMessage.setFontColor("#000000");
        styleMessage.setBackgroundColor("#6C5F5B");
        styleMessage.setFontSize(16);
        styleMessage.setBold(true);
        styleMessage.setItalic(false);
        styleMessage.setUnderline(false);
        return styleMessage;
    }




}

