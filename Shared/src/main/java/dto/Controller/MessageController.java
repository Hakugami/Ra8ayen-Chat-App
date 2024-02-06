package dto.Controller;

import dto.requests.ChatBotRequest;
import dto.requests.GetMessageRequest;
import dto.requests.RetrieveAttachmentRequest;
import dto.requests.SendMessageRequest;
import dto.responses.ChatBotResponse;
import dto.responses.GetMessageResponse;
import dto.responses.RetrieveAttachmentResponse;
import dto.responses.SendMessageResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageController extends Remote {

    SendMessageResponse sendMessage(SendMessageRequest request) throws RemoteException;
    GetMessageResponse getAllMessages(GetMessageRequest request) throws RemoteException;
    ChatBotResponse getChatBotResponse(ChatBotRequest request) throws RemoteException;
    RetrieveAttachmentResponse retrieveAttachment(RetrieveAttachmentRequest request) throws RemoteException;
}
