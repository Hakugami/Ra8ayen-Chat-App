package service;

import Mapper.RegisterMapper;
//import Mapper.RegisterMapperImpl;
import Mapper.RegisterMapperImpl;
import Mapper.UserMapper;
import Mapper.UserMapperImpl;
import dao.impl.UserDaoImpl;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import model.entities.User;

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

    public void registerUser(RegisterRequest registerRequest) {
        User user = registerMapper.requestToEntity(registerRequest);
        userDaoImpl.save(user);
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
