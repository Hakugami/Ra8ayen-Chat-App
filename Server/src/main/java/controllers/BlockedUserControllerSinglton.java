package controllers;

import dto.Controller.BlockedUsersController;
import dto.requests.BlockUserRequest;
import dto.requests.CheckIfFriendBlockedRequest;
import dto.requests.DeleteBlockContactRequest;
import dto.requests.GetBlockedContactRequest;
import dto.responses.BlockUserResponse;
import dto.responses.CheckIfFriendBlockedResponse;
import dto.responses.DeleteBlockContactResponse;
import dto.responses.GetBlockedContactResponse;
import service.BlockedUserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class BlockedUserControllerSinglton extends UnicastRemoteObject implements BlockedUsersController {

    private static BlockedUserControllerSinglton instance;

    private BlockedUserService blockedUserService;

    public static BlockedUserControllerSinglton getInstance() throws RemoteException {
        if(instance==null){
            instance = new BlockedUserControllerSinglton();
        }
        return instance;
    }

    private BlockedUserControllerSinglton() throws RemoteException{
        super();
        blockedUserService= new BlockedUserService();
    }
    @Override
    public BlockUserResponse blockUserByPhoneNumber(BlockUserRequest blockUserRequest) throws RemoteException, SQLException, ClassNotFoundException {
        return blockedUserService.blockUser(blockUserRequest);
    }

    @Override
    public List<GetBlockedContactResponse> getBlockedContacts(GetBlockedContactRequest getBlockedContactRequest) throws RemoteException, SQLException, ClassNotFoundException {
      System.out.println("getBlockedContactRequest Arraive "+getBlockedContactRequest.getUserID()+"  "+getBlockedContactRequest.getPhoneNumber());
        return blockedUserService.getBlockedContactResponseList(getBlockedContactRequest);
    }

    @Override
    public DeleteBlockContactResponse deleteBlockedContact(DeleteBlockContactRequest deleteBlockContactRequest) throws RemoteException, SQLException, ClassNotFoundException {
        return blockedUserService.deleteBlockedContact(deleteBlockContactRequest);
    }

    @Override
    public CheckIfFriendBlockedResponse checkIfFriendBlocked(CheckIfFriendBlockedRequest checkIfFriendBlockedRequest) throws RemoteException, SQLException, ClassNotFoundException {
      boolean CheckBlocked = blockedUserService.checkIfUserBlocked(checkIfFriendBlockedRequest.getPhoneNumberUser(), checkIfFriendBlockedRequest.getFriendPhoneNumber());
      CheckIfFriendBlockedResponse checkIfFriendBlockedResponse = new CheckIfFriendBlockedResponse();
      checkIfFriendBlockedResponse.setBlocked(CheckBlocked);
      if(CheckBlocked){
          checkIfFriendBlockedResponse.setBlockMessage("User Is Blocked");
      }else{
          checkIfFriendBlockedResponse.setBlockMessage("User Is not Blocked");
      }
        return checkIfFriendBlockedResponse;
    }


}
