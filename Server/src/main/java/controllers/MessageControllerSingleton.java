package controllers;

import dto.Controller.MessageController;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import dto.responses.GetMessageResponse;
import dto.responses.SendMessageResponse;
import network.manager.NetworkManagerSingleton;
import service.MessageService;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MessageControllerSingleton extends UnicastRemoteObject implements MessageController {
    private static MessageControllerSingleton instance;
    private MessageService messageService;
    protected MessageControllerSingleton() throws RemoteException {
        super();
        messageService = new MessageService();
    }

    public static MessageControllerSingleton getInstance() throws RemoteException {
        if (instance == null) {
            instance = new MessageControllerSingleton();
            Registry registry = NetworkManagerSingleton.getInstance().getRegistry();
            registry.rebind("MessageController", instance);
            System.out.println("MessageControllerSingleton object bound to name 'MessageController'.");
        }
        return instance;
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws RemoteException {
        return null;
    }

    @Override
    public GetMessageResponse getAllMessages(GetMessageRequest request) throws RemoteException {
        return null;
    }
}
