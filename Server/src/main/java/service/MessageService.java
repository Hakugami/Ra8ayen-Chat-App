package service;

import Mapper.MessageMapper;
import Mapper.MessageMapperImpl;
import dao.impl.MessageDaoImpl;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import model.entities.Message;

import java.util.List;

public class MessageService {
    private MessageMapper messageMapper;
    private MessageDaoImpl messageDao;

    public MessageService() {
        this.messageMapper = new MessageMapperImpl();
        this.messageDao = new MessageDaoImpl();
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
}
