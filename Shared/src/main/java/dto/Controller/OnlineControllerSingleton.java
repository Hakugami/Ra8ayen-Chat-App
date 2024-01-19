package dto.Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OnlineControllerSingleton extends Remote {
    public void connect (String name , CallBackController callBackController) throws RemoteException;
}
