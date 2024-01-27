package dto.responses;

public class DeleteUserContactResponse {
    boolean isDeleted;

    String errorMessage;

    public DeleteUserContactResponse(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public DeleteUserContactResponse(){

    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
