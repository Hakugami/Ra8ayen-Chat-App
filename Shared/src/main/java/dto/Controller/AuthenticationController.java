package dto.Controller;

import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import dto.requests.UpdateUserRequest;
import dto.responses.LoginResponse;
import dto.responses.RegisterResponse;
import dto.responses.UpdateUserResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface AuthenticationController extends Remote {
        LoginResponse login (LoginRequest loginRequest) throws RemoteException, SQLException, ClassNotFoundException;
        UpdateUserResponse update (UpdateUserRequest updateUserRequest) throws RemoteException, SQLException, ClassNotFoundException;
        RegisterResponse register (RegisterRequest registerRequest) throws RemoteException, SQLException, ClassNotFoundException;
}
