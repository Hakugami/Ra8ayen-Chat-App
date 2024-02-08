package dto.Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SendHeartBeatToServerFromClient extends Remote {
    void sendHeartbeat(String phoneNumber,CallBackController callBackController) throws RemoteException;
}
