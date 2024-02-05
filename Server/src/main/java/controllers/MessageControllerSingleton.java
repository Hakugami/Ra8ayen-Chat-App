package controllers;

import dto.Controller.MessageController;
import dto.Model.MessageModel;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import dto.responses.GetMessageResponse;
import dto.responses.SendMessageResponse;
import model.entities.Message;
import service.MessageService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MessageControllerSingleton extends UnicastRemoteObject implements MessageController {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private static MessageControllerSingleton instance;
    private MessageService messageService;

    protected MessageControllerSingleton() throws RemoteException {
        super();
        messageService = new MessageService();
    }

    public static MessageControllerSingleton getInstance() throws RemoteException {
        if (instance == null) {
            instance = new MessageControllerSingleton();
            logger.info("MessageControllerSingleton object bound to name 'MessageController'.");
        }
        return instance;
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws RemoteException {
        SendMessageResponse response = new SendMessageResponse();
        try {
            int messageId= messageService.sendMessage(request);
            response.setSuccess(true);
            response.setError("Message sent successfully");

            List<String> phoneNumber = messageService.getParticipantPhoneNumbers(request.getSenderId(), request.getReceiverId());

            System.out.println(phoneNumber);
            MessageModel messageModel = new MessageModel();

            messageModel.setMessageId(messageId);
            messageModel.setChatId(request.getReceiverId());
            messageModel.setMessageContent(request.getMessageContent());
            if (request.getIsAttachment()) {
                messageModel.setAttachment(true);
                messageModel.setAttachmentData(request.getAttachmentData());
            } else {
                messageModel.setAttachment(false);
            }
            //add UseModel to messageModel
            messageModel.setStyleMessage(request.getStyleMessage());
            messageModel.setSender(request.getSender());

            for (String number : phoneNumber) {
                if (OnlineControllerImpl.clients.containsKey(number)) {
                    if (request.isGroupMessage()) {
                        OnlineControllerImpl.clients.get(number).receiveGroupChatMessage(messageModel);
                    } else {
                        OnlineControllerImpl.clients.get(number).receiveNewMessage(messageModel);
                    }
                }
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError("Failed to send message: " + e.getMessage());
        }
        return response;
    }

    @Override
    public GetMessageResponse getAllMessages(GetMessageRequest request) throws RemoteException {
        GetMessageResponse response = new GetMessageResponse();
        try {
            List<Message> messages = messageService.getMessages(request);
            List<MessageModel> messageModels = messages.stream().map(message -> messageService.getMessageMapper().entityToModel(message)).collect(Collectors.toList());

            //   List<MessageModel> messageModels = new ArrayList<>();

            for (int i = 0; i < messages.size(); i++) {
                messageModels.get(i).setAttachment(messages.get(i).getIsAttachment());
                messageModels.get(i).setAttachmentData(messages.get(i).getAttachmentData());
                if (messageModels.get(i).isAttachment()) {
                    System.out.println("Attatch Size From Server " + messageModels.get(i).getAttachmentData().length);
                } else {
                    System.out.println("Attatch Size From Server " + 0);
                }
            }

            response.setMessageList(messageModels);
            response.setSuccess(true);
            response.setError("Messages retrieved successfully");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError("Failed to retrieve messages: " + e.getMessage());
        }
        return response;
    }

}