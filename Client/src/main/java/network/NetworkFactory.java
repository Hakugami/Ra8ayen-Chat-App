package network;// Java

import dto.Controller.*;
import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.*;
import javafx.application.Platform;
import lookupnames.LookUpNames;
import network.manager.NetworkManager;
import notification.NetworkDownNotification;
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

    private <T> T executeWithNotification(NetworkDownNotification<T> function) {
        try {
            return function.onNetworkDown();
        } catch (Exception e) {
            Platform.runLater(()-> Notifications.create().title("Server Down").text("Server is currently down").showError());
            return null;
        }
    }


    public LoginResponse login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException, RemoteException, NotBoundException {
        return executeWithNotification(() -> {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.login(loginRequest);
        });
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws SQLException, ClassNotFoundException {
        return executeWithNotification(() -> {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.register(registerRequest);
        });
    }

    public SendMessageResponse sendMessage(SendMessageRequest request) throws RemoteException, NotBoundException {
        return executeWithNotification(() -> {
            MessageController controller = (MessageController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.MESSAGECONTROLLER.name());
            return controller.sendMessage(request);
        });
    }

    public ChatBotResponse chatBot(ChatBotRequest request) throws RemoteException, NotBoundException {
        return executeWithNotification(() -> {
            MessageController controller = (MessageController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.MESSAGECONTROLLER.name());
            return controller.getChatBotResponse(request);
        });
    }

    public boolean connect(String phoneNumber, CallBackController callBackController) throws RemoteException, NotBoundException {
        return Boolean.TRUE.equals(executeWithNotification(() -> {
            OnlineController controller = (OnlineController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.ONLINECONTROLLER.name());
            return controller.connect(phoneNumber, callBackController);
        }));
    }

    public void disconnect(String phoneNumber, CallBackController callBackController) {
        executeWithNotification(() -> {
            OnlineController controller = (OnlineController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.ONLINECONTROLLER.name());
            controller.disconnect(phoneNumber, callBackController);
            return null;
        });
    }

    public void heartBeat() throws RemoteException, NotBoundException {
        executeWithNotification(() -> {
            OnlineController controller = (OnlineController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.ONLINECONTROLLER.name());
            controller.heartBeat();
            return null;
        });
    }

    public UserModel getUserModel(String Token) {
        return executeWithNotification(() -> {
            UserProfileController controller = (UserProfileController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.USERPROFILECONTROLLER.name());
            return controller.getUserModel(Token);
        });
    }

    public AddContactResponse addContact(AddContactRequest request) throws RemoteException, NotBoundException {
        return executeWithNotification(() -> {
            InvitationController controller = (InvitationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.INVITATIONCONTROLLER.name());
            return controller.addContact(request);
        });
    }

    public AcceptFriendResponse acceptFriendRequest(AcceptFriendRequest request) {
        return executeWithNotification(() -> {
            ContactsController controller = (ContactsController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.CONTACTCONTROLLER.name());
            return controller.acceptContact(request);
        });
    }

    public boolean rejectFriendRequest(RejectContactRequest request) throws RemoteException, NotBoundException {
        return Boolean.TRUE.equals(executeWithNotification(() -> {
            InvitationController controller = (InvitationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.INVITATIONCONTROLLER.name());
            return controller.rejectFriendRequest(request);
        }));
    }

    public List<GetContactsResponse> getContacts(GetContactsRequest request) {
        return executeWithNotification(() -> {
            ContactsController controller = (ContactsController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.CONTACTCONTROLLER.name());
            return controller.getContacts(request);
        });
    }
    public UpdateUserResponse updateUser(UpdateUserRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        return executeWithNotification(() -> {
            UserProfileController controller = (UserProfileController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.USERPROFILECONTROLLER.name());
            return controller.update(request);
        });
    }

    public CreateGroupChatResponse createGroupChat(CreateGroupChatRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        return executeWithNotification(() -> {
            GroupChatController controller = (GroupChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.GROUPCHATCONTROLLER.name());
            return controller.createGroupChat(request);
        });
    }

    public List<GetGroupResponse> getGroups(GetGroupRequest request) {
        return executeWithNotification(() -> {
            GroupChatController controller = (GroupChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.GROUPCHATCONTROLLER.name());
            return controller.getGroups(request);
        });
    }
    public GetMessageResponse getMessageOfChatID(GetMessageRequest request) {
        return executeWithNotification(() -> {
            MessageController controller = (MessageController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.MESSAGECONTROLLER.name());
            return controller.getAllMessages(request);
        });
    }

    public void sendVoicePacket(SendVoicePacketRequest request) throws NotBoundException {
        executeWithNotification(() -> {
            VoiceChatController controller = (VoiceChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.VOICECHATCONTROLLER.name());
            controller.sendVoiceMessage(request);
            return null;
        });
    }

    public VoiceCallResponse connect(VoiceCallRequest request) throws NotBoundException {
        return executeWithNotification(() -> {
            VoiceChatController controller = (VoiceChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.VOICECHATCONTROLLER.name());
            return controller.connect(request);
        });
    }

    public AcceptVoiceCallResponse acceptVoiceCallRequest(AcceptVoiceCallRequest request) {
        return executeWithNotification(() -> {
            VoiceChatController controller = (VoiceChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.VOICECHATCONTROLLER.name());
            return controller.AcceptVoiceCallRequest(request);
        });
    }

    public RefuseVoiceCallResponse refuseVoiceCallRequest(RefuseVoiceCallRequest request) {
        return executeWithNotification(() -> {
            VoiceChatController controller = (VoiceChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.VOICECHATCONTROLLER.name());
            return controller.refuseVoiceCallRequest(request);
        });
    }

    public SendVoicePacketRequest receiveVoiceMessage(String phoneNumber) throws NotBoundException {
        return executeWithNotification(() -> {
            VoiceChatController controller = (VoiceChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.VOICECHATCONTROLLER.name());
            return controller.receiveVoiceMessage(phoneNumber);
        });
    }

    public boolean checkPhoneNumber(String phoneNumber) throws RemoteException, NotBoundException {
        return Boolean.TRUE.equals(executeWithNotification(() -> {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.checkPhoneNumber(phoneNumber);
        }));
    }

    public GetNotificationsResponse getNotifications(GetNotificationsRequest request) {
        return executeWithNotification(() -> {
            InvitationController controller = (InvitationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.INVITATIONCONTROLLER.name());
            return controller.getNotifications(request);
        });
    }

    public RetrieveAttachmentResponse retrieveAttachment(RetrieveAttachmentRequest request) {
        return executeWithNotification(() -> {
            MessageController controller = (MessageController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.MESSAGECONTROLLER.name());
            return controller.retrieveAttachment(request);
        });
    }

    public BlockUserResponse blockUser(BlockUserRequest request) {
        return executeWithNotification(() -> {
            BlockedUsersController controller = (BlockedUsersController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.BLOCKUSERCONTROLLER.name());
            return controller.blockUserByPhoneNumber(request);
        });
    }


    public List<GetBlockedContactResponse> getBlockedContactResponseList(GetBlockedContactRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        return executeWithNotification(() -> {
            BlockedUsersController controller = (BlockedUsersController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.BLOCKUSERCONTROLLER.name());
            return controller.getBlockedContacts(request);
        });
    }

    public DeleteBlockContactResponse deleteBlockedContact(DeleteBlockContactRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        return executeWithNotification(() -> {
            BlockedUsersController controller = (BlockedUsersController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.BLOCKUSERCONTROLLER.name());
            return controller.deleteBlockedContact(request);
        });
    }
    public CheckIfFriendBlockedResponse checkIfUserBlocked(CheckIfFriendBlockedRequest request) throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        return executeWithNotification(() -> {
            BlockedUsersController controller = (BlockedUsersController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.BLOCKUSERCONTROLLER.name());
            return controller.checkIfFriendBlocked(request);
        });
    }
    public boolean checkToken(String token) {
        return Boolean.TRUE.equals(executeWithNotification(() -> {
            UserProfileController controller = (UserProfileController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.USERPROFILECONTROLLER.name());
            return controller.checkToken(token);
        }));
    }

    public UserModel getUserModelByPhoneNumber(String phoneNumber) throws RemoteException, NotBoundException {
        return executeWithNotification(() -> {
            UserProfileController controller = (UserProfileController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.USERPROFILECONTROLLER.name());
            return controller.getUserModelByPhoneNumber(phoneNumber);
        });
    }

    public void disconnectVoiceChat(String phoneNumber) throws RemoteException, NotBoundException {
        executeWithNotification(() -> {
            VoiceChatController controller = (VoiceChatController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.VOICECHATCONTROLLER.name());
            controller.disconnectVoiceChat(phoneNumber);
            return null;
        });
    }

}