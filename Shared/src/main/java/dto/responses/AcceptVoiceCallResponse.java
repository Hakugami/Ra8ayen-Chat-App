package dto.responses;

import java.io.Serializable;

public class AcceptVoiceCallResponse implements Serializable {
    private String receiverPhoneNumber;
    private String senderPhoneNumber;

    private boolean accepted;
    private String error;

    public AcceptVoiceCallResponse(String receiverPhoneNumber, String senderPhoneNumber, boolean accepted, String error) {
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.senderPhoneNumber = senderPhoneNumber;
        this.accepted = accepted;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "AcceptVoiceCallResponse{" +
                "receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", senderPhoneNumber='" + senderPhoneNumber + '\'' +
                ", accepted=" + accepted +
                ", error='" + error + '\'' +
                '}';
    }
}
