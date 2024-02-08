package controllers;
import concurrency.manager.ConcurrencyManager;
import dto.Controller.CallBackController;
import dto.Controller.OnlineController;
import model.entities.User;
import service.BlockedUserService;
import service.ContactService;
import service.TrackOnlineUsersService;
import service.UserService;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class OnlineControllerImpl extends UnicastRemoteObject implements OnlineController {
    private static final Logger logger = Logger.getLogger(OnlineController.class.getName());
    private static OnlineControllerImpl onlineController;
    private final ContactService contactService;
    TrackOnlineUsersService trackOnlineUsersService;
    AuthenticationControllerSingleton auth;

    private final BlockedUserService blockedUserService;
    public static Map<String, CallBackController> clients =new ConcurrentHashMap<>();
    private OnlineControllerImpl() throws RemoteException, MalformedURLException {
        super();
        contactService = new ContactService();
        blockedUserService = new BlockedUserService();
        trackOnlineUsersService = new TrackOnlineUsersService();
        auth = AuthenticationControllerSingleton.getInstance();
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

        if(clients.containsKey(phoneNumber)) {
            return false;
        }
        else {
            clients.put(phoneNumber, callBackController);
            System.out.println("connect clients = "+clients.size());
            trackOnlineUsersService.updateOnlineUsersCount(clients.size());
            System.out.println("connect 1 : " + auth.getOfflineUsers());
            auth.UpdateOfflineUsers(auth.getOfflineUsers()- clients.size());
            System.out.println("connect 2 : " + (auth.getOfflineUsers()));
            return true;
        }
    }
    @Override
    public void disconnect(String phoneNumber, CallBackController callBackController) throws RemoteException {
        ConcurrencyManager.getInstance().submitTask(() -> friendStatusNotifier(phoneNumber, User.UserStatus.Offline));
        clients.remove(phoneNumber);
        System.out.println("disconnect clients = "+clients.size());
        trackOnlineUsersService.updateOnlineUsersCount(clients.size());
        System.out.println("disconnect 1 : " + auth.getOfflineUsers());
        auth.UpdateOfflineUsers(auth.getOfflineUsers()+ 1);
        System.out.println("disconnect 2 : " + (auth.getOfflineUsers()+1));
    }

    private void friendStatusNotifier(String phoneNumber, User.UserStatus userStatus) {

        User user = new UserService().getUserByPhoneNumber(phoneNumber);
        List<String> userFriends = contactService.getFriendsPhoneNumbers(user.getUserID());
        for (String friendPhoneNumber : userFriends) {
            if (clients.containsKey(friendPhoneNumber) && UserNotBlocked(phoneNumber,friendPhoneNumber)) {

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
    private boolean UserNotBlocked(String UserPhoneNumber , String FriendPhoneNumber){
        return !blockedUserService.checkIfUserBlocked(UserPhoneNumber, FriendPhoneNumber);
    }

    public void heartBeat(String phoneNumber, CallBackController callBackController){
        ConcurrencyManager.getInstance().submitTask(() -> friendStatusNotifier(phoneNumber, User.UserStatus.Offline));
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            try {
                callBackController.respond();
            } catch (RemoteException e) {
                clients.remove(phoneNumber);
                System.out.println("disconnect clients = " + clients.size());
                try {
                    trackOnlineUsersService.updateOnlineUsersCount(clients.size());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                int offlineUsers = auth.getOfflineUsers() + 1;
                auth.UpdateOfflineUsers(offlineUsers);
                System.out.println("disconnect 1 : " + offlineUsers);
                executorService.shutdown();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


}

