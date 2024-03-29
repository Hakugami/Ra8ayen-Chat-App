package dto.responses;

import java.io.Serializable;
import java.util.Arrays;

public class RegisterResponse implements Serializable {
    private String phoneNumber;
    private String userName;
    private String emailAddress;
    private byte[] profilePicture;
    private String passwordHash;
    private Gender gender;
    private String country;
    private String dateOfBirth;
    private boolean success;
    private String Error;
    public enum Gender{
        Male,Female
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                ", passwordHash='" + passwordHash + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", success=" + success +
                ", Error='" + Error + '\'' +
                '}';
    }
}
