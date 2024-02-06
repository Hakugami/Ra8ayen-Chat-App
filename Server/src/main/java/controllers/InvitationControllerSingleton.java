package controllers;

import Mapper.InvitationMapper;
import Mapper.UserMapper;
import Mapper.UserMapperImpl;
import concurrency.manager.ConcurrencyManager;
import dto.Controller.InvitationController;
import dto.Model.NotificationModel;
import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.AddContactResponse;
import dto.responses.GetNotificationsResponse;
import model.entities.Notification;
import model.entities.User;
import service.ContactService;
import service.EmailService;
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
    private final EmailService emailService;
    private final ContactService contactService;
    protected InvitationControllerSingleton() throws RemoteException {
        super();
        invitationService = new InvitationService();
        userService = new UserService();
        invitationMapper = new InvitationMapper();
        userMapper = new UserMapperImpl();
        contactService = new ContactService();
        emailService = new EmailService();
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
        List<String> responses = new ArrayList<>();
        User currentUser = userService.getUserById(addContactRequest.getUserId());
        for(String phoneNumber : addContactRequest.getFriendsPhoneNumbers()) {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if(user != null) {
                if(phoneNumber.equalsIgnoreCase(currentUser.getPhoneNumber())){
                    responses.add("You can't add yourself");
                    continue;
                }
                Notification notification = invitationMapper.addContactRequestToNotification(currentUser.getUserID(), user.getUserID());
                if(invitationService.ReceiverMakeInviteBefore(notification)){
                    responses.add("Invitation has accepted " + phoneNumber);
                    int notificationID = invitationService.deleteNotificationBySenderAndReceiver(notification);
                    ConcurrencyManager.getInstance().submitTask(() -> {
                        try {
                            AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest();
                            acceptFriendRequest.setId(notificationID);
                            acceptFriendRequest.setUserID(user.getUserID());
                            acceptFriendRequest.setMyPhoneNumber(currentUser.getPhoneNumber());
                            acceptFriendRequest.setFriendPhoneNumber(user.getPhoneNumber());
                            acceptFriendRequest.setUserModel(userMapper.entityToModel(user));
                            OnlineControllerImpl.clients.get(currentUser.getPhoneNumber()).updateNotificationList(acceptFriendRequest);
                        } catch (RemoteException e) {
                            System.out.println("Error in updating notification list");
                        }
                    });
                    AcceptUserAsFriend(notification.getSenderId(), currentUser.getPhoneNumber(), phoneNumber);
                }
                else {
                    String myEmail = currentUser.getEmailAddress();

                    ConcurrencyManager.getInstance().submitTask(() -> emailService.sendEmail(myEmail, user.getEmailAddress(),
                            "Chat App (رغايين)",
                            "You have a friend request from "
                                    + currentUser.getUserName() +
                                    " with phone number " + phoneNumber));
                    int notificationID = invitationService.inviteContact(notification);
                    responses.add("Invitation has been sent to " + phoneNumber);
                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setReceiverPhoneNumber(phoneNumber);
                    friendRequest.setSenderPhoneNumber(currentUser.getPhoneNumber());
                    friendRequest.setUserModel(userMapper.entityToModel(currentUser));
                    friendRequest.setId(notificationID);
                    if(OnlineControllerImpl.clients.containsKey(phoneNumber)){
                        ConcurrencyManager.getInstance().submitTask(() -> {
                            try {
                                OnlineControllerImpl.clients.get(phoneNumber).receiveNotification(friendRequest);
                            } catch (RemoteException e) {
                                addContactResponse.setDone(false);
                            }
                        });
                    }
                }
            }
            else {
                responses.add(phoneNumber + "not found");
            }
        }
        addContactResponse.setDone(true);
        addContactResponse.setResponses(responses);
        addContactResponse.setFriendsPhoneNumbers(addContactRequest.getFriendsPhoneNumbers());

        return addContactResponse;
    }

    @Override
    public GetNotificationsResponse getNotifications(GetNotificationsRequest getNotificationsRequest) throws RemoteException {
        GetNotificationsResponse getNotificationsResponse = new GetNotificationsResponse();
        List<Notification> notifications = invitationService.getNotifications();
        notifications = notifications.stream()
                .filter(notification -> notification.getReceiverId() == getNotificationsRequest.getUserID())
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

    private void AcceptUserAsFriend(int UserID, String PhoneNumber, String friendPhoneNumber){
        AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest();
        acceptFriendRequest.setUserID(UserID);
        acceptFriendRequest.setMyPhoneNumber(PhoneNumber);
        acceptFriendRequest.setFriendPhoneNumber(friendPhoneNumber);
        try {
            contactService.acceptContact(acceptFriendRequest);
        } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
