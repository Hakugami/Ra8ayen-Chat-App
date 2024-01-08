package dao;

import java.util.List;

import model.entities.User;

public interface UserDao extends Dao<User> {

    User get(int userId);
    List<User> getAll();
    void save(User user);
    void update(User user);
    void delete(int userId);
} 
