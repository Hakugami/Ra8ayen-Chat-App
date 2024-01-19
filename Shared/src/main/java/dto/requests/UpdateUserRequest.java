package dto.requests;

import java.util.Arrays;

public class UpdateUserRequest {

    private String userName;
    private String emailAddress;
    private byte[] profilePicture;
    private String passwordHash;
    private String bio;
    private UserStatus userStatus;
    private UserMode userMode;


    public enum UserStatus {
        Online, Offline
    }
    public enum UserMode{
        Busy, Away,Available
    }

    public UpdateUserRequest(String userName, String emailAddress, byte[] profilePicture,
                     String passwordHash, String bio, UserStatus userStatus,UserMode userMode) {
        this.userName = userName;
        this.emailAddress = emailAddress;

        this.profilePicture = profilePicture;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.userStatus = userStatus;
        this.userMode = userMode;
    }

    //setters

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    //getters

    public String getUserName() {
        return userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }


    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public String getPasswordHash() {
        return passwordHash;
    }


    public String getBio() {
        return bio;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                ", passwordHash='" + passwordHash + '\'' +
                ", bio='" + bio + '\'' +
                ", userStatus=" + userStatus +
                ", userMode=" + userMode +
                '}';
    }
}
