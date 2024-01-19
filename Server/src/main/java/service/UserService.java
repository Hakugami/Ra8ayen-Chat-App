package service;

import Mapper.RegisterMapper;
import Mapper.RegisterMapperImpl;
import dao.impl.UserDaoImpl;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import model.entities.User;

public class UserService {

    public RegisterMapper registerMapper;

    public UserDaoImpl userDaoImpl;

    public UserService() {
        registerMapper = new RegisterMapperImpl();
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

    public User getUserByPhoneNumber(String phoneNumber) {
        return userDaoImpl.getUserByPhoneNumber(phoneNumber);
    }
}
