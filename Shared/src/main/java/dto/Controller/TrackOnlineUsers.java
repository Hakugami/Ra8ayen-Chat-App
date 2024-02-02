package dto.Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackOnlineUsers extends Remote {
    void updateOnlineUsersCount(int count) throws RemoteException;
    int getOnlineUsersCount() throws RemoteException;
}
