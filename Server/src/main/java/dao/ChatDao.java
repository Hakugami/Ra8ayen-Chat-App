package dao;

import model.entities.Chat;

import java.util.List;

public interface ChatDao extends Dao<Chat>{
    Chat get(int id);
    Chat getChatByName(String name);
    List<Chat> getAll();
    boolean save(Chat chatGroup);
    boolean update(Chat chatGroup);
    boolean delete(Chat chatGroup);

}
