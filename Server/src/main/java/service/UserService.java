package service;

import Mapper.RegisterMapper;
//import Mapper.RegisterMapperImpl;
import Mapper.RegisterMapperImpl;
import Mapper.UserMapper;
import Mapper.UserMapperImpl;
import dao.impl.UserDaoImpl;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import exceptions.DuplicateEntryException;
import model.entities.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    public RegisterMapper registerMapper;
    public UserMapper userMapper;

    public UserDaoImpl userDaoImpl;

    public UserService() {
        registerMapper = new RegisterMapperImpl();
        userMapper = new UserMapperImpl();
        userDaoImpl = new UserDaoImpl();
    }

    public void registerUser(RegisterRequest registerRequest) throws DuplicateEntryException {
        User user = registerMapper.requestToEntity(registerRequest);
        try {
            userDaoImpl.save(user);
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062) {
                throw new DuplicateEntryException(e.getMessage(), e);
            }
        }
    }

    public boolean loginUser(LoginRequest loginRequest) {
        User user = userDaoImpl.getUserByPhoneNumber(loginRequest.getPhoneNumber());
        if (user == null) {
            return false;
        }

        String storedPassword = user.getPasswordHash();
        String enteredPassword = loginRequest.getPassword();

        return storedPassword.equals(enteredPassword);
    }
    public boolean updateUser(User user) throws DuplicateEntryException {
        try {
            userDaoImpl.update(user);
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062) {
                throw new DuplicateEntryException(e.getMessage(), e);
            }
        }
        return false;
    }
    public void deleteUser(User user) {
        userDaoImpl.delete(user);
    }
    public List<User> getAllUsers() {
        return userDaoImpl.getAll();
    }
    public User getUserByPhoneNumber(String phoneNumber) {
        return userDaoImpl.getUserByPhoneNumber(phoneNumber);
    }
    public User getUserById(int id) {
        return userDaoImpl.get(id);
    }
}
