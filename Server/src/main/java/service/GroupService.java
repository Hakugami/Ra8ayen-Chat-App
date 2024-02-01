package service;

import dao.impl.ChatDaoImpl;
import dao.impl.ChatParticipantsDaoImpl;
import model.entities.Chat;
import model.entities.ChatParticipant;
import java.util.List;

public class GroupService {
    private final ChatDaoImpl chatDaoImpl;
    private final ChatParticipantsDaoImpl chatParticipantsDaoImpl;
    public GroupService() {
        chatDaoImpl = new ChatDaoImpl();
        chatParticipantsDaoImpl = new ChatParticipantsDaoImpl();
    }

    public List<Chat> getUserGroups(int userId) {
        return chatDaoImpl.getGroupChats(userId);
    }
    public List<Integer> getGroupChatParticipants(int chatId, String phoneNumber) {
        return chatParticipantsDaoImpl.getParticipantUserIds(chatId, phoneNumber);
    }
    public int createGroup(Chat chat) {
        return chatDaoImpl.saveGroupChat(chat);
    }

    public boolean addUserToGroup(ChatParticipant chatParticipant) {
        return chatParticipantsDaoImpl.save(chatParticipant);
    }
}
