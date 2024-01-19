package dto.Controller;

import dto.requests.DeleteUserContactRequest;
import dto.requests.GetContactsRequest;
import dto.responses.DeleteUserContactResponse;
import dto.responses.GetContactsResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ContactsController extends Remote {

    GetContactsResponse getContacts(GetContactsRequest getContactsRequest) throws RemoteException, SQLException, ClassNotFoundException;

    DeleteUserContactResponse deleteContact(DeleteUserContactRequest deleteUserContactRequest) throws RemoteException, SQLException, ClassNotFoundException;

}
