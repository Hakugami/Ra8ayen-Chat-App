package controllers;

import dto.Controller.UserProfileController;
import dto.Model.UserModel;
import model.entities.User;
import service.UserService;
import session.Session;
import session.manager.SessionManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class UserProfileControllerSingleton extends UnicastRemoteObject implements UserProfileController {
    private static final Logger logger = Logger.getLogger(UserProfileController.class.getName());
    private static UserProfileControllerSingleton userProfileControllerSingleton;
    UserService userService;
    SessionManager sessionManager;
    private UserProfileControllerSingleton() throws RemoteException {
        super();
        userService = new UserService();
        sessionManager = SessionManager.getInstance();
    }
    public static UserProfileControllerSingleton getInstance() throws RemoteException {
        if (userProfileControllerSingleton == null) {
            userProfileControllerSingleton = new UserProfileControllerSingleton();
            logger.info("UserProfileControllerSingleton object bound to name 'UserProfileController'.");
        }
        return userProfileControllerSingleton;
    }
    @Override
    public UserModel update(UserModel userModel) throws RemoteException {
        return null;
    }

    @Override
    public UserModel getUserModel(String Token) throws RemoteException {
        System.out.println("User profile request received from client.");
        Session session = sessionManager.getSession(Token) ;
        System.out.println(session);
        System.out.println(sessionManager);
        if(session != null){
            logger.info("User profile request received from client.");
            return userService.userMapper.phoneToModel(session.getUser().getPhoneNumber());

        }
        logger.info("Couldn't find session.");
        return null;
    }
}
