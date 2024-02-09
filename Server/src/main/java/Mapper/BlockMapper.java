package Mapper;

import dao.ChatDao;
import dao.UserDao;
import dao.impl.ChatDaoImpl;
import dao.impl.UserDaoImpl;
import dto.requests.BlockUserRequest;
import dto.requests.DeleteBlockContactRequest;
import dto.requests.GetBlockedContactRequest;
import dto.responses.BlockUserResponse;
import dto.responses.GetBlockedContactResponse;
import model.entities.BlockedUsers;
import model.entities.Chat;
import model.entities.User;

public class BlockMapper {



    public BlockMapper(){

    }
    public BlockedUsers getBlockUserFromBlockUserRequest(BlockUserRequest blockUserRequest){
        UserDao userDao;
        userDao = new UserDaoImpl();
        BlockedUsers blockedUsers = new BlockedUsers();
        User Friend = userDao.getUserByPhoneNumber(blockUserRequest.getFriendPhoneNumber());
        User user = userDao.getUserByPhoneNumber(blockUserRequest.getUserPhoneNumber());
        String date = blockUserRequest.getLocalDate().toString();

        blockedUsers.setBlockingUserId(user.getUserID());
        blockedUsers.setBlockedUserId(Friend.getUserID());
        blockedUsers.setBlockDate(date);
        return blockedUsers;
    }
    public BlockedUsers getBlockUserFromPhoneNumberUserAndFriendUser(String UserPhoneNumber ,String FriendPhoneNumber){
        UserDao userDao;
        userDao = new UserDaoImpl();
        User user = userDao.getUserByPhoneNumber(UserPhoneNumber);
        User Friend = userDao.getUserByPhoneNumber(FriendPhoneNumber);

        BlockedUsers blockedUsers = new BlockedUsers();
        blockedUsers.setBlockingUserId(user.getUserID());
        blockedUsers.setBlockedUserId(Friend.getUserID());

        return blockedUsers;
    }
    public BlockUserResponse getBlockUserResponseFromBlockUser(BlockedUsers blockedUsers,boolean isBlocked , String blockMessage){
        ChatDao chatDao = new ChatDaoImpl();
        Chat chat=chatDao.getPrivateChat(blockedUsers.getBlockingUserId(), blockedUsers.getBlockedUserId());
        UserDao userDao = new UserDaoImpl();
        if(chat==null){
            chat = new Chat();
            chat.setChatId(0);
        }

        BlockUserResponse blockUserResponse = new BlockUserResponse();
        blockUserResponse.setBlocked(isBlocked);
        blockUserResponse.setIDofFriend(blockedUsers.getBlockedUserId());
        blockUserResponse.setChatID(chat.getChatId());
        blockUserResponse.setPhoneNumberOfFriend(userDao.get(blockedUsers.getBlockedUserId()).getPhoneNumber());
       // if(!isBlocked){
            blockUserResponse.setBlockedMessage(blockMessage);
      //  }
        return blockUserResponse;
    }
    public BlockedUsers getBlockUserFromGetBlockedRequest(GetBlockedContactRequest getBlockedContactRequest){
        BlockedUsers blockedUsers = new BlockedUsers();
        blockedUsers.setBlockingUserId(getBlockedContactRequest.getUserID());
        return blockedUsers;
    }
    public GetBlockedContactResponse getBlockedContactResponseFromBlockedUsers(BlockedUsers blockedUsers){
        GetBlockedContactResponse getBlockedContactResponse = new GetBlockedContactResponse();
        UserDao userDao = new UserDaoImpl();
        User Friend = userDao.get(blockedUsers.getBlockedUserId());
        getBlockedContactResponse.setFriendID(blockedUsers.getBlockedUserId());
        getBlockedContactResponse.setFriendPhoneNumber(Friend.getPhoneNumber());
        getBlockedContactResponse.setName(Friend.getUserName());
        return getBlockedContactResponse;
    }
    public BlockedUsers getBlockContactFromDeleteContactRequest(DeleteBlockContactRequest deleteBlockContactRequest){
        BlockedUsers blockedUsers = new BlockedUsers();
        blockedUsers.setBlockingUserId(deleteBlockContactRequest.getUserID());
        blockedUsers.setBlockedUserId(deleteBlockContactRequest.getFriendUserID());
        return blockedUsers;
    }

}
