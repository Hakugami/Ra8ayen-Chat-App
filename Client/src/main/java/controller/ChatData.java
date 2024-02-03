package controller;

public class ChatData {

    private ContactData contactData;
    private int chatID;

    public ChatData(ContactData contactData, int chatID) {
        this.contactData = contactData;
        this.chatID = chatID;
    }

    public ChatData(){

    }
    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }
}
