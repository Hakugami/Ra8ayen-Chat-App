package dto.Controller;

import dto.Model.UserModel;
import dto.requests.UpdateUserRequest;
import dto.responses.UpdateUserResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface UserProfileController extends Remote {
    UpdateUserResponse update (UpdateUserRequest updateUserRequest) throws RemoteException, SQLException, ClassNotFoundException;
    UserModel getUserModel (String Token) throws RemoteException;
    public boolean checkToken(String token) throws RemoteException;
    public UserModel getUserModelByPhoneNumber(String phoneNumber) throws RemoteException;
}
