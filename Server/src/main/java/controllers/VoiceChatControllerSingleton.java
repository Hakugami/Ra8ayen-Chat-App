package controllers;

import dto.Controller.VoiceChatController;
import dto.requests.AcceptVoiceCallRequest;
import dto.requests.RefuseVoiceCallRequest;
import dto.requests.SendVoicePacketRequest;
import dto.requests.VoiceCallRequest;
import dto.responses.AcceptVoiceCallResponse;
import dto.responses.RefuseVoiceCallResponse;
import dto.responses.VoiceCallResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceChatControllerSingleton extends UnicastRemoteObject implements VoiceChatController {
    private static VoiceChatControllerSingleton instance;

    private final Map<String, List<SendVoicePacketRequest>> voicePackets;

    private VoiceChatControllerSingleton() throws RemoteException {
        super();
        voicePackets = new ConcurrentHashMap<>();
    }

    public static VoiceChatControllerSingleton getInstance() throws RemoteException {
        if (instance == null) {
            instance = new VoiceChatControllerSingleton();
        }
        return instance;
    }


    @Override
    public VoiceCallResponse connect(VoiceCallRequest voiceCallRequest) throws RemoteException {
        if (OnlineControllerImpl.clients.containsKey(voiceCallRequest.getReceiverPhoneNumber())) {
            OnlineControllerImpl.clients.get(voiceCallRequest.getReceiverPhoneNumber()).receiveVoiceCallRequest(voiceCallRequest);
            return new VoiceCallResponse(voiceCallRequest.getReceiverPhoneNumber(), voiceCallRequest.getSenderPhoneNumber(), true, null);
        }
        return new VoiceCallResponse(voiceCallRequest.getReceiverPhoneNumber(), voiceCallRequest.getSenderPhoneNumber(), false, "User is not online");
    }

    @Override
    public void disconnect(String phoneNumber) throws Exception {

    }

    @Override
    public AcceptVoiceCallResponse AcceptVoiceCallRequest(AcceptVoiceCallRequest acceptVoiceCallRequest) throws RemoteException {
        if (OnlineControllerImpl.clients.containsKey(acceptVoiceCallRequest.getReceiverPhoneNumber())) {
            AcceptVoiceCallResponse acceptVoiceCallResponse = new AcceptVoiceCallResponse(acceptVoiceCallRequest.getReceiverPhoneNumber(), acceptVoiceCallRequest.getSenderPhoneNumber(), true, null);
            OnlineControllerImpl.clients.get(acceptVoiceCallRequest.getReceiverPhoneNumber()).establishVoiceCall(acceptVoiceCallResponse);
//            OnlineControllerImpl.clients.get(acceptVoiceCallRequest.getSenderPhoneNumber()).establishVoiceCall(acceptVoiceCallResponse);
            return acceptVoiceCallResponse;
        }
        return new AcceptVoiceCallResponse(acceptVoiceCallRequest.getReceiverPhoneNumber(), acceptVoiceCallRequest.getSenderPhoneNumber(), false, "User is not online");
    }

    @Override
    public RefuseVoiceCallResponse refuseVoiceCallRequest(RefuseVoiceCallRequest refuseVoiceCallRequest) throws RemoteException {
        if (OnlineControllerImpl.clients.containsKey(refuseVoiceCallRequest.getReceiverPhoneNumber())) {
            return new RefuseVoiceCallResponse(refuseVoiceCallRequest.getReceiverPhoneNumber(), refuseVoiceCallRequest.getSenderPhoneNumber(), true, null);
        }
        return new RefuseVoiceCallResponse(refuseVoiceCallRequest.getReceiverPhoneNumber(), refuseVoiceCallRequest.getSenderPhoneNumber(), false, "User is not online");
    }


//    @Override
//    public void sendVoiceMessage(SendVoicePacketRequest voicePacketRequest) throws RemoteException {
////        if (OnlineControllerImpl.clients.containsKey(voicePacketRequest.getReceiverPhoneNumber())) {
////            System.out.println("Sending voice message to " + voicePacketRequest.getReceiverPhoneNumber());
////            System.out.println("Sending voice message from "+ voicePacketRequest.getSenderPhoneNumber());
////            OnlineControllerImpl.clients.get(voicePacketRequest.getReceiverPhoneNumber()).receiveVoiceMessage(voicePacketRequest);
////        }
//        voicePackets.get(voicePacketRequest.getReceiverPhoneNumber()).add(voicePacketRequest);
//    }

@Override
public void sendVoiceMessage(SendVoicePacketRequest voicePacketRequest) throws RemoteException {
    if (voicePackets != null) {
        List<SendVoicePacketRequest> packets = voicePackets.get(voicePacketRequest.getReceiverPhoneNumber());
        if (packets == null) {
            packets = new ArrayList<>();
            voicePackets.put(voicePacketRequest.getReceiverPhoneNumber(), packets);
        }
        packets.add(voicePacketRequest);
    } else {
        throw new RuntimeException("Voice packets is null");
    }
}

@Override
public SendVoicePacketRequest receiveVoiceMessage(String phoneNumber) throws RemoteException {
    synchronized (voicePackets) {
        List<SendVoicePacketRequest> packets = voicePackets.get(phoneNumber);
        if (packets != null && !packets.isEmpty()) {
            for (int i = packets.size() - 1; i >= 0; i--) {
                SendVoicePacketRequest packet = packets.get(i);
                if (!packet.getSenderPhoneNumber().equals(phoneNumber)) {
                    packets.remove(i);
                    System.out.println("Returning packet with sender phone number: " + packet.getSenderPhoneNumber() + " and receiver phone number: " + phoneNumber);
                    return packet;
                }
            }
        }
    }
    return null;
}
}
