package dto.responses;

import java.io.Serializable;
import java.util.List;

public class AddContactResponse implements Serializable {
    private List<String> friendsPhoneNumbers;
    private List<String> responses;
    private boolean isDone;
    public AddContactResponse() {
    }
    public AddContactResponse(List<String> friendsPhoneNumbers, List<String> responses, boolean isDone) {
        this.friendsPhoneNumbers = friendsPhoneNumbers;
        this.responses = responses;
        this.isDone = isDone;
    }

    public List<String> getFriendsPhoneNumbers() {
        return friendsPhoneNumbers;
    }

    public void setFriendsPhoneNumbers(List<String> friendsPhoneNumbers) {
        this.friendsPhoneNumbers = friendsPhoneNumbers;
    }

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "AddContactResponse{" +
                "friendsPhoneNumbers=" + friendsPhoneNumbers +
                ", responses=" + responses +
                ", isDone=" + isDone +
                '}';
    }
}
