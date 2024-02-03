package controllers;

import dto.Controller.ContactsController;
import dto.requests.AcceptFriendRequest;
import dto.requests.DeleteUserContactRequest;
import dto.requests.GetContactChatRequest;
import dto.requests.GetContactsRequest;
import dto.responses.AcceptFriendResponse;
import dto.responses.DeleteUserContactResponse;
import dto.responses.GetContactChatResponse;
import dto.responses.GetContactsResponse;
import service.ContactService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ContactsControllerSingleton extends UnicastRemoteObject  implements ContactsController {
    Logger logger = Logger.getLogger(ContactsControllerSingleton.class.getName());
    private static ContactsControllerSingleton instance;
    ContactService contactService;

    private ContactsControllerSingleton() throws RemoteException {
        super();
        contactService = new ContactService();
        logger.info("ContactsControllerSingleton object bound to name 'ContactsController'.");
    }

    public static ContactsControllerSingleton getInstance() throws RemoteException {
        if (instance == null) {
            instance = new ContactsControllerSingleton();
        }
        return instance;
    }

    @Override
    public AcceptFriendResponse acceptContact(AcceptFriendRequest acceptFriendRequest) throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
            return contactService.acceptContact(acceptFriendRequest);
    }

    @Override
    public GetContactChatResponse getContactChat(GetContactChatRequest getContactChatRequest) throws RemoteException {
        return contactService.getContactChat(getContactChatRequest);
    }

    @Override
    public List<GetContactsResponse> getContacts(GetContactsRequest getContactsRequest) throws RemoteException {
        return contactService.getContacts(getContactsRequest);
    }

    @Override
    public DeleteUserContactResponse deleteContact(DeleteUserContactRequest deleteUserContactRequest) throws RemoteException {
        return contactService.deleteContact(deleteUserContactRequest);
    }

    @Override
    public List<GetContactChatResponse> getPrivateChats(List<GetContactChatRequest> getContactChatRequests) throws RemoteException, SQLException, ClassNotFoundException {
        return contactService.getContactPrivateChat(getContactChatRequests);
    }
}

