package dao;

import model.entities.GroupChatMessage;

import java.util.List;

public interface GroupChatMessageDao extends Dao<GroupChatMessage> {
    GroupChatMessage get(int id);
    List<GroupChatMessage> getAll();
    void save(GroupChatMessage groupChatMessage);
    void update(GroupChatMessage groupChatMessage);
    void delete(GroupChatMessage groupChatMessage);
}
