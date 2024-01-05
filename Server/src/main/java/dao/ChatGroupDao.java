package dao;

import model.entities.ChatGroup;

import java.util.List;

public interface ChatGroupDao {
    ChatGroup getChatGroupById(int id);
    ChatGroup getChatGroupByName(String name);
    List<ChatGroup> findAllChatGroups();
    void saveChatGroup(ChatGroup chatGroup);
    void updateChatGroup(ChatGroup chatGroup);
    void deleteChatGroup(ChatGroup chatGroup);

}
