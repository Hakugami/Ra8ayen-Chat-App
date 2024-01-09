package dto.Controller;

import dto.requests.RegisterRequest;
import dto.responses.RegisterResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationController extends Remote {
        RegisterResponse register (RegisterRequest registerRequest) throws RemoteException;
}
