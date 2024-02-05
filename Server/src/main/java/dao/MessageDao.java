package dao;


import model.entities.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageDao extends Dao<Message> {
    //CRUD
    boolean save(Message message); //Create
    List<Message> getChatMessages(int chatID); //Read
    List<Message> getAll();
    Message get(int senderId);
    int saveAndReturnId(Message message) throws SQLException;
    boolean update(Message message); //update

    boolean delete(Message message); //delete



}