package dto.Controller;

import dto.Model.MessageModel;
import dto.requests.FriendRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
    public void receiveNewMessage(MessageModel message) throws RemoteException;
    public void receiveAddContactRequest(FriendRequest friendRequest) throws RemoteException;
    public void createNewChat(String senderPhoneNumber) throws RemoteException;
    void receiveAnnouncement(String announcement, String announcementTitle) throws RemoteException;
}
