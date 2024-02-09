package dao;

import java.sql.SQLException;
import java.util.List;

import model.entities.User;

public interface UserDao extends Dao<User> {

    User get(int userId);
    User getUserByPhoneNumber(String phoneNumber);
    List<User> getAll();
    List<User> getContactsByUserID(int userID);
    List<String> getContactsPhoneNumbers(int userID);
    boolean save(User user) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean delete(int userId);

} 
