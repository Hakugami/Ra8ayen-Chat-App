package controllers;

import Mapper.InvitationMapper;
import dto.Controller.InvitationController;
import dto.requests.AddContactRequest;
import dto.responses.AddContactResponse;
import model.entities.Notification;
import model.entities.User;
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
    private final UserService userService;
    protected InvitationControllerSingleton() throws RemoteException {
        super();
        invitationService = new InvitationService();
        userService = new UserService();
        invitationMapper = new InvitationMapper();
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
                isDone = invitationService.inviteContact(notification);
                responses.add("Invitation has been sent to " + phoneNumber);
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
}
