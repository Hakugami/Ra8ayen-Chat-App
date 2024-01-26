package dto.Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallBackController extends Remote {
   /* void refreshOnlineList() throws RemoteException;
    void refreshGroupChatList() throws RemoteException;
    void refreshGroupChatMessageList() throws RemoteException;
    void refreshPrivateChatList() throws RemoteException;
    void refreshPrivateChatMessageList() throws RemoteException;
    void addNewGroupChat() throws RemoteException;
    void addNewContact() throws RemoteException;
    void deleteGroupChat() throws RemoteException;
    void deleteContact() throws RemoteException;*/
    void receiveAnnouncement(String announcement, String announcementTitle) throws RemoteException;
}
