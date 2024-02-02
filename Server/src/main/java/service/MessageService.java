package service;

import Mapper.MessageMapper;
import Mapper.MessageMapperImpl;
import dao.impl.ChatParticipantsDaoImpl;
import dao.impl.MessageDaoImpl;
import dao.impl.UserDaoImpl;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import model.entities.ChatParticipant;
import model.entities.Message;
import model.entities.User;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private MessageMapper messageMapper;
    private MessageDaoImpl messageDao;
    private ChatParticipantsDaoImpl chatParticipantsDao;

    private UserDaoImpl userDao;

    public MessageService() {
        this.messageMapper = new MessageMapperImpl();
        this.messageDao = new MessageDaoImpl();
        this.chatParticipantsDao = new ChatParticipantsDaoImpl();
        this.userDao = new UserDaoImpl();
    }

    public void sendMessage(SendMessageRequest request) {
        Message message = messageMapper.sendRequestToEntity(request);
        if (messageDao.save(message)) {
            System.out.println("Message saved successfully");
        } else {
            System.out.println("Failed to save message");
        }

    }

    public List<Message> getMessages(GetMessageRequest request) {
        Message message = messageMapper.getMessageRequestToEntity(request);
        return messageDao.getChatMessages(message.getReceiverId());
    }

    public MessageMapper getMessageMapper() {
        return messageMapper;
    }

    public List<String> getParticipantPhoneNumbers(int senderID, int chatID) {
        List<ChatParticipant> chatParticipants = chatParticipantsDao.get(chatID, senderID);
        List<String> phoneNumbers = new ArrayList<>();
        for (ChatParticipant chatParticipant : chatParticipants) {
            if (chatParticipant.getParticipantUserId() != senderID) {
                User user = userDao.get(chatParticipant.getParticipantUserId());
                if (user != null && !user.getUserStatus().equals(User.UserStatus.Offline)) {
                    phoneNumbers.add(user.getPhoneNumber());
                }
            }
        }
        return phoneNumbers;
    }
}
