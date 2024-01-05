package dao;

import model.entities.GroupParticipant;
import java.util.List;

public interface GroupParticipantDao {
    GroupParticipant findById(int id);
    List<GroupParticipant> findAll();
    void save(GroupParticipant groupParticipant);
    void update(GroupParticipant groupParticipant);
    void delete(GroupParticipant groupParticipant);
}