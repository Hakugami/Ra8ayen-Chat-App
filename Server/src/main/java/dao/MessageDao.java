package dao;


import model.entities.Message;

import java.util.List;

public interface MessageDao extends Dao<Message> {
    //CRUD
    void save(Message message); //Create
    List<Message> get(int sender, int receiver); //Read
    List<Message> getAll();
    Message get(int senderId);

    void update(Message message); //update

    void delete(Message message); //delete



}