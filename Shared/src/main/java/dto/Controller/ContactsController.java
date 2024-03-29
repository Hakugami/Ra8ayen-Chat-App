package dto.Controller;

import dto.requests.AcceptFriendRequest;
import dto.requests.DeleteUserContactRequest;
import dto.requests.GetContactChatRequest;
import dto.requests.GetContactsRequest;
import dto.responses.AcceptFriendResponse;
import dto.responses.DeleteUserContactResponse;
import dto.responses.GetContactChatResponse;
import dto.responses.GetContactsResponse;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface ContactsController extends Remote {
    AcceptFriendResponse acceptContact(AcceptFriendRequest acceptFriendRequest) throws RemoteException, SQLException, NotBoundException, ClassNotFoundException;
    GetContactChatResponse getContactChat(GetContactChatRequest getContactChatRequest) throws RemoteException, SQLException, ClassNotFoundException;

    List<GetContactsResponse> getContacts(GetContactsRequest getContactsRequest) throws RemoteException, SQLException, ClassNotFoundException;

    DeleteUserContactResponse deleteContact(DeleteUserContactRequest deleteUserContactRequest) throws RemoteException, SQLException, ClassNotFoundException;

    List<GetContactChatResponse> getPrivateChats(List<GetContactChatRequest> getContactChatRequests) throws RemoteException, SQLException,ClassNotFoundException;
}
