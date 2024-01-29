package dto.Controller;

import dto.requests.AddContactRequest;
import dto.requests.GetNotificationsRequest;
import dto.responses.AddContactResponse;
import dto.responses.GetNotificationsResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InvitationController extends Remote {
    AddContactResponse addContact(AddContactRequest addContactRequest) throws RemoteException;
    GetNotificationsResponse getNotifications(GetNotificationsRequest getNotificationsRequest) throws RemoteException;
}
