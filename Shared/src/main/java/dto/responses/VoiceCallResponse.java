package dto.responses;

import java.io.Serializable;

public class VoiceCallResponse implements Serializable {
    private String receiverPhoneNumber;
    private String senderPhoneNumber;
    private boolean isSent;
    private String error;

    public VoiceCallResponse(String receiverPhoneNumber, String senderPhoneNumber, boolean isSent, String error) {
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.senderPhoneNumber = senderPhoneNumber;
        this.isSent = isSent;
        this.error = error;
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

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "VoiceCallResponse{" +
                "receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", senderPhoneNumber='" + senderPhoneNumber + '\'' +
                ", isSent=" + isSent +
                ", error='" + error + '\'' +
                '}';
    }
}
