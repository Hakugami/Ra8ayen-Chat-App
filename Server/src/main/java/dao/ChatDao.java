package dao;

import model.entities.Chat;

import java.util.List;

public interface ChatDao extends Dao<Chat>{
    Chat get(int id);
    Chat getChatByName(String name);
    List<Chat> getAll();
    void save(Chat chatGroup);
    void update(Chat chatGroup);
    void delete(Chat chatGroup);

}
