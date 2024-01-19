package dao;

import model.entities.User;
import model.entities.UserContacts;

import java.util.List;

public interface UserContactsDao extends Dao<UserContacts>{
    void save(UserContacts userContacts);
    UserContacts get(int userId);
    List<UserContacts> getAll();
    void update(UserContacts notification);
    void delete(UserContacts notification);
    List<UserContacts> getContactById(User user);
}
