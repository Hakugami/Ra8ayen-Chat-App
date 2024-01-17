package dto.responses;
import java.io.Serializable;
public class GetContactsResponse implements Serializable {

    private int IdOfFriend;
    private String name;

    private byte[] ProfilePicture;

    private String PhoneNumber;

    private boolean Status;


    public GetContactsResponse(){

    }

    public GetContactsResponse(int idOfFriend, String name, byte[] profilePicture, String phoneNumber, boolean status) {
        IdOfFriend = idOfFriend;
        this.name = name;
        this.ProfilePicture = profilePicture;
        PhoneNumber = phoneNumber;
        Status = status;
    }

    public int getIdOfFriend() {
        return IdOfFriend;
    }

    public String getName() {
        return name;
    }

    public byte[] getProfilePicture() {
        return ProfilePicture;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public boolean isStatus() {
        return Status;
    }
}
