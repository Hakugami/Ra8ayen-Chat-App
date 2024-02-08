package service;

import Mapper.BlockMapper;
import dao.BlockedUserDao;
import dao.ChatDao;
import dao.UserContactsDao;
import dao.UserDao;
import dao.impl.BlockedUserDaoImpl;
import dao.impl.ChatDaoImpl;
import dao.impl.UserContactsDaoImpl;
import dto.requests.BlockUserRequest;
import dto.responses.BlockUserResponse;
import model.entities.BlockedUsers;
import model.entities.Chat;

public class BlockedUserService {

    public BlockUserResponse blockUser(BlockUserRequest blockUserRequest){
        BlockMapper blockMapper = new BlockMapper();
        BlockedUsers blockedUsers = blockMapper.getBlockUserFromBlockUserRequest(blockUserRequest);
        BlockUserResponse blockUserResponse;

        System.out.println("From BlockedUserService Values of blockUserRequest"+blockUserRequest.getUserPhoneNumber()+" "+blockUserRequest.getFriendPhoneNumber());
        System.out.println("From BlockedUserService Values of blockUsers"+blockedUsers.getBlockingUserId()+" "+blockedUsers.getBlockedUserId());

        //check before add User as blocked if they are friend
        ChatDao chatDao = new ChatDaoImpl();
        Chat chat = chatDao.getPrivateChat(blockedUsers.getBlockingUserId(),blockedUsers.getBlockedUserId());

        if(chat==null){ //no private chat between them
             blockUserResponse = blockMapper.getBlockUserResponseFromBlockUser(blockedUsers,false,"No private chat between them");
        }else {
            BlockedUserDao blockedUserDao =new BlockedUserDaoImpl();
            boolean isBlocked = blockedUserDao.save(blockedUsers);
            if(!isBlocked){
                blockUserResponse = blockMapper.getBlockUserResponseFromBlockUser(blockedUsers,false,"Already Blocked");
            }else{
                blockUserResponse = blockMapper.getBlockUserResponseFromBlockUser(blockedUsers,true,"Blocked Successfully");
            }

        }

        return blockUserResponse;
    }
    public boolean checkIfUserBlocked(String UserPhoneNumber, String FriendPhoneNumber){
        BlockedUserDao blockedUserDao;
        BlockMapper blockMapper = new BlockMapper();
        blockedUserDao = new BlockedUserDaoImpl();

        BlockedUsers blockedUsers = blockMapper.getBlockUserFromPhoneNumberUserAndFriendUser(UserPhoneNumber,FriendPhoneNumber);
        return blockedUserDao.FriendIsBlocked(blockedUsers);
    }

}
