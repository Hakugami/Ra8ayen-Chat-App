package dao;

import java.util.List;

import model.entities.User;

public interface UserDao extends Dao<User> {

    User get(int userId);
    User getUserByPhoneNumber(String phoneNumber);
    List<User> getAll();
    boolean save(User user);
    boolean update(User user);
    boolean delete(int userId);
} 
