package network;// Java

import dto.Controller.*;
import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.*;
import lookupnames.LookUpNames;
import network.manager.NetworkManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class NetworkFactory {
    private static NetworkFactory instance;

    private NetworkFactory() {
    }

    public static NetworkFactory getInstance() {
        if (instance == null) {
            instance = new NetworkFactory();
        }
        return instance;
    }

    public LoginResponse login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException {
        try {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.login(loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws SQLException, ClassNotFoundException {
        try {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.register(registerRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SendMessageResponse sendMessage(SendMessageRequest request) throws RemoteException, NotBoundException {
        MessageController controller = (MessageController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.MESSAGECONTROLLER.name());
        return controller.sendMessage(request);
    }

    public void connect(String phoneNumber, CallBackController callBackController) throws RemoteException, NotBoundException {
        OnlineController controller = (OnlineController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.ONLINECONTROLLER.name());
        controller.connect(phoneNumber, callBackController);
    }

    public void disconnect(String phoneNumber, CallBackController callBackController) throws RemoteException, NotBoundException {
        OnlineController controller = (OnlineController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.ONLINECONTROLLER.name());
        controller.disconnect(phoneNumber, callBackController);
    }

    public UserModel getUserModel(String Token) throws RemoteException, NotBoundException {
        UserProfileController controller = (UserProfileController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.USERPROFILECONTROLLER.name());
        return controller.getUserModel(Token);
    }

    public AddContactResponse addContact(AddContactRequest request) throws RemoteException, NotBoundException {
        InvitationController controller = (InvitationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.INVITATIONCONTROLLER.name());
        return controller.addContact(request);
    }

    public AcceptFriendResponse acceptFriendRequest(AcceptFriendRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        ContactsController controller = (ContactsController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.CONTACTCONTROLLER.name());
        return controller.acceptContact(request);
    }

    public List<GetContactsResponse> getContacts(GetContactsRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        ContactsController controller = (ContactsController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.CONTACTCONTROLLER.name());
        return controller.getContacts(request);
    }

}