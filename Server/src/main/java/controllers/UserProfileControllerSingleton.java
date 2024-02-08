package controllers;

import Mapper.UserMapperImpl;
import concurrency.manager.ConcurrencyManager;
import dto.Controller.UserProfileController;
import dto.Model.UserModel;
import dto.requests.GetContactsRequest;
import dto.requests.UpdateUserRequest;
import dto.responses.GetContactsResponse;
import dto.responses.UpdateUserResponse;
import exceptions.DuplicateEntryException;
import model.entities.User;
import server.ServerApplication;
import service.BlockedUserService;
import service.ContactService;
import service.UserService;
import session.Session;
import session.manager.SessionManager;
import userstable.UsersTableStateSingleton;

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

    BlockedUserService blockedUserService;
    private UserProfileControllerSingleton() throws RemoteException {
        super();
        userService = new UserService();
        sessionManager = SessionManager.getInstance();
        userMapper = new UserMapperImpl();
        contactService = new ContactService();
        blockedUserService = new BlockedUserService();
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
        try {
            updateUserResponse.setUpdated(userService.updateUser(user));
        } catch (DuplicateEntryException e) {
            updateUserResponse.setUpdated(false);
            updateUserResponse.setErrorMessage("Update failed due to " + e.getDuplicateColumn() + ": " + e.getDuplicateValue() + " already being used.");
            return updateUserResponse;
        }
        System.out.println("User updated successfully.");

        List<GetContactsResponse> contacts = contactService.getContacts(new GetContactsRequest(user.getUserID()));
        ConcurrencyManager.getInstance().submitTask(() -> {
            for (GetContactsResponse contact : contacts) {
                try {
                    if (OnlineControllerImpl.clients.get(contact.getPhoneNumber()) != null){
                        //here i check if user need to change his status
                        if(updateUserRequest.isChangeStatus() && getInstance().FriendIsBlocked(updateUserRequest.getUserModel().getPhoneNumber(), contact.getPhoneNumber()) ){
                          System.out.println("This Contact is Blocked "+contact.getPhoneNumber());
                            continue;
                        }
                        OnlineControllerImpl.clients.get(contact.getPhoneNumber()).updateOnlineList();
                    }
                } catch (SQLException | ClassNotFoundException | NotBoundException | RemoteException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        UsersTableStateSingleton.getInstance().updateUser(user);
        ((UserListController) ServerApplication.controllers.get(Scenes.USER_LIST_VIEW)).loadUsers();
        return updateUserResponse;
    }

    @Override
    public UserModel getUserModel(String Token) throws RemoteException {
        System.out.println("User profile request received from client.");
        Session session = sessionManager.getSession(Token) ;
        if(session != null){
            logger.info("User profile request received from client.");
            return userService.userMapper.phoneToModel(session.getUser().getPhoneNumber());

        }
        logger.info("Couldn't find session.");
        return null;
    }

    @Override
    public boolean checkToken(String token) throws RemoteException {
        return sessionManager.getSession(token) != null;
    }

    @Override
    public UserModel getUserModelByPhoneNumber(String phoneNumber) throws RemoteException {
        return userService.userMapper.phoneToModel(phoneNumber);
    }

    private boolean FriendIsBlocked(String UserPhoneNumber , String FriendPhoneNumber){
        return blockedUserService.checkIfUserBlocked(UserPhoneNumber, FriendPhoneNumber);
    }

}
