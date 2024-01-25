package dto.Controller;

import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import dto.responses.GetMessageResponse;
import dto.responses.SendMessageResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageController extends Remote {

    SendMessageResponse sendMessage(SendMessageRequest request) throws RemoteException;
    GetMessageResponse getAllMessages(GetMessageRequest request) throws RemoteException;

}