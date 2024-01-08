package Services.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface RegisterService extends Remote {
    public boolean register(String phoneNumber, String displayName, String emailAddress, String passwordHash, String gender, String country, String dateOfBirth, String bio) throws RemoteException, SQLException, ClassNotFoundException;
}