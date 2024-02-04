package server;

import controllers.AuthenticationControllerSingleton;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import network.manager.NetworkManagerSingleton;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class StartServer {
    public static void main(String[] args) throws RemoteException {
        ServerApplication.main(args);
    }
}
