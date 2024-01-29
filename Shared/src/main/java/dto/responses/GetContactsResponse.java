package dto.responses;
import java.io.Serializable;
import java.util.Arrays;

public class GetContactsResponse implements Serializable {

    private int IdOfFriend;
    private String name;

    private byte[] ProfilePicture;

    private String PhoneNumber;

    private boolean Status;

    private UserStatus userStatus;
    private String lastLogin;
    private UserMode userMode;

    // enum classes
    public enum Gender {
        Male, Female
    }

    public enum UserStatus {
        Online, Offline
    }
    public enum UserMode{
        Busy, Away,Available
    }



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

    public void setIdOfFriend(int idOfFriend) {
        IdOfFriend = idOfFriend;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePicture(byte[] profilePicture) {
        ProfilePicture = profilePicture;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    @Override
    public String toString() {
        return "GetContactsResponse{" +
                "IdOfFriend=" + IdOfFriend +
                ", name='" + name + '\'' +
                ", ProfilePicture=" + Arrays.toString(ProfilePicture) +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Status=" + Status +
                ", userStatus=" + userStatus +
                ", lastLogin='" + lastLogin + '\'' +
                ", userMode=" + userMode +
                '}';
    }
}
