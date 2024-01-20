package dto.Controller;

import java.rmi.Remote;

public interface CallBackController extends Remote {
    public void refreshOnlineList() throws Exception;
    public void refreshGroupChatList() throws Exception;
    public void refreshGroupChatMessageList() throws Exception;
    public void refreshPrivateChatList() throws Exception;
    public void refreshPrivateChatMessageList() throws Exception;
    public void addNewGroupChat() throws Exception;
    public void addNewContact() throws Exception;
    public void deleteGroupChat() throws Exception;
    public void deleteContact() throws Exception;
    public void receiveMessage() throws Exception;
}
