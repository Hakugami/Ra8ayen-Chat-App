package dto.requests;

import java.io.Serializable;

public class RefuseVoiceCallRequest implements Serializable {
    private String receiverPhoneNumber;
    private String senderPhoneNumber;

    public RefuseVoiceCallRequest (String receiverPhoneNumber, String senderPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.senderPhoneNumber = senderPhoneNumber;
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

    @Override
    public String toString() {
        return "AcceptVoiceCallRequest{" +
                "receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", senderPhoneNumber='" + senderPhoneNumber + '\'' +
                '}';
    }
}
