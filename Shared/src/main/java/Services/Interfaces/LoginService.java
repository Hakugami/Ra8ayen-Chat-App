package Services.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface LoginService extends Remote {
    boolean authenticate(String phoneNumber, String password) throws RemoteException, SQLException, ClassNotFoundException;
}
