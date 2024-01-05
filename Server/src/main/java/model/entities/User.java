package model.entities;

public class User {

    // user attributes
    private int id;
    private String phoneNumber;
    private String name;
    private String email;
    private int profilePicture;
    private String passwordHash;
    private Gender gender;
    private String country;
    private String dateOfBirth;
    private String bio;
    private UserStatus userStatus;
    private String lastLogin;

    // enum classes
    public enum Gender {
        Male, Female
    }

    public enum UserStatus {
        Online, Offline, Busy, Away
    }

    public User() {

    }

    // user constructor
    public User(int id, String phoneNumber, String name, String email,
            int profilePicture, String passwordHash, Gender gender,
            String country, String dateOfBirth, String bio, UserStatus userStatus,
            String lastLogin) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.userStatus = userStatus;
        this.lastLogin = lastLogin;
    }
    // user methods

    // user setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicture(int profilePicture) {
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

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    // user getters

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getProfilePicture() {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", phoneNumber=" + phoneNumber + ", name=" + name + ", email=" + email + ", gender="
                + gender + ", country=" + country + ", dateOfBirth=" + dateOfBirth + ", userStatus=" + userStatus
                + ", lastLogin=" + lastLogin + "]";
    }

}