package controller;

import javafx.scene.paint.Color;

public class ContactData {
  private String Name;
  private Color color;

  private String url;

    public ContactData(String name, Color color, String url) {
        Name = name;
        this.color = color;
        this.url=url;
    }
    public ContactData(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
