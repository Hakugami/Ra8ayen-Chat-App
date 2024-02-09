package dto.Controller;

import dto.requests.BlockUserRequest;
import dto.requests.DeleteBlockContactRequest;
import dto.requests.GetBlockedContactRequest;
import dto.responses.BlockUserResponse;
import dto.responses.DeleteBlockContactResponse;
import dto.responses.GetBlockedContactResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface BlockedUsersController  extends Remote {
    BlockUserResponse blockUserByPhoneNumber(BlockUserRequest blockUserRequest) throws RemoteException, SQLException, ClassNotFoundException;

    List<GetBlockedContactResponse> getBlockedContacts(GetBlockedContactRequest getBlockedContactRequest) throws RemoteException, SQLException, ClassNotFoundException;

    DeleteBlockContactResponse deleteBlockedContact(DeleteBlockContactRequest deleteBlockContactRequest) throws RemoteException, SQLException, ClassNotFoundException;

}
