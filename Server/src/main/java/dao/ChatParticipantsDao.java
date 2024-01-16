package dao;

import model.entities.ChatParticipant;
import java.util.List;

public interface ChatParticipantsDao extends Dao<ChatParticipant> {
    ChatParticipant get(int id);
    List<ChatParticipant> getAll();
    void save(ChatParticipant chatParticipant);
    void update(ChatParticipant chatParticipant);
    void delete(ChatParticipant chatParticipant);
}