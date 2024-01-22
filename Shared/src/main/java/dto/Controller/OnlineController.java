package dto.Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OnlineController extends Remote {
     void connect (String phoneNumber, CallBackController callBackController) throws RemoteException;
     void disconnect (String phoneNumber, CallBackController callBackController) throws RemoteException;
}
