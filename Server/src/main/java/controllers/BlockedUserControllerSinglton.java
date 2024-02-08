package controllers;

import dto.Controller.BlockedUsersController;
import dto.requests.BlockUserRequest;
import dto.responses.BlockUserResponse;
import service.BlockedUserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class BlockedUserControllerSinglton extends UnicastRemoteObject implements BlockedUsersController {

    private static BlockedUserControllerSinglton instance;

    private BlockedUserService blockedUserService;
    private BlockedUserControllerSinglton() throws RemoteException{
        super();
        blockedUserService= new BlockedUserService();
    }
    @Override
    public BlockUserResponse blockUserByPhoneNumber(BlockUserRequest blockUserRequest) throws RemoteException, SQLException, ClassNotFoundException {
        return blockedUserService.blockUser(blockUserRequest);
    }

    public static BlockedUserControllerSinglton getInstance() throws RemoteException {
        if(instance==null){
            instance = new BlockedUserControllerSinglton();
        }
        return instance;
    }
}
