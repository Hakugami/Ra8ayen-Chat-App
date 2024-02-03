package Mapper;

import dao.UserContactsDao;
import dao.UserDao;
import dao.impl.UserContactsDaoImpl;
import dao.impl.UserDaoImpl;
import dto.requests.DeleteUserContactRequest;
import dto.requests.GetContactsRequest;
import dto.responses.DeleteUserContactResponse;
import dto.responses.GetContactsResponse;
import model.entities.User;
import model.entities.UserContacts;

import java.util.ArrayList;
import java.util.List;

import static model.entities.User.Gender.Female;

public class UserContactMapper {


    public UserContactMapper(){

    }
    //get Convert from GetContactsRequest to needed
    public User UserContactFromRequestGet(GetContactsRequest getContactsRequest){
            User user = new User();
            user.setUserID(user.getUserID());
            return user;
    }
    //get Response from User - UserContact
public GetContactsResponse ContactsResponseFromUserContact(UserContacts userContacts, User user){
    GetContactsResponse.UserStatus userStatus;
    if(user.getUserStatus().name().equals("Online")){
        userStatus = GetContactsResponse.UserStatus.Online;
    }else{
        userStatus = GetContactsResponse.UserStatus.Offline;
    }
    GetContactsResponse getContactsResponse = new GetContactsResponse(userContacts.getUserID(), user.getUserName(), user.getProfilePicture(), user.getPhoneNumber(), userStatus == GetContactsResponse.UserStatus.Online);
    getContactsResponse.setUserStatus(userStatus);
    getContactsResponse.setLastLogin(user.getLastLogin());
    getContactsResponse.setUserMode(GetContactsResponse.UserMode.Available);
    return getContactsResponse;
}
    public UserContacts UserContactFromRequestDelete(DeleteUserContactRequest deleteUserContactRequest){
        UserContacts userContacts = new UserContacts(deleteUserContactRequest.getId(),deleteUserContactRequest.getFriendId(),"random");
        return userContacts;
    }

    public UserContacts UserContactFromAcceptFriendRequest(int userID, int friendID) {
        UserContacts userContacts = new UserContacts(userID,friendID);
        return userContacts;
    }

}
