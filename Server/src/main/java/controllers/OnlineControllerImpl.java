package controllers;
import dto.Controller.CallBackController;
import dto.Controller.OnlineController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineControllerImpl extends UnicastRemoteObject implements OnlineController {

    private static OnlineControllerImpl onlineController;
    public static Map<String, CallBackController> clients =new ConcurrentHashMap<>();
    private OnlineControllerImpl() throws RemoteException {
        super();
    }
    public static OnlineControllerImpl getInstance() throws RemoteException {
        if (onlineController == null) {
            onlineController = new OnlineControllerImpl();
        }
        return onlineController;
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
