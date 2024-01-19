package model.entities;

import java.util.Date;

public class User {

    // user attributes
    private int userID;
    private String phoneNumber;
    private String userName;
    private String emailAddress;
    private byte[] profilePicture;
    private String passwordHash;
    private Gender gender;
    private String country;
    private Date dateOfBirth;
    private String bio;
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


    // user constructor
    public User() {

    }

    public User(int userID, String phoneNumber, String userName, String emailAddress, byte[] profilePicture,
            String passwordHash, Gender gender, String country, Date dateOfBirth, String bio, UserStatus userStatus,
            UserMode userMode,
            String lastLogin) {
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.profilePicture = profilePicture;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.userStatus = userStatus;
        this.userMode = userMode;
        this.lastLogin = lastLogin;
    }

    
    // user methods

    // user setters



    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    public void setUsermode(UserMode usermode) {
        this.userMode = usermode;
    }


    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
    

    // user getters
      public int getUserID() {
        return userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

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

    public Gender getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
    public UserMode getUsermode() {
        return userMode;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    



  
   
  
    
}