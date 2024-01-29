package service;

import Mapper.ChatMapper;
import Mapper.UserContactMapper;
import controllers.OnlineControllerImpl;
import dao.ChatDao;
import dao.ChatParticipantsDao;
import dao.UserContactsDao;
import dao.UserDao;
import dao.impl.ChatDaoImpl;
import dao.impl.ChatParticipantsDaoImpl;
import dao.impl.UserContactsDaoImpl;
import dao.impl.UserDaoImpl;
import dto.Controller.ContactsController;
import dto.Controller.OnlineController;
import dto.requests.AcceptFriendRequest;
import dto.requests.DeleteUserContactRequest;
import dto.requests.GetContactChatRequest;
import dto.requests.GetContactsRequest;
import dto.responses.AcceptFriendResponse;
import dto.responses.DeleteUserContactResponse;
import dto.responses.GetContactChatResponse;
import dto.responses.GetContactsResponse;
import model.entities.Chat;
import model.entities.ChatParticipant;
import model.entities.User;
import model.entities.UserContacts;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService{


    public AcceptFriendResponse acceptContact(AcceptFriendRequest acceptFriendRequest) throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
        int friendID = getFriendID(acceptFriendRequest);
        int chatID = createChat(acceptFriendRequest);
        addChatParticipants(acceptFriendRequest, friendID, chatID);
        addUserContacts(acceptFriendRequest, friendID);
        OnlineControllerImpl.clients.get(acceptFriendRequest.getFriendPhoneNumber()).updateOnlineList();
        return new AcceptFriendResponse(true, "");
    }

    private int getFriendID(AcceptFriendRequest acceptFriendRequest) {
        UserDao userDao = new UserDaoImpl();
        return userDao.getUserByPhoneNumber(acceptFriendRequest.getFriendPhoneNumber()).getUserID();
    }

    private int createChat(AcceptFriendRequest acceptFriendRequest) {
        ChatMapper chatMapper = new ChatMapper();
        Chat chat = chatMapper.chatFromAcceptFriendRequest(acceptFriendRequest);
        ChatDao chatDao = new ChatDaoImpl();
        return chatDao.savePrivateChat(chat);
    }

    private void addChatParticipants(AcceptFriendRequest acceptFriendRequest, int friendID, int chatID) {
        ChatParticipant chatParticipant = new ChatParticipant();
        ChatParticipantsDao chatParticipantsDao = new ChatParticipantsDaoImpl();
        chatParticipant.setChatId(chatID);
        chatParticipant.setParticipantUserId(acceptFriendRequest.getUserID());
        chatParticipantsDao.save(chatParticipant);
        chatParticipant.setParticipantUserId(friendID);
        chatParticipantsDao.save(chatParticipant);
    }

    private void addUserContacts(AcceptFriendRequest acceptFriendRequest, int friendID) {
        UserContactMapper userContactMapper = new UserContactMapper();
        UserContacts mineUserContacts = userContactMapper.UserContactFromAcceptFriendRequest(acceptFriendRequest.getUserID(),friendID);
        UserContacts friendUserContacts = userContactMapper.UserContactFromAcceptFriendRequest(friendID,acceptFriendRequest.getUserID());
        UserContactsDao userContactsDao = new UserContactsDaoImpl();
        userContactsDao.save(mineUserContacts);
        userContactsDao.save(friendUserContacts);
    }


    public GetContactChatResponse getContactChat(GetContactChatRequest getContactChatRequest) throws RemoteException {
        ChatDao chatDao = new ChatDaoImpl();
        ChatMapper chatMapper = new ChatMapper();
        Chat privateChat = chatDao.getPrivateChat(getContactChatRequest.getUserID(),getContactChatRequest.getFriendID());
        return chatMapper.chatToGetContactChatResponse(privateChat);
    }

    public List<GetContactsResponse> getContacts(GetContactsRequest getContactsRequest) throws RemoteException {
        UserDao userDao = new UserDaoImpl();
        List<User> listOfUser = userDao.getContactsByUserID(getContactsRequest.getIdUser());

        List<GetContactsResponse> listOfGetContactsResponse = new ArrayList<>();
        for (User user : listOfUser) {
            GetContactsResponse getContactsResponse = new GetContactsResponse();
            getContactsResponse.setIdOfFriend(user.getUserID());
            getContactsResponse.setName(user.getUserName());
            getContactsResponse.setProfilePicture(user.getProfilePicture());
            getContactsResponse.setPhoneNumber(user.getPhoneNumber());
            getContactsResponse.setStatus(user.getUserStatus() == User.UserStatus.Online);
            getContactsResponse.setUserStatus(GetContactsResponse.UserStatus.valueOf(user.getUserStatus().name()));
            getContactsResponse.setUserMode(GetContactsResponse.UserMode.valueOf(user.getUsermode().name()));
            getContactsResponse.setLastLogin(user.getLastLogin());
            listOfGetContactsResponse.add(getContactsResponse);
        }

        return listOfGetContactsResponse;
    }

    /*public List<GetContactsResponse> getContacts(GetContactsRequest getContactsRequest) throws RemoteException {

        UserContactMapper userContactMapper = new UserContactMapper();
        User userContacts = userContactMapper.UserContactFromRequestGet(getContactsRequest);

        UserContactsDao userContactsDao = new UserContactsDaoImpl();
        UserDao userDao = new UserDaoImpl();

        List<UserContacts> listOfUserContact=userContactsDao.getContactById(userContacts);

        List<GetContactsResponse> listOfUserContactResponse = new ArrayList<>();
        for(UserContacts userContacts1:listOfUserContact){
            User testUser = userDao.get(userContacts1.getFriendID());
            if(testUser!=null){
                listOfUserContactResponse.add(userContactMapper.ContactsResponseFromUserContact(userContacts1,testUser));
            }
        }
        return listOfUserContactResponse;
    }*/


    public DeleteUserContactResponse deleteContact(DeleteUserContactRequest deleteUserContactRequest) throws RemoteException {
        UserContactMapper userContactMapper = new UserContactMapper();

        UserContacts userContacts = userContactMapper.UserContactFromRequestDelete(deleteUserContactRequest);

        UserContactsDao userContactsDao = new UserContactsDaoImpl();

        userContactsDao.delete(userContacts);

        return new DeleteUserContactResponse();

    }
}
