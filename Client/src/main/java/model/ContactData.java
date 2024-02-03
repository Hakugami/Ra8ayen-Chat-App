package model;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Chat;

public class ContactData extends Chat {
    private int id;
    private String Name;
    private String phoneNumber;
    private Color color;

    private String url;

    private ImageView image;

    public int getChatId() {
        return ChatId;
    }

    public void setChatId(int chatId) {
        ChatId = chatId;
    }

    private int ChatId;

    public ContactData(String name, Color color, String url) {
        Name = name;
        this.color = color;
        this.url = url;
    }

    public ContactData() {

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

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getImage() {
        return image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
