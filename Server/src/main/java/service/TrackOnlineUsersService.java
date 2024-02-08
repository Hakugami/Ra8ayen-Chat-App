package service;

import dto.Controller.TrackOnlineUsers;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class TrackOnlineUsersService extends UnicastRemoteObject implements TrackOnlineUsers {


    private static final Logger logger = Logger.getLogger(TrackOnlineUsersService.class.getName());
    private static TrackOnlineUsersService trackOnlineUsersService;
    static int  onlineUsersCount;
    private static StringProperty onlineUsersCountString;

    public StringProperty onlineUsersCountStringProberty(){
        return onlineUsersCountString;
    }



    public TrackOnlineUsersService() throws RemoteException {
        super();
        this.onlineUsersCount = 0;
        this.onlineUsersCountString = new SimpleStringProperty(String.valueOf(onlineUsersCount));
    }
    public static TrackOnlineUsersService getInstance() throws RemoteException {

        if(trackOnlineUsersService == null){
            trackOnlineUsersService = new TrackOnlineUsersService();
            logger.info("TrackOnlineUsersService object bound to name 'TrackOnlineUsers'.");
        }
        logger.info("TrackOnlineUsersService object bound to name 'TrackOnlineUsers'.");
        onlineUsersCountString = new SimpleStringProperty(String.valueOf(onlineUsersCount));
        return trackOnlineUsersService;
    }
    @Override
    public void updateOnlineUsersCount(int count) throws RemoteException {
        System.out.println("before ---> Online users count updated: " + onlineUsersCount);
        setOnlineUsersCount(count);
        setOnlineUsersCountString(String.valueOf(count));
        System.out.println("after  ---> Online users count updated: " + onlineUsersCount);
        System.out.println("#2 setOnlineUsersCount method : "+onlineUsersCount+" , "+onlineUsersCountString);
    }

    @Override
    public int getOnlineUsersCount() throws RemoteException {
        return onlineUsersCount;
    }

    public void setOnlineUsersCount(int onlineUsersCount) {
        this.onlineUsersCount = onlineUsersCount;
        //Platform.runLater(() -> this.onlineUsersCountString.set(String.valueOf(onlineUsersCount)));
        System.out.println("#1 setOnlineUsersCount method : "+onlineUsersCount+" , "+onlineUsersCountString);
    }

    public  void  setOnlineUsersCountString(String onlineUsersCountString) {
        System.out.println("#1 print ---------> "+onlineUsersCountString);
        Platform.runLater(() -> this.onlineUsersCountString.set(onlineUsersCountString));
    }

    public  String  getOnlineUsersCountString() {
        System.out.println("#2 print ---------> "+onlineUsersCountString);
        return this.onlineUsersCountString.get();
    }




}
