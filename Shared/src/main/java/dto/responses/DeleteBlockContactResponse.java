package dto.responses;

import java.io.Serializable;

public class DeleteBlockContactResponse implements Serializable {

    private boolean Deleted;

    private String deleteMessage;

    public DeleteBlockContactResponse(){

    }
    public DeleteBlockContactResponse(boolean deleted, String deleteMessage) {
        Deleted = deleted;
        this.deleteMessage = deleteMessage;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }
}
