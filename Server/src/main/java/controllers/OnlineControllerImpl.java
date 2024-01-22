package controllers;
import dto.Controller.CallBackController;
import dto.Controller.OnlineController;
import server.ServerApplication;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OnlineControllerImpl extends UnicastRemoteObject implements OnlineController {
    public OnlineControllerImpl() throws RemoteException {
    }

    @Override
    public void connect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ServerApplication.clients.put(phoneNumber, callBackController);
    }

    @Override
    public void disconnect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ServerApplication.clients.remove(phoneNumber);
    }
}
