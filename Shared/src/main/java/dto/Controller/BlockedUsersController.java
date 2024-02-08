package dto.Controller;

import dto.requests.BlockUserRequest;
import dto.responses.BlockUserResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface BlockedUsersController  extends Remote {
    BlockUserResponse blockUserByPhoneNumber(BlockUserRequest blockUserRequest) throws RemoteException, SQLException, ClassNotFoundException;

}
