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


    public GetContactChatResponse getContactChat(GetContactChatRequest getContactChatRequest) {
        ChatDao chatDao = new ChatDaoImpl();
        ChatMapper chatMapper = new ChatMapper();
        Chat privateChat = chatDao.getPrivateChat(getContactChatRequest.getUserID(),getContactChatRequest.getFriendID());
        GetContactChatResponse response = chatMapper.chatToGetContactChatResponse(privateChat);
        response.setFriendID(getContactChatRequest.getFriendID());
        return response;
    }

    public List<GetContactsResponse> getContacts(GetContactsRequest getContactsRequest) {
        UserDao userDao = new UserDaoImpl();
        List<User> listOfUser = userDao.getContactsByUserID(getContactsRequest.getIdUser());

        List<GetContactsResponse> listOfGetContactsResponse = new ArrayList<>();
        ChatDaoImpl chatDao = new ChatDaoImpl();

        for (User user : listOfUser) {
            GetContactsResponse getContactsResponse = new GetContactsResponse();
            ChatDaoImpl chatDao = new ChatDaoImpl();
            Chat chat = chatDao.getPrivateChat(getContactsRequest.getIdUser(), user.getUserID());
            getContactsResponse.setChatID(chat.getChatId());
            getContactsResponse.setIdOfFriend(user.getUserID());
            getContactsResponse.setName(user.getUserName());
            getContactsResponse.setProfilePicture(user.getProfilePicture());
            getContactsResponse.setPhoneNumber(user.getPhoneNumber());
            getContactsResponse.setStatus(user.getUserStatus() == User.UserStatus.Online);
            getContactsResponse.setUserStatus(GetContactsResponse.UserStatus.valueOf(user.getUserStatus().name()));
            getContactsResponse.setUserMode(GetContactsResponse.UserMode.valueOf(user.getUsermode().name()));
            getContactsResponse.setLastLogin(user.getLastLogin());
            Chat chat = chatDao.getPrivateChat(getContactsRequest.getIdUser(), user.getUserID());
            getContactsResponse.setChatId(chat.getChatId());
            listOfGetContactsResponse.add(getContactsResponse);
        }

        return listOfGetContactsResponse;
    }
    public List<GetContactChatResponse> getContactPrivateChat(List<GetContactChatRequest> getContactChatRequests) throws RemoteException{
        List<GetContactChatResponse> getContactChatResponses = new ArrayList<>();
        for(GetContactChatRequest request: getContactChatRequests){
            getContactChatResponses.add(getContactChat(request));
        }
        return getContactChatResponses;
    }

    public DeleteUserContactResponse deleteContact(DeleteUserContactRequest deleteUserContactRequest) {
        UserContactMapper userContactMapper = new UserContactMapper();

        UserContacts userContacts = userContactMapper.UserContactFromRequestDelete(deleteUserContactRequest);

        UserContactsDao userContactsDao = new UserContactsDaoImpl();

        userContactsDao.delete(userContacts);

        return new DeleteUserContactResponse();
    }

    public List<Integer> getFriends(List<String> friendsPhoneNumbers, int userId) {
        List<Integer> users = new ArrayList<>();
        UserContactsDaoImpl userContactsDao = new UserContactsDaoImpl();
        List<Integer> friends = userContactsDao.getFriendsIDs(userId);
        UserDaoImpl userDao = new UserDaoImpl();
        int index = 0;
        for(String friendPhoneNumber : friendsPhoneNumbers) {
            User user = userDao.getUserByPhoneNumber(friendPhoneNumber);
            if(user != null && friends.contains(user.getUserID())) {
                friendsPhoneNumbers.set(index++, friendPhoneNumber + "added to group");
                users.add(user.getUserID());
            }
            else {
                friendsPhoneNumbers.set(index++, friendPhoneNumber + "not added to group");
            }
        }
        return users;
    }
}
