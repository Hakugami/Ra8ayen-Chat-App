package dto.Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OnlineController extends Remote {
     boolean connect (String phoneNumber, CallBackController callBackController) throws RemoteException;
     void disconnect (String phoneNumber, CallBackController callBackController) throws RemoteException;
     void heartBeat () throws RemoteException;

}
