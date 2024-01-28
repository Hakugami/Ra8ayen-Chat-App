package controllers;

import Mapper.UpdateUser;
import Mapper.UpdateUserImpl;
import dto.Controller.AuthenticationController;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import dto.requests.UpdateUserRequest;
import dto.responses.LoginResponse;
import dto.responses.RegisterResponse;
import dto.responses.UpdateUserResponse;
import model.entities.User;
import service.EncryptionService;
import service.HashService;
import service.UserService;
import session.Session;
import session.manager.SessionManager;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Logger;

public class AuthenticationControllerSingleton extends UnicastRemoteObject implements AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private static AuthenticationControllerSingleton instance;
    private final UserService userService;
    private final HashService hashService;
    private EncryptionService encryptionService;
    private final SessionManager sessionManager;
    private final UpdateUser updateUserMapper;

    private AuthenticationControllerSingleton() throws RemoteException {
        super();
        userService = new UserService();
        hashService = new HashService("hashing.properties");
        encryptionService = new EncryptionService("keystore.jceks", "Buh123!","Buh1234!", "encryption.properties");
        sessionManager = SessionManager.getInstance();
        updateUserMapper = new UpdateUserImpl();
    }


public static AuthenticationControllerSingleton getInstance() throws RemoteException, MalformedURLException {
    if (instance == null) {
        instance = new AuthenticationControllerSingleton();
         logger.info("AuthenticationControllerSingleton object bound to name 'AuthenticationController'.");
    }
    return instance;
}

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws RemoteException {
        logger.info("Login request received from client.");
        String hashedPassword = hashService.hashPassword(loginRequest.getPassword());
        loginRequest.setPassword(hashedPassword);
        boolean isSuccessful = userService.loginUser(loginRequest);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(isSuccessful);
        if (isSuccessful) {
            String token = generateToken();
            loginResponse.setToken(token);
            User user = userService.getUserByPhoneNumber(loginRequest.getPhoneNumber());
            Session session = new Session(token, user); // Create a new Session object
            sessionManager.addSession(session); // Add the Session object to the SessionManage
        } else {
            loginResponse.setError("Login failed. Invalid phone number or password.");
        }
        return loginResponse;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws RemoteException {
        logger.info("Register request received from client.");
        String hashedPassword = hashService.hashPassword(registerRequest.getPasswordHash());
        registerRequest.setPasswordHash(hashedPassword);
        userService.registerUser(registerRequest);
        User user = userService.getUserByPhoneNumber(registerRequest.getPhoneNumber());
        RegisterResponse registerResponse = new RegisterResponse();
        if (user != null) {
            registerResponse.setSuccess(true);
        } else {
            registerResponse.setSuccess(false);
            registerResponse.setError("Registration failed. Please try again.");
        }
        return registerResponse;
    }

    @Override
    public UpdateUserResponse update(UpdateUserRequest updateUserRequest) throws RemoteException {
        User user = updateUserMapper.updateUserRequestToEntity(updateUserRequest);
        userService.updateUser(user);
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setBio(user.getBio());
        updateUserResponse.setEmailAddress(user.getEmailAddress());
        updateUserResponse.setEmailAddress(user.getEmailAddress());
        //updateUserResponse.setUserMode(user.getUserMode());
        updateUserResponse.setUserName(user.getUserName());
        updateUserResponse.setUpdated(true);
        return updateUserResponse;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}