package dto.Controller;

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
    void receiveAnnouncement(String announcement) throws RemoteException;
}
