package dao;

import model.entities.ChatParticipant;
import java.util.List;

public interface ChatParticipantsDao extends Dao<ChatParticipant> {
    ChatParticipant get(int id);
    List<ChatParticipant> getAll();
    boolean save(ChatParticipant chatParticipant);
    boolean update(ChatParticipant chatParticipant);
    boolean delete(ChatParticipant chatParticipant);
}