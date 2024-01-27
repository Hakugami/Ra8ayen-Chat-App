package dto.requests;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class AddContactRequest implements Serializable {
    private int userId;
    private List<String> friendsPhoneNumbers;

    public AddContactRequest() {

    }
    public AddContactRequest(int userId, List<String> friendsPhoneNumbers) {
        this.userId = userId;
        this.friendsPhoneNumbers = friendsPhoneNumbers;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getFriendsPhoneNumbers() {
        return friendsPhoneNumbers;
    }

    public void setFriendsPhoneNumbers(List<String> friendsPhoneNumbers) {
        this.friendsPhoneNumbers = friendsPhoneNumbers;
    }

    @Override
    public String toString() {
        return "AddContactRequest{" +
                "userId=" + userId +
                ", friendsPhoneNumbers=" + friendsPhoneNumbers +
                '}';
    }
}
