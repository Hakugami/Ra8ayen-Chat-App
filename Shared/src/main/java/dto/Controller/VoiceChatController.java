package dto.Controller;

import dto.requests.AcceptVoiceCallRequest;
import dto.requests.RefuseVoiceCallRequest;
import dto.requests.SendVoicePacketRequest;
import dto.requests.VoiceCallRequest;
import dto.responses.AcceptVoiceCallResponse;
import dto.responses.RefuseVoiceCallResponse;
import dto.responses.VoiceCallResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VoiceChatController extends Remote {
    public VoiceCallResponse connect(VoiceCallRequest voiceCallRequest) throws RemoteException;
    public void disconnectVoiceChat(String phoneNumber) throws Exception;
    public AcceptVoiceCallResponse AcceptVoiceCallRequest(AcceptVoiceCallRequest acceptVoiceCallRequest) throws RemoteException;
    public RefuseVoiceCallResponse refuseVoiceCallRequest(RefuseVoiceCallRequest refuseVoiceCallRequest) throws RemoteException;
    public void sendVoiceMessage(SendVoicePacketRequest voicePacketRequest) throws RemoteException;
    public SendVoicePacketRequest receiveVoiceMessage(String phoneNumber) throws RemoteException;

}
