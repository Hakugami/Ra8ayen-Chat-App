package controllers;
import dto.Controller.CallBackController;
import dto.Controller.OnlineController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineControllerImpl extends UnicastRemoteObject implements OnlineController {
    public static Map<String, CallBackController> clients;
    public OnlineControllerImpl() throws RemoteException {
        clients = new ConcurrentHashMap<>();
    }

    @Override
    public void connect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        clients.put(phoneNumber, callBackController);
    }

    @Override
    public void disconnect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        clients.remove(phoneNumber);
    }
}
