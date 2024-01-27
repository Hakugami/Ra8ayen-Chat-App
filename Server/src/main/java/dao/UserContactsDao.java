package dao;

import model.entities.User;
import model.entities.UserContacts;

import java.util.List;

public interface UserContactsDao extends Dao<UserContacts>{
    boolean save(UserContacts userContacts);
    UserContacts get(int userId);
    List<UserContacts> getAll();
    boolean update(UserContacts notification);
    boolean delete(UserContacts notification);
    List<UserContacts> getContactById(User user);
}
