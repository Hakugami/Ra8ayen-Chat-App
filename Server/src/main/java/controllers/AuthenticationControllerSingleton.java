package controllers;

import dto.Controller.AuthenticationController;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import dto.responses.LoginResponse;
import dto.responses.RegisterResponse;
import model.entities.User;
import network.manager.NetworkManagerSingleton;
import service.EncryptionService;
import service.HashService;
import service.UserService;
import session.Session;
import session.manager.SessionManager;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Logger;

public class AuthenticationControllerSingleton extends UnicastRemoteObject implements AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private static AuthenticationControllerSingleton instance;
    private UserService userService;
    private HashService hashService;
    private EncryptionService encryptionService;
    private SessionManager sessionManager;

    private AuthenticationControllerSingleton() throws RemoteException {
        super();
        userService = new UserService();
        hashService = new HashService("hashing.properties");
        encryptionService = new EncryptionService("keystore.jceks", "Buh123!","Buh1234!", "encryption.properties");
        sessionManager = new SessionManager();
    }


public static AuthenticationControllerSingleton getInstance() throws RemoteException, MalformedURLException {
    if (instance == null) {
        instance = new AuthenticationControllerSingleton();
        // Get the port number from the NetworkManagerSingleton class
        Registry registry = NetworkManagerSingleton.getInstance().getRegistry();
        // Bind the AuthenticationControllerSingleton to the RMI registry
        registry.rebind("AuthenticationController", instance);
         logger.info("AuthenticationControllerSingleton object bound to name 'AuthenticationController'.");
        System.out.println("AuthenticationControllerSingleton object bound to name 'AuthenticationController'.");
    }
    return instance;
}

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws RemoteException {
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
            sessionManager.addSession(session); // Add the Session object to the SessionManager
        } else {
            loginResponse.setError("Login failed. Invalid phone number or password.");
        }
        return loginResponse;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws RemoteException {
        String hashedPassword = hashService.hashPassword(registerRequest.getPasswordHash());
        registerRequest.setPasswordHash(hashedPassword);
        userService.registerUser(registerRequest);
        User user = userService.getUserByPhoneNumber(registerRequest.getPhoneNumber());
        if (user != null) {
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setSuccess(true);
            return registerResponse;
        } else {
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setSuccess(false);
            registerResponse.setError("Registration failed. Please try again.");
            return registerResponse;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}