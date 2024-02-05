package dto.Model;

import java.io.Serializable;

public class StyleMessage implements Serializable {
    private boolean Italic;

    private   boolean Bold;


    private boolean Underline;

    private int FontSize;

    private String FontColor;

    private String BackgroundColor;

    public StyleMessage(boolean italic, boolean bold, boolean underline, int fontSize, String fontColor, String backgroundColor) {
        Italic = italic;
        Bold = bold;
        Underline = underline;
        FontSize = fontSize;
        FontColor = fontColor;
        BackgroundColor = backgroundColor;
    }
    public StyleMessage(){

    }

    public boolean isItalic() {
        return Italic;
    }

    public void setItalic(boolean italic) {
        Italic = italic;
    }

    public boolean isBold() {
        return Bold;
    }

    public void setBold(boolean bold) {
        Bold = bold;
    }

    public boolean isUnderline() {
        return Underline;
    }

    public void setUnderline(boolean underline) {
        Underline = underline;
    }

    public int getFontSize() {
        return FontSize;
    }

    public void setFontSize(int fontSize) {
        FontSize = fontSize;
    }

    public String getFontColor() {
        return FontColor;
    }

    public void setFontColor(String fontColor) {
        FontColor = fontColor;
    }

    public String getBackgroundColor() {
        return BackgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        BackgroundColor = backgroundColor;
    }
}
