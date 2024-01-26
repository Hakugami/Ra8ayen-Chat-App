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
        /*List<ChatParticipant> userChats = chatParticipantsDaoImpl.getChats(userId);
        List<Chat> chats = chatDaoImpl.getAll();
        chats = chats.stream()
                .filter(chat -> chat.getAdminId() != 0 && userChats.stream().anyMatch(chatParticipant -> chatParticipant.getChatId() == chat.getChatId()))
                .collect(Collectors.toList());*/
        return chatDaoImpl.getGroupChats(userId);
    }

    public boolean createGroup(Chat chat) {
        return chatDaoImpl.save(chat);
    }

    public boolean addUserToGroup(ChatParticipant chatParticipant) {
        return chatParticipantsDaoImpl.save(chatParticipant);
    }
}
