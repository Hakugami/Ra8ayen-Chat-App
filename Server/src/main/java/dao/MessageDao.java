package dao;


import model.entities.Message;

import java.util.List;

public interface MessageDao extends Dao<Message> {
    //CRUD
    boolean save(Message message); //Create
    List<Message> get(int sender, int receiver); //Read
    List<Message> getAll();
    Message get(int senderId);

    boolean update(Message message); //update

    boolean delete(Message message); //delete



}