package controllers;

import dto.Controller.MessageController;
import dto.Model.MessageModel;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import dto.responses.GetMessageResponse;
import dto.responses.SendMessageResponse;
import model.entities.Message;
import network.manager.NetworkManagerSingleton;
import service.MessageService;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Date;
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
            messageService.sendMessage(request);
            response.setSuccess(true);
            response.setError("Message sent successfully");
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