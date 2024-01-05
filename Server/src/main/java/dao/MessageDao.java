package dao;


import model.entities.Message;

import java.util.List;

public interface MessageDao {
    //CRUD
    void sendMessage(Message message); //Create
    List<Message> getMessages(int sender, int receiver); //Read

    void updateMessage(Message message); //update

    void deleteMessage(Message message); //delete



}