package dto.responses;

public class DeleteUserContactResponse {
    boolean isDeleted;

    public DeleteUserContactResponse(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public DeleteUserContactResponse(){

    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
