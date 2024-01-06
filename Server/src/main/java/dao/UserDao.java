package dao;

import java.util.List;

import model.entities.User;

public interface UserDao {

    User getUserById(int userId);
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);
} 
