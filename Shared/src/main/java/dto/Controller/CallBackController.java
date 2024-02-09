package dto.Controller;

import dto.Model.MessageModel;
import dto.Model.NotificationModel;
import dto.Model.UserModel;
import dto.requests.AcceptFriendRequest;
import dto.requests.FriendRequest;
import dto.requests.SendVoicePacketRequest;
import dto.requests.VoiceCallRequest;
import dto.responses.AcceptVoiceCallResponse;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface CallBackController extends Remote {
    void respond() throws RemoteException;
    void updateUserModel(UserModel userModel) throws RemoteException;
    void userIsOnline(String username) throws RemoteException;
    void updateNotificationList(AcceptFriendRequest notification) throws RemoteException;
    void userIsOffline(String username) throws RemoteException;
    void receiveNotification(NotificationModel notification) throws RemoteException;
    void receiveNewMessage(MessageModel message) throws RemoteException;
    void receiveGroupChatMessage(MessageModel message) throws RemoteException;
    void receiveAddContactRequest(FriendRequest friendRequest) throws RemoteException;
    void createNewChat(String senderPhoneNumber) throws RemoteException;
    void receiveAnnouncement(String announcement, String announcementTitle) throws RemoteException;
    void updateOnlineList() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException;
    void logout() throws RemoteException;
    void receiveVoiceCallRequest(VoiceCallRequest voiceCallRequest) throws RemoteException;
    void establishVoiceCall(AcceptVoiceCallResponse acceptVoiceCallResponse) throws RemoteException;
    void receiveVoiceMessage(SendVoicePacketRequest voicePacketRequest) throws RemoteException;

}
