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
        System.out.println(message.getMessageContent());
        if(messageDao.save(message)) {
            System.out.println("Message saved successfully");
        } else {
            System.out.println("Failed to save message");
        }

    }
    public List<Message> getMessages(GetMessageRequest request) {
        Message message = messageMapper.getMessageRequestToEntity(request);
        return messageDao.get(message.getSenderId(), message.getReceiverId());
    }

    public MessageMapper getMessageMapper() {
        return messageMapper;
    }

  public String getParticipantPhoneNumber(int senderID, int chatID){
    List<ChatParticipant> chatParticipant =chatParticipantsDao.get(chatID,senderID);
    System.out.println("chat "+chatParticipant.size());
    User user = null;
    for(ChatParticipant chatParticipant1: chatParticipant){
        if(chatParticipant1.getParticipantUserId()!=senderID){
            user = userDao.get(chatParticipant1.getParticipantUserId());
            break;
        }
    }
      System.out.println("user "+user);

    if(user == null || user.getUserStatus().equals(User.UserStatus.Offline)){
        return null;
    }else{
        return user.getPhoneNumber();
    }
}
}
