package dto.requests;

import java.io.Serializable;
import java.time.LocalDate;

public class BlockUserRequest implements Serializable {
   private String UserPhoneNumber;

    private String FriendPhoneNumber;

   private LocalDate localDate;

    public BlockUserRequest(){

    }
    public BlockUserRequest(String userPhoneNumber, String friendPhoneNumber , LocalDate localDate) {
        this.UserPhoneNumber = userPhoneNumber;
        this.FriendPhoneNumber = friendPhoneNumber;
        this.localDate = localDate;
    }

    public String getUserPhoneNumber() {
        return UserPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        UserPhoneNumber = userPhoneNumber;
    }

    public String getFriendPhoneNumber() {
        return FriendPhoneNumber;
    }

    public void setFriendPhoneNumber(String friendPhoneNumber) {
        FriendPhoneNumber = friendPhoneNumber;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
