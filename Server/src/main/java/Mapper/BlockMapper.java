package Mapper;

import dao.ChatDao;
import dao.UserDao;
import dao.impl.ChatDaoImpl;
import dao.impl.UserDaoImpl;
import dto.requests.BlockUserRequest;
import dto.responses.BlockUserResponse;
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
    public BlockUserResponse getBlockUserResponseFromBlockUser(BlockedUsers blockedUsers,boolean isBlocked , String blockMessage){
        ChatDao chatDao = new ChatDaoImpl();
        Chat chat=chatDao.getPrivateChat(blockedUsers.getBlockingUserId(), blockedUsers.getBlockId());
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
        if(!isBlocked){
            blockUserResponse.setBlockedMessage(blockMessage);
        }
        return blockUserResponse;
    }

}
