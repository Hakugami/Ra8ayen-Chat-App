package network;// Java
import dto.Controller.AuthenticationController;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import dto.responses.LoginResponse;
import dto.responses.RegisterResponse;
import lookupnames.LookUpNames;
import network.manager.NetworkManager;

import java.rmi.Naming;
import java.sql.SQLException;

public class NetworkFactory {
    private static NetworkFactory instance;
    private NetworkFactory() {
    }

    public static NetworkFactory getInstance() {
        if (instance == null) {
            instance = new NetworkFactory();
        }
        return instance;
    }
    public LoginResponse login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException {
        try {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.login(loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws SQLException, ClassNotFoundException {
        try {
            AuthenticationController controller = (AuthenticationController) NetworkManager.getInstance().getRegistry().lookup(LookUpNames.AUTHENTICATIONCONTROLLER.name());
            return controller.register(registerRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}