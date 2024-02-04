package dto.requests;

import java.io.Serializable;
import java.util.Arrays;

public class SendVoicePacketRequest implements Serializable {
    private String receiverPhoneNumber;
    private String senderPhoneNumber;
    private byte[] data;

    public SendVoicePacketRequest(String receiverPhoneNumber, String senderPhoneNumber, byte[] data) {
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.senderPhoneNumber = senderPhoneNumber;
        this.data = data;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
