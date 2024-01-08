package dao;

import model.entities.ChatGroup;

import java.util.List;

public interface ChatGroupDao extends Dao<ChatGroup>{
    ChatGroup get(int id);
    ChatGroup getChatGroupByName(String name);
    List<ChatGroup> getAll();
    void save(ChatGroup chatGroup);
    void update(ChatGroup chatGroup);
    void delete(ChatGroup chatGroup);

}
