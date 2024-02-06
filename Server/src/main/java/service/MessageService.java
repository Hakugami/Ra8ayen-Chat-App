package service;

import Mapper.MessageMapper;
import Mapper.MessageMapperImpl;
import dao.AttachmentDao;
import dao.impl.AttachmentDaoImpl;
import dao.impl.ChatParticipantsDaoImpl;
import dao.impl.MessageDaoImpl;
import dao.impl.UserDaoImpl;
import dto.requests.GetMessageRequest;
import dto.requests.RetrieveAttachmentRequest;
import dto.requests.SendMessageRequest;
import model.entities.Attachment;
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

    private AttachmentDao attachmentDao;

    public MessageService() {
        this.messageMapper = new MessageMapperImpl();
        this.messageDao = new MessageDaoImpl();
        this.chatParticipantsDao = new ChatParticipantsDaoImpl();
        this.userDao = new UserDaoImpl();
        this.attachmentDao = new AttachmentDaoImpl();
    }

    public int sendMessage(SendMessageRequest request) {
        System.out.println("Attachment received : "+request.getIsAttachment());
        int MessageID = -1;
        if(request.getIsAttachment()){
            Message message = MapMessageRequestToMessage(request);
            System.out.println(message.getAttachmentData().length+" From Message Service");
             MessageID = messageDao.sendMessageWithAttachment(message);
            if(MessageID !=-1){ //message Send Successfully send Attachment to Attachment Doa
                System.out.println("Message Content Save successfully");
                Attachment attachment = MapMessageRequestToAttachment(MessageID,request);
                if(attachmentDao.save(attachment)){
                    System.out.println("Attachment Content successfully");
                }else{
                    System.out.println("Failed to Save Attachment");
                }
            }
        }
        else {
            Message message = messageMapper.sendRequestToEntity(request);
            message.setBold(request.getStyleMessage().isBold());
            message.setItalic(request.getStyleMessage().isItalic());
            message.setUnderline(request.getStyleMessage().isUnderline());
            message.setFontSize(request.getStyleMessage().getFontSize());
            message.setFontColor(request.getStyleMessage().getFontColor());
            message.setTextBackground(request.getStyleMessage().getBackgroundColor());
            message.setFontStyle(request.getStyleMessage().getFontStyle());
          MessageID =  messageDao.saveAndReturnId(message);
        }
        return MessageID;
    }

    public List<Message> getMessages(GetMessageRequest request) {
        Message message = messageMapper.getMessageRequestToEntity(request);

        //need her to get all attachment of each message before return
        List<Message> messageListWithoutAttachment =messageDao.getChatMessages(message.getReceiverId());
            for(Message checkMessage:messageListWithoutAttachment){
                Attachment attachment=attachmentDao.get(checkMessage.getMessageId());
                if(attachment!=null){
                    checkMessage.setIsAttachment(true);
                 //   System.out.println("From Message Service "+attachment.getAttachment().length);
//                    checkMessage.setAttachmentData(attachment.getAttachment());
                    checkMessage.setAttachmentData(new byte[0]);
                }else{
                    checkMessage.setIsAttachment(false);
                }
            }
        return messageListWithoutAttachment;
    }

    public Attachment getAttachment(RetrieveAttachmentRequest request){
        return attachmentDao.get(request.getMessageId());
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
    public Message MapMessageRequestToMessage(SendMessageRequest sendMessageRequest){
        Message message = new Message();
        message.setSenderId(sendMessageRequest.getSenderId());
        message.setMessageContent(sendMessageRequest.getMessageContent());
        message.setReceiverId(sendMessageRequest.getReceiverId());
        message.setIsAttachment(sendMessageRequest.getIsAttachment());
        message.setAttachmentData(sendMessageRequest.getAttachmentData());
        message.setTime(sendMessageRequest.getTime());
        return message;
    }
    public Attachment MapMessageRequestToAttachment(int MessageID,SendMessageRequest sendMessageRequest){
        Attachment attachment = new Attachment();
        attachment.setMessageId(MessageID);
        attachment.setAttachment(sendMessageRequest.getAttachmentData());
        return attachment;
    }
}
