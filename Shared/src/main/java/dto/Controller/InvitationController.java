package dto.Controller;

import dto.requests.AddContactRequest;
import dto.responses.AddContactResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InvitationController extends Remote {
    AddContactResponse addContact(AddContactRequest addContactRequest) throws RemoteException;
}
