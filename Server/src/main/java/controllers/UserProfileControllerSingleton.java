package controllers;

import Mapper.UserMapperImpl;
import dto.Controller.UserProfileController;
import dto.Model.UserModel;
import dto.requests.GetContactsRequest;
import dto.requests.UpdateUserRequest;
import dto.responses.GetContactsResponse;
import dto.responses.UpdateUserResponse;
import model.entities.User;
import service.ContactService;
import service.UserService;
import session.Session;
import session.manager.SessionManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class UserProfileControllerSingleton extends UnicastRemoteObject implements UserProfileController {
    private static final Logger logger = Logger.getLogger(UserProfileController.class.getName());
    private static UserProfileControllerSingleton userProfileControllerSingleton;
    UserService userService;
    ContactService contactService;
    SessionManager sessionManager;
    UserMapperImpl userMapper;
    private UserProfileControllerSingleton() throws RemoteException {
        super();
        userService = new UserService();
        sessionManager = SessionManager.getInstance();
        userMapper = new UserMapperImpl();
        contactService = new ContactService();
    }
    public static UserProfileControllerSingleton getInstance() throws RemoteException {
        if (userProfileControllerSingleton == null) {
            userProfileControllerSingleton = new UserProfileControllerSingleton();
            logger.info("UserProfileControllerSingleton object bound to name 'UserProfileController'.");
        }
        return userProfileControllerSingleton;
    }
    @Override
    public UpdateUserResponse update(UpdateUserRequest updateUserRequest) throws RemoteException {
        User user = userMapper.modelToEntity(updateUserRequest.getUserModel());
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setUserModel(userMapper.entityToModel(user));
        updateUserResponse.setUpdated(userService.updateUser(user));
        List<GetContactsResponse> contacts = contactService.getContacts(new GetContactsRequest(user.getUserID()));
        for (GetContactsResponse contact : contacts) {
            try {
                //check if the user is online
                if (OnlineControllerImpl.clients.get(contact.getPhoneNumber()) != null){
                    OnlineControllerImpl.clients.get(contact.getPhoneNumber()).updateOnlineList();
                }
            } catch (SQLException | ClassNotFoundException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
        return updateUserResponse;
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
