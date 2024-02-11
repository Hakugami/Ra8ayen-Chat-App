package controllers;

import concurrency.manager.ConcurrencyManager;
import dto.Controller.CallBackController;
import dto.Controller.OnlineController;
import exceptions.DuplicateEntryException;
import model.entities.User;
import service.BlockedUserService;
import service.ContactService;
import service.TrackOnlineUsersService;
import service.UserService;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class OnlineControllerImpl extends UnicastRemoteObject implements OnlineController {
    private static final Logger logger = Logger.getLogger(OnlineController.class.getName());
    private static OnlineControllerImpl onlineController;
    private final ContactService contactService;
    TrackOnlineUsersService trackOnlineUsersService;
    AuthenticationControllerSingleton auth;

    private final BlockedUserService blockedUserService;
    public static Map<String, CallBackController> clients = new ConcurrentHashMap<>();

    private OnlineControllerImpl() throws RemoteException, MalformedURLException {
        super();
        contactService = new ContactService();
        blockedUserService = new BlockedUserService();
        trackOnlineUsersService = TrackOnlineUsersService.getInstance();
        auth = AuthenticationControllerSingleton.getInstance();
        heartBeat();
    }

    public static OnlineControllerImpl getInstance() throws RemoteException, MalformedURLException {
        if (onlineController == null) {
            onlineController = new OnlineControllerImpl();
            logger.info("OnlineControllerImpl object bound to name 'OnlineController'.");
        }
        return onlineController;
    }

    @Override
    public boolean connect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ConcurrencyManager.getInstance().submitTask(() -> friendStatusNotifier(phoneNumber, User.UserStatus.Online));

        if (clients.containsKey(phoneNumber)) {
            return false;
        } else {
            clients.put(phoneNumber, callBackController);
            trackOnlineUsersService.updateOnlineUsersCount(clients.size());
            auth.UpdateOfflineUsers(auth.getOfflineUsers() - 1);

            return true;
        }
    }

    @Override
    public void disconnect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ConcurrencyManager.getInstance().submitTask(() -> friendStatusNotifier(phoneNumber, User.UserStatus.Offline));
        clients.remove(phoneNumber);
        trackOnlineUsersService.updateOnlineUsersCount(clients.size());
        auth.UpdateOfflineUsers(auth.getOfflineUsers() + 1);
    }

    private void friendStatusNotifier(String phoneNumber, User.UserStatus userStatus) {

        User user = new UserService().getUserByPhoneNumber(phoneNumber);
        List<String> userFriends = contactService.getFriendsPhoneNumbers(user.getUserID());
        for (String friendPhoneNumber : userFriends) {
            if (clients.containsKey(friendPhoneNumber) && UserNotBlocked(phoneNumber, friendPhoneNumber)) {

                try {
                    if (userStatus.equals(User.UserStatus.Online))
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

    private boolean UserNotBlocked(String UserPhoneNumber, String FriendPhoneNumber) {
        return !blockedUserService.checkIfUserBlocked(UserPhoneNumber, FriendPhoneNumber);
    }

    public void heartBeat() {

        Thread thread = new Thread(() -> {
            for (Map.Entry<String, CallBackController> entry : clients.entrySet()) {
                String phoneNumber = entry.getKey();
                CallBackController callBackController = entry.getValue();
                try {
                    callBackController.respond();
                } catch (RemoteException e) {
                    try {
                        trackOnlineUsersService.updateOnlineUsersCount(clients.size());
                        disconnect(phoneNumber, callBackController);
                        User user = new User();
                        user.setUserStatus(User.UserStatus.Offline);
                        new UserService().updateUser(user);
                    } catch (RemoteException | DuplicateEntryException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        ConcurrencyManager.getInstance().submitScheduledTask(thread, 0, 2, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void updateListOfContactBlockedContact(String phoneNumber) throws SQLException, NotBoundException, RemoteException, ClassNotFoundException {
        if(clients.containsKey(phoneNumber)){
            clients.get(phoneNumber).updateOnlineList();
        }
    }
}

