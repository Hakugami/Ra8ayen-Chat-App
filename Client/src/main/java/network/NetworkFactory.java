package network;// Java

import dto.Controller.*;
import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.*;
import lookupnames.LookUpNames;
import network.manager.NetworkManager;
import org.controlsfx.control.Notifications;

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
            System.out.println(e.getMessage());
            return null;
        }
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws SQLException, ClassNotFoundException {
        try {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.register(registerRequest);
        } catch (Exception e) {
            Notifications.create().title("Server Down").text("Server is currently down").showError();
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

    //update Login Users numbers in dashboard----------------------------------------------
    public int  getOnlineUsersCount() throws RemoteException, NotBoundException {
        TrackOnlineUsers trackOnlineUsers = (TrackOnlineUsers) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.TRACKONLINEUSERS.name());
        return trackOnlineUsers.getOnlineUsersCount();
    }
    public void updateOnlineUsersCount(int onlineUsersCount) throws RemoteException, NotBoundException {
        TrackOnlineUsers trackOnlineUsers = (TrackOnlineUsers) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.TRACKONLINEUSERS.name());
        trackOnlineUsers.updateOnlineUsersCount(onlineUsersCount);
    }
    //--------------------------------------------------------------------------------------
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
    public UpdateUserResponse updateUser(UpdateUserRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        UserProfileController controller = (UserProfileController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.USERPROFILECONTROLLER.name());
        return controller.update(request);
    }
    public List<GetContactChatResponse> getPrivateChats(List<GetContactChatRequest> getContactChatRequests) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        ContactsController controller = (ContactsController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.CONTACTCONTROLLER.name());
        return controller.getPrivateChats(getContactChatRequests);

    }

    public CreateGroupChatResponse createGroupChat(CreateGroupChatRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        GroupChatController controller = (GroupChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.GROUPCHATCONTROLLER.name());
        return controller.createGroupChat(request);
    }

    public List<GetGroupResponse> getGroups(GetGroupRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        GroupChatController controller = (GroupChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.GROUPCHATCONTROLLER.name());
        return controller.getGroups(request);
    }
    public GetMessageResponse getMessageOfChatID(GetMessageRequest request) throws RemoteException, NotBoundException {
        MessageController controller = (MessageController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.MESSAGECONTROLLER.name());
        return controller.getAllMessages(request);
    }


}