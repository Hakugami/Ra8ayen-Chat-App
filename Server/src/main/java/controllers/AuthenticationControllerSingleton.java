package controllers;

import dao.impl.UserDaoImpl;
import dto.Controller.AuthenticationController;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import dto.responses.LoginResponse;
import dto.responses.RegisterResponse;
import exceptions.DuplicateEntryException;
import model.entities.User;
import service.EncryptionService;
import service.HashService;
import service.UserService;
import session.Session;
import session.manager.SessionManager;
import userstable.UsersTableStateSingleton;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class AuthenticationControllerSingleton extends UnicastRemoteObject implements AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private static AuthenticationControllerSingleton instance;
    private final UserService userService;
    private final HashService hashService;
    private EncryptionService encryptionService;
    private final SessionManager sessionManager;
    UserDaoImpl userDaoImp = new UserDaoImpl();
    List<User> users = userDaoImp.getAll();
    static int offlineUsers ;
    static List<User> newUsersList = new ArrayList<>();
private AuthenticationControllerSingleton() throws RemoteException {
    super();
    userService = new UserService();
    hashService = new HashService(getClass().getClassLoader().getResourceAsStream("hashing.properties"));
    encryptionService = new EncryptionService(getClass().getClassLoader().getResourceAsStream("keystore.jceks"), "Buh123!","Buh1234!", getClass().getClassLoader().getResourceAsStream("encryption.properties"));
    sessionManager = SessionManager.getInstance();
    offlineUsers = users.size();
    newUsersList.addAll(users);
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
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            userService.registerUser(registerRequest);
        } catch (DuplicateEntryException e) {
            registerResponse.setSuccess(false);
            registerResponse.setError("Registration failed due to " + e.getDuplicateColumn() + ": " + e.getDuplicateValue() + " already being used.");
            return registerResponse;
        }
        User user = userService.getUserByPhoneNumber(registerRequest.getPhoneNumber());
        if (user != null) {
            registerResponse.setSuccess(true);
            UsersTableStateSingleton.getInstance().addUser(user);
            //offlineUsers++;
            UpdateOfflineUsers(getOfflineUsers()+1);
            newUsersList.add(user);
        } else {
            registerResponse.setSuccess(false);
            registerResponse.setError("Registration failed. Please try again.");
        }
        return registerResponse;
    }

    @Override
    public boolean checkPhoneNumber(String phoneNumber) throws RemoteException {
        return userService.getUserByPhoneNumber(phoneNumber) != null;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static void setOfflineUsers(int offlineUsers) {
        AuthenticationControllerSingleton.offlineUsers = offlineUsers;
    }
    public static int getOfflineUsers(){
        return AuthenticationControllerSingleton.offlineUsers;
    }
    public static void UpdateOfflineUsers(int offlineUsers){
        setOfflineUsers(offlineUsers);
    }
}