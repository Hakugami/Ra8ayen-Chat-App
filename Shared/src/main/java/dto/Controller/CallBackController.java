package dto.Controller;

import dto.Model.MessageModel;
import dto.Model.NotificationModel;
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
   /* public void refreshOnlineList() throws RemoteException;
    public void refreshGroupChatList() throws RemoteException;
    public void refreshGroupChatMessageList() throws RemoteException;
    public void refreshPrivateChatList() throws RemoteException;
    public void refreshPrivateChatMessageList() throws RemoteException;
    public void addNewGroupChat() throws RemoteException;
    public void addNewContact() throws RemoteException;
    public void deleteGroupChat() throws RemoteException;
    public void deleteContact() throws RemoteException;*/

    public void respond() throws RemoteException;
    public void userIsOnline(String username) throws RemoteException;
    public void updateNotificationList(AcceptFriendRequest notification) throws RemoteException;
    public void userIsOffline(String username) throws RemoteException;
    public void receiveNotification(NotificationModel notification) throws RemoteException;
    public void receiveNewMessage(MessageModel message) throws RemoteException;
    public void receiveGroupChatMessage(MessageModel message) throws RemoteException;
    public void receiveAddContactRequest(FriendRequest friendRequest) throws RemoteException;
    public void createNewChat(String senderPhoneNumber) throws RemoteException;
    public void receiveAnnouncement(String announcement, String announcementTitle) throws RemoteException;
    public void updateOnlineList() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException;

    public void receiveVoiceCallRequest(VoiceCallRequest voiceCallRequest) throws RemoteException;
    public void establishVoiceCall(AcceptVoiceCallResponse acceptVoiceCallResponse) throws RemoteException;
    public void receiveVoiceMessage(SendVoicePacketRequest voicePacketRequest) throws RemoteException;



}
