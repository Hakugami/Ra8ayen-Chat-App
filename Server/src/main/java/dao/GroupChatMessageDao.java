package dao;

import model.entities.GroupChatMessage;

import java.util.List;

public interface GroupChatMessageDao {
    GroupChatMessage findById(int id);
    List<GroupChatMessage> findAll();
    void save(GroupChatMessage groupChatMessage);
    void update(GroupChatMessage groupChatMessage);
    void delete(GroupChatMessage groupChatMessage);
}
