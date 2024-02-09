package service;

import Mapper.BlockMapper;
import controllers.OnlineControllerImpl;
import dao.BlockedUserDao;
import dao.ChatDao;
import dao.UserContactsDao;
import dao.UserDao;
import dao.impl.BlockedUserDaoImpl;
import dao.impl.ChatDaoImpl;
import dao.impl.UserContactsDaoImpl;
import dto.requests.BlockUserRequest;
import dto.requests.DeleteBlockContactRequest;
import dto.requests.GetBlockedContactRequest;
import dto.responses.BlockUserResponse;
import dto.responses.DeleteBlockContactResponse;
import dto.responses.GetBlockedContactResponse;
import model.entities.BlockedUsers;
import model.entities.Chat;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                try {
                    OnlineControllerImpl onlineController = OnlineControllerImpl.getInstance();
                    onlineController.updateListOfContactBlockedContact(blockUserRequest.getFriendPhoneNumber());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                blockUserResponse = blockMapper.getBlockUserResponseFromBlockUser(blockedUsers,true,"Blocked Successfully");
            }

        }

        return blockUserResponse;
    }
    public List<GetBlockedContactResponse> getBlockedContactResponseList(GetBlockedContactRequest getBlockedContactRequest){
        BlockMapper blockMapper = new BlockMapper();
        BlockedUserDao blockedUserDao;
        blockedUserDao = new BlockedUserDaoImpl();
        List<GetBlockedContactResponse> getBlockedContactResponseList = new ArrayList<>();

        BlockedUsers blockedUsers = blockMapper.getBlockUserFromGetBlockedRequest(getBlockedContactRequest);
        List<BlockedUsers> blockedUsersList=blockedUserDao.getBlockedContact(blockedUsers);
        for(BlockedUsers blockedUsers1: blockedUsersList){
            getBlockedContactResponseList.add(blockMapper.getBlockedContactResponseFromBlockedUsers(blockedUsers1));
        }
        return getBlockedContactResponseList;
    }
    public boolean checkIfUserBlocked(String UserPhoneNumber, String FriendPhoneNumber){
        BlockedUserDao blockedUserDao;
        BlockMapper blockMapper = new BlockMapper();
        blockedUserDao = new BlockedUserDaoImpl();

        BlockedUsers blockedUsers = blockMapper.getBlockUserFromPhoneNumberUserAndFriendUser(UserPhoneNumber,FriendPhoneNumber);
        return blockedUserDao.FriendIsBlocked(blockedUsers);
    }

    public DeleteBlockContactResponse deleteBlockedContact(DeleteBlockContactRequest deleteBlockContactRequest){
        BlockedUserDao blockedUserDao;
        BlockMapper blockMapper = new BlockMapper();
        blockedUserDao = new BlockedUserDaoImpl();
        DeleteBlockContactResponse deleteBlockContactResponse = new DeleteBlockContactResponse();

        BlockedUsers blockedUsers = blockMapper.getBlockContactFromDeleteContactRequest(deleteBlockContactRequest);
        int blocking = blockedUserDao.deleteByBlockingAndBlockedUser(blockedUsers);
        if(blocking==0){
            deleteBlockContactResponse.setDeleted(false);
            deleteBlockContactResponse.setDeleteMessage("Failed To Delete Blocked Contact");
        }else{
            try {
                OnlineControllerImpl onlineController = OnlineControllerImpl.getInstance();
                onlineController.updateListOfContactBlockedContact(deleteBlockContactRequest.getPhoneNumberFriend());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            deleteBlockContactResponse.setDeleted(true);
            deleteBlockContactResponse.setDeleteMessage("Delete Blocked Contact Successfully");
        }
        return deleteBlockContactResponse;
    }
}
