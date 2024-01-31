package controllers;

import Mapper.InvitationMapper;
import Mapper.UserMapper;
import Mapper.UserMapperImpl;
import dto.Controller.InvitationController;
import dto.Model.NotificationModel;
import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.AddContactResponse;
import dto.responses.GetNotificationsResponse;
import model.entities.Notification;
import model.entities.User;
import service.ContactService;
import service.InvitationService;
import service.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public boolean rejectFriendRequest(RejectContactRequest rejectContactRequest) throws RemoteException {
        Notification notification = new Notification();
        notification.setNotificationId(rejectContactRequest.getNotificationId());
        return invitationService.deleteNotification(notification);
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

    @Override
    public GetNotificationsResponse getNotifications(GetNotificationsRequest getNotificationsRequest) throws RemoteException {
        GetNotificationsResponse getNotificationsResponse = new GetNotificationsResponse();
        List<Notification> notifications = invitationService.getNotifications();
        notifications = notifications.stream()
                .filter(notification -> notification.getReceiverId() != getNotificationsRequest.getUserID())
                .toList();
        List<Integer> senderIds = notifications.stream()
                .map(Notification::getSenderId)
                .toList();
        List<User> senders = userService.getAllUsers();
        senders = senders.stream()
                .filter(user -> senderIds.contains(user.getUserID()))
                .toList();

        List<NotificationModel> notificationModels = notifications.stream()
                .map(notification -> new NotificationModel(notification.getNotificationId(), notification.getNotificationMessage()))
                .collect(Collectors.toList());

        List<UserModel> userModels = senders.stream()
                .map(user -> new UserModel(user.getUserID(), user.getUserName(), user.getPhoneNumber(), user.getProfilePicture()))
                .collect(Collectors.toList());

        getNotificationsResponse.setNotifications(notificationModels);
        getNotificationsResponse.setUsers(userModels);
        return getNotificationsResponse;
    }

    private void AcceptUserAsFriend(int UserID, String PhoneNumber){
        AcceptFriendRequest acceptFriendRequest= new AcceptFriendRequest(UserID,PhoneNumber);
        try {
            contactService.acceptContact(acceptFriendRequest);
        } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
