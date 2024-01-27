package dto.Controller;

import dto.Model.UserModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserProfileController extends Remote {
    UserModel update (UserModel userModel) throws RemoteException;
    UserModel getUserModel (String Token) throws RemoteException;
}
