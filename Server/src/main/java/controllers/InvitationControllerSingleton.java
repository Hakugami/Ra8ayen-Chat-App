package controllers;

import Mapper.InvitationMapper;
import Mapper.UserMapper;
import Mapper.UserMapperImpl;
import dto.Controller.InvitationController;
import dto.requests.AcceptFriendRequest;
import dto.requests.AddContactRequest;
import dto.requests.FriendRequest;
import dto.responses.AddContactResponse;
import model.entities.Notification;
import model.entities.User;
import service.ContactService;
import service.InvitationService;
import service.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class InvitationControllerSingleton extends UnicastRemoteObject implements InvitationController {
    private static InvitationControllerSingleton instance;
    private final InvitationService invitationService;
    private final InvitationMapper invitationMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    private final ContactService contactService;
    protected InvitationControllerSingleton() throws RemoteException {
        super();
        invitationService = new InvitationService();
        userService = new UserService();
        invitationMapper = new InvitationMapper();
        userMapper = new UserMapperImpl();
        contactService = new ContactService();
    }
    public static InvitationControllerSingleton getInstance() throws RemoteException {
        if (instance == null) {
            instance = new InvitationControllerSingleton();
        }
        return instance;
    }
    @Override
    public AddContactResponse addContact(AddContactRequest addContactRequest) throws RemoteException {
        AddContactResponse addContactResponse = new AddContactResponse();
        boolean isDone = false;
        List<String> responses = new ArrayList<>();
        for(String phoneNumber : addContactRequest.getFriendsPhoneNumbers()) {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if(user != null) {
                Notification notification = invitationMapper.addContactRequestToNotification(addContactRequest.getUserId(), user.getUserID());
              if(invitationService.ReceiverMakeInviteBefore(notification)){
                  responses.add("Invitation has accepted " + phoneNumber);
                  //in this function I added Friend To Chat and Contact List of user
                  AcceptUserAsFriend(notification.getSenderId(),phoneNumber);
              }
              else {
                  isDone = invitationService.inviteContact(notification);
                  responses.add("Invitation has been sent to " + phoneNumber);
                  FriendRequest friendRequest = new FriendRequest();
                  friendRequest.setReceiverPhoneNumber(phoneNumber);
                  friendRequest.setSenderPhoneNumber(userService.getUserById(addContactRequest.getUserId()).getPhoneNumber());
                  friendRequest.setUserModel(userMapper.entityToModel(userService.getUserById(addContactRequest.getUserId())));
                  OnlineControllerImpl.clients.get(phoneNumber).receiveNotification(friendRequest);
              }
            }
            else {
                responses.add(phoneNumber + "not found");
            }
        }
        addContactResponse.setDone(isDone);
        addContactResponse.setResponses(responses);
        addContactResponse.setFriendsPhoneNumbers(addContactRequest.getFriendsPhoneNumbers());
        return addContactResponse;
    }
    private void AcceptUserAsFriend(int UserID, String PhoneNumber){
        AcceptFriendRequest acceptFriendRequest= new AcceptFriendRequest(UserID,PhoneNumber);
        try {
            contactService.addContact(acceptFriendRequest);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
