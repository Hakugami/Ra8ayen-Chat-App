package service;

import controllers.ContactsControllerSingleton;
import controllers.OnlineControllerImpl;
import dto.Controller.CallBackController;
import dto.Controller.SendHeartBeatToServerFromClient;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SendHeartBeatService extends UnicastRemoteObject implements SendHeartBeatToServerFromClient {

    private Map<String, Long> lastHeartbeatMap = new HashMap<>();
    private Map<String, CallBackController> clientCallBackMap = new HashMap<>();
    private TrackOnlineUsersService trackOnlineUsersService=new TrackOnlineUsersService();
    private Logger logger = Logger.getLogger(SendHeartBeatService.class.getName());
    private ScheduledExecutorService scheduler;
    private static SendHeartBeatService instance;

    public SendHeartBeatService() throws RemoteException {
        super();
        logger.info("SendHeartBeatService object bound to name SendHeartBeatToServerFromClient");
        startScheduler();
    }

    public static SendHeartBeatService getInstance() throws RemoteException {
        if (instance == null) {
            instance = new SendHeartBeatService();
        }
        return instance;
    }

    @Override
    public void sendHeartbeat(String phoneNumber, CallBackController callBackController) throws RemoteException {
        lastHeartbeatMap.put(phoneNumber, System.currentTimeMillis());
        clientCallBackMap.put(phoneNumber, callBackController);
    }

    public void checkHeartbeatTimeouts() {
        long currentTimestamp = System.currentTimeMillis();
        long heartbeatTimeout = 20 * 60 * 1000;

        for (Iterator<Map.Entry<String, Long>> iterator = lastHeartbeatMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, Long> entry = iterator.next();
            String phoneNumber = entry.getKey();
            long lastHeartbeatTime = entry.getValue();

            if (currentTimestamp - lastHeartbeatTime >= heartbeatTimeout) {
                try {
                    int onlineUsersCount = trackOnlineUsersService.getOnlineUsersCount();
                    trackOnlineUsersService.updateOnlineUsersCount(onlineUsersCount - 1);
                    CallBackController callBackController = clientCallBackMap.get(phoneNumber);
                    OnlineControllerImpl.getInstance().disconnect(phoneNumber, callBackController);
                    logger.info("client with phoneNumber =  " + phoneNumber + " is disconnected now");
                } catch (RemoteException | MalformedURLException e) {
                    System.out.println(e.getMessage());
                }
                iterator.remove();
                clientCallBackMap.remove(phoneNumber);
            }
        }
    }

    private void startScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkHeartbeatTimeouts, 0, 1, TimeUnit.MINUTES);
    }

    public void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}