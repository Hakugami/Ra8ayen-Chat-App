package dto.responses;

import dto.requests.UpdateUserRequest;

import java.util.Arrays;

public class UpdateUserRespnse {

    private String userName;
    private String emailAddress;
    private byte[] profilePicture;
    private String passwordHash;
    private String bio;
    private UserStatus userStatus;
    private UserMode userMode;
    private boolean isUpdated;
    private String errorMessage;

    public enum UserStatus {
        Online, Offline, Busy, Away
    }
    public enum UserMode{
        Busy, Away,Available
    }

    public UpdateUserRespnse(String userName, String emailAddress, byte[] profilePicture, String passwordHash, String bio, UserStatus userStatus,UserMode userMode, boolean isUpdated, String errorMessage) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.profilePicture = profilePicture;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.userStatus = userStatus;
        this.userMode = userMode;
        this.isUpdated = isUpdated;
        this.errorMessage = errorMessage;
    }

    //setter


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

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //getter
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

    public boolean isUpdated() {
        return isUpdated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "UpdateUserRespnse{" +
                "userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                ", passwordHash='" + passwordHash + '\'' +
                ", bio='" + bio + '\'' +
                ", userStatus=" + userStatus +
                ", userMode=" + userMode +
                ", isUpdated=" + isUpdated +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
