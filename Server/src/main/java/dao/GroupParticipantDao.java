package dao;

import model.entities.GroupParticipant;
import java.util.List;

public interface GroupParticipantDao extends Dao<GroupParticipant> {
    GroupParticipant get(int id);
    List<GroupParticipant> getAll();
    void save(GroupParticipant groupParticipant);
    void update(GroupParticipant groupParticipant);
    void delete(GroupParticipant groupParticipant);
}