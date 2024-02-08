package dto.Model;

import java.io.Serializable;

public class StyleMessage implements Serializable {
    private boolean isItalic;
    private boolean isBold;
    private boolean isUnderline;
    private int fontSize;
    private String fontColor;
    private String backgroundColor;
    private String fontStyle;

    public StyleMessage(boolean isItalic, boolean isBold, boolean isUnderline, int fontSize, String fontColor, String backgroundColor) {
        this.isItalic = isItalic;
        this.isBold = isBold;
        this.isUnderline = isUnderline;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
    }
    public StyleMessage(){

    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    @Override
    public String toString() {
        return "StyleMessage{" +
                "isItalic=" + isItalic +
                ", isBold=" + isBold +
                ", isUnderline=" + isUnderline +
                ", fontSize=" + fontSize +
                ", fontColor='" + fontColor + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", fontStyle='" + fontStyle + '\'' +
                '}';
    }
}