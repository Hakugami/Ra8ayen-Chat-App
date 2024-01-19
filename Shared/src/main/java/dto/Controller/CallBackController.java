package dto.Controller;

public interface CallBackController {
    public void refreshOnlineList() throws Exception;
    public void refreshGroupChatList() throws Exception;
    public void refreshGroupChatMessageList() throws Exception;
    public void refreshPrivateChatList() throws Exception;
    public void refreshPrivateChatMessageList() throws Exception;
    public void addNewGroupChat() throws Exception;
    public void addNewContact() throws Exception;
    public void deleteGroupChat() throws Exception;
    public void deleteContact() throws Exception;
}
