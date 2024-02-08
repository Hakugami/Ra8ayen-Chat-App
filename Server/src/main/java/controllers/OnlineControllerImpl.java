package controllers;
import concurrency.manager.ConcurrencyManager;
import dto.Controller.CallBackController;
import dto.Controller.OnlineController;
import model.entities.User;
import service.ContactService;
import service.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class OnlineControllerImpl extends UnicastRemoteObject implements OnlineController {
    private static final Logger logger = Logger.getLogger(OnlineController.class.getName());
    private static OnlineControllerImpl onlineController;
    private final ContactService contactService;
    public static Map<String, CallBackController> clients =new ConcurrentHashMap<>();
    private OnlineControllerImpl() throws RemoteException {
        super();
        contactService = new ContactService();
    }
    public static OnlineControllerImpl getInstance() throws RemoteException {
        if (onlineController == null) {
            onlineController = new OnlineControllerImpl();
            logger.info("OnlineControllerImpl object bound to name 'OnlineController'.");
        }
        return onlineController;
    }

    @Override
    public boolean connect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ConcurrencyManager.getInstance().submitTask(() -> friendStatusNotifier(phoneNumber, User.UserStatus.Online));
        if(clients.containsKey(phoneNumber)) {
            clients.remove(phoneNumber, callBackController);
            return false;
        }
        else {
            clients.put(phoneNumber, callBackController);
            return true;
        }
    }
    @Override
    public void disconnect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ConcurrencyManager.getInstance().submitTask(() -> friendStatusNotifier(phoneNumber, User.UserStatus.Offline));
        clients.remove(phoneNumber);
    }

    private void friendStatusNotifier(String phoneNumber, User.UserStatus userStatus) {

        User user = new UserService().getUserByPhoneNumber(phoneNumber);
        List<String> userFriends = contactService.getFriendsPhoneNumbers(user.getUserID());
        for (String friendPhoneNumber : userFriends) {
            if (clients.containsKey(friendPhoneNumber)) {
                try {
                    if(userStatus.equals(User.UserStatus.Online))
                        clients.get(friendPhoneNumber).userIsOnline(user.getUserName());
                    else {
                        clients.get(friendPhoneNumber).userIsOffline(user.getUserName());
                    }
                } catch (RemoteException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

